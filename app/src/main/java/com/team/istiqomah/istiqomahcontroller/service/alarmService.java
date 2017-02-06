package com.team.istiqomah.istiqomahcontroller.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.team.istiqomah.istiqomahcontroller.MainActivity;
import com.team.istiqomah.istiqomahcontroller.R;
import com.team.istiqomah.istiqomahcontroller.core.AppData;
import com.team.istiqomah.istiqomahcontroller.core.CoreApplication;
import com.team.istiqomah.istiqomahcontroller.helper.SessionManagement;
import com.team.istiqomah.istiqomahcontroller.helper.alarmHelper;
import com.team.istiqomah.istiqomahcontroller.helper.prayerTimeHelper;
import com.team.istiqomah.istiqomahcontroller.helper.sholatSunnahRealmHelper;
import com.team.istiqomah.istiqomahcontroller.helper.waktuSholatRealmHelper;
import com.team.istiqomah.istiqomahcontroller.model.sholatSunnahModel;
import com.team.istiqomah.istiqomahcontroller.model.waktuSholatModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import io.realm.Realm;

/**
 * Created by alde.asprilla on 01/02/2017.
 */

public class alarmService extends IntentService {
    private NotificationManager alarmNotificationManager;
    private Realm realm;
    private waktuSholatRealmHelper sholatHelper;
    private sholatSunnahRealmHelper sholatSunnahHelper;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

    public alarmService(){
        super("AlarmService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        realm = Realm.getDefaultInstance();
        sholatHelper = new waktuSholatRealmHelper(realm);
        sholatSunnahHelper = new sholatSunnahRealmHelper(realm);

        if(!Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            sendNotification(intent.getExtras().getString("sholatName"));
            //update sholat now for next day
            updateWaktuSholat();
        }
        //set alarm sholat sekarang
        setAlarm(this);

        realm.close();

        alarmReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        Notification alamNotificationBuilder = new Notification.Builder(this)
                .setContentTitle("Masuk waktu sholat " + msg)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new Notification.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .build();

        alarmNotificationManager.notify(1, alamNotificationBuilder);
        Log.d("AlarmService", "Notification sent.");
    }

    private void updateWaktuSholat(){
        //Mengambil Long Lang dari sharedPref
        HashMap<String, String> activeUser = CoreApplication.getInstance().getSession().getActiveInformation();

        //Mengambil waktu sholat dari database
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        waktuSholatModel objWaktuSholatSekarang = sholatHelper.getWaktuSholatSekarang();
        objWaktuSholatSekarang.setWaktu(
                getDataSholat(activeUser.get(SessionManagement.KEY_LONGITUDE)
                        , activeUser.get(SessionManagement.KEY_LATITUDE)
                        , calendar).get(objWaktuSholatSekarang.getId_Sholat())
        );
        objWaktuSholatSekarang.setTanggal(sdf.format(calendar.getTime()));

        sholatHelper.addUpdateWaktuSholat(objWaktuSholatSekarang);
    }

    private void setAlarm(Context context) {
        //Mengambil waktu sholat dari database
        waktuSholatModel objWaktuSholatSekarang = sholatHelper.getWaktuSholatSekarang();
        ArrayList<sholatSunnahModel> listSholatSunnahActive = sholatSunnahHelper.getListSholatSunnahActive();

        if(objWaktuSholatSekarang != null){
            //Set waktu sholat ke calendar
            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.DAY_OF_MONTH, pecahWaktu("Day", objWaktuSholatSekarang.getTanggal()));
            calendar.set(Calendar.HOUR_OF_DAY, pecahWaktu("Hour", objWaktuSholatSekarang.getWaktu()));
            calendar.set(Calendar.MINUTE, pecahWaktu("Minute", objWaktuSholatSekarang.getWaktu()));
            calendar.set(Calendar.SECOND, 0);

            //Set Alarm waktu sholat sekarang
            alarmHelper alarmHelper = new alarmHelper(context);
            alarmHelper.setAlarm(calendar, objWaktuSholatSekarang.getNama());
        }

        //set alarm sholat sunnah
        for(int i = 0; i < listSholatSunnahActive.size(); i++){
            sholatSunnahModel obj = listSholatSunnahActive.get(i);
            setSholatSunnahAlarm(obj.getWaktu(), obj.getNama());
        }
    }

    private void setSholatSunnahAlarm(String waktu, String sholatName) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, pecahWaktu("Hour", waktu));
        calendar.set(Calendar.MINUTE, pecahWaktu("Minute", waktu));
        calendar.set(Calendar.SECOND, 0);

        if(sholatIsPassed(waktu))
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);

        alarmHelper alarm = new alarmHelper(this);
        alarm.setSholatSunnahAlarm(calendar, sholatName);
    }

    private ArrayList<String> getDataSholat(String Long, String Lat, Calendar calNow) {
        double timezone = getTimeZone();
        double Longitude = Double.parseDouble(Long);
        double Latitude = Double.parseDouble(Lat);

        //Mengambil perhitungan waktu sholat
        prayerTimeHelper prayerTimes = new prayerTimeHelper();

        return prayerTimes.getPrayerTimes(calNow.get(Calendar.YEAR)
                , calNow.get(Calendar.MONTH)
                , calNow.get(Calendar.DAY_OF_MONTH)
                , Latitude
                , Longitude
                , timezone);
    }

    private boolean sholatIsPassed(String waktuSholat){
        String waktuSekarang = shf.format(Calendar.getInstance().getTime());

        if(pecahWaktu("Hour", waktuSholat) < pecahWaktu("Hour", waktuSekarang) ||
                (pecahWaktu("Hour", waktuSholat) == pecahWaktu("Hour", waktuSekarang) &&
                        pecahWaktu("Minute", waktuSholat) <= pecahWaktu("Minute", waktuSekarang)))
            return true;
        else
            return false;
    }

    private int pecahWaktu(String satuan, String waktu){
        switch (satuan){
            case "Day" : return Integer.parseInt(waktu.substring(8, 10));
            case "Hour" : return Integer.parseInt(waktu.substring(0,2));
            case "Minute" : return Integer.parseInt(waktu.substring(3, 5));
            case "Second" : return Integer.parseInt(waktu.substring(6, 8));
            default : return 0;
        }
    }

    private double getTimeZone(){
        return (Calendar.getInstance().getTimeZone().getOffset(Calendar.getInstance().getTimeInMillis())) / (1000 * 60 * 60);
    }
}
