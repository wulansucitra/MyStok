package com.example.sisabahan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Pemakaian {
    private int id;
    private double jumlah;
    private String tanggal; // format yyyy-MM-dd
    private String catatan;

    public Pemakaian(int id, double jumlah, String tanggal, String catatan) {
        this.id = id;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
        this.catatan = catatan;
    }

    public int getId() { return id; }
    public double getJumlah() { return jumlah; }
    public String getCatatan() { return catatan; }

    public String getTanggalTampil() {
        try {
            SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String hariIni = iso.format(new Date());
            if (tanggal.equals(hariIni)) return "Hari ini";
            Date d = iso.parse(tanggal);
            return new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID")).format(d);
        } catch (Exception e) {
            return tanggal;
        }
    }
}