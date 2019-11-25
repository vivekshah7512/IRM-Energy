package com.vbs.irmenergy.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class JobsheetDetailsActivity extends Activity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, VolleyResponseInterface {

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Context mContext;
    private VolleyAPIClass volleyAPIClass;
    private Button btn_comm_submit;
    private LinearLayout ll_take_photo;
    private ImageView img_capture;
    private TextView tv_img_title;
    private ImageView img_back;
    private EditText et_application_no, et_inst_date, et_testing_date, et_material_no, et_latitude,
            et_longitude, et_remarks;
    private Spinner sp_contractor, sp_house_type, sp_regulator;
    private APIProgressDialog mProgressDialog;
    private String[] contractor_id, contractor_name, house_id, house_type;
    private String stringContractorId = "0", stringHouseId = "0", meterPhotoName;
    private Calendar myCalendar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_jobsheet_details);
        mContext = this;

        initUI();

    }

    private void initUI() {
        mProgressDialog = new APIProgressDialog(mContext, R.style.DialogStyle);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        volleyAPIClass = new VolleyAPIClass();
        myCalendar = Calendar.getInstance();

        btn_comm_submit = (Button) findViewById(R.id.btn_jobsheet_submit);
        btn_comm_submit.setOnClickListener(this);
        ll_take_photo = (LinearLayout) findViewById(R.id.ll_take_photo);
        ll_take_photo.setOnClickListener(this);
        img_capture = (ImageView) findViewById(R.id.img_meter);
        tv_img_title = (TextView) findViewById(R.id.tv_meter_title);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        et_application_no = (EditText) findViewById(R.id.et_app_no);
        et_application_no.setText(getIntent().getStringExtra("app_no"));
        et_inst_date = (EditText) findViewById(R.id.et_job_inst_date);
        et_inst_date.setOnClickListener(this);
        et_testing_date = (EditText) findViewById(R.id.et_job_test_date);
        et_testing_date.setOnClickListener(this);
        et_material_no = (EditText) findViewById(R.id.et_job_material_no);
        et_latitude = (EditText) findViewById(R.id.et_job_latitude);
        et_latitude.setText(Utility.getAppPrefString(mContext, "latitude"));
        et_longitude = (EditText) findViewById(R.id.et_job_longitude);
        et_longitude.setText(Utility.getAppPrefString(mContext, "longitude"));
        et_remarks = (EditText) findViewById(R.id.et_job_remarks);
        sp_contractor = (Spinner) findViewById(R.id.sp_job_contractor);
        sp_contractor.setOnItemSelectedListener(this);
        sp_house_type = (Spinner) findViewById(R.id.sp_job_house_type);
        sp_house_type.setOnItemSelectedListener(this);
        sp_regulator = (Spinner) findViewById(R.id.sp_job_regulator);
        sp_regulator.setOnItemSelectedListener(this);

        getContractorName();
        getHouseType();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_job_inst_date:
                DatePickerDialog dialog_date = new DatePickerDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
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
                dialog_date.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog_date.show();
                break;
            case R.id.et_job_test_date:
                DatePickerDialog dialog_date1 = new DatePickerDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String myFormat = "dd-MMM-yyyy";
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                et_testing_date.setText(sdf.format(myCalendar.getTime()));
                            }
                        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog_date1.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog_date1.show();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.ll_take_photo:
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
                break;
            case R.id.btn_jobsheet_submit:
                Intent intent = new Intent(mContext, JobsheetConnectionTypeActivity.class);
                intent.putExtra("woType", getIntent().getStringExtra("woType"));
                intent.putExtra("app_id", getIntent().getStringExtra("app_id"));
                intent.putExtra("application_no", et_application_no.getText().toString());
                intent.putExtra("workorder_id", getIntent().getStringExtra("workorder_id"));
                intent.putExtra("contractor_id", stringContractorId);
                intent.putExtra("house_type", stringHouseId);
                intent.putExtra("installation_date", et_inst_date.getText().toString());
                intent.putExtra("meter_sr_no", et_material_no.getText().toString());
                intent.putExtra("latitude", et_latitude.getText().toString());
                intent.putExtra("longitude", et_longitude.getText().toString());
                intent.putExtra("remarks", et_remarks.getText().toString());
                intent.putExtra("meter_photo", meterPhotoName);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void getContractorName() {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, String> params = new HashMap<>();
            params.put("center_code", Utility.getAppPrefString(mContext, "center_code"));
            params.put("WO_type", getIntent().getStringExtra("woType"));
            VolleyCacheRequestClass.getInstance().volleyJsonAPI(mContext, Constant.GET_CONTRACTOR,
                    Constant.URL_GET_CONTRACTOR, params);
        } else
            Utility.toast("No Internet Connection", mContext);
    }

    private void getHouseType() {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            volleyAPIClass.volleyGetJsonAPI(mContext, null,
                    Constant.GET_HOUSE_TYPE,
                    Constant.URL_GET_HOUSE_TYPE);
        } else
            Utility.toast("No Internet Connection", mContext);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            img_capture.setImageBitmap(photo);
            tv_img_title.setText(Utility.getTimeStamp() + ".jpg");
            meterPhotoName = tv_img_title.getText().toString();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String selectedText = adapterView.getItemAtPosition(position).toString();
        switch (adapterView.getId()) {
            case R.id.sp_job_contractor:
                if (!contractor_id[position].equalsIgnoreCase("0")) {
                    stringContractorId = contractor_id[position];
                }
                break;
            case R.id.sp_job_house_type:
                if (!house_id[position].equalsIgnoreCase("0")) {
                    stringHouseId = house_id[position];
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void vResponse(int reqCode, String result) {
        String response, message;
        JSONObject jsonObjectMessage;
        JSONArray jsonArray;
        try {
            JSONObject jObject = new JSONObject(result);
            if (reqCode == Constant.GET_CONTRACTOR) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("contractor_data");
                    int lenth = jsonArray.length() + 1;
                    contractor_id = new String[lenth];
                    contractor_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            contractor_id[0] = "0";
                            contractor_name[0] = "Select Contractor";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            contractor_id[a] = jsonObjectMessage.getString("contractor_id");
                            contractor_name[a] = jsonObjectMessage.getString("contractor_name");
                        }
                    }
                    Utility.setSpinnerAdapter(mContext, sp_contractor, contractor_name);
                }
            } else if (reqCode == Constant.GET_HOUSE_TYPE) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("house_data");
                    int lenth = jsonArray.length() + 1;
                    house_id = new String[lenth];
                    house_type = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            house_id[0] = "0";
                            house_type[0] = "Select House Type";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            house_id[a] = jsonObjectMessage.getString("house_id");
                            house_type[a] = jsonObjectMessage.getString("house_type");
                        }
                    }
                    Utility.setSpinnerAdapter(mContext, sp_house_type, house_type);
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
