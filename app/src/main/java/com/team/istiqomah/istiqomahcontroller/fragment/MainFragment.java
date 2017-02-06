package com.team.istiqomah.istiqomahcontroller.fragment;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.team.istiqomah.istiqomahcontroller.R;
import com.team.istiqomah.istiqomahcontroller.adapter.listSholatAdapter;
import com.team.istiqomah.istiqomahcontroller.core.AppData;
import com.team.istiqomah.istiqomahcontroller.core.CoreApplication;
import com.team.istiqomah.istiqomahcontroller.helper.SessionManagement;
import com.team.istiqomah.istiqomahcontroller.helper.alarmHelper;
import com.team.istiqomah.istiqomahcontroller.helper.persentaseSholatRealmHelper;
import com.team.istiqomah.istiqomahcontroller.helper.prayerTimeHelper;
import com.team.istiqomah.istiqomahcontroller.helper.sholatSunnahRealmHelper;
import com.team.istiqomah.istiqomahcontroller.helper.waktuSholatRealmHelper;
import com.team.istiqomah.istiqomahcontroller.model.persentaseSholatModel;
import com.team.istiqomah.istiqomahcontroller.model.sholatSunnahModel;
import com.team.istiqomah.istiqomahcontroller.model.waktuSholatModel;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    @BindView(R.id.tvSholat)
    TextView tvSholat;
    @BindView(R.id.tvTimer)
    TextView tvTimer;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.listSholat)
    RecyclerView listSholat;
    @BindView(R.id.switchTahajud)
    Switch switchTahajud;
    @BindView(R.id.switchDhuha)
    Switch switchDhuha;
    @BindView(R.id.btnSholat)
    Button btnSholat;

    private Realm realm;
    private waktuSholatRealmHelper waktuSholatHelper;
    private sholatSunnahRealmHelper sholatSunnahHelper;
    private persentaseSholatRealmHelper persentaseRealmHelper;
    private HashMap<String, String> activeUser;
    private listSholatAdapter listSholatAdapter;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        realm = Realm.getDefaultInstance();
        waktuSholatHelper = new waktuSholatRealmHelper(realm);
        sholatSunnahHelper = new sholatSunnahRealmHelper(realm);
        persentaseRealmHelper = new persentaseSholatRealmHelper(realm);
        activeUser = CoreApplication.getInstance().getSession().getActiveInformation();

        //Menambahkan data sholat ke database
        addWaktuSholatToRealm(activeUser.get(SessionManagement.KEY_LONGITUDE), activeUser.get(SessionManagement.KEY_LATITUDE));

        //Inisialisasi widget component
        initView(activeUser.get(SessionManagement.KEY_LOKASI));

        return rootView;
    }

    @OnClick(R.id.btnSholat)
    public void onClick() {
        //Menambahkan persentase ke realm
        addPersentaseToRealm();

        //update layout
        initView(activeUser.get(SessionManagement.KEY_LOKASI));
    }

    private void addPersentaseToRealm() {
        waktuSholatModel sholatNow = waktuSholatHelper.getWaktuSholatSekarang();
        waktuSholatModel sholatPref = waktuSholatHelper.getWaktuSholatSebelum();

        //add persentase to realm
        persentaseSholatModel persentase = new persentaseSholatModel();
        persentase.setId_Sholat(sholatPref.getId_Sholat());
        persentase.setPersentase(calculatePersentase(sholatNow, sholatPref));
        persentase.setTanggal(sdf.format(Calendar.getInstance().getTime()));
        persentaseRealmHelper.addPersentaseSholat(persentase);

        persentaseRealmHelper.getListPersentaseSholat();
    }

    private double calculatePersentase(waktuSholatModel sholatNow, waktuSholatModel sholatPref) {
        String getTimer = tvTimer.getText().toString();
        double timeOnPress = (pecahWaktu("Hour", getTimer) * 3600) +
                (pecahWaktu("Minute", getTimer)) * 60 +
                pecahWaktu("Second", getTimer);

        BigDecimal hasil = new BigDecimal((timeOnPress/jarakWaktuSholatDalamDetik(sholatNow.getWaktu(), sholatPref.getWaktu()))*100);
        hasil.setScale(2, BigDecimal.ROUND_CEILING);

        return hasil.doubleValue();
    }

    private void addWaktuSholatToRealm(String Long, String Lat){
        Calendar calNow = Calendar.getInstance();
        ArrayList<waktuSholatModel> KEY_SHOLAT = AppData.getKeySholat();
        ArrayList<String> listSholat = getDataSholat(Long, Lat, calNow);

        for(int i = 0; i < 5; i++){

            waktuSholatModel obj = KEY_SHOLAT.get(i);

            if(sholatIsPassed(listSholat.get(obj.getId_Sholat()))){
                Calendar calTomorrow = Calendar.getInstance();
                calTomorrow.add(Calendar.DAY_OF_MONTH, 1);

                obj.setWaktu(getDataSholat(Long, Lat, calTomorrow).get(obj.getId_Sholat()));
                obj.setTanggal(sdf.format(calTomorrow.getTime()));
            }else{
                obj.setWaktu(listSholat.get(obj.getId_Sholat()));
                obj.setTanggal(sdf.format(calNow.getTime()));
            }

            waktuSholatHelper.addUpdateWaktuSholat(obj);
        }

        //set alarm sholat saat ini
        setAlarm();
    }

    private void initView(String Location) {
        waktuSholatModel sholatNow = waktuSholatHelper.getWaktuSholatSekarang();
        waktuSholatModel sholatPref = waktuSholatHelper.getWaktuSholatSebelum();
        //cek apakah sudah sholat
        boolean isPrayed = persentaseRealmHelper.isPrayed(sholatPref.getId_Sholat());
        showLog("Tanggal : " + sholatNow.getTanggal() + " , " + "Waktu : " + sholatNow.getWaktu());

        //penyesuaian layout sesudah dan sebelum sholat
        if(isPrayed){
            tvSholat.setText(sholatNow.getNama() + " ( " + sholatNow.getWaktu() + " )");
            tvTimer.setTextColor(getResources().getColor(android.R.color.black));
            btnSholat.setVisibility(View.GONE);
        }
        else{
            tvSholat.setText("Masuk waktu sholat " + sholatPref.getNama());
            tvTimer.setTextColor(getResources().getColor(R.color.colorPrimary));
            btnSholat.setVisibility(View.VISIBLE);
        }

        tvLocation.setText(Location);
        setTimer(sholatNow.getWaktu());

        listSholatAdapter = new listSholatAdapter(waktuSholatHelper.getListWaktuSholat(isPrayed));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listSholat.setLayoutManager(mLayoutManager);
        listSholat.setItemAnimator(new DefaultItemAnimator());
        listSholat.setAdapter(listSholatAdapter);

        //set switcher sholat sunnah
        ArrayList<sholatSunnahModel> listSholatSunnahActive = sholatSunnahHelper.getListSholatSunnahActive();
        for(int i = 0; i < listSholatSunnahActive.size(); i++){
            sholatSunnahModel obj = listSholatSunnahActive.get(i);
            if(obj.getId_SholatSunnah() == AppData.KEY_DHUHA){
                switchDhuha.setChecked(true);
                switchDhuha.setText(obj.getWaktu());

                //set alarm
                setSholatSunnahAlarm(obj.getWaktu(), obj.getNama());
            }else{
                switchTahajud.setChecked(true);
                switchTahajud.setText(obj.getWaktu());
            }
            showLog(obj.getNama());
        }

        switchTahajud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    openTimePickerDialog("Tahajud");
                else
                    cancelSholatSunnah(switchTahajud, AppData.KEY_TAHAJUD);
            }
        });

        switchDhuha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    openTimePickerDialog("Dhuha");
                else
                    cancelSholatSunnah(switchDhuha, AppData.KEY_DHUHA);
            }
        });
    }

    private void setAlarm() {
        //Mengambil waktu sholat dari database
        waktuSholatModel objWaktuSholatSekarang = waktuSholatHelper.getWaktuSholatSekarang();
        //ArrayList<sholatSunnahModel> waktuSholatSunnahAktif = sholatSunnahHelper.getListSholatSunnahActive();

        if(objWaktuSholatSekarang != null){
            //Set waktu sholat ke calendar
            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.DAY_OF_MONTH, pecahWaktu("Day", objWaktuSholatSekarang.getTanggal()));
            calendar.set(Calendar.HOUR_OF_DAY, pecahWaktu("Hour", objWaktuSholatSekarang.getWaktu()));
            calendar.set(Calendar.MINUTE, pecahWaktu("Minute", objWaktuSholatSekarang.getWaktu()));
            calendar.set(Calendar.SECOND, 0);

            //Set Alarm waktu sholat sekarang
            alarmHelper alarmHelper = new alarmHelper(getActivity());
            alarmHelper.setAlarm(calendar, objWaktuSholatSekarang.getNama());
        }
    }

    private void setSholatSunnahAlarm(String waktu, String sholatName) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, pecahWaktu("Hour", waktu));
        calendar.set(Calendar.MINUTE, pecahWaktu("Minute", waktu));
        calendar.set(Calendar.SECOND, 0);

        if(sholatIsPassed(waktu))
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);

        alarmHelper alarm = new alarmHelper(getActivity());
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

    private void openTimePickerDialog(String sholatSunnahName){
        final int sholatSunnahId = sholatSunnahName == "Dhuha" ? AppData.KEY_DHUHA : AppData.KEY_TAHAJUD;
        final Switch sholatSunnahSwitcher = sholatSunnahName == "Dhuha" ? switchDhuha : switchTahajud;

        final sholatSunnahModel sholatSunnahModel = sholatSunnahHelper.getSholatSunnah(sholatSunnahId);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //get picked time
                        String waktuSholatSunnah = String.format("%02d:%02d", hourOfDay, minute);

                        //save picked time to realm
                        sholatSunnahModel.setStatus(true);
                        sholatSunnahModel.setWaktu(waktuSholatSunnah);
                        sholatSunnahHelper.addUpdateSholatSunnah(sholatSunnahModel);

                        //set switcher text
                        sholatSunnahSwitcher.setText(waktuSholatSunnah);

                        //set alarm
                        setSholatSunnahAlarm(waktuSholatSunnah, sholatSunnahModel.getNama());
                    }
                }
                , pecahWaktu("Hour", sholatSunnahModel.getWaktu()),
                pecahWaktu("Minute", sholatSunnahModel.getWaktu()), true);

        timePickerDialog.setTitle("Set "+ sholatSunnahName +" Time");
        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                sholatSunnahSwitcher.setChecked(false);
            }
        });

        timePickerDialog.show();
    }

    private void cancelSholatSunnah(Switch sholatSunnahSwitcher, int sholatSunnahId){
        sholatSunnahModel obj = sholatSunnahHelper.getSholatSunnah(sholatSunnahId);
        obj.setStatus(false);
        sholatSunnahHelper.addUpdateSholatSunnah(obj);

        sholatSunnahSwitcher.setText("");

        alarmHelper alarmHelper = new alarmHelper(getActivity());
        alarmHelper.cancelAlarm(sholatSunnahId);
    }

    private void setTimer(String waktuSholat){
        Long l = jarakWaktuSholatDalamDetik(waktuSholat, shf.format(Calendar.getInstance().getTime()));
        MyCountDownTimer timer = new MyCountDownTimer(l * 1000, 1000);
        timer.start();
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

    private long jarakWaktuSholatDalamDetik(String waktuSholatNow, String waktuSholatPref){
        int JamSholat = pecahWaktu("Hour", waktuSholatNow);
        int MenitSholat = pecahWaktu("Minute", waktuSholatNow);
        int DetikSholat = 0;
        int JamSekarang = pecahWaktu("Hour", waktuSholatPref);
        int MenitSekarang = pecahWaktu("Minute", waktuSholatPref);
        int DetikSekarang = pecahWaktu("Second", waktuSholatPref);

        if(JamSholat != 0 && MenitSholat != 0){
            int jamHasil;
            if(JamSholat < JamSekarang){
                jamHasil = (24 - JamSekarang) + JamSholat;
            }else {
                jamHasil = JamSholat - JamSekarang;
            }
            int menitHasil = (jamHasil * 60 + MenitSholat) - MenitSekarang;
            return (menitHasil * 60 + DetikSholat) - DetikSekarang;
        }
        else
            return 0;
    }

    private int pecahWaktu(String satuan, String waktu){
        switch (satuan){
            case "Day" : return Integer.parseInt(waktu.substring(8, 10));
            case "Hour" : return Integer.parseInt(waktu.substring(0,2));
            case "Minute" : return Integer.parseInt(waktu.substring(3, 5));
            case "Second" : {
                if(waktu.length() == 8)
                    return Integer.parseInt(waktu.substring(6, 8));
                else
                    return 0;
            }
            default : return 0;
        }
    }

    private double getTimeZone(){
        return (Calendar.getInstance().getTimeZone().getOffset(Calendar.getInstance().getTimeInMillis())) / (1000 * 60 * 60);
    }

    class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval){
            super(startTime, interval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String timer = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

            tvTimer.setText(timer);
        }

        @Override
        public void onFinish() {
//            if(!db.sudahSholat("Subuh") && db.getSholat(4, 1)[0][0].equals("Subuh")) {
//                db.insertDashboard("Subuh", 0, sdf.format(Calendar.getInstance().getTime()));
//            }
        }
    }

    private void showLog(String message){
        Log.d(TAG, message);
    }

}
