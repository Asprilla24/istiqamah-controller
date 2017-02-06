package com.team.istiqomah.istiqomahcontroller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.team.istiqomah.istiqomahcontroller.core.AppData;
import com.team.istiqomah.istiqomahcontroller.core.CoreApplication;
import com.team.istiqomah.istiqomahcontroller.helper.GPSTracker;
import com.team.istiqomah.istiqomahcontroller.helper.SessionManagement;
import com.team.istiqomah.istiqomahcontroller.helper.prayerTimeHelper;
import com.team.istiqomah.istiqomahcontroller.helper.sholatSunnahRealmHelper;
import com.team.istiqomah.istiqomahcontroller.model.sholatSunnahModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;

/**
 * Created by alde.asprilla on 01/11/2016.
 */

public class SplashScreen extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_LOCATION = 100;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private GPSTracker gps;
    private SessionManagement session;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        session = new SessionManagement(this);
        realm = Realm.getDefaultInstance();

        // check status apakah user sudah login
        if(CoreApplication.getInstance().getSession().isFistOpen()) {
            if(ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(SplashScreen.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_LOCATION);
            }else{
                getLocation();
            }

            setSholatSunnahTime();

        }else{
            splashHandler();
        }
    }

    private void setSholatSunnahTime() {
        sholatSunnahRealmHelper sholatSunnahHelper = new sholatSunnahRealmHelper(realm);

        //set default sholat sunnah dhuha
        sholatSunnahModel obj = new sholatSunnahModel();
        obj.setId_SholatSunnah(AppData.KEY_DHUHA);
        obj.setNama("Dhuha");
        obj.setWaktu("07:00");
        obj.setStatus(false);
        sholatSunnahHelper.addUpdateSholatSunnah(obj);

        //set default sholat sunnah tahajud
        obj.setId_SholatSunnah(AppData.KEY_TAHAJUD);
        obj.setNama("Tahajud");
        obj.setWaktu("01:00");
        obj.setStatus(false);
        sholatSunnahHelper.addUpdateSholatSunnah(obj);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSIONS_REQUEST_LOCATION : {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    splashHandler();
                }else{
                    finish();
                }
                break;
            }
        }
    }

    private void splashHandler(){
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over

                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void getLocation(){
        gps = new GPSTracker(SplashScreen.this);

        String address = null;

        // check if GPS enabled
        do {
            if (gps.canGetLocation()) {

                String latitude = String.valueOf(gps.getLatitude());
                String longitude = String.valueOf(gps.getLongitude());
                address = getAddress(gps.getLatitude(), gps.getLongitude());

                session.setActiveInformation(address, longitude, latitude);

                Log.d("Long", longitude);
                Log.d("Lat", latitude);

                splashHandler();
            }
        }while(address == null);

        gps.stopUsingGPS();
    }

    private String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String address = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            address = obj.getLocality() + ", " + obj.getAdminArea();
            Log.v("IGA", "Address" + address);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return address;
    }
}