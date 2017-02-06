package com.team.istiqomah.istiqomahcontroller.model;

import com.team.istiqomah.istiqomahcontroller.database.persentaseSholatRealmObject;

/**
 * Created by alde.asprilla on 11/10/2016.
 */

public class persentaseSholatModel {
    int Id_Persentase;
    int Id_Sholat;
    Double Persentase;
    String Tanggal;

    public persentaseSholatModel(persentaseSholatRealmObject obj){
        this.Id_Persentase = obj.getId_Persentase();
        this.Id_Sholat = obj.getId_Sholat();
        this.Persentase = obj.getPersentase();
        this.Tanggal = obj.getTanggal();
    }

    public persentaseSholatModel(){}

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
