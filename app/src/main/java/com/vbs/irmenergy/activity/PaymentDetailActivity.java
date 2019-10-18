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
import android.widget.Spinner;

import com.vbs.irmenergy.R;
import com.vbs.irmenergy.utilities.APIProgressDialog;
import com.vbs.irmenergy.utilities.Constant;
import com.vbs.irmenergy.utilities.Utility;
import com.vbs.irmenergy.utilities.volley.VolleyCacheRequestClass;
import com.vbs.irmenergy.utilities.volley.VolleyResponseInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentDetailActivity extends AppCompatActivity implements View.OnClickListener,
        VolleyResponseInterface {

    private Context mContext;
    private Button btn_payment_submit;
    private ImageView img_back;
    private Spinner sp_plan;
    private EditText et_receipt_type, et_receipt_date, et_payment_mode, et_inst_no, et_inst_date,
            et_amount, et_micr_no, et_bank_name, et_remarks;
    private APIProgressDialog mProgressDialog;

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
        mProgressDialog = new APIProgressDialog(mContext, R.style.DialogStyle);
        btn_payment_submit = findViewById(R.id.btn_payment_submit);
        btn_payment_submit.setOnClickListener(this);

        sp_plan = (Spinner) findViewById(R.id.sp_payment_plan);
        et_receipt_type = (EditText) findViewById(R.id.et_payment_receipt_type);
        et_receipt_date = (EditText) findViewById(R.id.et_payment_receipt_date);
        et_receipt_date.setOnClickListener(this);
        et_payment_mode = (EditText) findViewById(R.id.et_payment_type);
        et_inst_no = (EditText) findViewById(R.id.et_payment_inst_no);
        et_inst_date = (EditText) findViewById(R.id.et_payment_inst_date);
        et_inst_date.setOnClickListener(this);
        et_amount = (EditText) findViewById(R.id.et_payment_amount);
        et_micr_no = (EditText) findViewById(R.id.et_payment_micr_no);
        et_bank_name = (EditText) findViewById(R.id.et_payment_bank_name);
        et_remarks = (EditText) findViewById(R.id.et_payment_remarks);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_payment_receipt_date:

                break;
            case R.id.et_payment_inst_date:

                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_payment_submit:
//                savePaymentDetails();
                Intent intent = new Intent(mContext, VerifyPaymentDetailActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void savePaymentDetails() {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, String> params = new HashMap<>();
            params.put("user_id", Utility.getAppPrefString(mContext, Constant.USER_ID));
            params.put("application_no", getIntent().getStringExtra("app_no"));
            params.put("plan_id", "");
            params.put("receipt_type", et_receipt_type.getText().toString());
            params.put("receipt_date", et_receipt_date.getText().toString());
            params.put("payment_mode", et_payment_mode.getText().toString());
            params.put("inst_no", et_inst_no.getText().toString());
            params.put("inst_date", et_inst_date.getText().toString());
            params.put("amount", et_amount.getText().toString());
            params.put("micr_no", et_micr_no.getText().toString());
            params.put("bank_name", et_bank_name.getText().toString());
            params.put("remarks", et_remarks.getText().toString());
            VolleyCacheRequestClass.getInstance().volleyJsonAPI(mContext, Constant.REGISTRATION_PAYMENT,
                    Constant.URL_REGISTRATION_PAYMENT, params);
        } else
            Utility.toast("No Internet Connection", mContext);
    }

    @Override
    public void vResponse(int reqCode, String result) {
        String response, message;
        try {
            JSONObject jObject = new JSONObject(result);
            if (reqCode == Constant.REGISTRATION_PAYMENT) {
                response = jObject.getString("response");
                message = jObject.getString("message");
                if (response.equalsIgnoreCase("true")) {
                    Utility.toast(message, mContext);
                } else {
                    Utility.toast(message, mContext);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void vErrorMsg(int reqCode, String error) {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
