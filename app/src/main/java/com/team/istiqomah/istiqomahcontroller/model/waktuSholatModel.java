package com.team.istiqomah.istiqomahcontroller.model;

import com.team.istiqomah.istiqomahcontroller.database.waktuSholatRealmObject;

/**
 * Created by alde.asprilla on 11/10/2016.
 */

public class waktuSholatModel {
    int Id_Sholat;
    String Nama;
    String Waktu;
    String Tanggal;

    public waktuSholatModel(waktuSholatRealmObject obj){
        this.Id_Sholat = obj.getId_Sholat();
        this.Nama = obj.getNama();
        this.Waktu = obj.getWaktu();
        this.Tanggal = obj.getTanggal();
    }

    public waktuSholatModel(int id_Sholat, String nama){
        this.Id_Sholat = id_Sholat;
        this.Nama = nama;
    }

    public waktuSholatModel(){}

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
