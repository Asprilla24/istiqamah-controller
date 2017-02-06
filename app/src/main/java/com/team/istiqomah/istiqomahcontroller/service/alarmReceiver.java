package com.team.istiqomah.istiqomahcontroller.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.team.istiqomah.istiqomahcontroller.core.CoreApplication;
import com.team.istiqomah.istiqomahcontroller.helper.SessionManagement;
import com.team.istiqomah.istiqomahcontroller.helper.alarmHelper;
import com.team.istiqomah.istiqomahcontroller.helper.prayerTimeHelper;
import com.team.istiqomah.istiqomahcontroller.helper.waktuSholatRealmHelper;
import com.team.istiqomah.istiqomahcontroller.model.waktuSholatModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import io.realm.Realm;

/**
 * Created by alde.asprilla on 01/02/2017.
 */

public class alarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Uri alarmUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + context.getPackageName() + "/raw/adzan");

        if (intent.getExtras().getString("sholatName").equals("Subuh")) {
            alarmUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + context.getPackageName() + "/raw/adzan_subuh");
        }

        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                alarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
