package com.weini.maogou.utils.dj;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimeUtil {
    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<>();

    public static boolean IsToday(String str) throws ParseException {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date(System.currentTimeMillis()));
        Calendar instance2 = Calendar.getInstance();
        instance2.setTime(new Date(Long.parseLong(str)));
        if (instance2.get(1) == instance.get(1) && instance2.get(6) - instance.get(6) == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsToday(Long day) throws ParseException {
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        Date date = getDateFormatTwo().parse(getFormatedDateTime(day));
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    public static SimpleDateFormat getDateFormatTwo() {
        if (DateLocal.get() == null) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }

    public static SimpleDateFormat getDateFormat() {
        if (DateLocal.get() == null) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA));
        }
        return DateLocal.get();
    }

    public static int getGapCount(Date date, Date date2) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(11, 0);
        instance.set(12, 0);
        instance.set(13, 0);
        instance.set(14, 0);
        Calendar instance2 = Calendar.getInstance();
        instance2.setTime(date2);
        instance2.set(11, 0);
        instance2.set(12, 0);
        instance2.set(13, 0);
        instance2.set(14, 0);
        return (int) ((instance2.getTime().getTime() - instance.getTime().getTime()) / 86400000);
    }
    /**
     * 将long转换为日期（yyyy-MM-dd HH:mm）
     * @param dateTime
     * @return 到分
     */
    @SuppressLint("SimpleDateFormat")
    public static String getFormatedDateTime(long dateTime) {
        String time="";
        try {
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            time=sDateFormat.format(new Date(dateTime + 0));
        }catch (Exception e){

        }
        return time;
    }


    /**
     * 将时间戳转换为日期（yyyy-MM-dd）
     * @param dateTime
     */
    @SuppressLint("SimpleDateFormat")
    public static String parseTimestampTransferDateTime(long dateTime) {
        String time="";
        try {
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            time=sDateFormat.format(new Date(dateTime + 0));
        }catch (Exception e){

        }
        return time;
    }


    /**
     * 将时间戳转换为日期（yyyy-MM）
     * @param dateTime
     */
    @SuppressLint("SimpleDateFormat")
    public static String parseTimestampTransferDateYearMonth(long dateTime) {
        String time="";
        try {
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM");
            time=sDateFormat.format(new Date(dateTime + 0));
        }catch (Exception e){

        }
        return time;
    }



    /**
     * 将日期转换为时间戳（yyyy-MM-dd）
     * @param dateTime
     */
    @SuppressLint("SimpleDateFormat")
    public static Long parseDateTimeTransferTimestamp(String dateTime) {
        Long time=0L;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse(dateTime);
            time = date.getTime();
        }catch (Exception e){

        }
        return time;
    }



    /**
     * 将日期转换为时间戳（yyyy-MM）
     * @param dateTime
     */
    @SuppressLint("SimpleDateFormat")
    public static Long parseDateYearMonthTransferTimestamp(String dateTime) {
        Long time=0L;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            Date date = simpleDateFormat.parse(dateTime);
            time = date.getTime();
        }catch (Exception e){

        }
        return time;
    }


    /**
     * 判断是否是超过15天
     *
     */
    public static Boolean getSet15Time() {
        boolean isSet ;
        long set15Time = UserInfoModel.getSet15Time();
        long currentTime = System.currentTimeMillis();
        long s = (currentTime - set15Time) / (1000 * 60 * 60 * 24);
        if (s >= 15) {//是否大于半个月
            isSet = true;
        }else {
            isSet=false;
        }
        return isSet;
    }

    /**
     * 格式到天
     *
     * @param time
     * @return
     */
    public static String getDay(long time) {
        return new SimpleDateFormat("yyyy-MM-dd").format(time);
    }


    /**
     * 获得当前时间的<code java.util.Date</code 对象
     *
     * @return
     */
    public static Date now() {
        return new Date();
    }
    /**
     * 获取当天的零点时间
     */
    public static long getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis());
    }


    /**
     * 获得本月第一天0点时间
     */
    public static long getTimesMonthmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1); // M月置1
        cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
        cal.set(Calendar.MINUTE, 0);// m置零
        cal.set(Calendar.SECOND, 0);// s置零
        cal.set(Calendar.MILLISECOND, 0);// S置零

        return cal.getTimeInMillis();
    }

    /**
     * 获得当前月的第一天
     * <p>
     * HH:mm:ss SS为零
     *
     * @return
     */
    public static Date firstDayOfMonthData() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1); // M月置1
        cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
        cal.set(Calendar.MINUTE, 0);// m置零
        cal.set(Calendar.SECOND, 0);// s置零
        cal.set(Calendar.MILLISECOND, 0);// S置零
        return cal.getTime();
    }

    /**
     * 获得当前月的第一天
     * <p>
     * HH:mm:ss SS为零
     *
     *
     */
    public static long firstDayOfMonth(int DAY_OF_MONTH) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, DAY_OF_MONTH); // M月置1
        cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
        cal.set(Calendar.MINUTE, 0);// m置零
        cal.set(Calendar.SECOND, 0);// s置零
        cal.set(Calendar.MILLISECOND, 0);// S置零
        return cal.getTimeInMillis();
    }


    /**
     * 获得当前月的第一天到当前时间的时间戳集合
     * <p>
     * HH:mm:ss SS为零
     *
     *
     */
    public static List<Long> getListDayOfMonth() {
        List<Long> dayOfMonth = new ArrayList<>();
        long days = getDayDiff(firstDayOfMonthData(), now());
        for (int i = 1; i <= days; i++) {
            long day = firstDayOfMonth(i);
            dayOfMonth.add(day);
        }
        Collections.sort(dayOfMonth, new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return (int) (o2 - o1);
            }
        });
        return dayOfMonth;
    }


    /**
     * 获得天数差
     *
     * @param begin 开始
     * @param end   结束
     * @return 天数
     */
    public static long getDayDiff(Date begin, Date end) {
        long day = 1;
        if (end.getTime() < begin.getTime()) {
            day = -1;
        } else if (end.getTime() == begin.getTime()) {
            day = 1;
        } else {
            day += (end.getTime() - begin.getTime()) / (24 * 60 * 60 * 1000);
        }
        return day;
    }

    /**
     *通过时间戳判断是否是当天
     */
    public static boolean isCurrentDay(long timeStap){
        long currentTimeMillis = System.currentTimeMillis();
        String dateStr =  TimeUtil.parseTimestampTransferDateTime(currentTimeMillis);
        long currentStap = TimeUtil.parseDateTimeTransferTimestamp(dateStr);


        if(timeStap == currentStap){
            return true;
        }
        return false;
    }


    /**
     *通过时间戳判断是否是当月
     */
    public static boolean isCurrentMonth(long timeStap){
        try {
            long currentTimeMillis = System.currentTimeMillis();
            String dateStr = TimeUtil.parseTimestampTransferDateYearMonth(currentTimeMillis);
            long currentStap = TimeUtil.parseDateYearMonthTransferTimestamp(dateStr);
            if (timeStap == currentStap) {
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }



    /**
     *通过时间戳转换为年月时间戳
     */
    public static long getYearMonthTimeStap(long timeStap){
        long currentStap = 0;
        try {
            String dateStr = TimeUtil.parseTimestampTransferDateYearMonth(timeStap);
            currentStap = TimeUtil.parseDateYearMonthTransferTimestamp(dateStr);
        }catch (Exception e){
            currentStap = 0;
        }

        return currentStap;
    }








    /**
     *获取当天时间戳
     */
    public static long getCurrentDaytimeStap(){
        long currentStap = 0;
        long currentTimeMillis = System.currentTimeMillis();
        String dateStr =  TimeUtil.parseTimestampTransferDateTime(currentTimeMillis);
        currentStap = TimeUtil.parseDateTimeTransferTimestamp(dateStr);
        return currentStap;
    }

}
