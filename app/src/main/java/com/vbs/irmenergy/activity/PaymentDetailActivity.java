package com.vbs.irmenergy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.vbs.irmenergy.R;

public class PaymentDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private Button btn_next;
    private ImageView img_back;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_payment_detail);
        mContext = this;

        initUI();

    }

    private void initUI() {
        btn_next = findViewById(R.id.btn_cust_next);
        btn_next.setOnClickListener(this);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_cust_next:
                Intent intent = new Intent(mContext,VerifyPaymentDetailActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
