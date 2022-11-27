package com.ukang.baiyu.common;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * 屏幕显示工具类
 * @author SAN
 *
 */
public class DisplayUtils {
	/**
	 * 获取屏幕宽度和高度
	 * @param a
	 * @return
	 */
	public static int[] getWidthAndHeight(Activity a){
		DisplayMetrics  dm = new DisplayMetrics();     
		a.getWindowManager().getDefaultDisplay().getMetrics(dm);     
		int screenWidth = dm.widthPixels;               
		int screenHeight = dm.heightPixels;
		int[] result = {screenWidth, screenHeight};
		return result;
	}
}
