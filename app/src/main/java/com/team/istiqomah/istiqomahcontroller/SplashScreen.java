package com.team.istiqomah.istiqomahcontroller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.team.istiqomah.istiqomahcontroller.core.AppData;
import com.team.istiqomah.istiqomahcontroller.core.CoreApplication;
import com.team.istiqomah.istiqomahcontroller.helper.SessionManagement;
import com.team.istiqomah.istiqomahcontroller.helper.sholatSunnahRealmHelper;
import com.team.istiqomah.istiqomahcontroller.model.sholatSunnahModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;

/**
 * Created by alde.asprilla on 01/11/2016.
 */

public class SplashScreen extends AppCompatActivity implements LocationListener {

    private static final int PERMISSIONS_REQUEST_LOCATION = 100;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
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
                ActivityCompat.requestPermissions(SplashScreen.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
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

    private void getLocation() {
        AppData.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = AppData.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location != null){
            String lat = String.valueOf(location.getLatitude());
            String lng = String.valueOf(location.getLongitude());
            String address = getAddress(location.getLatitude(), location.getLongitude());

            Log.i("LAT", lat);

            session.setActiveInformation(address, lng, lat);

            splashHandler();
        } else{
            AppData.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
    }

    private String getAddress(Double lat, Double lng){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation( lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if(addresses.get(0) != null){
                String address = addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea();

                return address;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        String lat = String.valueOf(location.getLatitude());
        String lng = String.valueOf(location.getLongitude());
        String address = getAddress(location.getLatitude(), location.getLongitude());

        Log.i("LAT", lat);

        session.setActiveInformation(address, lng, lat);

        splashHandler();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}