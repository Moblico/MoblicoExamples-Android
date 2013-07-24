package com.moblico.example.push;

import com.moblico.services.PushServices;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class PushReceiver extends BroadcastReceiver {
	private static int count = 1;

	@Override
	public void onReceive(Context context, Intent intent) {		   
		if (intent != null && intent.getExtras() != null) {
			String regId = intent.getExtras().getString("registration_id");
			if(regId != null && !regId.equals("")) {
				PushServices.setRegistrationId(context, regId);
			}

			String msg = intent.getExtras().getString("message");
			if (msg != null) {
				setResultCode(Activity.RESULT_OK);

				NotificationManager notificationManager = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);

				Intent notificationIntent = new Intent(context,
						MainActivity.class);
				notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				PendingIntent pendingIntent = PendingIntent.getActivity(
						context, 0, notificationIntent, 0);

				Notification note = new NotificationCompat.Builder(context)
						.setContentTitle("App Notification")
						.setContentText(msg).setContentIntent(pendingIntent)
						.setSmallIcon(R.drawable.ic_launcher)
						.setAutoCancel(true)
						.setDefaults(Notification.DEFAULT_ALL)
						.setNumber(count++).setWhen(System.currentTimeMillis())
						.build();
				notificationManager.notify(0, note);
			}
		}
	}
}
