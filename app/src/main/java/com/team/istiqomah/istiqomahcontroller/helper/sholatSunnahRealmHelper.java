package com.team.istiqomah.istiqomahcontroller.helper;

import android.content.Context;
import android.util.Log;

import com.team.istiqomah.istiqomahcontroller.database.sholatSunnahRealmObject;
import com.team.istiqomah.istiqomahcontroller.model.sholatSunnahModel;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by alde.asprilla on 11/10/2016.
 */

public class sholatSunnahRealmHelper {
    private static final String TAG = "sholatSunnahRealmHelper";

    private Realm realm;

    public sholatSunnahRealmHelper(Realm mRealm) {
        realm = mRealm;
    }

    public void addUpdateSholatSunnah(sholatSunnahModel ncc) {
        sholatSunnahRealmObject listObj = new sholatSunnahRealmObject();
        listObj.setId_SholatSunnah(ncc.getId_SholatSunnah());
        listObj.setNama(ncc.getNama());
        listObj.setWaktu(ncc.getWaktu());
        listObj.setStatus(ncc.getStatus());

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(listObj);
        realm.commitTransaction();

        showLog("Added " + listObj.getNama());
    }

    public sholatSunnahModel getSholatSunnah(int id) {
        return new sholatSunnahModel(realm.where(sholatSunnahRealmObject.class).equalTo("Id_SholatSunnah", id).findFirst());
    }

    public ArrayList<sholatSunnahModel> getListSholatSunnahActive() {
        ArrayList<sholatSunnahModel> data = new ArrayList<>();

        RealmResults<sholatSunnahRealmObject> rCall = realm.where(sholatSunnahRealmObject.class).equalTo("Status", true).findAll();
        if(rCall.size() > 0)
            showLog("Size : " + rCall.size());

        for (int i = 0; i < rCall.size(); i++) {
            data.add(new sholatSunnahModel(rCall.get(i)));
        }

        return data;
    }

    private void showLog(String s) {
        Log.d(TAG, s);
    }
}
