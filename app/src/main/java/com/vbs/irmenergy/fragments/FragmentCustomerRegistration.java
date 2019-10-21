package com.vbs.irmenergy.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
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
            contractor_id, contractor_name, billing_id, billing_name, state_id, state_name,
            city_id, city_name, area_id, area_name;
    private String stringTypeId = "0", stringCategoryId = "0",
            stringCorporateId = "0", stringPropertyId = "0", stringOwnerId = "0",
            stringContractorId = "0", stringBillingTo = "0", stringState = "0",
            stringCity = "0", stringArea = "0", stringDoc1 = "0", stringDoc2 = "0";
    private Spinner sp_customer_type, sp_customer_category, sp_corporate_name,
            sp_owner_type, sp_property_type, sp_contractor, sp_billing_to,
            sp_doc1, sp_doc2, sp_state, sp_city, sp_area;
    private EditText et_application_no, et_date, et_firstname, et_middlename, et_lastname,
            et_dob, et_aadhar_no, et_block_no, et_address1, et_address2, et_pincode, et_contact_no,
            et_mobile_no, et_email, et_lpg_no, et_lpg_distributor, et_lpg_omc, et_property_name,
            et_owner_name, et_owner_contact_no, et_remarks;
    private RadioGroup rg_payment;
    private String paymentType = "online";
    private Calendar myCalendar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_customer_registration, container, false);

        init();

        rg_payment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_online:
                        paymentType = "online";
                        break;
                    case R.id.rb_cheque:
                        paymentType = "cheque";
                        break;
                }
            }
        });

        return view;
    }

    public void init() {
        Utility.setTitle(getActivity(), "Registration");
        mProgressDialog = new APIProgressDialog(getActivity(), R.style.DialogStyle);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);

        volleyAPIClass = new VolleyAPIClass();
        myCalendar = Calendar.getInstance();

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
        sp_billing_to = (Spinner) view.findViewById(R.id.sp_billing_to);
        sp_billing_to.setOnItemSelectedListener(this);
        sp_doc1 = (Spinner) view.findViewById(R.id.sp_doc1);
        sp_doc1.setOnItemSelectedListener(this);
        sp_doc2 = (Spinner) view.findViewById(R.id.sp_doc2);
        sp_doc2.setOnItemSelectedListener(this);
        sp_state = (Spinner) view.findViewById(R.id.sp_reg_state);
        sp_state.setOnItemSelectedListener(this);
        sp_city = (Spinner) view.findViewById(R.id.sp_reg_city);
        sp_city.setOnItemSelectedListener(this);
        sp_area = (Spinner) view.findViewById(R.id.sp_reg_area);
        sp_area.setOnItemSelectedListener(this);

        et_application_no = (EditText) view.findViewById(R.id.et_reg_application_no);
        et_date = (EditText) view.findViewById(R.id.et_reg_application_date);
        et_date.setOnClickListener(this);
        et_firstname = (EditText) view.findViewById(R.id.et_reg_first_name);
        et_middlename = (EditText) view.findViewById(R.id.et_reg_middle_name);
        et_lastname = (EditText) view.findViewById(R.id.et_reg_last_name);
        et_dob = (EditText) view.findViewById(R.id.et_reg_dob);
        et_dob.setOnClickListener(this);
        et_aadhar_no = (EditText) view.findViewById(R.id.et_reg_aadhar_no);
        et_block_no = (EditText) view.findViewById(R.id.et_reg_block_no);
        et_address1 = (EditText) view.findViewById(R.id.et_reg_address1);
        et_address2 = (EditText) view.findViewById(R.id.et_reg_address2);
        et_pincode = (EditText) view.findViewById(R.id.et_reg_pincode);
        et_contact_no = (EditText) view.findViewById(R.id.et_reg_contact_no);
        et_mobile_no = (EditText) view.findViewById(R.id.et_reg_mobile_no);
        et_email = (EditText) view.findViewById(R.id.et_reg_email_id);
        et_lpg_no = (EditText) view.findViewById(R.id.et_reg_lpg_no);
        et_lpg_distributor = (EditText) view.findViewById(R.id.et_reg_distributor);
        et_lpg_omc = (EditText) view.findViewById(R.id.et_reg_lpg_omc);
        et_property_name = (EditText) view.findViewById(R.id.et_reg_property_name);
        et_owner_name = (EditText) view.findViewById(R.id.et_reg_owner_name);
        et_owner_contact_no = (EditText) view.findViewById(R.id.et_reg_owner_contact_no);
        et_remarks = (EditText) view.findViewById(R.id.et_reg_remarks);
        rg_payment = (RadioGroup) view.findViewById(R.id.rg_payment);

        getCustomerType();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_reg_application_date:
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String myFormat = "dd-MM-yyyy";
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                et_date.setText(sdf.format(myCalendar.getTime()));
                            }
                        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
                dialog.show();
                break;
            case R.id.et_reg_dob:
                DatePickerDialog dialog1 = new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String myFormat = "dd-MM-yyyy";
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                et_dob.setText(sdf.format(myCalendar.getTime()));
                            }
                        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog1.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
                dialog1.show();
                break;
            case R.id.btn_cust_payment:
//                saveRegistrationDetails();
                if (paymentType.equalsIgnoreCase("cheque")) {
                    Intent intent = new Intent(getActivity(), PaymentDetailActivity.class);
                    intent.putExtra("app_no", et_application_no.getText().toString().trim());
                    startActivity(intent);
                }
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
                getState();
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
            params.put("center_code", Utility.getAppPrefString(getActivity(),"center_code"));
            volleyAPIClass.volleyAPICall(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_CORPORATE_NAME,
                    Constant.URL_GET_CORPORATE_NAME, params);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    private void getBillingInfo() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, Object> params = new HashMap<>();
            params.put("category_id", stringCategoryId);
            volleyAPIClass.volleyAPICall(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_BILLING_INFO,
                    Constant.URL_GET_BILLING_INFO, params);
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
            params.put("center_code", Utility.getAppPrefString(getActivity(),"center_code"));
            volleyAPIClass.volleyAPICall(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_CONTRACTOR,
                    Constant.URL_GET_CONTRACTOR, params);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    private void getState() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, Object> params = new HashMap<>();
            params.put("center_code", Utility.getAppPrefString(getActivity(),"center_code"));
            volleyAPIClass.volleyAPICall(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_STATE,
                    Constant.URL_GET_STATE, params);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    private void getCity() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, Object> params = new HashMap<>();
            params.put("center_code", Utility.getAppPrefString(getActivity(),"center_code"));
            params.put("state_id", stringState);
            volleyAPIClass.volleyAPICall(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_CITY,
                    Constant.URL_GET_CITY, params);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    private void getArea() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, Object> params = new HashMap<>();
            params.put("center_code", Utility.getAppPrefString(getActivity(),"center_code"));
            params.put("state_id", stringState);
            params.put("city_id", stringCity);
            volleyAPIClass.volleyAPICall(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_AREA,
                    Constant.URL_GET_AREA, params);
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
                    int lenth = jsonArray.length() + 1;
                    type_id = new String[lenth];
                    type_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            type_id[0] = "0";
                            type_name[0] = "Select Customer Type";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            type_id[a] = jsonObjectMessage.getString("type_id");
                            type_name[a] = jsonObjectMessage.getString("type_name");
                        }
                    }
                    Utility.setSpinnerAdapter(getActivity(), sp_customer_type, type_name);
                }
            } else if (reqCode == Constant.GET_CUSTOMER_CATEGORY) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("category_data");
                    int lenth = jsonArray.length() + 1;
                    category_id = new String[lenth];
                    category_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            category_id[0] = "0";
                            category_name[0] = "Select Customer Category";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            category_id[a] = jsonObjectMessage.getString("category_id");
                            category_name[a] = jsonObjectMessage.getString("category_name");
                        }
                    }
                    Utility.setSpinnerAdapter(getActivity(), sp_customer_category, category_name);
                }
            } else if (reqCode == Constant.GET_CORPORATE_NAME) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("corporate_data");
                    int lenth = jsonArray.length() + 1;
                    corporate_id = new String[lenth];
                    corporate_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            corporate_id[0] = "0";
                            corporate_name[0] = "Select Corporate Name";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            corporate_id[a] = jsonObjectMessage.getString("corporate_id");
                            corporate_name[a] = jsonObjectMessage.getString("corporate_name");
                        }
                    }
                    ll_corporate.setVisibility(View.VISIBLE);
                    Utility.setSpinnerAdapter(getActivity(), sp_corporate_name, corporate_name);
                }
            } else if (reqCode == Constant.GET_PROPERTY_TYPE) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("property_data");
                    int lenth = jsonArray.length() + 1;
                    property_id = new String[lenth];
                    property_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            property_id[0] = "0";
                            property_name[0] = "Select Property Type";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            property_id[a] = jsonObjectMessage.getString("property_id");
                            property_name[a] = jsonObjectMessage.getString("property_name");
                        }
                    }
                    Utility.setSpinnerAdapter(getActivity(), sp_property_type, property_name);
                }
            } else if (reqCode == Constant.GET_OWNER_TYPE) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("ownership_data");
                    int lenth = jsonArray.length() + 1;
                    ownership_id = new String[lenth];
                    ownership_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            ownership_id[0] = "0";
                            ownership_name[0] = "Select Owner Type";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            ownership_id[a] = jsonObjectMessage.getString("ownership_id");
                            ownership_name[a] = jsonObjectMessage.getString("ownership_name");
                        }
                    }
                    Utility.setSpinnerAdapter(getActivity(), sp_owner_type, ownership_name);
                }
            } else if (reqCode == Constant.GET_CONTRACTOR) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("contractor_data");
                    int lenth = jsonArray.length() + 1;
                    contractor_id = new String[lenth];
                    contractor_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            contractor_id[0] = "0";
                            contractor_name[0] = "Select DMA Contractor";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            contractor_id[a] = jsonObjectMessage.getString("contractor_id");
                            contractor_name[a] = jsonObjectMessage.getString("contractor_name");
                        }
                    }
                    Utility.setSpinnerAdapter(getActivity(), sp_contractor, contractor_name);
                }
            } else if (reqCode == Constant.GET_BILLING_INFO) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("billing_data");
                    int lenth = jsonArray.length() + 1;
                    billing_id = new String[lenth];
                    billing_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            billing_id[0] = "0";
                            billing_name[0] = "Select Billing To";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            billing_id[a] = jsonObjectMessage.getString("billing_id");
                            billing_name[a] = jsonObjectMessage.getString("billing_name");
                        }
                    }
                    Utility.setSpinnerAdapter(getActivity(), sp_billing_to, billing_name);
                }
            } else if (reqCode == Constant.GET_STATE) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("state_data");
                    int lenth = jsonArray.length() + 1;
                    state_id = new String[lenth];
                    state_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            state_id[0] = "0";
                            state_name[0] = "Select State";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            state_id[a] = jsonObjectMessage.getString("state_id");
                            state_name[a] = jsonObjectMessage.getString("state_name");
                        }
                    }
                    Utility.setSpinnerAdapter(getActivity(), sp_state, state_name);
                }
            } else if (reqCode == Constant.GET_CITY) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("city_data");
                    int lenth = jsonArray.length() + 1;
                    city_id = new String[lenth];
                    city_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            city_id[0] = "0";
                            city_name[0] = "Select City";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            city_id[a] = jsonObjectMessage.getString("city_id");
                            city_name[a] = jsonObjectMessage.getString("city_name");
                        }
                    }
                    Utility.setSpinnerAdapter(getActivity(), sp_city, city_name);
                }
            } else if (reqCode == Constant.GET_AREA) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("area_data");
                    int lenth = jsonArray.length() + 1;
                    area_id = new String[lenth];
                    area_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            area_id[0] = "0";
                            area_name[0] = "Select Area";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            area_id[a] = jsonObjectMessage.getString("area_id");
                            area_name[a] = jsonObjectMessage.getString("area_name");
                        }
                    }
                    Utility.setSpinnerAdapter(getActivity(), sp_area, area_name);
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

    private void saveRegistrationDetails() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Utility.getAppPrefString(getActivity(), Constant.USER_ID));
            params.put("application_no", et_application_no.getText().toString().trim());
            params.put("application_date", et_date.getText().toString().trim());
            params.put("customer_type", stringTypeId);
            params.put("customer_category", stringCategoryId);
            params.put("corporation_name", stringCorporateId);
            params.put("billing_to", stringBillingTo);
            params.put("first_name", et_firstname.getText().toString());
            params.put("middle_name", et_middlename.getText().toString());
            params.put("last_name", et_lastname.getText().toString());
            params.put("dob", et_dob.getText().toString().trim());
            params.put("adhar_no", et_aadhar_no.getText().toString().trim());
            params.put("block_no", et_block_no.getText().toString().trim());
            params.put("address", "");
            params.put("society", et_address1.getText().toString());
            params.put("landmark", et_address2.getText().toString());
            params.put("state", stringState);
            params.put("city", stringCity);
            params.put("area", stringArea);
            params.put("pincode", et_pincode.getText().toString());
            params.put("customer_phone", et_contact_no.getText().toString().trim());
            params.put("customer_mobile", et_mobile_no.getText().toString().trim());
            params.put("customer_email", et_email.getText().toString().trim());
            params.put("lpg_cust_no", et_lpg_no.getText().toString().trim());
            params.put("lpg_distributor", et_lpg_distributor.getText().toString().trim());
            params.put("lpg_omc", et_lpg_omc.getText().toString().trim());
            params.put("property_type", stringPropertyId);
            params.put("property_name", et_property_name.getText().toString().trim());
            params.put("ownership_type", stringOwnerId);
            params.put("ownership_name", et_owner_name.getText().toString().trim());
            params.put("owner_mobile", et_owner_contact_no.getText().toString().trim());
            params.put("dma_contractor", stringContractorId);
            params.put("remarks", et_remarks.getText().toString());
            params.put("doc1", stringDoc1);
            params.put("doc2", stringDoc2);
            params.put("payment_mode", paymentType);
            volleyAPIClass.volleyAPICall(getActivity(), FragmentCustomerRegistration.this,
                    Constant.SAVE_REGISTRATION,
                    Constant.URL_SAVE_REGISTRATION, params);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String selectedText = adapterView.getItemAtPosition(position).toString();
        switch (adapterView.getId()) {
            case R.id.sp_cust_type:
                if (!type_id[position].equalsIgnoreCase("0")) {
                    stringTypeId = type_id[position];
                    getCustomerCategory();
                }
                break;
            case R.id.sp_cust_category:
                if (!category_id[position].equalsIgnoreCase("0")) {
                    stringCategoryId = category_id[position];
                    if (category_name[position].equalsIgnoreCase("corporate"))
                        getCorporateName();
                    else
                        ll_corporate.setVisibility(View.GONE);
                    getBillingInfo();
                }
                break;
            case R.id.sp_corporate_name:
                if (!corporate_id[position].equalsIgnoreCase("0")) {
                    stringCorporateId = corporate_id[position];
                }
                break;
            case R.id.sp_property_type:
                if (!property_id[position].equalsIgnoreCase("0")) {
                    stringPropertyId = property_id[position];
                }
                break;
            case R.id.sp_owner_type:
                if (!ownership_id[position].equalsIgnoreCase("0")) {
                    stringOwnerId = ownership_id[position];
                }
                break;
            case R.id.sp_contractor:
                if (!contractor_id[position].equalsIgnoreCase("0")) {
                    stringContractorId = contractor_id[position];
                }
                break;
            case R.id.sp_reg_state:
                if (!state_id[position].equalsIgnoreCase("0")) {
                    stringState = state_id[position];
                    getCity();
                }
                break;
            case R.id.sp_reg_city:
                if (!city_id[position].equalsIgnoreCase("0")) {
                    stringCity = city_id[position];
                    getArea();
                }
                break;
            case R.id.sp_reg_area:
                if (!area_id[position].equalsIgnoreCase("0")) {
                    stringArea = area_id[position];
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
