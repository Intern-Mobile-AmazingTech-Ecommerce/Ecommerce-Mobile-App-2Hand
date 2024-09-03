package com.example.ecommercemobileapp2hand.Views;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.PrimitiveIterator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App extends Application {
    private static Cache<String, Object> cache;
    private static ScheduledExecutorService scheduledExecutorService;
    @Override
    public void onCreate() {
        super.onCreate();
        scheduledExecutorService = Executors.newScheduledThreadPool(4);
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();
        scheduleCacheCleanup();
        configureNightMode();
    }
    public static Cache<String, Object> getCache() {
        return cache;
    }
    public static ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }
    private void scheduleCacheCleanup() {
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            cache.invalidateAll();
        }, 0, 5, TimeUnit.MINUTES);
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdown();
            try {
                if (!scheduledExecutorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    scheduledExecutorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduledExecutorService.shutdownNow();
            }
        }
    }

    private void configureNightMode() {
        SharedPreferences sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);
        AppCompatDelegate.setDefaultNightMode(nightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

}
