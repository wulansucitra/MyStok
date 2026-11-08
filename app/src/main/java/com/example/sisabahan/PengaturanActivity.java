package com.example.sisabahan;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PengaturanActivity extends AppCompatActivity {

    private PrefsManager prefs;
    private EditText etNama, etBatas;
    private RadioGroup rgUrutan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootPengaturan), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        prefs = new PrefsManager(this);

        etNama = findViewById(R.id.etNama);
        etBatas = findViewById(R.id.etBatas);
        rgUrutan = findViewById(R.id.rgUrutan);
        Button btnSimpan = findViewById(R.id.btnSimpan);

        tampilkanPengaturan();

        btnSimpan.setOnClickListener(v -> simpanPengaturan());
    }

    private void tampilkanPengaturan() {
        etNama.setText(prefs.getNama());
        etBatas.setText(String.valueOf(prefs.getBatasStok()));

        switch (prefs.getUrutan()) {
            case PrefsManager.URUTAN_TERSEDIKIT:
                rgUrutan.check(R.id.rbTersedikit);
                break;
            case PrefsManager.URUTAN_TERBANYAK:
                rgUrutan.check(R.id.rbTerbanyak);
                break;
            default:
                rgUrutan.check(R.id.rbNama);
        }
    }

    private void simpanPengaturan() {
        String nama = etNama.getText().toString().trim();
        String batasText = etBatas.getText().toString().trim();

        if (nama.isEmpty()) {
            etNama.setError("Nama tidak boleh kosong");
            return;
        }
        if (batasText.isEmpty()) {
            etBatas.setError("Batas stok harus diisi");
            return;
        }

        int urutan = PrefsManager.URUTAN_NAMA;
        int idTerpilih = rgUrutan.getCheckedRadioButtonId();
        if (idTerpilih == R.id.rbTersedikit) {
            urutan = PrefsManager.URUTAN_TERSEDIKIT;
        } else if (idTerpilih == R.id.rbTerbanyak) {
            urutan = PrefsManager.URUTAN_TERBANYAK;
        }

        prefs.setNama(nama);
        prefs.setBatasStok(Integer.parseInt(batasText));
        prefs.setUrutan(urutan);

        Toast.makeText(this, "Pengaturan tersimpan", Toast.LENGTH_SHORT).show();
        finish();
    }
}