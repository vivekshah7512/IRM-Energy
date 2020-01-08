package com.vbs.irmenergy.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
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
    private Button btn_search, btn_verify, btn_view;
    private CardView cardView_data, cardView_data1;
    private EditText et_app_no;
    private String flag = "";
    private APIProgressDialog mProgressDialog;
    private VolleyAPIClass volleyAPIClass;
    private TextView tv_application_no, tv_customer_name, tv_address, tv_area, tv_city,
            tv_contact_no, tv_email;
    private TextView tv_app_no, tv_cust_name, tv_meter_no, tv_meter_reading, tv_tube,
            tv_date, tv_remarks, tv_location, tv_cust_address, tv_cust_area, tv_cust_city;
    private String latitude, longitude, geoUri;

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
        btn_view = (Button) view.findViewById(R.id.btn_view);
        btn_view.setOnClickListener(this);
        btn_verify = (Button) view.findViewById(R.id.btn_verify);
        btn_verify.setOnClickListener(this);
        cardView_data = (CardView) view.findViewById(R.id.cv_search_data);
        cardView_data1 = (CardView) view.findViewById(R.id.cv_view_data);
        et_app_no = (EditText) view.findViewById(R.id.et_app_no);

        tv_application_no = (TextView) view.findViewById(R.id.tv_application_no);
        tv_customer_name = (TextView) view.findViewById(R.id.tv_customer_name);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        tv_area = (TextView) view.findViewById(R.id.tv_area);
        tv_city = (TextView) view.findViewById(R.id.tv_city);
        tv_contact_no = (TextView) view.findViewById(R.id.tv_contact_no);
        tv_email = (TextView) view.findViewById(R.id.tv_email_id);

        tv_app_no = (TextView) view.findViewById(R.id.tv_view_application_no);
        tv_cust_name = (TextView) view.findViewById(R.id.tv_view_customer_name);
        tv_cust_address = (TextView) view.findViewById(R.id.tv_view_customer_address);
        tv_cust_area = (TextView) view.findViewById(R.id.tv_view_customer_area);
        tv_cust_city = (TextView) view.findViewById(R.id.tv_view_customer_city);
        tv_meter_no = (TextView) view.findViewById(R.id.tv_view_meter_no);
        tv_meter_reading = (TextView) view.findViewById(R.id.tv_view_meter_reading);
        tv_tube = (TextView) view.findViewById(R.id.tv_view_rubber_tube);
        tv_date = (TextView) view.findViewById(R.id.tv_view_date);
        tv_location = (TextView) view.findViewById(R.id.tv_view_location);
        tv_location.setOnClickListener(this);
        tv_remarks = (TextView) view.findViewById(R.id.tv_view_remarks);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_view_location:
                if (!TextUtils.isEmpty(latitude)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                    startActivity(intent);
                } else {
                    Utility.toast("Location not found", getActivity());
                }
                break;
            case R.id.btn_view:
                if (et_app_no.getText().toString().equalsIgnoreCase("")) {
                    Utility.toast("Please enter application no", getActivity());
                } else {
                    viewSurveyDetails();
                }
                break;
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

    private void viewSurveyDetails() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, Object> params = new HashMap<>();
            params.put("center_code", Utility.getAppPrefString(getActivity(), "center_code"));
            params.put("application_no", et_app_no.getText().toString().trim());
            volleyAPIClass.volleyAPICall(getActivity(), FragmentSearch.this,
                    Constant.VIEW_APPLICATION_NO,
                    Constant.URL_VIEW_APPLICATION_NO, params);
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
                    cardView_data1.setVisibility(View.GONE);
                    tv_application_no.setText(jObject.getString("application_no"));
                    tv_customer_name.setText(jObject.getString("customer_name"));
                    tv_address.setText(jObject.getString("customer_address"));
                    tv_area.setText(jObject.getString("customer_areaname"));
                    tv_city.setText(jObject.getString("customer_cityname"));
                    tv_contact_no.setText(jObject.getString("customer_mobille"));
                    tv_email.setText(jObject.getString("customer_email"));
                } else {
                    Utility.toast(message, getActivity());
                    cardView_data.setVisibility(View.GONE);
                    cardView_data1.setVisibility(View.GONE);
                }
            } else if (reqCode == Constant.VIEW_APPLICATION_NO) {
                response = jObject.getString("response");
                message = jObject.getString("message");
                if (response.equalsIgnoreCase("true")) {
                    et_app_no.clearFocus();
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(et_app_no.getWindowToken(), 0);
                    cardView_data.setVisibility(View.GONE);
                    cardView_data1.setVisibility(View.VISIBLE);
                    tv_app_no.setText(jObject.getString("application_no"));
                    tv_cust_name.setText(jObject.getString("customer_name"));
                    tv_cust_address.setText(jObject.getString("customer_address"));
                    tv_cust_area.setText(jObject.getString("customer_area"));
                    tv_cust_city.setText(jObject.getString("customer_city"));
                    tv_meter_no.setText(jObject.getString("meter_no"));
                    tv_meter_reading.setText(jObject.getString("meter_reading"));
                    tv_tube.setText(jObject.getString("cs_rubberTube"));
                    tv_date.setText(jObject.getString("cs_date"));
                    tv_remarks.setText(jObject.getString("cs_remarks"));

                    latitude = jObject.getString("cs_latitude");
                    longitude = jObject.getString("cs_longitude");

                    geoUri = "http://maps.google.com/maps?q=loc:" + latitude + "," +
                            longitude + " (App No. : " +
                            tv_app_no.getText().toString() + ")";
                } else {
                    Utility.toast(message, getActivity());
                    cardView_data.setVisibility(View.GONE);
                    cardView_data1.setVisibility(View.GONE);
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
