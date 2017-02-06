package com.team.istiqomah.istiqomahcontroller.helper;

import android.util.Log;

import com.team.istiqomah.istiqomahcontroller.database.persentaseSholatRealmObject;
import com.team.istiqomah.istiqomahcontroller.model.persentaseSholatModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by alde.asprilla on 11/10/2016.
 */

public class persentaseSholatRealmHelper {
    private static final String TAG = "persentaseSholatHelper";

    private Realm realm;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public persentaseSholatRealmHelper(Realm mRealm) {
        realm = mRealm;
    }

    public void addPersentaseSholat(persentaseSholatModel ncc) {
        persentaseSholatRealmObject listObj = new persentaseSholatRealmObject();
        listObj.setId_Persentase(getPrimaryKey());
        listObj.setId_Sholat(ncc.getId_Sholat());
        listObj.setPersentase(ncc.getPersentase());
        listObj.setTanggal(ncc.getTanggal());

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(listObj);
        realm.commitTransaction();

        showLog("Added " + listObj.getId_Persentase() + " : " + listObj.getId_Sholat() + " : " + listObj.getPersentase());
    }

    public ArrayList<persentaseSholatModel> getListPersentaseSholat() {
        ArrayList<persentaseSholatModel> data = new ArrayList<>();

        RealmResults<persentaseSholatRealmObject> rCall = realm.where(persentaseSholatRealmObject.class).findAll();
        if(rCall.size() > 0)
            showLog("Size : " + rCall.size());

        for (int i = 0; i < rCall.size(); i++) {
            data.add(new persentaseSholatModel(rCall.get(i)));
            showLog(rCall.get(i).getId_Persentase() + " " + rCall.get(i).getId_Sholat() + " " + rCall.get(i).getPersentase() + " " + rCall.get(i).getTanggal());
        }

        return data;
    }

    public ArrayList<persentaseSholatModel> getListPersentaseSholat(int sholatId) {
        ArrayList<persentaseSholatModel> data = new ArrayList<>();

        RealmResults<persentaseSholatRealmObject> rCall = realm.where(persentaseSholatRealmObject.class).equalTo("Id_Sholat", sholatId).findAll();
        if(rCall.size() > 0)
            showLog("Size : " + rCall.size());

        for (int i = 0; i < rCall.size(); i++) {
            data.add(new persentaseSholatModel(rCall.get(i)));
        }

        return data;
    }

    public boolean isPrayed(int sholatId){
        Calendar calendar = Calendar.getInstance();

        RealmResults<persentaseSholatRealmObject> rCall = realm.where(persentaseSholatRealmObject.class).equalTo("Id_Sholat", sholatId).equalTo("Tanggal", sdf.format(calendar.getTime())).findAll();
        if(rCall.size() > 0)
            return true;
        else
            return false;
    }

    private int getPrimaryKey(){
        Number num = realm.where(persentaseSholatRealmObject.class).max("Id_Persentase");
        int nextID;
        if(num == null) {
            nextID = 1;
        } else {
            nextID = num.intValue() + 1;
        }
        return nextID;
    }

    private void showLog(String s) {
        Log.d(TAG, s);
    }
}