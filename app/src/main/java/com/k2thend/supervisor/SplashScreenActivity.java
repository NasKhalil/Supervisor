package com.k2thend.supervisor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.k2thend.supervisor.test.EmailActivity;
import com.k2thend.supervisor.utils.Utils;

public class SplashScreenActivity extends AppCompatActivity {

    public static int TIME_OUT = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, NavigationActivity.class);
                startActivity(intent);
                finish();
            }
        }, TIME_OUT);
    }
}
