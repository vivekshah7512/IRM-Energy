package com.vbs.irmenergy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vbs.irmenergy.R;
import com.vbs.irmenergy.activity.LoginActivity;
import com.vbs.irmenergy.utilities.APIProgressDialog;
import com.vbs.irmenergy.utilities.Constant;
import com.vbs.irmenergy.utilities.Utility;
import com.vbs.irmenergy.utilities.volley.VolleyAPIClass;
import com.vbs.irmenergy.utilities.volley.VolleyResponseInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragmentChangePassword extends Fragment implements OnClickListener,
        VolleyResponseInterface {

    private View view;
    private Button btn_change;
    private EditText et_confirm, et_new, et_old;
    private APIProgressDialog mProgressDialog;
    private VolleyAPIClass volleyAPIClass;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_pass, container, false);

        init();

        return view;
    }

    public void init() {
        Utility.setTitle(getActivity(), "Change Password");
        mProgressDialog = new APIProgressDialog(getActivity(), R.style.DialogStyle);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);

        volleyAPIClass = new VolleyAPIClass();

        et_old = (EditText) view.findViewById(R.id.et_old_pass);
        et_new = (EditText) view.findViewById(R.id.et_new_pass);
        et_confirm = (EditText) view.findViewById(R.id.et_confirm_pass);
        btn_change = (Button) view.findViewById(R.id.btn_change_pass);
        btn_change.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change_pass:
                if (et_old.getText().toString().equalsIgnoreCase("") ||
                        et_new.getText().toString().equalsIgnoreCase("") ||
                        et_confirm.getText().toString().equalsIgnoreCase("")) {
                    if (et_old.getText().toString().equalsIgnoreCase("")) {
                        Utility.toast("Please enter old password", getActivity());
                    } else if (et_new.getText().toString().equalsIgnoreCase("")) {
                        Utility.toast("Please enter new password", getActivity());
                    } else {
                        Utility.toast("Please enter confirm password", getActivity());
                    }
                } else {
                    if (!et_new.getText().toString().equalsIgnoreCase(et_confirm.getText().toString())) {
                        Utility.toast("Your both password are same", getActivity());
                    } else if (!et_new.getText().toString().equalsIgnoreCase(et_confirm.getText().toString())) {
                        Utility.toast("These passwords don't match", getActivity());
                    } else {
                        changePassword();
                    }
                }
                break;
            default:
                return;
        }
    }

    private void changePassword() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Utility.getAppPrefString(getActivity(), Constant.USER_ID));
            params.put("center_code", "1");
            params.put("old_password", et_old.getText().toString());
            params.put("new_password", et_new.getText().toString());
            volleyAPIClass.volleyAPICall(getActivity(), FragmentChangePassword.this,
                    Constant.CHANGE_PASSWORD, Constant.URL_CHANGE_PASSWORD, params);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    @Override
    public void vResponse(int reqCode, String result) {
        String response, message;
        try {
            JSONObject jObject = new JSONObject(result);
            if (reqCode == Constant.CHANGE_PASSWORD) {
                response = jObject.getString("response");
                message = jObject.getString("message");
                if (response.equalsIgnoreCase("true")) {
                    Utility.toast(message, getActivity());
                    et_old.setText("");
                    et_new.setText("");
                    et_confirm.setText("");
                    getActivity().finish();
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
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
