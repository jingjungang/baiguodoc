package com.ukang.baiyu.receiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.ukang.baiyu.activity.R;
import com.ukang.baiyu.activity.main.TabMainActivity;
import com.ukang.baiyu.entity.PushNotification;
/**
 * 消息推送接收器
 * @author SAN
 *
 */
public class MWDPushReceiver extends BroadcastReceiver {

	private final String TAG = "MWDPushReceiver";
	private String title = "";
	private String content = "";
	private final int NOTIFICATION_ID = 33333;
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "onReceive() action=" + bundle.getInt("action"));
		switch (bundle.getInt(PushConsts.CMD_ACTION)) {
		
		case PushConsts.GET_MSG_DATA:
			// 获取透传数据
			String appid = bundle.getString("appid");
			byte[] payload = bundle.getByteArray("payload");
			String data = null;
			String taskid = bundle.getString("taskid");
			String messageid = bundle.getString("messageid");
			if(payload != null){
				data = new String(payload);
				parseData(data);
			}
			Log.d(TAG, "data: " + data);
			PushNotification notific = new PushNotification();
//			notific.setMsg_id(notifiShowedRlt.getMsgId());
			notific.setTitle(title);
			notific.setContent(content);
			// notificationActionType==1为Activity，2为url，3为intent
			notific.setReadState(0);
			// Activity,url,intent都可以通过getActivity()获得
//			notific.setActivity(notifiShowedRlt.getActivity());
			notific.setUpdate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(Calendar.getInstance().getTime()));
//			NotificationService.getInstance(context).save(notific);
			try {
				DbUtils.create(context).save(notific);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			showNotify(context);
			
			// smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
//			boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
			
			break;
		case PushConsts.GET_CLIENTID:
			// 获取ClientID(CID)
			// 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
			break;
		case PushConsts.THIRDPART_FEEDBACK:
			/*String appid = bundle.getString("appid");
			String taskid = bundle.getString("taskid");
			String actionid = bundle.getString("actionid");
			String result = bundle.getString("result");
			long timestamp = bundle.getLong("timestamp");

			Log.d("GetuiSdkDemo", "appid = " + appid);
			Log.d("GetuiSdkDemo", "taskid = " + taskid);
			Log.d("GetuiSdkDemo", "actionid = " + actionid);
			Log.d("GetuiSdkDemo", "result = " + result);
			Log.d("GetuiSdkDemo", "timestamp = " + timestamp);*/
			break;
		default:
			break;
		}
	}
	
	private void showNotify(Context context){
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIFICATION_ID);
		Notification notification = new Notification();
		notification.icon = R.drawable.ic_launcher;// 设置通知的图标  
        notification.tickerText = ""; // 显示在状态栏中的文字  
        notification.when = System.currentTimeMillis(); // 设置来通知时的时间  
        notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击清除按钮或点击通知后会自动消失  
//        notification.flags |= Notification.f; // 点击清除按钮或点击通知后会自动消失  
        notification.defaults = Notification.DEFAULT_SOUND;// 设置默认铃声  
//        notification.defaults = Notification.DEFAULT_VIBRATE;// 设置默认震动  
        notification.defaults = Notification.DEFAULT_ALL; // 把所有的属性设置成默认
        Intent notificationIntent =new Intent(context, TabMainActivity.class); // 点击该通知后要跳转的Activity     
        notificationIntent.putExtra("msgId", "");
        notificationIntent.putExtra("msgText", "");
        notificationIntent.putExtra("readState", true);
        PendingIntent contentItent = PendingIntent.getActivity(context, 0, notificationIntent, 0);     
        notification.setLatestEventInfo(context, title, content, contentItent);     
          
        // 把Notification传递给NotificationManager     
        notificationManager.notify(NOTIFICATION_ID, notification);  
	}
	
	private void parseData(String data){
		try {
			JSONObject obj = new JSONObject(data);
			title = obj.getString("msg_title");
			content = obj.getString("msg_content");
			if(title == null){
				title = "新的消息";
			}
			if(content == null){
				content = "";
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
