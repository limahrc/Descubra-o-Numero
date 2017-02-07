package com.ericklima.descubraonumero;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        MediaPlayer opening = MediaPlayer.create(this, R.raw.intro);
        opening.start();
        Handler wait = new Handler();
        wait.postDelayed(this, 3000);
    }

    @Override
    public void run() {
        startActivity(new Intent(this, Registro.class));
        finish();
    }
}
