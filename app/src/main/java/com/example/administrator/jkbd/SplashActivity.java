package com.example.administrator.jkbd;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Who on 2017/6/28.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_splash);
        cdt.start();
    }
    CountDownTimer cdt=new CountDownTimer(2000,1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            Intent intent=new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();
        }
    };
}
