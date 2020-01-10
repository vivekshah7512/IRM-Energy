package com.vbs.irmenergy.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vbs.irmenergy.R;

public class VerifyPaymentDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private Button btn_save;
    private ImageView img_back;
    private TextView tv_app_no, tv_cust_name, tv_corporate_code, tv_plan, tv_receipt,
            tv_receipt_date, tv_mode, tv_inst_no, tv_inst_date, tv_amount, tv_micr_no, tv_bank,
            tv_remarks;
    private String app_no, cust_name, plan, receipt, receipt_dt, payment_mode,
            inst_no, inst_date, amount, micr_no, bank_name, remarks;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_payment_detail);
        mContext = this;

        initUI();

    }

    private void initUI() {
        btn_save = findViewById(R.id.btn_cust_verify);
        btn_save.setOnClickListener(this);

        tv_app_no = (TextView) findViewById(R.id.tv_payment_app_no);
        tv_cust_name = (TextView) findViewById(R.id.tv_payment_cust_nmae);
        tv_corporate_code = (TextView) findViewById(R.id.tv_payment_corporate_code);
        tv_plan = (TextView) findViewById(R.id.tv_payment_plan);
        tv_receipt = (TextView) findViewById(R.id.tv_payment_receipt);
        tv_receipt_date = (TextView) findViewById(R.id.tv_payment_receipt_date);
        tv_mode = (TextView) findViewById(R.id.tv_payment_mode);
        tv_inst_no = (TextView) findViewById(R.id.tv_payment_inst_no);
        tv_inst_date = (TextView) findViewById(R.id.tv_payment_inst_date);
        tv_amount = (TextView) findViewById(R.id.tv_payment_amount);
        tv_micr_no = (TextView) findViewById(R.id.tv_payment_micr_no);
        tv_bank = (TextView) findViewById(R.id.tv_payment_bank_name);
        tv_remarks = (TextView) findViewById(R.id.tv_payment_remarks);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        try {
            app_no = getIntent().getStringExtra("app_no");
            cust_name = getIntent().getStringExtra("cust_name");
            plan = getIntent().getStringExtra("plan");
            receipt = getIntent().getStringExtra("receipt");
            receipt_dt = getIntent().getStringExtra("receipt_dt");
            payment_mode = getIntent().getStringExtra("payment_mode");
            inst_no = getIntent().getStringExtra("inst_no");
            inst_date = getIntent().getStringExtra("inst_date");
            amount = getIntent().getStringExtra("amount");
            micr_no = getIntent().getStringExtra("micr_no");
            bank_name = getIntent().getStringExtra("bank_name");
            remarks = getIntent().getStringExtra("remarks");

            tv_app_no.setText(app_no);
            tv_cust_name.setText(cust_name);
            tv_corporate_code.setText("0");
            tv_plan.setText(plan);
            tv_receipt.setText(receipt);
            tv_receipt_date.setText(receipt_dt);
            tv_mode.setText(payment_mode);
            tv_inst_no.setText(inst_no);
            tv_inst_date.setText(inst_date);
            tv_amount.setText(amount);
            tv_micr_no.setText(micr_no);
            tv_bank.setText(bank_name);
            tv_remarks.setText(remarks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_cust_verify:
                Dialog dialog1 = new Dialog(mContext);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.setContentView(R.layout.dialog_sucess);
                dialog1.setCanceledOnTouchOutside(false);
                dialog1.setCancelable(false);

                Button btn_dashboard = (Button) dialog1.findViewById(R.id.btn_dashboard);
                TextView tv_success_msg = (TextView) dialog1.findViewById(R.id.tv_success_msg);
                tv_success_msg.setText("Your Data Saved Successfully");

                btn_dashboard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                        finish();
                        Intent i = new Intent(mContext, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                });

                Window window1 = dialog1.getWindow();
                WindowManager.LayoutParams wlp1 = window1.getAttributes();
                wlp1.width = ActionBar.LayoutParams.MATCH_PARENT;
                wlp1.height = ActionBar.LayoutParams.MATCH_PARENT;
                window1.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                window1.setAttributes(wlp1);
                dialog1.show();
                break;
            default:
                break;
        }
    }
}
