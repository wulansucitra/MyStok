package com.example.sisabahan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAMA = "sisabahan.db";
    private static final int DB_VERSI = 3;

    public DatabaseHelper(Context context) {
        super(context, DB_NAMA, null, DB_VERSI);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE bahan (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nama TEXT NOT NULL, " +
                "jumlah REAL NOT NULL, " +
                "satuan TEXT NOT NULL, " +
                "ikon TEXT)");

        db.execSQL("CREATE TABLE pemakaian (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_bahan INTEGER NOT NULL, " +
                "jumlah_dipakai REAL NOT NULL, " +
                "tanggal TEXT NOT NULL, " +
                "catatan TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int lama, int baru) {
        db.execSQL("DROP TABLE IF EXISTS pemakaian");
        db.execSQL("DROP TABLE IF EXISTS bahan");
        onCreate(db);
    }

    private String hariIni() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
    }

    public void tambahBahan(String nama, double jumlah, String satuan, String ikon) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nama", nama);
        cv.put("jumlah", jumlah);
        cv.put("satuan", satuan);
        cv.put("ikon", ikon);
        db.insert("bahan", null, cv);
    }

    public List<Bahan> getSemuaBahan() {
        List<Bahan> daftar = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id, nama, jumlah, satuan, ikon FROM bahan", null);
        while (c.moveToNext()) {
            daftar.add(new Bahan(c.getInt(0), c.getString(1),
                    c.getDouble(2), c.getString(3), c.getString(4)));
        }
        c.close();
        return daftar;
    }

    public Bahan getBahan(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id, nama, jumlah, satuan, ikon FROM bahan WHERE id = ?",
                new String[]{String.valueOf(id)});
        Bahan b = null;
        if (c.moveToFirst()) {
            b = new Bahan(c.getInt(0), c.getString(1), c.getDouble(2),
                    c.getString(3), c.getString(4));
        }
        c.close();
        return b;
    }

    public void hapusBahan(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete("pemakaian", "id_bahan = ?", new String[]{String.valueOf(id)});
            db.delete("bahan", "id = ?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void catatPemakaian(int idBahan, double dipakai, String catatan) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("UPDATE bahan SET jumlah = jumlah - ? WHERE id = ?",
                    new Object[]{dipakai, idBahan});

            ContentValues cv = new ContentValues();
            cv.put("id_bahan", idBahan);
            cv.put("jumlah_dipakai", dipakai);
            cv.put("tanggal", hariIni());
            cv.put("catatan", catatan);
            db.insert("pemakaian", null, cv);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<Pemakaian> getRiwayat(int idBahan) {
        List<Pemakaian> daftar = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id, jumlah_dipakai, tanggal, catatan " +
                        "FROM pemakaian WHERE id_bahan = ? ORDER BY id DESC",
                new String[]{String.valueOf(idBahan)});
        while (c.moveToNext()) {
            daftar.add(new Pemakaian(c.getInt(0), c.getDouble(1),
                    c.getString(2), c.getString(3)));
        }
        c.close();
        return daftar;
    }

    public double getTotalHariIni(int idBahan) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(jumlah_dipakai) FROM pemakaian " +
                        "WHERE id_bahan = ? AND tanggal = ?",
                new String[]{String.valueOf(idBahan), hariIni()});
        double total = 0;
        if (c.moveToFirst()) {
            total = c.getDouble(0);
        }
        c.close();
        return total;
    }
}