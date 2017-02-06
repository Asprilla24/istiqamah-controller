package com.team.istiqomah.istiqomahcontroller.model;

import com.team.istiqomah.istiqomahcontroller.database.sholatSunnahRealmObject;

/**
 * Created by alde.asprilla on 11/10/2016.
 */

public class sholatSunnahModel {
    int Id_SholatSunnah;
    String Nama;
    String Waktu;
    boolean Status;

    public sholatSunnahModel(sholatSunnahRealmObject obj){
        this.Id_SholatSunnah = obj.getId_SholatSunnah();
        this.Nama = obj.getNama();
        this.Waktu = obj.getWaktu();
        this.Status = obj.getStatus();
    }

    public sholatSunnahModel(){}

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
