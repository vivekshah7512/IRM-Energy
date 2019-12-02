package com.vbs.irmenergy.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kyanogen.signatureview.SignatureView;
import com.vbs.irmenergy.R;
import com.vbs.irmenergy.utilities.APIProgressDialog;
import com.vbs.irmenergy.utilities.Constant;
import com.vbs.irmenergy.utilities.Utility;
import com.vbs.irmenergy.utilities.volley.VolleyCacheRequestClass;
import com.vbs.irmenergy.utilities.volley.VolleyResponseInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ExtraMaterialEstimationActivity extends Activity implements View.OnClickListener,
        VolleyResponseInterface {

    private Context mContext;
    private Button btn_comm_submit, btn_material_calculate, btn_add_more;
    private LinearLayout ll_take_sign;
    private ImageView img_sign;
    private TextView tv_amount;
    private ImageView img_back;
    private APIProgressDialog mProgressDialog;
    private String woType = "8", estapp_id = "0", workorderid = "0";
    private JSONArray jsonArray1 = null, jsonArray2 = null, jsonArray3 = null;
    private ArrayList<String> arrayList;
    private LinearLayout ll_material_list;
    private String[] workorder_id, workorder_name, material_id, material_name,
            material_amount, connection_id, connection_name;
    private ArrayList<String> arrayListWorkType, arrayListConnectionType, arrayListMaterial,
            arrayListUsedQty, arrayListNoConnection;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_extra_material_estimation);
        mContext = this;

        initUI();

    }

    private void initUI() {
        btn_comm_submit = (Button) findViewById(R.id.btn_material_submit);
        btn_comm_submit.setOnClickListener(this);
        btn_add_more = (Button) findViewById(R.id.btn_material_add_more);
        btn_add_more.setOnClickListener(this);
        btn_material_calculate = (Button) findViewById(R.id.btn_material_calculate);
        btn_material_calculate.setOnClickListener(this);
        ll_take_sign = (LinearLayout) findViewById(R.id.ll_take_sign);
        ll_take_sign.setOnClickListener(this);
        img_sign = (ImageView) findViewById(R.id.img_sign);
        ll_material_list = (LinearLayout) findViewById(R.id.ll_material_list);
        tv_amount = (TextView) findViewById(R.id.tv_amount);

        mProgressDialog = new APIProgressDialog(mContext, R.style.DialogStyle);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);

        arrayList = new ArrayList<>();
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        estapp_id = getIntent().getStringExtra("estapp_id");
        workorderid = getIntent().getStringExtra("workorder_id");

        getWorkType();
        getConnection();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_material_calculate:
                int size = ll_material_list.getChildCount();
                View v = null;
                arrayListWorkType = new ArrayList<>();
                arrayListConnectionType = new ArrayList<>();
                arrayListMaterial = new ArrayList<>();
                arrayListUsedQty = new ArrayList<>();
                arrayListNoConnection = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    v = ll_material_list.getChildAt(i);
                    LinearLayout ll_child = (LinearLayout) v.findViewById(R.id.ll_child);
                    Spinner sp_work_type = (Spinner) ll_child.findViewById(R.id.sp_work_type);
                    Spinner sp_connection_type = (Spinner) ll_child.findViewById(R.id.sp_conn_type);
                    Spinner sp_material_name = (Spinner) ll_child.findViewById(R.id.sp_material_name);
                    EditText et_qty = (EditText) ll_child.findViewById(R.id.et_qty_used);
                    EditText et_no_conn = (EditText) ll_child.findViewById(R.id.et_no_of_conn);

                    String type = sp_work_type.getSelectedItem().toString();
                    String connection = sp_connection_type.getSelectedItem().toString();
                    String material = sp_material_name.getSelectedItem().toString();

                    int indexType = new ArrayList<String>(Arrays.asList(workorder_name)).indexOf(type);
                    int indexMaterial = new ArrayList<String>(Arrays.asList(material_name)).indexOf(material);
                    int indexConnection = new ArrayList<String>(Arrays.asList(connection_name)).indexOf(connection);

                    if (ll_child.getVisibility() == View.VISIBLE) {
                        arrayListWorkType.add(workorder_id[indexType]);
                        arrayListConnectionType.add(connection_id[indexConnection]);
                        arrayListMaterial.add(material_id[indexMaterial]);
                        arrayListUsedQty.add(et_qty.getText().toString().trim());
                        arrayListNoConnection.add(et_no_conn.getText().toString().trim());
                    }
                }
                calculateAmount();
                break;
            case R.id.btn_material_add_more:
                addMore();
                break;
            case R.id.btn_material_submit:
                saveEstimation();
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

    private void getWorkType() {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, String> params = new HashMap<>();
            params.put("WOtype", woType);
            VolleyCacheRequestClass.getInstance().volleyJsonAPI(mContext, Constant.GET_WORK_TYPE,
                    Constant.URL_GET_WORK_TYPE, params);
        } else
            Utility.toast("No Internet Connection", mContext);
    }

    private void getConnection() {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, String> params = new HashMap<>();
            params.put("WOtype", woType);
            params.put("app_id", estapp_id);
            VolleyCacheRequestClass.getInstance().volleyJsonAPI(mContext, Constant.GET_CONNECTION_TYPE,
                    Constant.URL_GET_CONNECTION_TYPE, params);
        } else
            Utility.toast("No Internet Connection", mContext);
    }

    private void getMaterial() {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, String> params = new HashMap<>();
            params.put("WOtype", woType);
            VolleyCacheRequestClass.getInstance().volleyJsonAPI(mContext, Constant.GET_MATERIAL_DATA,
                    Constant.URL_GET_MATERIAL_DATA, params);
        } else
            Utility.toast("No Internet Connection", mContext);
    }

    private void calculateAmount() {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            try {
                JSONObject obj = null;
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < arrayListWorkType.size(); i++) {
                    obj = new JSONObject();
                    obj.put("work_type", arrayListWorkType.get(i));
                    obj.put("material_id", arrayListMaterial.get(i));
                    obj.put("material_qty", arrayListUsedQty.get(i));
                    obj.put("connection_type", arrayListConnectionType.get(i));
                    obj.put("noof_connection", arrayListNoConnection.get(i));
                    jsonArray.put(obj);
                }

                Map<String, Object> params = new HashMap<>();
                params.put("user_id", Utility.getAppPrefString(mContext, Constant.USER_ID));
                params.put("workorder_id", workorderid);
                params.put("material_data", jsonArray);
                VolleyCacheRequestClass.getInstance().volleyJsonAPI1(mContext, Constant.CALCULATE_AMOUNT,
                        Constant.URL_CALCULATE_AMOUNT, params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Utility.toast("No Internet Connection", mContext);
    }

    private void saveEstimation() {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            try {
                JSONObject obj = null;
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < arrayListWorkType.size(); i++) {
                    obj = new JSONObject();
                    obj.put("work_type", arrayListWorkType.get(i));
                    obj.put("material_id", arrayListMaterial.get(i));
                    obj.put("material_qty", arrayListUsedQty.get(i));
                    obj.put("connection_type", arrayListConnectionType.get(i));
                    obj.put("noof_connection", arrayListNoConnection.get(i));
                    jsonArray.put(obj);
                }

                Map<String, Object> params = new HashMap<>();
                params.put("user_id", Utility.getAppPrefString(mContext, Constant.USER_ID));
                params.put("workorder_id", workorderid);
                params.put("signature", "");
                params.put("material_data", jsonArray);
                VolleyCacheRequestClass.getInstance().volleyJsonAPI1(mContext, Constant.SAVE_EXTRA_ESTIMATION,
                        Constant.URL_SAVE_EXTRA_ESTIMATION, params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Utility.toast("No Internet Connection", mContext);
    }

    @Override
    public void vResponse(int reqCode, String result) {
        String response, message;
        try {
            JSONObject jObject = new JSONObject(result);
            if (reqCode == Constant.GET_WORK_TYPE) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray1 = jObject.getJSONArray("workorder_data");
                }
                getMaterial();
            } else if (reqCode == Constant.GET_MATERIAL_DATA) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray2 = jObject.getJSONArray("material_data");
                    arrayList.add("1");
                    addMore();
                }
            } else if (reqCode == Constant.GET_CONNECTION_TYPE) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray3 = jObject.getJSONArray("connection_data");
                }
            } else if (reqCode == Constant.CALCULATE_AMOUNT) {
                response = jObject.getString("response");
                message = jObject.getString("message");
                if (response.equalsIgnoreCase("true")) {
                    tv_amount.setText("₹ " + jObject.getString("total_amount"));
                } else {
                    Utility.toast(message, mContext);
                }
            } else if (reqCode == Constant.SAVE_EXTRA_ESTIMATION) {
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
                    tv_success_msg.setText("Extra Material Estimation Saved Successfully");

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

    public void addMore() {
        View view = LayoutInflater.from(this).inflate(R.layout.custom_connection_list_item, null);
        Spinner sp_work_type = (Spinner) view.findViewById(R.id.sp_work_type);
        Spinner sp_connection_type = (Spinner) view.findViewById(R.id.sp_conn_type);
        Spinner sp_material_name = (Spinner) view.findViewById(R.id.sp_material_name);
        ImageView img_remove = (ImageView) view.findViewById(R.id.img_remove);
        EditText et_qty = (EditText) view.findViewById(R.id.et_qty_used);
        EditText et_no_conn = (EditText) view.findViewById(R.id.et_no_of_conn);
        LinearLayout ll_child = (LinearLayout) view.findViewById(R.id.ll_child);

        img_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll_material_list.getChildCount() != 1)
                    ll_child.setVisibility(View.GONE);
            }
        });

        try {
            JSONObject jsonObjectMessage;
            int lenth = jsonArray1.length() + 1;
            workorder_id = new String[lenth];
            workorder_name = new String[lenth];
            for (int a = 0; a < lenth; a++) {
                if (a == 0) {
                    workorder_id[0] = "0";
                    workorder_name[0] = "Select Work Type";
                } else {
                    jsonObjectMessage = jsonArray1.getJSONObject(a - 1);
                    workorder_id[a] = jsonObjectMessage.getString("workorder_id");
                    workorder_name[a] = jsonObjectMessage.getString("workorder_name");
                }
            }
            Utility.setSpinnerAdapter1(mContext, sp_work_type, workorder_name);

            int lenth2 = jsonArray2.length() + 1;
            material_id = new String[lenth2];
            material_name = new String[lenth2];
            material_amount = new String[lenth2];
            for (int a = 0; a < lenth2; a++) {
                if (a == 0) {
                    material_id[0] = "0";
                    material_name[0] = "Select Material Name";
                    material_amount[0] = "0";
                } else {
                    jsonObjectMessage = jsonArray2.getJSONObject(a - 1);
                    material_id[a] = jsonObjectMessage.getString("material_id");
                    material_name[a] = jsonObjectMessage.getString("material_name");
                    material_amount[a] = jsonObjectMessage.getString("material_amount");
                }
            }
            Utility.setSpinnerAdapter1(mContext, sp_material_name, material_name);

            int lenth3 = jsonArray3.length() + 1;
            connection_id = new String[lenth3];
            connection_name = new String[lenth3];
            for (int a = 0; a < lenth3; a++) {
                if (a == 0) {
                    connection_id[0] = "0";
                    connection_name[0] = "Select";
                } else {
                    jsonObjectMessage = jsonArray3.getJSONObject(a - 1);
                    connection_id[a] = jsonObjectMessage.getString("connection_id");
                    connection_name[a] = jsonObjectMessage.getString("connection_name");
                }
            }
            Utility.setSpinnerAdapter1(mContext, sp_connection_type, connection_name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ll_material_list.addView(view);
    }
}
