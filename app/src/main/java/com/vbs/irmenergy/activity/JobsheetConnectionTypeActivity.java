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
        VolleyResponseInterface, ConnectionAdapter.AddItem {

    private Context mContext;
    private Button btn_comm_submit;
    private ExpandableHeightListView listView;
    private ImageView img_back;
    private VolleyAPIClass volleyAPIClass;
    private APIProgressDialog mProgressDialog;
    private String woType = "0";
    private JSONArray jsonArray1 = null, jsonArray2 = null;
    private ArrayList<String> arrayList;
    private ConnectionAdapter adapter;

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
        arrayList = new ArrayList<>();

        btn_comm_submit = (Button) findViewById(R.id.btn_jobsheet_submit);
        btn_comm_submit.setOnClickListener(this);
        listView = (ExpandableHeightListView) findViewById(R.id.list_connection);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        woType = getIntent().getStringExtra("woType");

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
            params.put("WOtype", woType);
            VolleyCacheRequestClass.getInstance().volleyJsonAPI(mContext, Constant.GET_WORK_TYPE,
                    Constant.URL_GET_WORK_TYPE, params);
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
                    adapter = new ConnectionAdapter(mContext, arrayList,
                            jsonArray1, jsonArray2);
                    listView.setAdapter(adapter);
                    listView.setExpanded(true);
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
    public void add(int type, int pos) {
        if (type == 0) {
            // Add
            arrayList.add("1");
            adapter = new ConnectionAdapter(mContext, arrayList,
                    jsonArray1, jsonArray2);
            listView.setAdapter(adapter);
            listView.setExpanded(true);
        } else {
            // Remove
            arrayList.remove(0);
            adapter.notifyDataSetChanged();
        }
    }
}
