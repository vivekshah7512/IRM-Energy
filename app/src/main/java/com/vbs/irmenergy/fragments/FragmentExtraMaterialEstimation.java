package com.vbs.irmenergy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.vbs.irmenergy.R;
import com.vbs.irmenergy.adapter.EstimationAdapter;
import com.vbs.irmenergy.utilities.APIProgressDialog;
import com.vbs.irmenergy.utilities.Constant;
import com.vbs.irmenergy.utilities.Utility;
import com.vbs.irmenergy.utilities.volley.VolleyAPIClass;
import com.vbs.irmenergy.utilities.volley.VolleyResponseInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragmentExtraMaterialEstimation extends Fragment implements OnClickListener,
        VolleyResponseInterface {

    private View view;
    private RelativeLayout btn_search;
    private APIProgressDialog mProgressDialog;
    private VolleyAPIClass volleyAPIClass;
    private RecyclerView recyclerView;
    private LinearLayoutManager lLayout;
    private EditText et_app_no;
    private String[] estworkorder_refno, estworkorder_id, estworkorder_date,
            estworkorder_type_id, estworkorder_type_name, estplan_name,
            estapplication_no, estapp_id, estcustomer_name, estcustomer_address,
            estcustomer_area, estcustomer_city, estworkorder_estimationstatus,
            estworkorder_pdfpath, estworkorder_estamount;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_extra_material_estimation, container, false);

        init();

        return view;
    }

    public void init() {
        mProgressDialog = new APIProgressDialog(getActivity(), R.style.DialogStyle);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);

        volleyAPIClass = new VolleyAPIClass();

        Utility.setTitle(getActivity(), "Extra Material Estimation");

        lLayout = new LinearLayoutManager(getActivity(), 1, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_estimation);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), 1);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        recyclerView.addItemDecoration(itemDecorator);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(lLayout);

        btn_search = (RelativeLayout) view.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        et_app_no = (EditText) view.findViewById(R.id.et_app_no);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                if (et_app_no.getText().toString().equalsIgnoreCase("")) {
                    Utility.toast("Please enter application no", getActivity());
                } else {
                    searchApplicationNo();
                }
                break;
            default:
                return;
        }
    }

    private void searchApplicationNo() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, Object> params = new HashMap<>();
            params.put("center_code", Utility.getAppPrefString(getActivity(), "center_code"));
            params.put("application_no", et_app_no.getText().toString().trim());
            params.put("user_id", Utility.getAppPrefString(getActivity(), Constant.USER_ID));
            volleyAPIClass.volleyAPICall(getActivity(), FragmentExtraMaterialEstimation.this,
                    Constant.GET_ESTIMATE_LIST,
                    Constant.URL_GET_ESTIMATE_LIST, params);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    @Override
    public void vResponse(int reqCode, String result) {
        String response, message;
        JSONObject jsonObjectMessage;
        JSONArray jsonArray;
        try {
            JSONObject jObject = new JSONObject(result);
            if (reqCode == Constant.GET_ESTIMATE_LIST) {
                response = jObject.getString("response");
                message = jObject.getString("message");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("estworkorderlist_data");
                    int lenth = jsonArray.length();
                    estworkorder_refno = new String[lenth];
                    estworkorder_id = new String[lenth];
                    estworkorder_date = new String[lenth];
                    estworkorder_type_id = new String[lenth];
                    estworkorder_type_name = new String[lenth];
                    estplan_name = new String[lenth];
                    estapplication_no = new String[lenth];
                    estapp_id = new String[lenth];
                    estcustomer_name = new String[lenth];
                    estcustomer_address = new String[lenth];
                    estcustomer_area = new String[lenth];
                    estcustomer_city = new String[lenth];
                    estworkorder_estimationstatus = new String[lenth];
                    estworkorder_pdfpath = new String[lenth];
                    estworkorder_estamount = new String[lenth];
                    for (int j = 0; j < lenth; j++) {
                        jsonObjectMessage = jsonArray.getJSONObject(j);
                        estworkorder_refno[j] = jsonObjectMessage.getString("estworkorder_refno");
                        estworkorder_id[j] = jsonObjectMessage.getString("estworkorder_id");
                        estworkorder_date[j] = jsonObjectMessage.getString("estworkorder_date");
                        estworkorder_type_id[j] = jsonObjectMessage.getString("estworkorder_type_id");
                        estworkorder_type_name[j] = jsonObjectMessage.getString("estworkorder_type_name");
                        estplan_name[j] = jsonObjectMessage.getString("estplan_name");
                        estapplication_no[j] = jsonObjectMessage.getString("estapplication_no");
                        estapp_id[j] = jsonObjectMessage.getString("estapp_id");
                        estcustomer_name[j] = jsonObjectMessage.getString("estcustomer_name");
                        estcustomer_address[j] = jsonObjectMessage.getString("estcustomer_address");
                        estcustomer_area[j] = jsonObjectMessage.getString("estcustomer_area");
                        estcustomer_city[j] = jsonObjectMessage.getString("estcustomer_city");
                        estworkorder_estimationstatus[j] = jsonObjectMessage.getString("estworkorder_estimationstatus");
                        estworkorder_pdfpath[j] = jsonObjectMessage.getString("estworkorder_pdfpath");
                        estworkorder_estamount[j] = jsonObjectMessage.getString("estworkorder_estamount");
                    }
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(et_app_no.getWindowToken(), 0);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(new EstimationAdapter(getActivity(), estworkorder_refno, estworkorder_id, estworkorder_date,
                            estworkorder_type_id, estworkorder_type_name, estplan_name,
                            estapplication_no, estapp_id, estcustomer_name, estcustomer_address,
                            estcustomer_area, estcustomer_city, estworkorder_estimationstatus,
                            estworkorder_pdfpath, estworkorder_estamount));
                } else {
                    Utility.toast(message, getActivity());
                    recyclerView.setVisibility(View.GONE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Utility.toast("No Records Found.", getActivity());
            recyclerView.setVisibility(View.GONE);
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
