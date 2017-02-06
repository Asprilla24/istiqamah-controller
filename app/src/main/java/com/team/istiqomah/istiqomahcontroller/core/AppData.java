package com.team.istiqomah.istiqomahcontroller.core;

import com.team.istiqomah.istiqomahcontroller.model.waktuSholatModel;

import java.util.ArrayList;

/**
 * Created by alde.asprilla on 02/02/2017.
 */

public class AppData {
    public static final int KEY_ISYA = 6;
    public static final int KEY_SUBUH = 0;
    public static final int KEY_DHUHUR = 2;
    public static final int KEY_ASHAR = 3;
    public static final int KEY_MAGHRIB = 4;
    public static final int KEY_DHUHA = 7;
    public static final int KEY_TAHAJUD = 8;

    private static ArrayList<waktuSholatModel> KEY_SHOLAT = new ArrayList<>();

    public static ArrayList<waktuSholatModel> getKeySholat(){
        KEY_SHOLAT.add(new waktuSholatModel(KEY_ISYA, "Isya"));
        KEY_SHOLAT.add(new waktuSholatModel(KEY_SUBUH, "Subuh"));
        KEY_SHOLAT.add(new waktuSholatModel(KEY_DHUHUR, "Dhuhur"));
        KEY_SHOLAT.add(new waktuSholatModel(KEY_ASHAR, "Ashar"));
        KEY_SHOLAT.add(new waktuSholatModel(KEY_MAGHRIB, "Maghrib"));
        return KEY_SHOLAT;
    }
}
