package com.vbs.irmenergy.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.vbs.irmenergy.R;
import com.vbs.irmenergy.utilities.APIProgressDialog;
import com.vbs.irmenergy.utilities.Constant;
import com.vbs.irmenergy.utilities.Utility;
import com.vbs.irmenergy.utilities.volley.VolleyAPIClass;
import com.vbs.irmenergy.utilities.volley.VolleyCacheRequestClass;
import com.vbs.irmenergy.utilities.volley.VolleyResponseInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaymentDetailActivity extends AppCompatActivity implements View.OnClickListener,
        VolleyResponseInterface, AdapterView.OnItemSelectedListener {

    private Context mContext;
    private Button btn_payment_submit;
    private ImageView img_back;
    private Spinner sp_plan, sp_bank, sp_payment_mode;
    private EditText et_receipt_type, et_receipt_date, et_inst_no, et_inst_date,
            et_amount, et_micr_no, et_remarks;
    private APIProgressDialog mProgressDialog;
    private String[] plan_id, plan_name, plan_amount, bank_id, bank_name, bank_name_temp;
    private VolleyAPIClass volleyAPIClass;
    private String stringPlanId = "0", stringBankId = "0", stringAmount = "0", stringType;
    private Calendar myCalendar;
    private ArrayList<String> arrayListBankName, arrayListBankID;

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

        et_micr_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_micr_no.getText().toString().trim().length() > 0) {
                    for (int j = 0; j < bank_name.length; j++) {
                        String text = bank_name[j];
                        Pattern word = Pattern.compile(et_micr_no.getText().toString());
                        Matcher match = word.matcher(text);
                        if (match.find()) {
                            String t1 = String.valueOf(text.charAt(0));
                            String t2 = String.valueOf(et_micr_no.getText().toString().charAt(0));
                            if (t1.equalsIgnoreCase(t2)) {
                                sp_bank.setSelection(j);
                                Log.v("Bank : ", text);
                                break;
                            } else {
                                sp_bank.setSelection(0);
                            }
                            break;
                        } else {
                            sp_bank.setSelection(0);
                            Log.v("Bank : ", text);
                        }
                    }
                } else {
                    sp_bank.setSelection(0);
                }
            }
        });

    }

    private void initUI() {
        volleyAPIClass = new VolleyAPIClass();
        myCalendar = Calendar.getInstance();
        mProgressDialog = new APIProgressDialog(mContext, R.style.DialogStyle);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);

        btn_payment_submit = findViewById(R.id.btn_payment_submit);
        btn_payment_submit.setOnClickListener(this);

        sp_plan = (Spinner) findViewById(R.id.sp_payment_plan);
        sp_plan.setOnItemSelectedListener(this);
        sp_bank = (Spinner) findViewById(R.id.sp_payment_bank_name);
        sp_bank.setOnItemSelectedListener(this);
        sp_bank.setEnabled(false);
        sp_payment_mode = (Spinner) findViewById(R.id.et_payment_type);
        sp_payment_mode.setOnItemSelectedListener(this);
        Utility.setSpinnerAdapter(mContext, sp_payment_mode, getResources().getStringArray(R.array.payment_type));
        et_receipt_type = (EditText) findViewById(R.id.et_payment_receipt_type);
        et_receipt_date = (EditText) findViewById(R.id.et_payment_receipt_date);
        et_receipt_date.setOnClickListener(this);
        et_inst_no = (EditText) findViewById(R.id.et_payment_inst_no);
        et_inst_date = (EditText) findViewById(R.id.et_payment_inst_date);
        et_inst_date.setOnClickListener(this);
        et_amount = (EditText) findViewById(R.id.et_payment_amount);
        et_micr_no = (EditText) findViewById(R.id.et_payment_micr_no);
        et_remarks = (EditText) findViewById(R.id.et_payment_remarks);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        getPlanList();
        getBankList();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_payment_receipt_date:
                DatePickerDialog dialog = new DatePickerDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String myFormat = "dd-MMM-yyyy";
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                et_receipt_date.setText(sdf.format(myCalendar.getTime()));
                            }
                        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
                break;
            case R.id.et_payment_inst_date:
                DatePickerDialog dialog1 = new DatePickerDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String myFormat = "dd-MMM-yyyy";
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                et_inst_date.setText(sdf.format(myCalendar.getTime()));
                            }
                        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog1.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog1.show();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_payment_submit:
                if (et_micr_no.getText().toString().trim().length() == 9) {
                    savePaymentDetails();
                } else {
                    Utility.toast("Please enter valid MICR No.", mContext);
                }
                break;
            default:
                break;
        }
    }

    private void getPlanList() {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            volleyAPIClass.volleyGetJsonAPI(mContext, null,
                    Constant.GET_PLAN,
                    Constant.URL_GET_PLAN);
        } else
            Utility.toast("No Internet Connection", mContext);
    }

    private void getBankList() {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, String> params = new HashMap<>();
            params.put("center_code", Utility.getAppPrefString(mContext, "center_code"));
            VolleyCacheRequestClass.getInstance().volleyJsonAPI(mContext, Constant.GET_BANK_NAME,
                    Constant.URL_GET_BANK_NAME, params);
        } else
            Utility.toast("No Internet Connection", mContext);
    }

    private void savePaymentDetails() {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, String> params = new HashMap<>();
            params.put("user_id", Utility.getAppPrefString(mContext, Constant.USER_ID));
            params.put("center_code", Utility.getAppPrefString(mContext, "center_code"));
            params.put("application_no", getIntent().getStringExtra("app_no"));
            params.put("corporate_id", "0");
            params.put("receipt_type", et_receipt_type.getText().toString());
            params.put("receipt_date", et_receipt_date.getText().toString());
            params.put("payment_mode", stringType);
            params.put("inst_no", et_inst_no.getText().toString());
            params.put("inst_date", et_inst_date.getText().toString());
            params.put("amount", stringAmount);
            params.put("micr_no", et_micr_no.getText().toString());
            params.put("bank_id", stringBankId);
            params.put("plan_id", stringPlanId);
            params.put("remarks", et_remarks.getText().toString());
            VolleyCacheRequestClass.getInstance().volleyJsonAPI(mContext, Constant.REGISTRATION_PAYMENT,
                    Constant.URL_REGISTRATION_PAYMENT, params);
        } else
            Utility.toast("No Internet Connection", mContext);
    }

    @Override
    public void vResponse(int reqCode, String result) {
        String response, message;
        JSONObject jsonObjectMessage;
        JSONArray jsonArray;
        try {
            JSONObject jObject = new JSONObject(result);
            if (reqCode == Constant.REGISTRATION_PAYMENT) {
                response = jObject.getString("response");
                message = jObject.getString("message");
                if (response.equalsIgnoreCase("true")) {
                    Utility.toast(message, mContext);
                    Intent intent = new Intent(mContext, VerifyPaymentDetailActivity.class);
                    intent.putExtra("app_no", getIntent().getStringExtra("app_no"));
                    intent.putExtra("cust_name", getIntent().getStringExtra("cust_name"));
                    intent.putExtra("plan", sp_plan.getSelectedItem().toString());
                    intent.putExtra("receipt", et_receipt_type.getText().toString());
                    intent.putExtra("receipt_dt", et_receipt_date.getText().toString());
                    intent.putExtra("payment_mode", sp_payment_mode.getSelectedItem().toString());
                    intent.putExtra("inst_no", et_inst_no.getText().toString());
                    intent.putExtra("inst_date", et_inst_date.getText().toString());
                    intent.putExtra("amount", et_amount.getText().toString());
                    intent.putExtra("micr_no", et_micr_no.getText().toString());
                    intent.putExtra("bank_name", sp_bank.getSelectedItem().toString());
                    intent.putExtra("remarks", et_remarks.getText().toString());
                    startActivity(intent);
                } else {
                    Utility.toast(message, mContext);
                }
            } else if (reqCode == Constant.GET_PLAN) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("plan_data");
                    int lenth = jsonArray.length() + 1;
                    plan_id = new String[lenth];
                    plan_name = new String[lenth];
                    plan_amount = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            plan_id[0] = "0";
                            plan_name[0] = "Select Plan";
                            plan_amount[0] = "0";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            plan_id[a] = jsonObjectMessage.getString("plan_id");
                            plan_name[a] = jsonObjectMessage.getString("plan_name");
                            plan_amount[a] = jsonObjectMessage.getString("plan_amount");
                        }
                    }
                    Utility.setSpinnerAdapter(mContext, sp_plan, plan_name);
                }
            } else if (reqCode == Constant.GET_BANK_NAME) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("bank_data");
                    int lenth = jsonArray.length() + 1;
                    bank_id = new String[lenth];
                    bank_name = new String[lenth];
                    bank_name_temp = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            bank_id[0] = "0";
                            bank_name[0] = "Select Bank Name";
                            bank_name_temp[0] = "Bank Name";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            bank_id[a] = jsonObjectMessage.getString("bank_id");
                            bank_name[a] = jsonObjectMessage.getString("bank_name");
                            String[] separated = bank_name[a].split("-");
                            bank_name_temp[a] = separated[1].trim();
                        }
                    }
                    Utility.setSpinnerAdapter(mContext, sp_bank, bank_name_temp);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String selectedText = adapterView.getItemAtPosition(position).toString();
        switch (adapterView.getId()) {
            case R.id.sp_payment_plan:
                if (!plan_id[position].equalsIgnoreCase("0")) {
                    stringPlanId = plan_id[position];
                    et_amount.setText("\u20B9 " + plan_amount[position]);
                    stringAmount = plan_amount[position];
                }
                break;
            case R.id.sp_payment_bank_name:
                if (!bank_id[position].equalsIgnoreCase("0")) {
                    stringBankId = bank_id[position];
                }
                break;
            case R.id.et_payment_type:
                if (selectedText.equalsIgnoreCase("Cheque"))
                    stringType = "cheque";
                else if (selectedText.equalsIgnoreCase("Swipe card"))
                    stringType = "swipecard";
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
