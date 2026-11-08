# MyStok

Sistem Pengelolaan Stok dan Pemakaian Bahan Makanan Berbasis Android.

Aplikasi untuk mahasiswa kos dan rumah tangga agar bisa mencatat bahan makanan,
mencatat pemakaian harian, dan melihat sisa stok secara otomatis.

## Fitur
- Tambah bahan dengan ikon otomatis atau pilihan sendiri
- Catat pemakaian harian beserta catatan
- Hapus bahan dengan tekan lama
- Status stok: Tersedia, Menipis, Habis
- Mode gelap/terang lewat ikon
- Pengaturan: nama pengguna, batas stok menipis, urutan daftar

## Penyimpanan Data
- **SharedPreferences** (`PrefsManager`): nama pengguna, mode gelap, batas stok, urutan daftar
- **SQLite** (`DatabaseHelper`): data bahan dan riwayat pemakaian

## Teknologi
Java, Android Studio, SQLite, SharedPreferences

Tugas P10 Pemrograman Mobile, Prodi STI, Universitas Muhammadiyah Kotabumi
