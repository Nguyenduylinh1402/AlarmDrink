package com.linhnguyen.alarmdrink;

import java.util.Calendar;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmDrink extends ActionBarActivity {
	private AlarmBroadcastReceiver alarm;
	Button btnSetTime, btnClearAlarm;
	TextView txvTimeSet;
	CheckBox cbAutoAlarm;
	TimePickerDialog timePickerDialog;
	private static final int[] WORKINGDAY = { 2, 3, 4, 5, 6 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_drink);
		txvTimeSet = (TextView) findViewById(R.id.txvTimeSet);
		cbAutoAlarm = (CheckBox) findViewById(R.id.cbAutoAlarm);
		alarm = new AlarmBroadcastReceiver();

		cbAutoAlarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (cbAutoAlarm.isChecked()) {
					save(cbAutoAlarm.isChecked());

					Calendar nowTime = Calendar.getInstance();

					Calendar startTime = (Calendar) nowTime.clone();
					startTime.set(Calendar.HOUR_OF_DAY, 8);
					startTime.set(Calendar.MINUTE, 30);
					startTime.set(Calendar.SECOND, 0);
					startTime.set(Calendar.MILLISECOND, 0);

					Calendar endTime = (Calendar) nowTime.clone();
					endTime.set(Calendar.HOUR_OF_DAY, 17);
					endTime.set(Calendar.MINUTE, 30);
					endTime.set(Calendar.SECOND, 0);
					endTime.set(Calendar.MILLISECOND, 0);

					// if (endTime.compareTo(nowTime) < 0) {
					// endTime.add(Calendar.DATE, 1);
					// }

					if (nowTime.DAY_OF_MONTH == 7 | nowTime.DAY_OF_MONTH == 8) {
						startTime.set(Calendar.DAY_OF_WEEK, 2);
						alarm.setAlarm(AlarmDrink.this, startTime);
					} else {
						if (nowTime.compareTo(startTime) > 0
								&& nowTime.compareTo(endTime) < 0) {
							alarm.setAlarm(AlarmDrink.this, nowTime);
						} else {
							{
								startTime.add(Calendar.DATE, 1);
								alarm.setAlarm(AlarmDrink.this, startTime);
								txvTimeSet.setText(startTime
										.get(Calendar.HOUR_OF_DAY)
										+ ":"
										+ startTime.get(Calendar.MINUTE));
							}

						}

					}
				} else {
					save(false);
					openTimePickerDialog(false);
				}

			}
		});

	}

	private void save(final boolean isChecked) {
		SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean("check", isChecked);
		editor.commit();
	}

	private boolean load() {
		SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean("check", false);
	}

	@Override
	protected void onResume() {
		SharedPreferences shareReferences = getSharedPreferences("saveState",
				MODE_PRIVATE);
		long time = shareReferences.getLong("timeSave", 0);

		// String abc = shareReferences.getString("clearAl", "");

		Calendar calen = Calendar.getInstance();
		calen.setTimeInMillis(time);
		// luc đầu set time ở đâu ra ???
		if (shareReferences.getLong("timeSave", 0) == 0) {
			txvTimeSet.setText("");
		} else {
			txvTimeSet.setText(calen.get(Calendar.HOUR_OF_DAY) + ":"
					+ calen.get(Calendar.MINUTE));
		}

		super.onResume();
		cbAutoAlarm.setChecked(load());
	}

	public void clearAlarm(View view) {
		cbAutoAlarm.setChecked(false);
		save(false);
		Context context = this.getApplicationContext();
		if (alarm != null) {

			alarm.cancelAlarm(context);
			txvTimeSet.setText("");
			String clearAl = "";
			SharedPreferences sharePreferences = getSharedPreferences(
					"saveState", MODE_PRIVATE);
			SharedPreferences.Editor editor = sharePreferences.edit();
			editor.clear();
			editor.commit();
		} else
			Toast.makeText(AlarmDrink.this, "Alarm is null", Toast.LENGTH_SHORT)
					.show();
	}

	private void openTimePickerDialog(boolean b) {
		Calendar calender = Calendar.getInstance();
		timePickerDialog = new TimePickerDialog(this, onTimeSetListener,
				calender.get(Calendar.HOUR_OF_DAY),
				calender.get(Calendar.MINUTE), b);
		timePickerDialog.setTitle("Set Alarm Time");

		timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						cbAutoAlarm.setChecked(true);
						save(true);
					}
				});
		timePickerDialog.show();
	}

	OnTimeSetListener onTimeSetListener = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			Calendar calNow = Calendar.getInstance();
			Calendar calSet = (Calendar) calNow.clone();

			txvTimeSet.setText(hourOfDay + ":" + minute);

			calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
			calSet.set(Calendar.MINUTE, minute);
			calSet.set(Calendar.SECOND, 0);
			calSet.set(Calendar.MILLISECOND, 0);

			if (calSet.compareTo(calNow) < 0) {
				calSet.add(Calendar.DATE, 1);
			}

			if (alarm != null) {
				alarm.setAlarm(AlarmDrink.this, calSet);
			} else
				Toast.makeText(AlarmDrink.this, "Alarm is null",
						Toast.LENGTH_SHORT).show();
		}
	};

}
