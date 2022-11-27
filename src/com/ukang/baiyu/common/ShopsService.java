package com.ukang.baiyu.common;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ShopsService {
	private final String TAG = "ShopsService";
	private DBOpenHelper dbOpenHelper;
	private static ShopsService instance = null;

	public ShopsService(Context context) {
		this.dbOpenHelper = new DBOpenHelper(context);
	}

	public synchronized static ShopsService getInstance(Context ctx) {
		if (null == instance) {
			instance = new ShopsService(ctx);
		}
		return instance;
	}
	
	public void delete(Integer id) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.delete("shops_hostory", "id=?", new String[] { id.toString() });
	}

	public void deleteAll() {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.delete("shops_hostory", "", null);
	}

	public int getCount() {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from shops_hostory", null);
		try {
			cursor.moveToFirst();
			return cursor.getInt(0);
		} finally {
			cursor.close();
		}
	}
}
