package com.example.sisabahan;

public class Bahan {
    private int id;
    private String nama;
    private double jumlah;
    private String satuan;
    private String ikon;

    public Bahan(int id, String nama, double jumlah, String satuan, String ikon) {
        this.id = id;
        this.nama = nama;
        this.jumlah = jumlah;
        this.satuan = satuan;
        this.ikon = ikon;
    }

    public int getId() { return id; }
    public String getNama() { return nama; }
    public double getJumlah() { return jumlah; }
    public String getSatuan() { return satuan; }
    public String getIkon() { return ikon; }
}