package com.vbs.irmenergy.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vbs.irmenergy.R;
import com.vbs.irmenergy.utilities.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private Context mContext;
    private EditText et_username, et_password;
    private Button btn_login;
    private TextView tv_forgot;
    private CheckBox cb_pass, check_remember;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setContentView(R.layout.activity_login);
        mContext = this;

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkAndRequestPermissions()) {
                initUI();
            }
        } else {
            initUI();
        }

    }

    private void initUI() {
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        tv_forgot = findViewById(R.id.tv_forgot);
        tv_forgot.setOnClickListener(this);
        cb_pass = findViewById(R.id.cb_pass);

        check_remember = findViewById(R.id.check_remember);
        if (Utility.getAppPrefString(mContext, "check_flag").equalsIgnoreCase("true")) {
            et_username.setText(Utility.getAppPrefString(mContext, "username"));
            et_password.setText(Utility.getAppPrefString(mContext, "password"));
            check_remember.setChecked(true);
        } else {
            check_remember.setChecked(false);
        }

        cb_pass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    et_password.setTransformationMethod(null);
                } else {
                    et_password.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (check_remember.isChecked()) {
                    Utility.writeSharedPreferences(mContext, "username", et_username.getText().toString());
                    Utility.writeSharedPreferences(mContext, "password", et_password.getText().toString());
                    Utility.writeSharedPreferences(mContext, "check_flag", "true");
                } else {
                    Utility.writeSharedPreferences(mContext, "username", "");
                    Utility.writeSharedPreferences(mContext, "password", "");
                    Utility.writeSharedPreferences(mContext, "check_flag", "false");
                }
                Intent i = new Intent(mContext, MainActivity.class);
                startActivity(i);
                break;
            case R.id.tv_forgot:
                Intent i1 = new Intent(mContext, ForgotPasswordActivity.class);
                startActivity(i1);
                break;
            default:
                break;
        }
    }

    private boolean checkAndRequestPermissions() {
        int openCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (openCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("Permission", "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        recreate();
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.CAMERA)) {
                            Utility.showDialogOK(mContext, "Camera Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }
    }

}
