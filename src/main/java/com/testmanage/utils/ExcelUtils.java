package com.testmanage.utils;

import com.testmanage.annotation.ExcelInfo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ExcelUtils<T> {

    private Class<T> clazz;

    public ExcelUtils(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 导入excel文件
     *
     * @param sheetName 页名称
     * @param input     输入流程
     * @return 文件数据
     */
    public List<T> importExcel(String sheetName, InputStream input) {
        List<T> list = new ArrayList<>();
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(input);
            HSSFSheet sheet = workbook.getSheetAt(0);
            if (sheetName != null && !sheetName.isEmpty()) {
                sheet = workbook.getSheet(sheetName);
            }
            int rows = sheet.getPhysicalNumberOfRows();
            if (rows > 0) {// 有数据时才处理
                // Field[] allFields = clazz.getDeclaredFields();// 得到类的所有field.
                List<Field> allFields = getMappedFiled(clazz, null);
                Map<String, Field> fieldsMap = new HashMap<>();// 定义一个map用于存放列的序号和field.
                for (Field field : allFields) { // 将有注解的field存放到map中.
                    if (field.isAnnotationPresent(ExcelInfo.class)) {
                        ExcelInfo attr = field.getAnnotation(ExcelInfo.class);
                        String col = attr.column();// 获得列
                        field.setAccessible(true);// 设置类的私有字段属性可访问.
                        fieldsMap.put(col, field);
                    }
                }
                List<String> head = new ArrayList<>();
                HSSFRow row0 = sheet.getRow(0);
                for (int k = 0; k < 20; k++) {
                    HSSFCell cell = row0.getCell(k);
                    if (cell == null) {
                        break;
                    }
                    head.add(cell.getStringCellValue());
                }
                if (head.size() == 0) {
                    return null;
                }
                for (int i = 1; i < rows; i++) {// 从第2行开始取数据,默认第一行是表头.
                    HSSFRow row = sheet.getRow(i);
                    // int cellNum = row.getPhysicalNumberOfCells();
                    // int cellNum = row.getLastCellNum();
                    T entity = null;
                    for (int j = 0; j <= head.size(); j++) {
                        HSSFCell cell = row.getCell(j);
                        if (cell == null) {
                            continue;
                        }
                        CellType cellType = cell.getCellType();
                        String c;
                        if (cellType == CellType.NUMERIC) {
                            c = String.valueOf(cell.getNumericCellValue());
                        } else if (cellType == CellType.BOOLEAN) {
                            c = String.valueOf(cell.getBooleanCellValue());
                        } else {
                            c = cell.getStringCellValue();
                        }
                        if (c == null || c.equals("")) {
                            continue;
                        }
                        entity = (entity == null ? clazz.newInstance() : entity);// 如果不存在实例则新建.
                        // System.out.println(cells[j].getContents());
                        Field field = fieldsMap.get(head.get(j));// 从map中得到对应列的field.
                        if (field == null) {
                            continue;
                        }
                        // 取得类型,并根据对象类型设置值.
                        Class<?> fieldType = field.getType();
                        if (String.class == fieldType) {
                            field.set(entity, String.valueOf(c));
                        } else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
                            field.set(entity, Integer.parseInt(c));
                        } else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
                            field.set(entity, Long.valueOf(c));
                        } else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
                            field.set(entity, Float.valueOf(c));
                        } else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
                            field.set(entity, Short.valueOf(c));
                        } else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
                            field.set(entity, Double.valueOf(c));
                        } else if (Character.TYPE == fieldType) {
                            if (c.length() > 0) {
                                field.set(entity, c.charAt(0));
                            }
                        }
                    }
                    if (entity != null) {
                        list.add(entity);
                    }
                }
            }
        } catch (IOException | InstantiationException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param lists      数据
     * @param sheetNames 工作表的名称
     * @param output     java输出流
     * @return 状态
     */
    public boolean exportExcel(List<T> lists[], String sheetNames[], OutputStream output) {
        if (lists.length != sheetNames.length) {
            System.out.println("数组长度不一致");
            return false;
        }
        HSSFWorkbook workbook = new HSSFWorkbook();// 产生工作薄对象
        for (int ii = 0; ii < lists.length; ii++) {
            List<T> list = lists[ii];
            String sheetName = sheetNames[ii];
            List<Field> fields = getMappedFiled(clazz, null);
            HSSFSheet sheet = workbook.createSheet();// 产生工作表对象
            workbook.setSheetName(ii, sheetName);
            HSSFRow row;
            HSSFCell cell;// 产生单元格
            HSSFCellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.SKY_BLUE.index);
            style.setFillBackgroundColor(IndexedColors.GREY_40_PERCENT.index);
            row = sheet.createRow(0);// 产生一行
            // 写入各个字段的列头名称
            for (Field field : fields) {
                ExcelInfo attr = field.getAnnotation(ExcelInfo.class);
                int col = getExcelCol(attr.column());// 获得列号
                cell = row.createCell(col);// 创建列
                cell.setCellType(CellType.STRING);// 设置列中写入内容为String类型
                cell.setCellValue(attr.name());// 写入列名
                // 如果设置了提示信息则鼠标放上去提示.
                if (!attr.prompt().trim().equals("")) {
                    setHSSFPrompt(sheet, "", attr.prompt(), 1, lists[0].size(), col, col);// 这里默认设了2-101列提示.
                }
                // 如果设置了combo属性则本列只能选择不能输入
                if (attr.combo().length > 0) {
                    setHSSFValidation(sheet, attr.combo(), 1, lists[0].size(), col, col);// 这里默认设了2-101列只能选择不能输入.
                }
                cell.setCellStyle(style);
            }
            int startNo = 0;
            int endNo = list.size(); // 写入各条记录,每条记录对应excel表中的一行
            for (int i = startNo; i < endNo; i++) {
                row = sheet.createRow(i + 1 - startNo);
                T vo = list.get(i); // 得到导出对象.
                for (Field field : fields) {
                    field.setAccessible(true);// 设置实体类私有属性可访问
                    ExcelInfo attr = field.getAnnotation(ExcelInfo.class);
                    try {
                        // 根据ExcelVOAttribute中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
                        if (attr.isExport()) {
                            cell = row.createCell(getExcelCol(attr.column()));
                            // 创建cell
                            cell.setCellType(CellType.STRING);
                            cell.setCellValue(field.get(vo) == null ? ""
                                    : String.valueOf(field.get(vo)));// 如果数据存在就填入,不存在填入空格.
                        }
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        try {
            output.flush();
            workbook.write(output);
            output.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Output is closed ");
            return false;
        }
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param list      数据
     * @param sheetName 工作表的名称
     * @param output    java输出流
     * @return 状态
     */
    @SuppressWarnings("unchecked")
    public boolean exportExcel(List<T> list, String sheetName, OutputStream output) {
        //此处 对类型进行转换
        List<T> ilist = new ArrayList<>();
        ilist.addAll(list);
        List<T>[] lists = new ArrayList[1];
        lists[0] = ilist;
        String[] sheetNames = new String[1];
        sheetNames[0] = sheetName;
        return exportExcel(lists, sheetNames, output);
    }

    /**
     * 将EXCEL中A, B, C, D, E列映射成0, 1, 2, 3
     *
     * @param col 行
     */
    public static int getExcelCol(String col) {
        col = col.toUpperCase();
        // 从-1开始计算,字母重1开始运算。这种总数下来算数正好相同。
        int count = -1;
        char[] cs = col.toCharArray();
        count += IntStream.range(0, cs.length).map(i -> (int) ((cs[i] - 64) * Math.pow(26, cs.length - 1 - i))).sum();
        return count;
    }

    /**
     * 设置单元格上提示
     *
     * @param sheet         要设置的sheet.
     * @param promptTitle   标题
     * @param promptContent 内容
     * @param firstRow      开始行
     * @param endRow        结束行
     * @param firstCol      开始列
     * @param endCol        结束列
     * @return 设置好的sheet.
     */
    public static HSSFSheet setHSSFPrompt(HSSFSheet sheet, String promptTitle, String promptContent, int firstRow,
                                          int endRow, int firstCol, int endCol) {
        // 构造constraint对象
        DVConstraint constraint = DVConstraint.createCustomFormulaConstraint("DD1");
        // 四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        HSSFDataValidation data_validation_view = new HSSFDataValidation(regions, constraint);
        data_validation_view.createPromptBox(promptTitle, promptContent);
        sheet.addValidationData(data_validation_view);
        return sheet;
    }

    /**
     * 设置某些列的值只能输入预制的数据, 显示下拉框.
     *
     * @param sheet    要设置的sheet.
     * @param textlist 下拉框显示的内容
     * @param firstRow 开始行
     * @param endRow   结束行
     * @param firstCol 开始列
     * @param endCol   结束列
     * @return 设置好的sheet.
     */
    public static HSSFSheet setHSSFValidation(HSSFSheet sheet, String[] textlist, int firstRow, int endRow,
                                              int firstCol, int endCol) {
        // 加载下拉列表内容
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(data_validation_list);
        return sheet;
    }

    /**
     * 得到实体类所有通过注解映射了数据表的字段
     *
     * @param clazz  类型
     * @param fields 文件集合
     * @return 文件集合
     */
    @SuppressWarnings("rawtypes")
    private List<Field> getMappedFiled(Class clazz, List<Field> fields) {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        Field[] allFields = clazz.getDeclaredFields();// 得到所有定义字段
        // 得到所有field并存放到一个list中.
        for (Field field : allFields) {
            if (field.isAnnotationPresent(ExcelInfo.class)) {
                fields.add(field);
            }
        }
        if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class)) {
            getMappedFiled(clazz.getSuperclass(), fields);
        }
        return fields;
    }
}
