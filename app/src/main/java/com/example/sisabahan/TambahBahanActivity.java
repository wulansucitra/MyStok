package com.example.sisabahan;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TambahBahanActivity extends AppCompatActivity {

    private static final String[] PILIHAN_EMOJI = {
            "🍚", "🥚", "🍜", "🍗", "🥩", "🐟", "🧅", "🌶️", "🍅", "🥕",
            "🥬", "🍞", "🥛", "☕", "🧂", "🍬", "🛢️", "🍌", "🍎", "🥫"
    };

    private EditText etNamaBahan, etJumlah, etIkon;
    private TextView tvPratinjau;
    private Spinner spSatuan;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_bahan);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootTambah), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        db = new DatabaseHelper(this);

        etNamaBahan = findViewById(R.id.etNamaBahan);
        etJumlah = findViewById(R.id.etJumlah);
        etIkon = findViewById(R.id.etIkon);
        tvPratinjau = findViewById(R.id.tvPratinjau);
        spSatuan = findViewById(R.id.spSatuan);

        String[] satuan = {"kg", "gram", "liter", "butir", "buah", "bungkus"};
        spSatuan.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, satuan));

        siapkanPilihanEmoji();

        // Pratinjau ikut berubah saat nama atau emoji diubah
        TextWatcher pemantau = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int a, int b, int c) { }

            @Override
            public void onTextChanged(CharSequence s, int a, int b, int c) { }

            @Override
            public void afterTextChanged(Editable s) {
                perbaruiPratinjau();
            }
        };
        etNamaBahan.addTextChangedListener(pemantau);
        etIkon.addTextChangedListener(pemantau);

        findViewById(R.id.btnSimpanBahan).setOnClickListener(v -> simpan());
    }

    private void siapkanPilihanEmoji() {
        LinearLayout ll = findViewById(R.id.llEmoji);
        for (String emoji : PILIHAN_EMOJI) {
            TextView tv = new TextView(this);
            tv.setText(emoji);
            tv.setTextSize(28);
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(18, 10, 18, 10);
            tv.setOnClickListener(v -> etIkon.setText(emoji));
            ll.addView(tv);
        }
    }

    // Ikon yang dipakai: pilihan pengguna, atau tebakan dari nama kalau kosong
    private String ikonTerpilih() {
        String manual = etIkon.getText().toString().trim();
        if (!manual.isEmpty()) return manual;
        return BahanAdapter.ikonUntuk(etNamaBahan.getText().toString().trim());
    }

    private void perbaruiPratinjau() {
        tvPratinjau.setText(ikonTerpilih());
    }

    private void simpan() {
        String nama = etNamaBahan.getText().toString().trim();
        String jumlahText = etJumlah.getText().toString().trim().replace(',', '.');

        if (nama.isEmpty()) {
            etNamaBahan.setError("Nama bahan harus diisi");
            return;
        }
        if (jumlahText.isEmpty()) {
            etJumlah.setError("Jumlah harus diisi");
            return;
        }

        double jumlah;
        try {
            jumlah = Double.parseDouble(jumlahText);
        } catch (NumberFormatException e) {
            etJumlah.setError("Jumlah tidak valid");
            return;
        }

        db.tambahBahan(nama, jumlah, spSatuan.getSelectedItem().toString(), ikonTerpilih());
        Toast.makeText(this, "Bahan tersimpan", Toast.LENGTH_SHORT).show();
        finish();
    }
}