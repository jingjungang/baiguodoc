package com.ukang.baiyu.common;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressLint("SimpleDateFormat")
public class DateUtil {
	/**
	 * 计算给定时间到现在相隔多少天
	 * @param date
	 * @return
	 */
	public static int dateBetwee(String date) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    long to = new Date().getTime();
	    long from = 0;
		try {
			from = df.parse(date).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	    System.out.println((int)(to - from) / (1000 * 60 * 60 * 24));
	    return (int) ((to - from) / (1000 * 60 * 60 * 24));
	}
	
	/**
	 * 计算给定时间到现在相隔多少天，精确计算，用于在数据中心模块
	 * @param date
	 * @return
	 */
	public static float dateBetwee2(String date) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    long to = new Date().getTime();
	    long from = 0;
		try {
			from = df.parse(date).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	    System.out.println((float)(to - from) / (1000 * 60 * 60 * 24));
	    return (float)(to - from) / (1000 * 60 * 60 * 24);
	}
	
	/**
	 * 根据给定日期计算出上周一和上周日日期<BR/>
	 * 返回格式："2015-01-05,2015-01-11"
	 * @param date
	 * @return
	 */
	public static String getStartEndDate(Date date) {
	    Calendar calendar = Calendar.getInstance(Locale.CHINA);
	    calendar.setFirstDayOfWeek(Calendar.MONDAY);//将每周第一天设为星期一，默认是星期天
	    calendar.setTime(date);
	    calendar.add(Calendar.WEEK_OF_MONTH,-1);//周数减一，即上周
	    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);//日子设为星期天
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    String sunday = format.format(calendar.getTime());
	    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);//日子设为星期天
	    String monday = format.format(calendar.getTime());
	    return monday + "," + sunday;
	}
	
	/**
	 * 根据给定日期返回时间段（给定日期向前推六天）
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static String getSevenDay(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long d1 = d.getTime();
		long d2 = d1 - (6 * (1000 * 60 * 60 * 24));
		String date1 = format.format(new Date(d2));
		return date1 + "," + date;
	}
	
	/**
	 * 输入格式yyyy-MM-dd，转换成日期格式：月/日
	 * @param date
	 * @return
	 */
	public static String getMonthDate(String date) throws Exception{
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		d = f.parse(date);
		SimpleDateFormat format = new SimpleDateFormat("MM/dd");
		return format.format(d);
	}
	
	/**
	 * 输入格式MM/dd，转换成日期格式：xx月xx日
	 * @param date
	 * @return
	 */
	public static String getMonthDate2(String date) throws Exception{
		SimpleDateFormat f = new SimpleDateFormat("MM/dd");
		Date d = null;
		d = f.parse(date);
		SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
		return format.format(d);
	}
	
	/**
	 * 根据字符串转换成日期，格式：yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String getYearDate(String date) throws Exception{
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		d = f.parse(date);
		return f.format(d);
	}
	
	/**
	 * 根据字符串转换成日期，格式：yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String getYearDate(Date date){
		System.out.println("date: " + date + "\n " + "time: " + date.getTime());
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return f.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 字符串日子转Date
	 * @param d
	 * @return
	 */
	public static Date toYearDate(String d){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return format.parse(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Long toTimeMil(String d){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return format.parse(d).getTime()/1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
