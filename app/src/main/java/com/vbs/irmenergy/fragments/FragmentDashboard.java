package com.vbs.irmenergy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.vbs.irmenergy.R;
import com.vbs.irmenergy.utilities.Utility;

public class FragmentDashboard extends Fragment implements OnClickListener {

    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        init();

        return view;
    }

    public void init() {
        Utility.setTitle(getActivity(), "Dashboard");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            default:
                return;
        }
    }

}