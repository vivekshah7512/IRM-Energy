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
import com.vbs.irmenergy.adapter.JobsheetAdapter;
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

public class FragmentJobsheetSearch extends Fragment implements OnClickListener,
        VolleyResponseInterface {

    private View view;
    private RelativeLayout btn_search;
    private APIProgressDialog mProgressDialog;
    private VolleyAPIClass volleyAPIClass;
    private RecyclerView recyclerView;
    private LinearLayoutManager lLayout;
    private EditText et_app_no;
    private String[] workorder_id, workorder_refno, workorder_date, workorder_type,
            workorder_type_id, plan_name,
            application_no, workorder_contractorID, app_id, customer_name,
            customer_address, customer_area;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_jobsheet_search, container, false);

        init();

        return view;
    }

    public void init() {
        mProgressDialog = new APIProgressDialog(getActivity(), R.style.DialogStyle);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);

        volleyAPIClass = new VolleyAPIClass();

        Utility.setTitle(getActivity(), "Jobsheet Upload");

        lLayout = new LinearLayoutManager(getActivity(), 1, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_jobsheet);
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
                    searchApplicationNo1();
                }
                break;
            default:
                return;
        }
    }

    private void searchApplicationNo1() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, Object> params = new HashMap<>();
            params.put("center_code", Utility.getAppPrefString(getActivity(), "center_code"));
            params.put("application_no", et_app_no.getText().toString().trim());
            volleyAPIClass.volleyAPICall(getActivity(), FragmentJobsheetSearch.this,
                    Constant.GET_WORK_ORDER_LIST,
                    Constant.URL_GET_WORK_ORDER_LIST, params);
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
            if (reqCode == Constant.GET_WORK_ORDER_LIST) {
                response = jObject.getString("response");
                message = jObject.getString("message");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("workorderlist_data");
                    int lenth = jsonArray.length();
                    workorder_id = new String[lenth];
                    workorder_refno = new String[lenth];
                    workorder_date = new String[lenth];
                    workorder_type = new String[lenth];
                    workorder_type_id = new String[lenth];
                    plan_name = new String[lenth];
                    application_no = new String[lenth];
                    workorder_contractorID = new String[lenth];
                    app_id = new String[lenth];
                    customer_name = new String[lenth];
                    customer_address = new String[lenth];
                    customer_area = new String[lenth];
                    for (int j = 0; j < lenth; j++) {
                        jsonObjectMessage = jsonArray.getJSONObject(j);
                        workorder_id[j] = jsonObjectMessage.getString("workorder_id");
                        workorder_refno[j] = jsonObjectMessage.getString("workorder_refno");
                        workorder_date[j] = jsonObjectMessage.getString("workorder_date");
                        workorder_type[j] = jsonObjectMessage.getString("workorder_type_name");
                        workorder_type_id[j] = jsonObjectMessage.getString("workorder_type_id");
                        plan_name[j] = jsonObjectMessage.getString("plan_name");
                        application_no[j] = jsonObjectMessage.getString("application_no");
                        workorder_contractorID[j] = jsonObjectMessage.getString("workorder_contractorID");
                        app_id[j] = jsonObjectMessage.getString("app_id");
                        customer_name[j] = jsonObjectMessage.getString("customer_name");
                        customer_address[j] = jsonObjectMessage.getString("customer_address");
                        customer_area[j] = jsonObjectMessage.getString("customer_area");
                    }
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(et_app_no.getWindowToken(), 0);
                    recyclerView.setAdapter(new JobsheetAdapter(getActivity(), workorder_id,
                            workorder_refno, workorder_date, workorder_type, workorder_type_id, plan_name,
                            application_no, workorder_contractorID, app_id, customer_name,
                            customer_address, customer_area));
                } else {
                    Utility.toast(message, getActivity());
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
