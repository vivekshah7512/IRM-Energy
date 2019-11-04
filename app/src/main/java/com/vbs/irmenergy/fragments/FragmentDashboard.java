package com.vbs.irmenergy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.vbs.irmenergy.R;
import com.vbs.irmenergy.utilities.APIProgressDialog;
import com.vbs.irmenergy.utilities.Constant;
import com.vbs.irmenergy.utilities.Utility;
import com.vbs.irmenergy.utilities.volley.VolleyAPIClass;
import com.vbs.irmenergy.utilities.volley.VolleyResponseInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentDashboard extends Fragment implements OnClickListener,
        VolleyResponseInterface {

    View view;
    private Spinner sp_center;
    private VolleyAPIClass volleyAPIClass;
    private APIProgressDialog mProgressDialog;
    private String[] center_code, center_name;
    private LinearLayout ll_center, ll_no_center;
    private String stringCenterId = "0";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        init();

        sp_center.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (!center_code[position].equalsIgnoreCase("0")) {
                    stringCenterId = center_code[position];
                    Utility.writeSharedPreferences(getActivity(),"center_code",
                            stringCenterId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    public void init() {
        Utility.setTitle(getActivity(), "Home");

        mProgressDialog = new APIProgressDialog(getActivity(), R.style.DialogStyle);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);

        volleyAPIClass = new VolleyAPIClass();
        sp_center = (Spinner) view.findViewById(R.id.sp_center);
        ll_center = (LinearLayout) view.findViewById(R.id.ll_center);
        ll_no_center = (LinearLayout) view.findViewById(R.id.ll_no_center);

        if (Utility.getAppPrefString(getActivity(), Constant.USER_TYPE)
                .equalsIgnoreCase("1")) {
            ll_center.setVisibility(View.VISIBLE);
            ll_no_center.setVisibility(View.GONE);
            getCenter();
        } else {
            ll_center.setVisibility(View.GONE);
            ll_no_center.setVisibility(View.VISIBLE);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            default:
                return;
        }
    }

    private void getCenter() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            volleyAPIClass.volleyGetJsonAPI(getActivity(), FragmentDashboard.this,
                    Constant.GET_CENTER, Constant.URL_GET_CENTER);
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
            if (reqCode == Constant.GET_CENTER) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("center_data");
                    int lenth = jsonArray.length() + 1;
                    center_code = new String[lenth];
                    center_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            center_code[0] = "0";
                            center_name[0] = "Select Center";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            center_code[a] = jsonObjectMessage.getString("center_code");
                            center_name[a] = jsonObjectMessage.getString("center_name");
                        }
                    }
                    Utility.setSpinnerAdapter2(getActivity(), sp_center, center_name);
                    Utility.writeSharedPreferences(getActivity(),"center_code",
                            "1");
                    sp_center.setSelection(1);
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

    }
}
