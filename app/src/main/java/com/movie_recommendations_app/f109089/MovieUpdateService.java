package com.movie_recommendations_app.f109089;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

public class MovieUpdateService extends Service {

    private Handler handler;
    private Runnable updateTask;

    private final int ONE_HOUR = 60 * 60 * 1000;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper());
        updateTask = new Runnable() {
            @Override
            public void run() {
                updateMovies();
                handler.postDelayed(this, ONE_HOUR); // Update every hour
            }
        };
        handler.post(updateTask);
    }

    private void updateMovies() {
        Toast.makeText(this, "Movies updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateTask);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
