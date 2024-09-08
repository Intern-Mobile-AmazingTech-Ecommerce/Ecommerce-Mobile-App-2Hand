package com.example.ecommercemobileapp2hand.Views;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.common.cache.Cache;

import java.util.concurrent.ScheduledExecutorService;

public class App extends Application {
    private static Cache<String, Object> cache;
    private static ScheduledExecutorService scheduledExecutorService;
    @Override
    public void onCreate() {
        super.onCreate();
        configureNightMode();


    }
    private void configureNightMode() {
        SharedPreferences sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);
        AppCompatDelegate.setDefaultNightMode(nightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

}
