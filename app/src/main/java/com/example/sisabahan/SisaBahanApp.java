package com.example.sisabahan;

import android.app.Application;
import androidx.appcompat.app.AppCompatDelegate;

public class SisaBahanApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PrefsManager prefs = new PrefsManager(this);
        AppCompatDelegate.setDefaultNightMode(
                prefs.isModeGelap()
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO);
    }
}