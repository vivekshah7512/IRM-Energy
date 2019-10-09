package com.vbs.irmenergy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.vbs.irmenergy.R;
import com.vbs.irmenergy.utilities.Constant;
import com.vbs.irmenergy.utilities.Utility;

public class SplashActivity extends Activity {

    private Context mContext;
    private final int SPLASH_DISPLAY_LENGTH = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mContext = this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utility.getAppPrefString(mContext, Constant.isLogin)
                        .equalsIgnoreCase("true")) {
                    Intent i = new Intent(mContext, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(mContext, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}
