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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

public class CustomerDetailActivity extends AppCompatActivity implements View.OnClickListener,
        VolleyResponseInterface {

    private Context mContext;
    private ImageView img_back;
    private EditText et_meter_no, et_meter_reading, et_latitude, et_longitude,
            et_remarks;
    private EditText et_rubber_mf;
    private Button btn_submit;
    private APIProgressDialog mProgressDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_customer_details);
        mContext = this;

        initUI();

    }

    private void initUI() {

        mProgressDialog = new APIProgressDialog(mContext, R.style.DialogStyle);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        et_meter_no = (EditText) findViewById(R.id.et_survey_meter_no);
        et_meter_reading = (EditText) findViewById(R.id.et_survey_meter_reading);
        et_latitude = (EditText) findViewById(R.id.et_survey_meter_latitude);
        et_longitude = (EditText) findViewById(R.id.et_survey_meter_longitude);
        et_rubber_mf = (EditText) findViewById(R.id.et_survey_rubber_mf);
        et_remarks = (EditText) findViewById(R.id.et_survey_remarks);
        btn_submit = (Button) findViewById(R.id.btn_survey_submit);
        btn_submit.setOnClickListener(this);

        et_latitude.setText(Utility.getAppPrefString(mContext, "latitude"));
        et_longitude.setText(Utility.getAppPrefString(mContext, "longitude"));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_survey_rubber_mf:

                break;
            case R.id.btn_survey_submit:
                if (et_meter_no.getText().toString().equalsIgnoreCase("")) {
                    Utility.toast("Please enter meter serial no", mContext);
                } else if (et_meter_reading.getText().toString().equalsIgnoreCase("")) {
                    Utility.toast("Please enter meter reading", mContext);
                } else if (et_rubber_mf.getText().toString().equalsIgnoreCase("")) {
                    Utility.toast("Please select rubber tube mf.", mContext);
                } else {
                    saveSurvey();
                }
                break;
            case R.id.img_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void saveSurvey() {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, String> params = new HashMap<>();
            params.put("user_id", Utility.getAppPrefString(mContext, Constant.USER_ID));
            params.put("application_no", getIntent().getStringExtra("app_no"));
            params.put("meter_sr_no", et_meter_no.getText().toString().trim());
            params.put("meter_reading", et_meter_reading.getText().toString().trim());
            params.put("latitude", et_latitude.getText().toString().trim());
            params.put("longitude", et_longitude.getText().toString().trim());
            params.put("rubber_tube_mf", et_rubber_mf.getText().toString().trim());
            params.put("remarks", et_remarks.getText().toString());
            VolleyCacheRequestClass.getInstance().volleyJsonAPI(mContext, Constant.SAVE_SURVEY,
                    Constant.URL_SAVE_SURVEY, params);
        } else
            Utility.toast("No Internet Connection", mContext);
    }

    @Override
    public void vResponse(int reqCode, String result) {
        String response, message;
        try {
            JSONObject jObject = new JSONObject(result);
            if (reqCode == Constant.SAVE_SURVEY) {
                response = jObject.getString("response");
                message = jObject.getString("message");
                if (response.equalsIgnoreCase("true")) {
                    Dialog dialog1 = new Dialog(mContext);
                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog1.setContentView(R.layout.dialog_sucess);
                    dialog1.setCanceledOnTouchOutside(false);
                    dialog1.setCancelable(false);

                    Button btn_dashboard = (Button) dialog1.findViewById(R.id.btn_dashboard);
                    TextView tv_success_msg = (TextView) dialog1.findViewById(R.id.tv_success_msg);
                    tv_success_msg.setText("Customer Survey Saved Successfully");

                    btn_dashboard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
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
