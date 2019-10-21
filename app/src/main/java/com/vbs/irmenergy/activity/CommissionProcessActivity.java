package com.vbs.irmenergy.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
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

import com.kyanogen.signatureview.SignatureView;
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

public class CommissionProcessActivity extends Activity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, VolleyResponseInterface {

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Context mContext;
    private VolleyAPIClass volleyAPIClass;
    private Button btn_comm_submit;
    private LinearLayout ll_take_photo, ll_take_sign;
    private ImageView img_capture, img_sign;
    private TextView tv_img_title;
    private ImageView img_back;
    private EditText et_app_no, et_com_date, et_meter_no, et_initial_meter_reading, et_latitude,
            et_longitude, et_remarks;
    private Spinner sp_contractor, sp_house_type;
    private APIProgressDialog mProgressDialog;
    private String[] contractor_id, contractor_name, house_id, house_type;
    private String stringContractorId = "0", stringHouseId = "0";
    private Calendar myCalendar;

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
        mProgressDialog = new APIProgressDialog(mContext, R.style.DialogStyle);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        volleyAPIClass = new VolleyAPIClass();
        myCalendar = Calendar.getInstance();

        btn_comm_submit = (Button) findViewById(R.id.btn_comm_submit);
        btn_comm_submit.setOnClickListener(this);
        ll_take_photo = (LinearLayout) findViewById(R.id.ll_take_photo);
        ll_take_photo.setOnClickListener(this);
        ll_take_sign = (LinearLayout) findViewById(R.id.ll_take_sign);
        ll_take_sign.setOnClickListener(this);
        img_capture = (ImageView) findViewById(R.id.img_meter);
        img_sign = (ImageView) findViewById(R.id.img_sign);
        tv_img_title = (TextView) findViewById(R.id.tv_meter_title);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        et_app_no = (EditText) findViewById(R.id.et_com_app_no);
        et_app_no.setText(getIntent().getStringExtra("app_no"));
        et_com_date = (EditText) findViewById(R.id.et_com_date);
        et_com_date.setOnClickListener(this);
        et_meter_no = (EditText) findViewById(R.id.et_com_meter_no);
        et_initial_meter_reading = (EditText) findViewById(R.id.et_com_initial_meter_reading);
        et_latitude = (EditText) findViewById(R.id.et_com_latitude);
        et_longitude = (EditText) findViewById(R.id.et_com_longitude);
        et_remarks = (EditText) findViewById(R.id.et_com_remarks);
        sp_contractor = (Spinner) findViewById(R.id.sp_com_contractor);
        sp_contractor.setOnItemSelectedListener(this);
        sp_house_type = (Spinner) findViewById(R.id.sp_com_house_type);
        sp_house_type.setOnItemSelectedListener(this);

        getContractorName();
        getHouseType();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_com_date:
                DatePickerDialog dialog_date = new DatePickerDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String myFormat = "dd-MM-yyyy";
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                et_com_date.setText(sdf.format(myCalendar.getTime()));
                            }
                        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog_date.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
                dialog_date.show();
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
            case R.id.ll_take_sign:
                Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_sign);
                dialog.setCanceledOnTouchOutside(false);

                Button btn_clear = (Button) dialog.findViewById(R.id.btn_sign_cancel);
                Button btn_save = (Button) dialog.findViewById(R.id.btn_sign_submit);
                ImageView img_cancel = (ImageView) dialog.findViewById(R.id.img_cancel);
                SignatureView signatureView = (SignatureView) dialog.findViewById(R.id.signature_view);

                btn_clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (signatureView != null)
                            signatureView.clearCanvas();
                    }
                });

                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (signatureView.getSignatureBitmap() != null) {
                            dialog.dismiss();
                            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(signatureView.getSignatureBitmap(), 100, 100);
                            img_sign.setImageBitmap(ThumbImage);
                        } else {
                            Utility.toast("Draw your signature.", mContext);
                        }
                    }
                });

                img_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER;
                wlp.width = ActionBar.LayoutParams.MATCH_PARENT;
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                window.setAttributes(wlp);
                dialog.show();
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
            params.put("center_code", Utility.getAppPrefString(mContext,"center_code"));
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
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String selectedText = adapterView.getItemAtPosition(position).toString();
        switch (adapterView.getId()) {
            case R.id.sp_com_contractor:
                if (!contractor_id[position].equalsIgnoreCase("0")) {
                    stringContractorId = contractor_id[position];
                }
                break;
            case R.id.sp_com_house_type:
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
