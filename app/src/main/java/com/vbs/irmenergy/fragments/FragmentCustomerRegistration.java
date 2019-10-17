package com.vbs.irmenergy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.vbs.irmenergy.R;
import com.vbs.irmenergy.activity.PaymentDetailActivity;
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

public class FragmentCustomerRegistration extends Fragment implements OnClickListener,
        VolleyResponseInterface, AdapterView.OnItemSelectedListener {

    private View view;
    private LinearLayout ll_reg1, ll_reg2, ll_reg3, ll_corporate;
    private ImageView img_reg1, img_reg2, img_reg3;
    private Button btn_save;
    private VolleyAPIClass volleyAPIClass;
    private APIProgressDialog mProgressDialog;
    private String[] type_id, type_name, category_id, category_name, corporate_id,
            corporate_name, property_id, property_name, ownership_id, ownership_name,
            contractor_id, contractor_name;
    private String stringTypeId = "0", stringCategoryId = "0",
            stringCorporateId = "0", stringPropertyId = "0", stringOwnerId = "0",
            stringContractorId = "0";
    private Spinner sp_customer_type, sp_customer_category, sp_corporate_name,
            sp_owner_type, sp_property_type, sp_contractor;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_customer_registration, container, false);

        init();

        return view;
    }

    public void init() {
        Utility.setTitle(getActivity(), "Registration");
        mProgressDialog = new APIProgressDialog(getActivity(), R.style.DialogStyle);
        volleyAPIClass = new VolleyAPIClass();

        ll_corporate = view.findViewById(R.id.ll_corporate);
        ll_reg1 = view.findViewById(R.id.ll_reg1);
        ll_reg2 = view.findViewById(R.id.ll_reg2);
        ll_reg3 = view.findViewById(R.id.ll_reg3);
        img_reg1 = view.findViewById(R.id.img_reg1);
        img_reg1.setOnClickListener(this);
        img_reg2 = view.findViewById(R.id.img_reg2);
        img_reg2.setOnClickListener(this);
        img_reg3 = view.findViewById(R.id.img_reg3);
        img_reg3.setOnClickListener(this);
        btn_save = view.findViewById(R.id.btn_cust_payment);
        btn_save.setOnClickListener(this);

        sp_customer_type = (Spinner) view.findViewById(R.id.sp_cust_type);
        sp_customer_type.setOnItemSelectedListener(this);
        sp_customer_category = (Spinner) view.findViewById(R.id.sp_cust_category);
        sp_customer_category.setOnItemSelectedListener(this);
        sp_corporate_name = (Spinner) view.findViewById(R.id.sp_corporate_name);
        sp_corporate_name.setOnItemSelectedListener(this);
        sp_property_type = (Spinner) view.findViewById(R.id.sp_property_type);
        sp_property_type.setOnItemSelectedListener(this);
        sp_owner_type = (Spinner) view.findViewById(R.id.sp_owner_type);
        sp_owner_type.setOnItemSelectedListener(this);
        sp_contractor = (Spinner) view.findViewById(R.id.sp_contractor);
        sp_contractor.setOnItemSelectedListener(this);

        getCustomerType();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cust_payment:
                Intent intent = new Intent(getActivity(), PaymentDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.img_reg1:
                ll_reg1.setVisibility(View.VISIBLE);
                ll_reg2.setVisibility(View.GONE);
                ll_reg3.setVisibility(View.GONE);
                img_reg1.setImageResource(R.drawable.right);
                img_reg1.setRotation(90);
                img_reg2.setImageResource(R.drawable.right);
                img_reg2.setRotation(0);
                img_reg3.setImageResource(R.drawable.right);
                img_reg3.setRotation(0);
                break;
            case R.id.img_reg2:
                ll_reg1.setVisibility(View.GONE);
                ll_reg2.setVisibility(View.VISIBLE);
                ll_reg3.setVisibility(View.GONE);
                img_reg1.setImageResource(R.drawable.right);
                img_reg1.setRotation(0);
                img_reg2.setImageResource(R.drawable.right);
                img_reg2.setRotation(90);
                img_reg3.setImageResource(R.drawable.right);
                img_reg3.setRotation(0);
                break;
            case R.id.img_reg3:
                ll_reg1.setVisibility(View.GONE);
                ll_reg2.setVisibility(View.GONE);
                ll_reg3.setVisibility(View.VISIBLE);
                img_reg1.setImageResource(R.drawable.right);
                img_reg1.setRotation(0);
                img_reg2.setImageResource(R.drawable.right);
                img_reg2.setRotation(0);
                img_reg3.setImageResource(R.drawable.right);
                img_reg3.setRotation(90);
                getPropertyType();
                getOwnershipType();
                getContractorName();
                break;
            default:
                return;
        }
    }

    private void getCustomerType() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            volleyAPIClass.volleyGetJsonAPI(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_CUSTOMER_TYPE,
                    Constant.URL_GET_CUSTOMER_TYPE);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    private void getCustomerCategory() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, Object> params = new HashMap<>();
            params.put("type_id", stringTypeId);
            volleyAPIClass.volleyAPICall(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_CUSTOMER_CATEGORY,
                    Constant.URL_GET_CUSTOMER_CATEGORY, params);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    private void getCorporateName() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, Object> params = new HashMap<>();
            params.put("type_id", stringTypeId);
            params.put("category_id", stringCategoryId);
            params.put("center_code", "BKPP10");
            volleyAPIClass.volleyAPICall(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_CORPORATE_NAME,
                    Constant.URL_GET_CORPORATE_NAME, params);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    private void getPropertyType() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            volleyAPIClass.volleyGetJsonAPI(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_PROPERTY_TYPE,
                    Constant.URL_GET_PROPERTY_TYPE);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    private void getOwnershipType() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            volleyAPIClass.volleyGetJsonAPI(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_OWNER_TYPE,
                    Constant.URL_GET_OWNER_TYPE);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    private void getContractorName() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, Object> params = new HashMap<>();
            params.put("center_code", "BKPP10");
            volleyAPIClass.volleyAPICall(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_CONTRACTOR,
                    Constant.URL_GET_CONTRACTOR, params);
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
            if (reqCode == Constant.GET_CUSTOMER_TYPE) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("type_data");
                    int lenth = jsonArray.length();
                    type_id = new String[lenth];
                    type_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        jsonObjectMessage = jsonArray.getJSONObject(a);
                        type_id[a] = jsonObjectMessage.getString("type_id");
                        type_name[a] = jsonObjectMessage.getString("type_name");
                    }
                    ArrayAdapter adapter = new ArrayAdapter<String>(
                            getActivity(), R.layout.spinner_item, type_name);
                    sp_customer_type.setAdapter(adapter);
                    getCustomerCategory();
                }
            } else if (reqCode == Constant.GET_CUSTOMER_CATEGORY) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("category_data");
                    int lenth = jsonArray.length();
                    category_id = new String[lenth];
                    category_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        jsonObjectMessage = jsonArray.getJSONObject(a);
                        category_id[a] = jsonObjectMessage.getString("category_id");
                        category_name[a] = jsonObjectMessage.getString("category_name");
                    }
                    ArrayAdapter adapter = new ArrayAdapter<String>(
                            getActivity(), R.layout.spinner_item, category_name);
                    sp_customer_category.setAdapter(adapter);

                }
            } else if (reqCode == Constant.GET_CORPORATE_NAME) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("corporate_data");
                    int lenth = jsonArray.length();
                    corporate_id = new String[lenth];
                    corporate_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        jsonObjectMessage = jsonArray.getJSONObject(a);
                        corporate_id[a] = jsonObjectMessage.getString("corporate_id");
                        corporate_name[a] = jsonObjectMessage.getString("corporate_name");
                    }
                    ll_corporate.setVisibility(View.VISIBLE);
                    ArrayAdapter adapter = new ArrayAdapter<String>(
                            getActivity(), R.layout.spinner_item, corporate_name);
                    sp_corporate_name.setAdapter(adapter);

                }
            } else if (reqCode == Constant.GET_PROPERTY_TYPE) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("property_data");
                    int lenth = jsonArray.length();
                    property_id = new String[lenth];
                    property_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        jsonObjectMessage = jsonArray.getJSONObject(a);
                        property_id[a] = jsonObjectMessage.getString("property_id");
                        property_name[a] = jsonObjectMessage.getString("property_name");
                    }
                    ll_corporate.setVisibility(View.VISIBLE);
                    ArrayAdapter adapter = new ArrayAdapter<String>(
                            getActivity(), R.layout.spinner_item, property_name);
                    sp_property_type.setAdapter(adapter);

                }
            } else if (reqCode == Constant.GET_OWNER_TYPE) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("ownership_data");
                    int lenth = jsonArray.length();
                    ownership_id = new String[lenth];
                    ownership_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        jsonObjectMessage = jsonArray.getJSONObject(a);
                        ownership_id[a] = jsonObjectMessage.getString("ownership_id");
                        ownership_name[a] = jsonObjectMessage.getString("ownership_name");
                    }
                    ll_corporate.setVisibility(View.VISIBLE);
                    ArrayAdapter adapter = new ArrayAdapter<String>(
                            getActivity(), R.layout.spinner_item, ownership_name);
                    sp_owner_type.setAdapter(adapter);

                }
            } else if (reqCode == Constant.GET_CONTRACTOR) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("contractor_data");
                    int lenth = jsonArray.length();
                    contractor_id = new String[lenth];
                    contractor_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        jsonObjectMessage = jsonArray.getJSONObject(a);
                        contractor_id[a] = jsonObjectMessage.getString("contractor_id");
                        contractor_name[a] = jsonObjectMessage.getString("contractor_name");
                    }
                    ll_corporate.setVisibility(View.VISIBLE);
                    ArrayAdapter adapter = new ArrayAdapter<String>(
                            getActivity(), R.layout.spinner_item, contractor_name);
                    sp_contractor.setAdapter(adapter);

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
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String selectedText = adapterView.getItemAtPosition(position).toString();
        switch (adapterView.getId()) {
            case R.id.sp_cust_type:
                stringTypeId = type_id[position];
                getCustomerCategory();
                break;
            case R.id.sp_cust_category:
                stringCategoryId = category_id[position];
                if (category_name[position].equalsIgnoreCase("corporate"))
                    getCorporateName();
                else
                    ll_corporate.setVisibility(View.GONE);
                break;
            case R.id.sp_corporate_name:
                stringCorporateId = corporate_id[position];
                break;
            case R.id.sp_property_type:
                stringPropertyId = property_id[position];
                break;
            case R.id.sp_owner_type:
                stringOwnerId = ownership_id[position];
                break;
            case R.id.sp_contractor:
                stringContractorId = contractor_id[position];
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
