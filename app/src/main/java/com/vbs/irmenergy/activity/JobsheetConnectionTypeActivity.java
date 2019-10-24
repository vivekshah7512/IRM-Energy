package com.vbs.irmenergy.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.vbs.irmenergy.R;
import com.vbs.irmenergy.adapter.ConnectionAdapter;
import com.vbs.irmenergy.utilities.APIProgressDialog;
import com.vbs.irmenergy.utilities.Constant;
import com.vbs.irmenergy.utilities.ExpandableHeightListView;
import com.vbs.irmenergy.utilities.Utility;
import com.vbs.irmenergy.utilities.volley.VolleyAPIClass;
import com.vbs.irmenergy.utilities.volley.VolleyCacheRequestClass;
import com.vbs.irmenergy.utilities.volley.VolleyResponseInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JobsheetConnectionTypeActivity extends Activity implements View.OnClickListener,
        VolleyResponseInterface {

    private Context mContext;
    private Button btn_comm_submit;
    private RadioGroup rg_connection;
    private ExpandableHeightListView listView;
    private ArrayList<String> arrayList;
    private ImageView img_back;
    private VolleyAPIClass volleyAPIClass;
    private APIProgressDialog mProgressDialog;
    private String[] material_id, material_name, material_amount, workorder_id, workorder_name;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_jobsheet_connection_type);
        mContext = this;

        initUI();

    }

    private void initUI() {
        mProgressDialog = new APIProgressDialog(mContext, R.style.DialogStyle);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        volleyAPIClass = new VolleyAPIClass();

        btn_comm_submit = (Button) findViewById(R.id.btn_jobsheet_submit);
        btn_comm_submit.setOnClickListener(this);
        rg_connection = (RadioGroup) findViewById(R.id.rg_connection);

        arrayList = new ArrayList<>();
        arrayList.add("GI Pipe");
        arrayList.add("PEO inside");
        arrayList.add("PEO outside");
        listView = (ExpandableHeightListView) findViewById(R.id.list_connection);
        listView.setAdapter(new ConnectionAdapter(mContext, arrayList));
        listView.setExpanded(true);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        getWorkType();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_jobsheet_submit:
                Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_sucess);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);

                Button btn_dashboard = (Button) dialog.findViewById(R.id.btn_dashboard);
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

                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.width = ActionBar.LayoutParams.MATCH_PARENT;
                wlp.height = ActionBar.LayoutParams.MATCH_PARENT;
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
            params.put("WOtype", "1");
            VolleyCacheRequestClass.getInstance().volleyJsonAPI(mContext, Constant.GET_WORK_TYPE,
                    Constant.URL_GET_WORK_TYPE, params);
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
            if (reqCode == Constant.GET_WORK_TYPE) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("workorder_data");
                    int lenth = jsonArray.length() + 1;
                    workorder_id = new String[lenth];
                    workorder_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            workorder_id[0] = "0";
                            workorder_name[0] = "Select Work Type";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            workorder_id[a] = jsonObjectMessage.getString("workorder_id");
                            workorder_name[a] = jsonObjectMessage.getString("workorder_name");
                        }
                    }
                }
            } else if (reqCode == Constant.GET_MATERIAL_DATA) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("material_data");
                    int lenth = jsonArray.length() + 1;
                    material_id = new String[lenth];
                    material_name = new String[lenth];
                    material_amount = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            material_id[0] = "0";
                            material_name[0] = "Select House Type";
                            material_amount[0] = "0";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            material_id[a] = jsonObjectMessage.getString("material_id");
                            material_name[a] = jsonObjectMessage.getString("material_name");
                            material_amount[a] = jsonObjectMessage.getString("material_amount");
                        }
                    }
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
