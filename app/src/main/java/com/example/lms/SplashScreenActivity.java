package com.example.lms;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import com.example.lms.utils.AppConfig;

/**
 * Created by admin on 11/10/2014.
 */
public class SplashScreenActivity extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    AppConfig config = new AppConfig(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = null;

                if (config.isLoggedIn()) {
                    if(!config.isManager()) {
                        i = new Intent(SplashScreenActivity.this, HomeScreenActivity.class);
                    }else{
                        i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    }
                } else {
                    i = new Intent(SplashScreenActivity.this, MainActivity.class);
                }

                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}