package com.testmanage.service;

import com.google.gson.*;
import com.testmanage.entity.*;
import com.testmanage.mapper.AutoCaseExecMapper;
import com.testmanage.mapper.AutoCaseMapper;
import com.testmanage.mapper.ScatterChartMapper;
import com.testmanage.service.user.UserContext;
import com.testmanage.utils.JsonParse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class AutoCaseService {

    @Autowired
    AutoCaseMapper autoCaseMapper;

    @Autowired
    AutoCaseExecMapper autoCaseExecMapper;

    @Autowired
    ScatterChartMapper scatterChartMapper;

    @Autowired
    CaseInfoService caseInfoService;

    public synchronized void addAndUpdateCase(JsonObject bodyJson) throws Exception {
        AutoCase autoCase = addAuto(bodyJson);
        String case_id = autoCase.getCase_id();
        AutoCase currentCase = autoCaseMapper.findCaseById(case_id);
        if (currentCase instanceof AutoCase) {
            //update
            if (!JsonParse.getGson().toJson(autoCase)
                    .equalsIgnoreCase(JsonParse.getGson().toJson(currentCase))) {
                autoCaseMapper.update(autoCase);
            }
        } else {
            autoCaseMapper.add(autoCase);
            //新增时更新一下case_info is_auto，auto case必须要有手工用例存在
            CaseInfo current = caseInfoService.queryCase(autoCase.getCase_id());
            if (current instanceof CaseInfo) {
                CaseInfo caseInfo = new CaseInfo();
                caseInfo.setCase_id(autoCase.getCase_id());
                caseInfo.setIs_auto(true);
                caseInfo.setUpdate_date(new Date());
                caseInfo.setUpdate_user("Automation");
                caseInfoService.updateCase(caseInfo);
            } else {
                log.error("auto case is not existed , caseId : " + autoCase.getCase_id());
            }
        }
        AutoCaseExec autoCaseExec = addExec(bodyJson);
        AutoCaseExec currentCaseExec = autoCaseExecMapper
                .findCaseExecById(autoCaseExec.getExec_id(), autoCaseExec.getCase_id(),
                        autoCaseExec.getExec_parameter());
        if (currentCaseExec instanceof AutoCaseExec) {
            //update
            if (!JsonParse.getGson().toJson(autoCaseExec)
                    .equalsIgnoreCase(JsonParse.getGson().toJson(currentCaseExec))) {
                //如果成功的请求在开始的请求之前到，更新开始时间
                if (currentCaseExec.getCurrent() > autoCaseExec.getCurrent() &&
                        autoCaseExec.getStatus() == 5 &&
                        currentCaseExec.getStart_date() == null) {
                    autoCaseExec.setStatus(null);//不更新status
                }
                autoCaseExec.setUpdate_date(new Date());
                autoCaseExecMapper.update(autoCaseExec);
            }
        } else {
            autoCaseExecMapper.add(autoCaseExec);
        }
    }

    public void addCase(AutoCase autoCase) {
        autoCaseMapper.add(autoCase);
    }

    public AutoCase findCaseById(String caseId) {
        return autoCaseMapper.findCaseById(caseId);
    }

    private AutoCase addAuto(JsonObject bodyJson) {
        JsonObject autoCaseJson = new JsonObject();
        autoCaseJson.addProperty("case_id", bodyJson.get("caseId").getAsString());
        autoCaseJson.addProperty("type", bodyJson.get("type").getAsString());
        autoCaseJson.addProperty("create_user", bodyJson.get("author").getAsString());
        autoCaseJson.addProperty("create_date", bodyJson.get("createDate").getAsString());
        return JsonParse.getGson().fromJson(autoCaseJson, AutoCase.class);
    }

    private AutoCaseExec addExec(JsonObject bodyJson) {
        JsonObject autoCaseJson = new JsonObject();
        autoCaseJson.addProperty("exec_id", bodyJson.get("execId").getAsString());
        autoCaseJson.addProperty("current", bodyJson.get("currentTime").getAsString());
        autoCaseJson.addProperty("case_id", bodyJson.get("caseId").getAsString());
        autoCaseJson.addProperty("status", bodyJson.get("status").getAsInt());
        String star_date = bodyJson.get("startDate") == null || bodyJson.get("startDate") instanceof JsonNull
                ? null : bodyJson.get("startDate").getAsString();
        if (!StringUtils.isEmpty(star_date)) {
            autoCaseJson.addProperty("start_date", star_date);
        }
        String end_date = bodyJson.get("endDate") == null || bodyJson.get("endDate") instanceof JsonNull
                ? null : bodyJson.get("endDate").getAsString();
        if (!StringUtils.isEmpty(end_date)) {
            autoCaseJson.addProperty("end_date", end_date);
        }
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        return gson.fromJson(autoCaseJson, AutoCaseExec.class);
    }

    public Integer queryCaseCount() {
        return autoCaseMapper.queryCaseCount(UserContext.get().getIsV());
    }


    public synchronized JsonObject drawColumnChart() {
        //统计最近15次，统计4个小时前开始（因为正在执行的任务暂时不统计）
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, (c.get(Calendar.HOUR_OF_DAY) - 4));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Long baseExecId = Long.valueOf(simpleDateFormat.format(c.getTime()));
        List<Long> execIdList = autoCaseExecMapper.findExecId(UserContext.get().getIsV());
        List<Long> showList = new ArrayList<>();//展示的从4小时前15条记录
        if (execIdList.size() == 0) {
            return null;
        }
        for (int i = 0; i < execIdList.size(); i++) {
            if (execIdList.get(i) >= baseExecId) {
                continue;
            } else {
                if (showList.size() < 15) {
                    showList.add(execIdList.get(i));
                }
            }
        }
        if (showList.size() <= 1) {
            return null;
        }
        Collections.reverse(showList);
        Long maxId = showList.get(showList.size() - 1);
        Long minId = showList.get(0);
        List<ColumnChart> countList =
                autoCaseExecMapper.findColumnChart(UserContext.get().getIsV(), minId, maxId);
        Map<Long, Integer> totalMap = new HashMap<>();
        Map<Long, Integer> successMap = new HashMap<>();
        Map<Long, Integer> failedMap = new HashMap<>();
        Map<Long, Integer> skipMap = new HashMap<>();
        Map<Long, Long> timeMap = new HashMap<>();

        Date minDate = null;
        Date maxDate = null;
        for (int j = 0; j < countList.size(); j++) {
            ColumnChart columnChart = countList.get(j);
            Long execId = columnChart.getExec_id();
            Integer count = columnChart.getTotal();
            Integer status = columnChart.getStatus();
            Date min = columnChart.getMin_start_date();
            Date max = columnChart.getMax_end_date();
            if (totalMap.containsKey(execId)) {
                Integer total = totalMap.get(execId) + count;
                totalMap.put(execId, total);
            } else {
                totalMap.put(execId, count);
            }
            if (status == 1) {
                successMap.put(execId, count);
            } else if (status == 3) {
                skipMap.put(execId, count);
            } else {
                if (failedMap.containsKey(execId)) {
                    Integer fCount = failedMap.get(execId) + count;
                    failedMap.put(execId, fCount);
                } else {
                    failedMap.put(execId, count);
                }
            }

            if (!timeMap.containsKey(execId)) {
                if (j > 0) {
                    Long time = 0L;
                    if (minDate instanceof Date && maxDate instanceof Date) {
                        time = (maxDate.getTime() - minDate.getTime()) / 60000;
                    }
                    Long idTmp;
                    idTmp = countList.get(j - 1).getExec_id();
                    timeMap.put(idTmp, time);
                }
                minDate = null;
                maxDate = null;
            }
            if (min != null) {
                if (minDate == null || min.getTime() < minDate.getTime()) {
                    minDate = min;
                }
            }

            if (max != null) {
                if (maxDate == null || max.getTime() > maxDate.getTime()) {
                    maxDate = max;
                }
            }

            if (j == countList.size()) {
                Long time = 0L;
                if (minDate instanceof Date && maxDate instanceof Date) {
                    time = (maxDate.getTime() - minDate.getTime()) / 60000;
                }
                timeMap.put(execId, time);
            }
        }
        List<Integer> totalList = new ArrayList<>();
        List<Integer> successList = new ArrayList<>();
        List<Integer> failedList = new ArrayList<>();
        List<Integer> skipList = new ArrayList<>();
        List<Long> timeList = new ArrayList<>();
        for (Long id : showList) {
            totalList.add(totalMap.get(id));
            successList.add(successMap.get(id));
            failedList.add(failedMap.get(id));
            skipList.add(skipMap.get(id));
            timeList.add(timeMap.get(id));
        }
        JsonArray xData = JsonParse.getGson().fromJson(JsonParse.getGson().toJson(showList), JsonArray.class);
        JsonArray timeData = JsonParse.getGson().fromJson(JsonParse.getGson().toJson(timeList), JsonArray.class);
        JsonArray totalData = JsonParse.getGson().fromJson(JsonParse.getGson().toJson(totalList), JsonArray.class);
        JsonArray successData = JsonParse.getGson().fromJson(JsonParse.getGson().toJson(successList), JsonArray.class);
        JsonArray failedData = JsonParse.getGson().fromJson(JsonParse.getGson().toJson(failedList), JsonArray.class);
        JsonArray skipData = JsonParse.getGson().fromJson(JsonParse.getGson().toJson(skipList), JsonArray.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("xData", xData);
        jsonObject.add("timeData", timeData);
        jsonObject.add("totalData", totalData);
        jsonObject.add("successData", successData);
        jsonObject.add("failedData", failedData);
        jsonObject.add("skipData", skipData);
        return jsonObject;
    }

    public synchronized JsonArray getRecentExec() {
        Date currentDate = new Date();
        //统计4个小时内
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, (c.get(Calendar.HOUR_OF_DAY) - 4));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Long baseExecId = Long.valueOf(simpleDateFormat.format(c.getTime()));
        List<Long> execIdList = autoCaseExecMapper.findExecId(UserContext.get().getIsV());
        List<Long> showList = new ArrayList<>();//展示的从4小时前15条记录
        if (execIdList.size() == 0) {
            return null;
        }
        for (int i = 0; i < execIdList.size(); i++) {
            if (execIdList.get(i) < baseExecId) {
                continue;
            } else {
                showList.add(execIdList.get(i));
            }
        }
        if (showList.size() == 0) {
            return null;
        }
        JsonArray jsonArray = new JsonArray();
        List<ColumnChart> execs = autoCaseExecMapper.findColumnChart(UserContext.get().getIsV(),
                showList.get(showList.size() - 1), showList.get(0));
        Map<Long, JsonObject> result = new LinkedHashMap<>();
        for (ColumnChart columnChart : execs) {
            if (result.containsKey(columnChart.getExec_id())) {
                JsonObject resultJson = result.get(columnChart.getExec_id());
                Integer total = resultJson.get("total").getAsInt();
                resultJson.addProperty("total", total + columnChart.getTotal());
                if (columnChart.getStatus() == 1) {
                    resultJson.addProperty("success", columnChart.getTotal());
                } else if (columnChart.getStatus() == 2) {
                    resultJson.addProperty("failed", columnChart.getTotal());
                } else if (columnChart.getStatus() == 3) {
                    resultJson.addProperty("skip", columnChart.getTotal());
                } else {
                    resultJson.addProperty("other", columnChart.getTotal());
                }
                if (columnChart.getMin_start_date() != null
                        && (columnChart.getMin_start_date().getTime() < resultJson.get("min_start_date").getAsLong()
                        || currentDate.getTime() == resultJson.get("min_start_date").getAsLong()
                )) {
                    resultJson.addProperty("min_start_date", columnChart.getMin_start_date().getTime());
                }
                if (columnChart.getMax_end_date() != null
                        && (columnChart.getMax_end_date().getTime() > resultJson.get("max_end_date").getAsLong()
                        || currentDate.getTime() == resultJson.get("max_end_date").getAsLong()
                )) {
                    resultJson.addProperty("max_end_date", columnChart.getMax_end_date().getTime());
                }
            } else {
                JsonObject resultJson = new JsonObject();
                resultJson.addProperty("exec_id", columnChart.getExec_id());
                resultJson.addProperty("total", columnChart.getTotal());
                if (columnChart.getStatus() == 1) {
                    resultJson.addProperty("success", columnChart.getTotal());
                } else if (columnChart.getStatus() == 2) {
                    resultJson.addProperty("failed", columnChart.getTotal());
                } else if (columnChart.getStatus() == 3) {
                    resultJson.addProperty("skip", columnChart.getTotal());
                } else {
                    resultJson.addProperty("other", columnChart.getTotal());
                }
                if (columnChart.getMin_start_date() == null) {
                    columnChart.setMin_start_date(currentDate);
                }
                if (columnChart.getMax_end_date() == null) {
                    columnChart.setMax_end_date(currentDate);
                }
                resultJson.addProperty("min_start_date", columnChart.getMin_start_date().getTime());
                resultJson.addProperty("max_end_date", columnChart.getMax_end_date().getTime());
                result.put(columnChart.getExec_id(), resultJson);
            }
        }
        for (Map.Entry<Long, JsonObject> entry : result.entrySet()) {
            jsonArray.add(entry.getValue());
        }
        return jsonArray;
    }


    public synchronized JsonObject drawScatterChart() {
        //统计4个小时前开始（因为正在执行的任务暂时不统计）
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, (c.get(Calendar.HOUR_OF_DAY) - 4));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Long baseExecId = Long.valueOf(simpleDateFormat.format(c.getTime()));
        List<Long> execIdList = autoCaseExecMapper.findExecId(UserContext.get().getIsV());
        List<Long> showList = new ArrayList<>();//展示的从4小时前15条记录
        List<Long> allList = new ArrayList<>();//展示的从4小时前所有记录
        List<Long> addList = new ArrayList<>();//展示的从4小时前增量记录
        //从chart表拿exec_id
        List<Long> scatterExecIdList = scatterChartMapper.findExecId(UserContext.get().getIsV());
        for (int i = 0; i < execIdList.size(); i++) {
            if (execIdList.get(i) >= baseExecId) {
                continue;
            } else {
                if (showList.size() < 15) {
                    showList.add(execIdList.get(i));
                }
                allList.add(execIdList.get(i));
                if (scatterExecIdList.size() > 0 && execIdList.get(i) > scatterExecIdList.get(0)) {
                    addList.add(execIdList.get(i));
                }
            }
        }

        if (scatterExecIdList.size() == 0) {
            //空表，直接进行全表计算
            scatterRecord(allList, new ArrayList<>());
        } else {
            //4个小时内执行太多记录超过15条，则进行全表增量计算
            if (scatterExecIdList.get(0) < showList.get(showList.size() - 1)) {
                scatterRecord(addList, scatterExecIdList);
            } else {
                //对showList进行增量计算
                scatterRecord(showList, scatterExecIdList);
            }
        }

        //计算完成后从scatter表查询结果
        List<Integer> timelineList = new ArrayList<>();
        for (int j = 0; j < showList.size(); j++) {
            timelineList.add(j + 1);
        }
        JsonArray timeline = JsonParse.getGson().fromJson(JsonParse.getGson().toJson(timelineList), JsonArray.class);
        Collections.reverse(timelineList);//反转对应series
        List<String> descList = new ArrayList<>();
        for (Long id : showList) {
            String tm = id.toString();
            String time = tm.substring(0, 4) + "-" + tm.substring(4, 6) + "-" +
                    tm.substring(6, 8) + " " +
                    tm.substring(8, 10) + ":" + tm.substring(10, 12) + ":" + tm.substring(12);
            descList.add(time);
        }
        List<List<List<Object>>> sList = new ArrayList<>();
        Set<String> caseIdSet = new HashSet<>();
        for (int i = 0; i < showList.size(); i++) {
            List<List<Object>> sListTmp = new ArrayList<>();
            Long id = showList.get(i);
            Integer tl = timelineList.get(i);
            String time = descList.get(i);
            List<ScatterChart> scatterChartList = scatterChartMapper.findByExecId(id, UserContext.get().getIsV());
            if (scatterChartList.size() > 0) {
                for (ScatterChart scatterChart : scatterChartList) {
                    List<Object> list = new ArrayList<>();
                    list.add(scatterChart.getExec_time());
                    list.add(scatterChart.getFailed_rate());
                    list.add(time);
                    list.add(scatterChart.getCase_id());
                    list.add(tl);
                    caseIdSet.add(scatterChart.getCase_id());
                    sListTmp.add(list);
                }
                sList.add(sListTmp);
            }
        }
        //反转让最新的结果对应在前面
        Collections.reverse(sList);
        JsonArray series = JsonParse.getGson().fromJson(JsonParse.getGson().toJson(sList), JsonArray.class);
        JsonArray counties = JsonParse.getGson().fromJson(JsonParse.getGson().toJson(caseIdSet), JsonArray.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("counties", counties);
        jsonObject.add("timeline", timeline);
        jsonObject.add("series", series);
        return jsonObject;
    }

    private void scatterRecord(List<Long> caseExecIdList, List<Long> scatterExecIdList) {
        List<ScatterChart> scatterChartList = new ArrayList<>();
        if (scatterExecIdList.size() == 0) {
            List<AutoCaseExec> autoCaseExecs = autoCaseExecMapper.findALL(UserContext.get().getIsV());
            List<String> caseIdList = new ArrayList<>();
            for (AutoCaseExec autoCaseExec : autoCaseExecs) {
                String caseTmp = autoCaseExec.getCase_id() + autoCaseExec.getExec_parameter();
                if (!caseIdList.contains(caseTmp)) {
                    List<AutoCaseExec> totalList = autoCaseExecMapper
                            .findCaseExecByCaseId(autoCaseExec.getCase_id(), autoCaseExec.getExec_parameter());
                    caseIdList.add(caseTmp);//记下已处理的case,后续直接跳过
                    Map<String, Object> countMap = new HashMap<>();
                    Integer failed = 0;
                    List<Integer> indexList = new ArrayList<>();
                    for (int j = 0; j < totalList.size(); j++) {
                        if (totalList.get(j).getStatus() != 1) {
                            failed++;
                            indexList.add(j);
                        }
                    }
                    //一次性处理掉所有该caseId
                    for (int i = 0; i < totalList.size(); i++) {
                        Integer total = 0;
                        Integer failedCount = 0;
                        AutoCaseExec autoCaseExecTmp = totalList.get(i);
                        total = totalList.size() - i;
                        countMap.put("total", total);
                        for (int m = 0; m < indexList.size(); m++) {
                            if (i == indexList.get(m)) {
                                failedCount = failed - m;
                            }
                        }
                        countMap.put("failedCount", failedCount);
                        ScatterChart scatterChart = autoToScatter(autoCaseExecTmp, countMap);
                        scatterChartList.add(scatterChart);
                    }
                }
            }
        } else {
            Collections.reverse(caseExecIdList);
            for (Long id2 : caseExecIdList) {
                if (id2 <= scatterExecIdList.get(0)) {
                    continue;
                } else {
                    List<AutoCaseExec> list = autoCaseExecMapper.findCaseExecByExecId(id2);
                    for (AutoCaseExec a : list) {
                        ScatterChart s = scatterChartMapper.findOneByCaseId(a.getCase_id(), UserContext.get().getIsV());
                        Map<String, Object> countMap = new HashMap<>();

                        if (s instanceof ScatterChart) {
                            countMap.put("total", s.getExec_total() + 1);
                            if (a.getStatus() != 1) {
                                countMap.put("failedCount", s.getFailed_count() + 1);
                            }
                        } else {
                            countMap.put("total", 1);
                            if (a.getStatus() != 1) {
                                countMap.put("failedCount", 1);
                            }
                        }
                        ScatterChart scatterChart = autoToScatter(a, countMap);
                        scatterChartList.add(scatterChart);
                    }
                }
            }
        }
        if (scatterChartList.size() > 0) {
            //批量插入scatter表
            scatterChartMapper.batchInsert(scatterChartList);
        }
    }

    private ScatterChart autoToScatter(AutoCaseExec autoCaseExec, Map<String, Object> countMap) {
        ScatterChart scatterChart = new ScatterChart();
        scatterChart.setExec_id(autoCaseExec.getExec_id());
        scatterChart.setCase_id(autoCaseExec.getCase_id());
        scatterChart.setExec_parameter(autoCaseExec.getExec_parameter());
        if (autoCaseExec.getStart_date() != null && autoCaseExec.getEnd_date() != null) {
            Date startDate = autoCaseExec.getStart_date();
            Date endDate = autoCaseExec.getEnd_date();
            long time = (endDate.getTime() - startDate.getTime()) / 1000;
            scatterChart.setExec_time(time);
        }
        Integer total = countMap.get("total") == null ? 0 : Integer.parseInt(countMap.get("total").toString());
        Integer failedCount =
                countMap.get("failedCount") == null ? 0 : Integer.parseInt(countMap.get("failedCount").toString());
        Double failedRate = 0.00;
        if (total > 0) {
            failedRate = (Double.valueOf(failedCount) / total) * 100;
            countMap.put("failedRate", failedRate);
        }
        scatterChart.setExec_total(total);
        scatterChart.setFailed_count(failedCount);
        scatterChart.setFailed_rate(failedRate);
        scatterChart.setIs_v(UserContext.get().getIsV());
        return scatterChart;
    }
}
