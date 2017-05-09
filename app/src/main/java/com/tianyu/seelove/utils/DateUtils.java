package com.tianyu.seelove.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

/**
 * 时间工具
 *
 * @author shizheng.zhao
 * @date 2014-1-28
 */
public class DateUtils {
    private static final int ONEDAY = 1000 * 60 * 60 * 24;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.CHINA);
    private static SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat(
            "yyyy-MM-dd_HH_mm_ss", Locale.CHINA);

    public static String getPastDate(long time) {
        long currentTime = new Date().getTime();
        long timeSpan = currentTime - time;
        DecimalFormat decimalFormat = new DecimalFormat("#");
        if (timeSpan < 1000 * 60) {
            return decimalFormat.format(Math.floor(timeSpan * 1.0 / (1000)))
                    + "秒前";
        } else if (timeSpan < 1000 * 60 * 60) {
            return decimalFormat.format(Math
                    .floor(timeSpan * 1.0 / (1000 * 60))) + "分钟前";
        } else if (timeSpan < 1000 * 60 * 60 * 24) {
            return decimalFormat
                    .format(Math.floor(timeSpan / (1000 * 60 * 60))) + "小时前";
        } else if (timeSpan < 1d * 1000 * 60 * 60 * 24 * 30) {
            return decimalFormat.format(Math.floor(timeSpan
                    / (1000 * 60 * 60 * 24)))
                    + "天前";
        } else if (timeSpan < 1d * 1000 * 60 * 60 * 24 * 365) {
            Log.e("==========",
                    Math.floor(timeSpan / (1000 * 60 * 60 * 24 * 30)) + "");
            double lastTime = Math.floor(timeSpan / (1000 * 60 * 60 * 24 * 30));
            if (lastTime < 0) {
                lastTime = Math.abs(lastTime);
            }
            return decimalFormat.format(lastTime)
                    + "月前";
        } else {
            return decimalFormat.format(Math.floor(timeSpan
                    / (1000 * 60 * 60 * 24 * 365)))
                    + "年前";
        }
    }

    public static String getFriendlyDate(long time) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六",
                "星期日"};
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long today = calendar.getTimeInMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        String hour = sdf.format(new Date(time));
        calendar.setTimeInMillis(time);
        if (time >= today) {
            return hour;
        }
        if ((today - time) / ONEDAY == 0) {
            return "昨天" + hour;
        }
        if ((today - time) / ONEDAY == 1) {
            return hour;
        }
        if (calendar.get(Calendar.DAY_OF_WEEK) > 0
                && (today - time) / ONEDAY < 7) {
            return weekDays[calendar.get(Calendar.DAY_OF_WEEK)] + " " + hour;
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
                .format(new Date(time));
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getDate() {
        return dateFormat.format(new Date());
    }

    /**
     * 获取指定日期
     *
     * @param time
     * @return
     */
    public static String getDate(long time) {
        return dateFormat.format(new Date(time));
    }

    /**
     * 获取当前日期和时间 ，因为文件名等不允许“：”，因此时间用_隔开
     *
     * @return
     */
    public static String getDateAndTime() {
        return dateAndTimeFormat.format(new Date());
    }

    /**
     * 获取指定日期和时间 ，因为文件名等不允许“：”，因此时间用_隔开
     *
     * @param time
     * @return
     */
    public static String getDateAndTime(long time) {
        return dateAndTimeFormat.format(new Date(time));
    }

    /**
     *
     */
    public static Date stringToDate(String strTime, String formatType) {

        if (strTime == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static long dateToLong(Date date) {
        return date.getTime();
    }

    public static String longToString(long currentTime, String formatType)
            throws ParseException {
        if (currentTime == 0) {
            return "";
        }
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

}
