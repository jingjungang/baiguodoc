package com.ukang.baiyu.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	public DBOpenHelper(Context context) {
		super(context, "maiwaidi.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//创建通知消息表
		db.execSQL("CREATE TABLE notification (id integer primary key autoincrement,msg_id varchar(64),title varchar(128),activity varchar(256),readState integer,content text,update_time varchar(16))");
		//创建浏览场所历史表
		db.execSQL("CREATE TABLE shops_hostory (id integer primary key autoincrement,shop_id varchar(20) unique,name varchar(64),brandname varchar(128),address varchar(128),province_name varchar(20),update_time varchar(16))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//创建通知消息表
		db.execSQL("CREATE TABLE notification (id integer primary key autoincrement,msg_id varchar(64),title varchar(128),activity varchar(256),readState integer,content text,update_time varchar(16))");
		//创建浏览场所历史表
		db.execSQL("CREATE TABLE shops_hostory (id integer primary key autoincrement,shop_id varchar(20) unique,name varchar(64),brandname varchar(128),address varchar(128),province_name varchar(20),update_time varchar(16))");
	}

}
