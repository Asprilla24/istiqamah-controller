package com.team.istiqomah.istiqomahcontroller.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by alde.asprilla on 11/10/2016.
 */

public class sholatSunnahRealmObject extends RealmObject {
    @PrimaryKey
    private int Id_SholatSunnah;
    private String Nama;
    private String Waktu;
    private boolean Status;

    public int getId_SholatSunnah() {
        return Id_SholatSunnah;
    }

    public void setId_SholatSunnah(int id_SholatSunnah) {
        Id_SholatSunnah = id_SholatSunnah;
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

    public boolean getStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }
}
