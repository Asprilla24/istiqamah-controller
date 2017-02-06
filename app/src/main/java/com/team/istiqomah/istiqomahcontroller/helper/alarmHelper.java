package com.team.istiqomah.istiqomahcontroller.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.team.istiqomah.istiqomahcontroller.core.AppData;
import com.team.istiqomah.istiqomahcontroller.service.alarmReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by alde.asprilla on 01/02/2017.
 */

public class alarmHelper {
    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
    private AlarmManager alarmManager;
    private Context mContext;

    public alarmHelper(Context context){
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.mContext = context;
    }

    public void setAlarm(Calendar calendar, String sholatName){
        Intent i = new Intent(mContext, alarmReceiver.class);
        i.putExtra("sholatName", sholatName);
        PendingIntent pi = PendingIntent.getBroadcast(mContext, 1, i, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi); // Millisec * Second * Minute
        showLog(sholatName + " : " + sdf.format(calendar.getTime()));
    }

    public void setSholatSunnahAlarm(Calendar calendar, String sholatName){
        int sholatSunnahId = sholatName.equals("Dhuha") ? AppData.KEY_DHUHA : AppData.KEY_TAHAJUD;

        Intent i = new Intent(mContext, alarmReceiver.class);
        i.putExtra("sholatName", sholatName);

        PendingIntent pi = PendingIntent.getBroadcast(mContext, sholatSunnahId, i, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi); // Millisec * Second * Minute
        showLog(sholatName + " : " + sdf.format(calendar.getTime()));
    }

    public void cancelAlarm(int sholatId) {
        Intent i = new Intent(mContext, alarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, sholatId, i, 0);
        alarmManager.cancel(sender);
    }

    private void showLog(String msg){
        Log.i("AlarmHelper", msg);
    }
}
