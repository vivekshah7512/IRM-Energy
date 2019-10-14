package com.vbs.irmenergy.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.vbs.irmenergy.R;

public class CaptureImageActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private Button btn_comm_submit;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_commission_process);
        mContext = this;

        initUI();

    }

    private void initUI() {
        btn_comm_submit = (Button) findViewById(R.id.btn_comm_submit);
        btn_comm_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_comm_submit:

                break;
            default:
                break;
        }
    }
}
