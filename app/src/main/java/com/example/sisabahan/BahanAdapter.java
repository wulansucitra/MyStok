package com.example.sisabahan;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class BahanAdapter extends RecyclerView.Adapter<BahanAdapter.VH> {

    public interface Listener {
        void onClick(Bahan bahan);
        void onLongClick(Bahan bahan);
    }

    private List<Bahan> data;
    private int batas;
    private final Listener listener;

    public BahanAdapter(List<Bahan> data, int batas, Listener listener) {
        this.data = data;
        this.batas = batas;
        this.listener = listener;
    }

    public void setData(List<Bahan> data, int batas) {
        this.data = data;
        this.batas = batas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bahan, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Bahan b = data.get(position);

        // Pakai ikon pilihan pengguna; kalau kosong, tebak dari nama
        String ikon = b.getIkon();
        if (ikon == null || ikon.trim().isEmpty()) {
            ikon = ikonUntuk(b.getNama());
        }
        h.tvIkon.setText(ikon);

        h.tvNama.setText(b.getNama());
        h.tvJumlah.setText(formatAngka(b.getJumlah()) + " " + b.getSatuan() + " tersisa");

        int warnaLatar, warnaTeks;
        String teks;
        if (b.getJumlah() <= 0) {
            teks = "Habis";
            warnaLatar = R.color.merah_muda;
            warnaTeks = R.color.merah_tua;
        } else if (b.getJumlah() <= batas) {
            teks = "Menipis";
            warnaLatar = R.color.oranye_muda;
            warnaTeks = R.color.oranye_tua;
        } else {
            teks = "Tersedia";
            warnaLatar = R.color.hijau_muda;
            warnaTeks = R.color.hijau_utama;
        }

        GradientDrawable bg = new GradientDrawable();
        bg.setCornerRadius(100f);
        bg.setColor(ContextCompat.getColor(h.itemView.getContext(), warnaLatar));
        h.tvStatus.setBackground(bg);
        h.tvStatus.setText(teks);
        h.tvStatus.setTextColor(ContextCompat.getColor(h.itemView.getContext(), warnaTeks));

        h.itemView.setOnClickListener(v -> listener.onClick(b));
        h.itemView.setOnLongClickListener(v -> {
            listener.onLongClick(b);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static String formatAngka(double angka) {
        if (angka == Math.floor(angka)) {
            return String.valueOf((long) angka);
        }
        return String.format(new Locale("id", "ID"), "%.2f", angka);
    }

    public static String ikonUntuk(String nama) {
        String n = nama.toLowerCase();

        // Makanan pokok
        if (n.contains("beras") || n.contains("nasi")) return "🍚";
        if (n.contains("mie") || n.contains("indomie")) return "🍜";
        if (n.contains("roti")) return "🍞";
        if (n.contains("tepung")) return "🌾";
        if (n.contains("kentang")) return "🥔";
        if (n.contains("jagung")) return "🌽";

        // Lauk
        if (n.contains("telur")) return "🥚";
        if (n.contains("ayam")) return "🍗";
        if (n.contains("daging") || n.contains("sapi")) return "🥩";
        if (n.contains("ikan") || n.contains("lele") || n.contains("tuna")) return "🐟";
        if (n.contains("udang")) return "🦐";
        if (n.contains("tahu")) return "🧈";
        if (n.contains("tempe")) return "🟫";
        if (n.contains("sosis") || n.contains("nugget")) return "🌭";

        // Sayur
        if (n.contains("bawang")) return "🧅";
        if (n.contains("cabai") || n.contains("cabe") || n.contains("lombok")) return "🌶️";
        if (n.contains("tomat")) return "🍅";
        if (n.contains("wortel")) return "🥕";
        if (n.contains("sayur") || n.contains("bayam") || n.contains("kangkung")
                || n.contains("sawi")) return "🥬";
        if (n.contains("jamur")) return "🍄";
        if (n.contains("kol") || n.contains("brokoli")) return "🥦";
        if (n.contains("timun") || n.contains("mentimun")) return "🥒";

        // Buah
        if (n.contains("pisang")) return "🍌";
        if (n.contains("apel")) return "🍎";
        if (n.contains("jeruk")) return "🍊";
        if (n.contains("semangka")) return "🍉";

        // Bumbu dan minuman
        if (n.contains("minyak")) return "🛢️";
        if (n.contains("gula")) return "🍬";
        if (n.contains("garam")) return "🧂";
        if (n.contains("kecap") || n.contains("saus")) return "🍶";
        if (n.contains("susu")) return "🥛";
        if (n.contains("kopi")) return "☕";
        if (n.contains("teh")) return "🍵";
        if (n.contains("galon")) return "💧";
        if (n.contains("keju")) return "🧀";
        if (n.contains("mentega") || n.contains("margarin")) return "🧈";

        return "🥫";
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvIkon, tvNama, tvJumlah, tvStatus;

        VH(@NonNull View itemView) {
            super(itemView);
            tvIkon = itemView.findViewById(R.id.tvIkon);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvJumlah = itemView.findViewById(R.id.tvJumlah);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}