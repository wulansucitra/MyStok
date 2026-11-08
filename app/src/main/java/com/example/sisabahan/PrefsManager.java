package com.example.sisabahan;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsManager {

    private static final String NAMA_FILE = "pengaturan_sisabahan";

    private static final String KEY_NAMA = "nama_pengguna";
    private static final String KEY_MODE_GELAP = "mode_gelap";
    private static final String KEY_BATAS_STOK = "batas_stok";
    private static final String KEY_URUTAN = "urutan_daftar";

    public static final int URUTAN_NAMA = 0;
    public static final int URUTAN_TERSEDIKIT = 1;
    public static final int URUTAN_TERBANYAK = 2;

    private final SharedPreferences prefs;

    public PrefsManager(Context context) {
        prefs = context.getSharedPreferences(NAMA_FILE, Context.MODE_PRIVATE);
    }

    public void setNama(String nama) {
        prefs.edit().putString(KEY_NAMA, nama).apply();
    }

    public String getNama() {
        return prefs.getString(KEY_NAMA, "Pengguna");
    }

    public void setModeGelap(boolean aktif) {
        prefs.edit().putBoolean(KEY_MODE_GELAP, aktif).apply();
    }

    public boolean isModeGelap() {
        return prefs.getBoolean(KEY_MODE_GELAP, false);
    }

    public void setBatasStok(int batas) {
        prefs.edit().putInt(KEY_BATAS_STOK, batas).apply();
    }

    public int getBatasStok() {
        return prefs.getInt(KEY_BATAS_STOK, 2);
    }

    public void setUrutan(int urutan) {
        prefs.edit().putInt(KEY_URUTAN, urutan).apply();
    }

    public int getUrutan() {
        return prefs.getInt(KEY_URUTAN, URUTAN_NAMA);
    }
}