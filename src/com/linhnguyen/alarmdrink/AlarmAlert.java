package com.linhnguyen.alarmdrink;

import java.util.Calendar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AlarmAlert extends ActionBarActivity {
	private AlarmBroadcastReceiver alarm;
	Button btnWait, btnOk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_alert);
		alarm = new AlarmBroadcastReceiver();
		btnOk = (Button) findViewById(R.id.btnOk);
		btnWait = (Button) findViewById(R.id.btnWait);
		btnWait.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar calSet = Calendar.getInstance();
				calSet.add(Calendar.MINUTE, 1);
				alarm.setAlarm(AlarmAlert.this, calSet);

				// save time when wait
				long timeWait = calSet.getTimeInMillis();

				SharedPreferences sharePreferences = getSharedPreferences(
						"saveState", MODE_PRIVATE);
				SharedPreferences.Editor editor = sharePreferences.edit();
				editor.putLong("timeSave", timeWait);
				editor.commit();

				finish();
			}
		});
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(),
						"Shut up and drink right now", Toast.LENGTH_LONG)
						.show();
				Calendar calSet = Calendar.getInstance();
				calSet.add(Calendar.MINUTE, 60);
				alarm.setAlarm(AlarmAlert.this, calSet);

				// save time when wait
				long timeWait = calSet.getTimeInMillis();

				SharedPreferences sharePreferences = getSharedPreferences(
						"saveState", MODE_PRIVATE);
				SharedPreferences.Editor editor = sharePreferences.edit();
				editor.putLong("timeSave", timeWait);
				editor.commit();

				finish();
			}
		});
	}
}
