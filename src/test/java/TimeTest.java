import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeTest {

    public static void main(String[] args) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, (c.get(Calendar.HOUR_OF_DAY) - 4));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        System.out.println(Long.valueOf(simpleDateFormat.format(new Date())));
        System.out.println(Long.valueOf(simpleDateFormat.format(c.getTime())));
        SimpleDateFormat s2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long id = 20210809132259L;
        String aa = id.toString();
        String d = aa.substring(0,4)+"-"+aa.substring(4,6)+"-"+
                aa.substring(6,8)+" "+
                aa.substring(8,10)+":"+aa.substring(10,12)+":"+aa.substring(12);
        System.out.println(d);
    }
}
