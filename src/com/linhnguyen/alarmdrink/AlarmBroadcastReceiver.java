package com.linhnguyen.alarmdrink;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.Toast;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

	}

	public void setAlarm(Context context, Calendar calendar) {
		Intent intent = new Intent(context, AlarmAlert.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		AlarmManager alarm = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		long time = calendar.getTimeInMillis();

		alarm.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);

		SharedPreferences sharePreferences = context.getSharedPreferences(
				"saveState", context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharePreferences.edit();
		editor.putLong("timeSave", time);
		editor.commit();
	}

	public void cancelAlarm(Context context) {
		Intent intent = new Intent(context, AlarmAlert.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
	}

}
