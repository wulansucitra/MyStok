package com.example.sisabahan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PrefsManager prefs;
    private DatabaseHelper db;
    private BahanAdapter adapter;

    private TextView tvSapaan, tvTotal, tvHabis, tvKosong, btnTema;
    private RecyclerView rvBahan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        prefs = new PrefsManager(this);
        db = new DatabaseHelper(this);

        tvSapaan = findViewById(R.id.tvSapaan);
        tvTotal = findViewById(R.id.tvTotal);
        tvHabis = findViewById(R.id.tvHabis);
        tvKosong = findViewById(R.id.tvKosong);
        btnTema = findViewById(R.id.btnTema);
        rvBahan = findViewById(R.id.rvBahan);

        adapter = new BahanAdapter(new ArrayList<>(), prefs.getBatasStok(),
                new BahanAdapter.Listener() {
                    @Override
                    public void onClick(Bahan bahan) {
                        Intent i = new Intent(MainActivity.this, PemakaianActivity.class);
                        i.putExtra("id_bahan", bahan.getId());
                        startActivity(i);
                    }

                    @Override
                    public void onLongClick(Bahan bahan) {
                        konfirmasiHapus(bahan);
                    }
                });
        rvBahan.setLayoutManager(new LinearLayoutManager(this));
        rvBahan.setAdapter(adapter);

        findViewById(R.id.btnPengaturan).setOnClickListener(v ->
                startActivity(new Intent(this, PengaturanActivity.class)));
        findViewById(R.id.btnTambah).setOnClickListener(v ->
                startActivity(new Intent(this, TambahBahanActivity.class)));

        // Ikon ganti tema: simpan lewat PrefsManager (apply), lalu terapkan langsung
        btnTema.setOnClickListener(v -> {
            boolean gelapBaru = !prefs.isModeGelap();
            prefs.setModeGelap(gelapBaru);
            AppCompatDelegate.setDefaultNightMode(
                    gelapBaru ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO);
        });
    }

    private void konfirmasiHapus(Bahan bahan) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus bahan?")
                .setMessage(bahan.getNama() + " beserta riwayat pemakaiannya akan dihapus.")
                .setPositiveButton("Hapus", (d, w) -> {
                    db.hapusBahan(bahan.getId());
                    Toast.makeText(this, "Bahan dihapus", Toast.LENGTH_SHORT).show();
                    tampilkanData();
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    // Dipanggil setiap kali layar ini tampil lagi (misalnya kembali dari Pengaturan).
    // Di sinilah pengaturan dibaca ulang lewat PrefsManager.
    @Override
    protected void onResume() {
        super.onResume();
        tampilkanData();
    }

    private void tampilkanData() {
        // Baca dari SharedPreferences
        tvSapaan.setText("Halo, " + prefs.getNama());
        btnTema.setText(prefs.isModeGelap() ? "☀️" : "🌙");

        // Baca dari database
        List<Bahan> daftar = db.getSemuaBahan();

        // Urutan dari SharedPreferences
        int urutan = prefs.getUrutan();
        if (urutan == PrefsManager.URUTAN_TERSEDIKIT) {
            Collections.sort(daftar, (a, b) -> Double.compare(a.getJumlah(), b.getJumlah()));
        } else if (urutan == PrefsManager.URUTAN_TERBANYAK) {
            Collections.sort(daftar, (a, b) -> Double.compare(b.getJumlah(), a.getJumlah()));
        } else {
            Collections.sort(daftar, (a, b) -> a.getNama().compareToIgnoreCase(b.getNama()));
        }

        // Batas stok dari SharedPreferences
        adapter.setData(daftar, prefs.getBatasStok());

        int habis = 0;
        for (Bahan b : daftar) {
            if (b.getJumlah() <= 0) habis++;
        }
        tvTotal.setText(String.valueOf(daftar.size()));
        tvHabis.setText(String.valueOf(habis));

        boolean kosong = daftar.isEmpty();
        tvKosong.setVisibility(kosong ? View.VISIBLE : View.GONE);
        rvBahan.setVisibility(kosong ? View.GONE : View.VISIBLE);
    }
}