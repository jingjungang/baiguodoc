package com.ukang.baiyu.views;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 可下拉更新的linearLayout
 * @author SAN
 *
 */
public class PullableLinearLayout extends LinearLayout implements Pullable
{

	public PullableLinearLayout(Context context)
	{
		super(context);
	}

	public PullableLinearLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

//	public PullableTextView(Context context, AttributeSet attrs, int defStyle)
//	{
//		super(context, attrs, defStyle);
//	}

	@Override
	public boolean canPullDown()
	{
		return true;
	}

	@Override
	public boolean canPullUp()
	{
		return false;
	}

}
