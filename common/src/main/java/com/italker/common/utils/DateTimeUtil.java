package com.italker.common.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class DateTimeUtil {


    public static final String FORMAT_YYYYMMDD = "yyyy-MM-dd";
    public static final String FORMAT_YYYYMMDD1 = "yyyy/MM/dd";
    public static final String FORMAT_MMDD1 = "MM/dd";
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yy-MM-dd", Locale.ENGLISH);

    /**
     * 获取一个简单的时间字符串
     *
     * @param date Date
     * @return 时间字符串
     */
    public static String getSampleDate(Date date) {
        return getTime(date.getTime());
    }

    /**
     * 根据间隔的时间返回指定字符串
     * @param past 时间戳
     * @return 间隔的文字
     */
    public static String getTime(long past) {
        if (String.valueOf(past).length() == 10) {
            past = past * 1000;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.DAY_OF_MONTH);
        long now = calendar.getTimeInMillis();
        // 相差的秒数
        long time = (now - past) / 1000;
        StringBuilder sb = new StringBuilder();
        if (time >= 0 && time < 60) { // 1小时内
            return sb.append("刚刚").toString();
        } else if (time >= 60 && time < 3600) {
            return sb.append(time / 60 + "分钟前").toString();
        }
        java.text.DateFormat df = new SimpleDateFormat(FORMAT_YYYYMMDD);
        java.util.Calendar c1 = java.util.Calendar.getInstance();
        java.util.Calendar c2 = java.util.Calendar.getInstance();

        try {
            c1.setTime(df.parse(df.format(now)));
            c2.setTime(df.parse(df.format(past)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long now0 = getTimesmorning();
        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
            if (past - now0 > 0) {
                return sb.append(longToString(past, "a hh:mm")).toString();
            } else if (now0 - past < 86400000) {
                return "昨天";
            } else if (now0 - past < 86400000 * 7) {
                return sb.append(longToString(past, "EEEE")).toString();
            } else {
                return sb.append(longToString(past, FORMAT_MMDD1)).toString();
            }
        } else {
            return sb.append(longToString(past, FORMAT_YYYYMMDD1)).toString();
        }
    }

    /**
     * 获得当天零点时间戳
     */
    private static Long getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }


    /**
     * 时间戳进行时间格式化
     * @param time 时间戳
     * @param format1 时间格式
     * @return 处理后的时间
     */
    @SuppressLint("SimpleDateFormat")
    private static String longToString(Long time, String format1) {
        return new SimpleDateFormat(format1).format(time);
    }
}
