package com.example.sisabahan;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class PemakaianActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private Bahan bahan;
    private int idBahan;

    private TextView tvNama, tvStok, tvHariIni, tvSisa;
    private EditText etDipakai, etCatatan;
    private LinearLayout llRiwayat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemakaian);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootPemakaian), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        db = new DatabaseHelper(this);
        idBahan = getIntent().getIntExtra("id_bahan", -1);

        tvNama = findViewById(R.id.tvNamaBahan);
        tvStok = findViewById(R.id.tvStok);
        tvHariIni = findViewById(R.id.tvHariIni);
        tvSisa = findViewById(R.id.tvSisa);
        etDipakai = findViewById(R.id.etDipakai);
        etCatatan = findViewById(R.id.etCatatan);
        llRiwayat = findViewById(R.id.llRiwayat);

        etDipakai.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int a, int b, int c) { }

            @Override
            public void onTextChanged(CharSequence s, int a, int b, int c) { }

            @Override
            public void afterTextChanged(Editable s) {
                hitungSisa();
            }
        });

        findViewById(R.id.btnSimpanPakai).setOnClickListener(v -> simpan());

        muat();
    }

    private void muat() {
        bahan = db.getBahan(idBahan);
        if (bahan == null) {
            finish();
            return;
        }

        String satuan = bahan.getSatuan();
        tvNama.setText(bahan.getNama());
        tvStok.setText("Stok saat ini: "
                + BahanAdapter.formatAngka(bahan.getJumlah()) + " " + satuan);
        tvHariIni.setText("Hari ini dipakai: "
                + BahanAdapter.formatAngka(db.getTotalHariIni(idBahan)) + " " + satuan);

        llRiwayat.removeAllViews();
        List<Pemakaian> riwayat = db.getRiwayat(idBahan);

        if (riwayat.isEmpty()) {
            TextView kosong = new TextView(this);
            kosong.setText("Belum ada pemakaian.");
            kosong.setTextColor(ContextCompat.getColor(this, R.color.teks_abu));
            llRiwayat.addView(kosong);
        }

        for (Pemakaian p : riwayat) {
            TextView tv = new TextView(this);
            String teks = p.getTanggalTampil() + "  ·  "
                    + BahanAdapter.formatAngka(p.getJumlah()) + " " + satuan;
            if (p.getCatatan() != null && !p.getCatatan().isEmpty()) {
                teks += "\n" + p.getCatatan();
            }
            tv.setText(teks);
            tv.setTextColor(ContextCompat.getColor(this, R.color.teks_utama));
            tv.setTextSize(14);
            tv.setGravity(Gravity.START);
            tv.setPadding(0, 10, 0, 10);
            llRiwayat.addView(tv);
        }

        hitungSisa();
    }

    private void hitungSisa() {
        if (bahan == null) return;
        String teks = etDipakai.getText().toString().trim().replace(',', '.');
        if (teks.isEmpty()) {
            tvSisa.setText("Sisa setelah dipakai: -");
            return;
        }
        try {
            double dipakai = Double.parseDouble(teks);
            double sisa = Math.max(0, bahan.getJumlah() - dipakai);
            tvSisa.setText("Sisa setelah dipakai: "
                    + BahanAdapter.formatAngka(sisa) + " " + bahan.getSatuan());
        } catch (NumberFormatException e) {
            tvSisa.setText("Sisa setelah dipakai: -");
        }
    }

    private void simpan() {
        String teks = etDipakai.getText().toString().trim().replace(',', '.');
        if (teks.isEmpty()) {
            etDipakai.setError("Jumlah harus diisi");
            return;
        }

        double dipakai;
        try {
            dipakai = Double.parseDouble(teks);
        } catch (NumberFormatException e) {
            etDipakai.setError("Jumlah tidak valid");
            return;
        }

        if (dipakai <= 0) {
            etDipakai.setError("Jumlah harus lebih dari 0");
            return;
        }
        if (dipakai > bahan.getJumlah()) {
            etDipakai.setError("Melebihi stok yang ada");
            return;
        }

        db.catatPemakaian(idBahan, dipakai, etCatatan.getText().toString().trim());
        etDipakai.setText("");
        etCatatan.setText("");
        Toast.makeText(this, "Pemakaian tercatat", Toast.LENGTH_SHORT).show();
        muat();
    }
}