package com.vbs.irmenergy.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vbs.irmenergy.R;
import com.vbs.irmenergy.activity.CommissionProcessActivity;
import com.vbs.irmenergy.activity.CustomerDetailActivity;
import com.vbs.irmenergy.activity.ExtraMaterialEstimationActivity;
import com.vbs.irmenergy.activity.JobsheetDetailsActivity;
import com.vbs.irmenergy.utilities.APIProgressDialog;
import com.vbs.irmenergy.utilities.Constant;
import com.vbs.irmenergy.utilities.Utility;
import com.vbs.irmenergy.utilities.volley.VolleyAPIClass;
import com.vbs.irmenergy.utilities.volley.VolleyResponseInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragmentSearch extends Fragment implements OnClickListener,
        VolleyResponseInterface {

    private View view;
    private Button btn_search, btn_verify;
    private CardView cardView_data;
    private EditText et_app_no;
    private String flag = "";
    private APIProgressDialog mProgressDialog;
    private VolleyAPIClass volleyAPIClass;
    private TextView tv_application_no, tv_customer_name, tv_address, tv_area, tv_city,
            tv_contact_no, tv_email;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        init();

        return view;
    }

    public void init() {
        mProgressDialog = new APIProgressDialog(getActivity(), R.style.DialogStyle);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);

        volleyAPIClass = new VolleyAPIClass();

        if (Utility.getAppPrefString(getActivity(), "searchFlag")
                .equalsIgnoreCase("estimation"))
            Utility.setTitle(getActivity(), "Extra Material Estimation");
        else if (Utility.getAppPrefString(getActivity(), "searchFlag")
                .equalsIgnoreCase("jobsheet"))
            Utility.setTitle(getActivity(), "Jobsheet Upload");
        else if (Utility.getAppPrefString(getActivity(), "searchFlag")
                .equalsIgnoreCase("commission"))
            Utility.setTitle(getActivity(), "Commissioning");
        else if (Utility.getAppPrefString(getActivity(), "searchFlag")
                .equalsIgnoreCase("survey"))
            Utility.setTitle(getActivity(), "Customer Survey");
        flag = Utility.getAppPrefString(getActivity(), "searchFlag");

        btn_search = (Button) view.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        btn_verify = (Button) view.findViewById(R.id.btn_verify);
        btn_verify.setOnClickListener(this);
        cardView_data = (CardView) view.findViewById(R.id.cv_search_data);
        et_app_no = (EditText) view.findViewById(R.id.et_app_no);

        tv_application_no = (TextView) view.findViewById(R.id.tv_application_no);
        tv_customer_name = (TextView) view.findViewById(R.id.tv_customer_name);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        tv_area = (TextView) view.findViewById(R.id.tv_area);
        tv_city = (TextView) view.findViewById(R.id.tv_city);
        tv_contact_no = (TextView) view.findViewById(R.id.tv_contact_no);
        tv_email = (TextView) view.findViewById(R.id.tv_email_id);
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
            case R.id.btn_verify:
                if (flag.equalsIgnoreCase("survey")) {
                    Intent intent = new Intent(getActivity(), CustomerDetailActivity.class);
                    intent.putExtra("app_no", et_app_no.getText().toString().trim());
                    startActivity(intent);
                } else if (flag.equalsIgnoreCase("commission")) {
                    Intent intent = new Intent(getActivity(), CommissionProcessActivity.class);
                    intent.putExtra("app_no", et_app_no.getText().toString().trim());
                    startActivity(intent);
                } else if (flag.equalsIgnoreCase("jobsheet")) {
                    Intent intent = new Intent(getActivity(), JobsheetDetailsActivity.class);
                    intent.putExtra("app_no", et_app_no.getText().toString().trim());
                    startActivity(intent);
                } else if (flag.equalsIgnoreCase("estimation")) {
                    Intent intent = new Intent(getActivity(), ExtraMaterialEstimationActivity.class);
                    startActivity(intent);
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
            params.put("user_id", Utility.getAppPrefString(getActivity(), Constant.USER_ID));
            params.put("user_type", Utility.getAppPrefString(getActivity(), Constant.USER_TYPE));
            params.put("application_no", et_app_no.getText().toString().trim());
            volleyAPIClass.volleyAPICall(getActivity(), FragmentSearch.this,
                    Constant.SEARCH_APPLICATION_NO,
                    Constant.URL_SEARCH_APPLICATION_NO, params);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    @Override
    public void vResponse(int reqCode, String result) {
        String response, message;
        try {
            JSONObject jObject = new JSONObject(result);
            if (reqCode == Constant.SEARCH_APPLICATION_NO) {
                response = jObject.getString("response");
                message = jObject.getString("message");
                if (response.equalsIgnoreCase("true")) {
                    et_app_no.clearFocus();
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(et_app_no.getWindowToken(), 0);
                    cardView_data.setVisibility(View.VISIBLE);
                    tv_application_no.setText(jObject.getString("application_no"));
                    tv_customer_name.setText(jObject.getString("customer_name"));
                    tv_address.setText(jObject.getString("customer_address"));
                    tv_area.setText(jObject.getString("customer_areaname"));
                    tv_city.setText(jObject.getString("customer_cityname"));
                    tv_contact_no.setText(jObject.getString("customer_mobille"));
                    tv_email.setText(jObject.getString("customer_email"));
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
