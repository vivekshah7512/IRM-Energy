package com.vbs.irmenergy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.vbs.irmenergy.R;
import com.vbs.irmenergy.utilities.Constant;
import com.vbs.irmenergy.utilities.Utility;

public class FragmentProfile extends Fragment {

    private View view;
    private TextView tv_name, tv_designation;
    private EditText et_email, et_mobile;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        init();

        return view;
    }

    public void init() {
        Utility.setTitle(getActivity(), "My Profile");
        tv_name = (TextView) view.findViewById(R.id.tv_profile_name);
        tv_designation = (TextView) view.findViewById(R.id.tv_profile_designation);
        et_email = (EditText) view.findViewById(R.id.et_profile_email);
        et_mobile = (EditText) view.findViewById(R.id.et_profile_mobile);

        tv_name.setText(Utility.getAppPrefString(getActivity(), Constant.USER_NAME));
        if (Utility.getAppPrefString(getActivity(), Constant.USER_TYPE)
                .equalsIgnoreCase("1")) {
            tv_designation.setText("HOD");
        } else if (Utility.getAppPrefString(getActivity(), Constant.USER_TYPE)
                .equalsIgnoreCase("2")) {
            tv_designation.setText("Executive");
        } else if (Utility.getAppPrefString(getActivity(), Constant.USER_TYPE)
                .equalsIgnoreCase("3")) {
            tv_designation.setText("Contractor");
        }
        et_email.setText(Utility.getAppPrefString(getActivity(), Constant.USER_EMAIL));
        et_mobile.setText(Utility.getAppPrefString(getActivity(), Constant.USER_PHONE));

    }

}
