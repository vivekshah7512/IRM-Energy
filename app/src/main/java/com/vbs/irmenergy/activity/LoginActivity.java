package com.vbs.irmenergy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.vbs.irmenergy.R;
import com.vbs.irmenergy.utilities.Utility;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

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

        initUI();

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
}
