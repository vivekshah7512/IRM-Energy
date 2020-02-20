package com.vbs.irmenergy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.vbs.irmenergy.R;
import com.vbs.irmenergy.utilities.Constant;
import com.vbs.irmenergy.utilities.Utility;
import com.vbs.irmenergy.utilities.volley.VolleyAPIClass;
import com.vbs.irmenergy.utilities.volley.VolleyCacheRequestClass;
import com.vbs.irmenergy.utilities.volley.VolleyResponseInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends Activity implements VolleyResponseInterface {

    private final int SPLASH_DISPLAY_LENGTH = 1200;
    private Context mContext;
    private ProgressBar progressBar;
    private String version;
    private VolleyAPIClass volleyAPIClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mContext = this;
        volleyAPIClass = new VolleyAPIClass();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        verifyVersion();

    }

    private void verifyVersion() {
        if (Utility.isNetworkAvaliable(mContext)) {
            volleyAPIClass.volleyGetJsonAPI(mContext, null,
                    Constant.VERIFY_VERSION, Constant.URL_VERIFY_VERSION);
        } else
            Utility.toast("No Internet Connection", mContext);
    }

    @Override
    public void vResponse(int reqCode, String result) {
        String response, app_version;
        try {
            JSONObject jObject = new JSONObject(result);
            if (reqCode == Constant.VERIFY_VERSION) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    app_version = jObject.getString("app_version");
                    if (app_version.equalsIgnoreCase("1")){
                        app_version = "1.0";
                    }
                    if (app_version.equalsIgnoreCase(version)) {
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
                        }, 1000);
                    } else {
                        new AlertDialog.Builder(SplashActivity.this, R.style.AlertDialogTheme)
                                .setCancelable(false)
                                .setTitle("New version available")
                                .setMessage("Your IRM Energy app is of older version. Please update the latest version to continue using services.")
                                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                                Uri.parse("https://play.google.com/store/apps/details?id=com.vbs.irmenergy"));
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        moveTaskToBack(true);
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                        System.exit(1);
                                    }
                                }).show();
                    }
                } else {
                    new AlertDialog.Builder(SplashActivity.this, R.style.AlertDialogTheme)
                            .setCancelable(false)
                            .setTitle("New version available")
                            .setMessage("Your IRM Energy app is of older version. Please update the latest version to continue using services.")
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("https://play.google.com/store/apps/details?id=com.vbs.irmenergy"));
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    moveTaskToBack(true);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(1);
                                }
                            }).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void vErrorMsg(int reqCode, String error) {
        progressBar.setVisibility(View.GONE);
    }
}
