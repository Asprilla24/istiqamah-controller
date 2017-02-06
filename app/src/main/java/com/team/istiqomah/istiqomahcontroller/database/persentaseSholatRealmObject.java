package com.team.istiqomah.istiqomahcontroller.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by alde.asprilla on 11/10/2016.
 */

public class persentaseSholatRealmObject extends RealmObject{
    @PrimaryKey
    private int Id_Persentase;
    private int Id_Sholat;
    private Double Persentase;
    private String Tanggal;

    public int getId_Persentase() {
        return Id_Persentase;
    }

    public void setId_Persentase(int id_Persentase) {
        Id_Persentase = id_Persentase;
    }

    public int getId_Sholat() {
        return Id_Sholat;
    }

    public void setId_Sholat(int id_Sholat) {
        Id_Sholat = id_Sholat;
    }

    public Double getPersentase() {
        return Persentase;
    }

    public void setPersentase(Double persentase) {
        Persentase = persentase;
    }

    public String getTanggal() {
        return Tanggal;
    }

    public void setTanggal(String tanggal) {
        Tanggal = tanggal;
    }
}
