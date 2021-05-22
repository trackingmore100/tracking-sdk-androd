package Classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by User on 18/6/14.
 */
final class DateMethods {
    private final static String DateFormat = "yyyy-MM-dd HH:mm:ss";


    public static Date getDate(String date) throws RequestAPIException, ParseException {
        SimpleDateFormat dateFormat;
        StringBuilder sb = new StringBuilder(date);
        Date newDate = null;
        if (sb.length() == 25) {
            dateFormat = new SimpleDateFormat(DateFormat, Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            sb.deleteCharAt(22);
            newDate = dateFormat.parse(sb.toString());
            return newDate;
        } else {
            throw new RequestAPIException("The date receive is not properly formatted yyyy-MM-dd HH:mm:ss  and is: "
                    + date);
        }
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateFormat, Locale.US);
        Date date = simpleDateFormat.parse(s);
        assert date != null;
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateFormat, Locale.US);
        long lt = Long.parseLong(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String toString(Date date) {
        SimpleDateFormat dateFormat;
        StringBuilder sb;
        dateFormat = new SimpleDateFormat(DateFormat, Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        sb = new StringBuilder(dateFormat.format(date));
        sb.insert(22, ':');

        return sb.toString();


    }
}
