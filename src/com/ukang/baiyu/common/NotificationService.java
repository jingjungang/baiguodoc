package com.ukang.baiyu.common;

import java.util.ArrayList;
import java.util.List;

import com.ukang.baiyu.entity.PushNotification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NotificationService {
	private DBOpenHelper dbOpenHelper;
	private static NotificationService instance = null;

	public NotificationService(Context context) {
		this.dbOpenHelper = new DBOpenHelper(context);
	}

	public synchronized static NotificationService getInstance(Context ctx) {
		if (null == instance) {
			instance = new NotificationService(ctx);
		}
		return instance;
	}

	public void save(PushNotification notification) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("msg_id", notification.getMsg_id());
		values.put("title", notification.getTitle());
		values.put("content", notification.getContent());
		values.put("activity", notification.getActivity());
		values.put("readState", notification.getReadState());
		values.put("update_time", notification.getUpdate_time());
		db.insert("notification", null, values);
	}

	public void delete(Integer id) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.delete("notification", "id=?", new String[] { id.toString() });
	}

	public void deleteAll() {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.delete("notification", "", null);
	}

	public void update(PushNotification notification) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("msg_id", notification.getMsg_id());
		values.put("title", notification.getTitle());
		values.put("content", notification.getContent());
		values.put("activity", notification.getActivity());
		values.put("readState", notification.getReadState());
		values.put("update_time", notification.getUpdate_time());
		db.update("notification", values, "id=?", new String[] { notification
				.getId().toString() });
	}

	public PushNotification find(Integer id) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db
				.query("notification",
						new String[] { "id,msg_id,title,content,activity,readState,update_time" },
						"id=?", new String[] { id.toString() }, null, null,
						null, "1");
		try {
			if (cursor.moveToFirst()) {
				return new PushNotification(cursor.getInt(cursor
						.getColumnIndex("id")), cursor.getLong(cursor
						.getColumnIndex("msg_id")), cursor.getString(cursor
						.getColumnIndex("title")), cursor.getString(cursor
						.getColumnIndex("content")), cursor.getString(cursor
						.getColumnIndex("activity")), cursor.getInt(cursor
						.getColumnIndex("readState")), cursor.getString(cursor
						.getColumnIndex("update_time")));
			}
			return null;
		} finally {
			cursor.close();
		}
	}

	public List<PushNotification> getScrollData(int currentPage, int lineSize,
			String msg_id) {
		String firstResult = String.valueOf((currentPage - 1) * lineSize);
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = null;
		try {
			if (msg_id == null || "".equals(msg_id)) {
				cursor = db
						.query("notification",
								new String[] { "id,msg_id,title,content,activity,readState,update_time" },
								null, null, null, null, "update_time DESC",
								firstResult + "," + lineSize);
			} else {
				cursor = db
						.query("notification",
								new String[] { "id,msg_id,title,content,activity,readState,update_time" },
								"msg_id like ?", new String[] { msg_id + "%" },
								null, null, "update_time DESC", firstResult
										+ "," + lineSize);
			}
			List<PushNotification> notifications = new ArrayList<PushNotification>();
			while (cursor.moveToNext()) {
				notifications.add(new PushNotification(cursor.getInt(cursor
						.getColumnIndex("id")), cursor.getLong(cursor
						.getColumnIndex("msg_id")), cursor.getString(cursor
						.getColumnIndex("title")), cursor.getString(cursor
						.getColumnIndex("content")), cursor.getString(cursor
						.getColumnIndex("activity")), cursor.getInt(cursor
						.getColumnIndex("readState")), cursor.getString(cursor
						.getColumnIndex("update_time"))));
			}
			return notifications;
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return null;
	}

	public int getCount() {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from notification", null);
		try {
			cursor.moveToFirst();
			return cursor.getInt(0);
		} finally {
			cursor.close();
		}
	}
}
