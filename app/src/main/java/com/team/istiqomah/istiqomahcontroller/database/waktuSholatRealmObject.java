package com.team.istiqomah.istiqomahcontroller.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by alde.asprilla on 11/10/2016.
 */

public class waktuSholatRealmObject extends RealmObject {
    @PrimaryKey
    private int Id_Sholat;
    private String Nama;
    private String Waktu;
    private String Tanggal;

    public int getId_Sholat() {
        return Id_Sholat;
    }

    public void setId_Sholat(int id_Sholat) {
        Id_Sholat = id_Sholat;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getWaktu() {
        return Waktu;
    }

    public void setWaktu(String waktu) {
        Waktu = waktu;
    }

    public String getTanggal() {
        return Tanggal;
    }

    public void setTanggal(String tanggal) {
        Tanggal = tanggal;
    }
}
