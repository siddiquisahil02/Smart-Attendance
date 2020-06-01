package com.boiz.experiment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread bc = new Thread()
        {
          public void run()
          {
              try {
                  sleep(2000);
                  startActivity(new Intent(SplashScreen.this, MainActivity.class));
                  finish();
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
        };
        bc.start();
    }
}
