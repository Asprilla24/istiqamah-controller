package com.team.istiqomah.istiqomahcontroller.helper;

import android.util.Log;

import com.team.istiqomah.istiqomahcontroller.database.waktuSholatRealmObject;
import com.team.istiqomah.istiqomahcontroller.model.waktuSholatModel;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by alde.asprilla on 11/10/2016.
 */

public class waktuSholatRealmHelper {
    private static final String TAG = "waktuSholatRealmHelper";

    private Realm realm;

    public waktuSholatRealmHelper(Realm mRealm) {
        realm = mRealm;
    }

    public void addUpdateWaktuSholat(waktuSholatModel ncc) {
        waktuSholatRealmObject listObj = new waktuSholatRealmObject();
        listObj.setId_Sholat(ncc.getId_Sholat());
        listObj.setNama(ncc.getNama());
        listObj.setWaktu(ncc.getWaktu());
        listObj.setTanggal(ncc.getTanggal());

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(listObj);
        realm.commitTransaction();

        showLog("Added " + listObj.getNama());
    }

    public ArrayList<waktuSholatModel> getListWaktuSholat(boolean isPrayed) {
        ArrayList<waktuSholatModel> data = new ArrayList<>();

        RealmResults<waktuSholatRealmObject> rCall = realm.where(waktuSholatRealmObject.class).findAllSorted("Tanggal", Sort.ASCENDING, "Waktu", Sort.ASCENDING);
        if(rCall.size() > 0)
            showLog("Size : " + rCall.size());

        for (int i = isPrayed ? 1 : 0; i < (isPrayed ? rCall.size() : rCall.size() - 1); i++) {
            data.add(new waktuSholatModel(rCall.get(i)));
            showLog("Sholat ke-" + i + " : " + rCall.get(i).getNama());
        }

        return data;
    }

    public waktuSholatModel getWaktuSholatSekarang(){
        RealmResults<waktuSholatRealmObject> rCall = realm.where(waktuSholatRealmObject.class).findAllSorted("Tanggal", Sort.ASCENDING, "Waktu", Sort.ASCENDING);
        if(rCall.size() > 0)
            return new waktuSholatModel(rCall.get(0));
        else
            return null;
    }

    public waktuSholatModel getWaktuSholatSebelum(){
        RealmResults<waktuSholatRealmObject> rCall = realm.where(waktuSholatRealmObject.class).findAllSorted("Tanggal", Sort.ASCENDING, "Waktu", Sort.ASCENDING);
        if(rCall.size() > 0)
            return new waktuSholatModel(rCall.get(4));
        else
            return null;
    }

    private void showLog(String s) {
        Log.d(TAG, s);
    }
}
