package com.vbs.irmenergy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vbs.irmenergy.R;
import com.vbs.irmenergy.utilities.Utility;

public class FragmentSearch extends Fragment implements OnClickListener {

    private View view;
    private Button btn_search, btn_verify;
    private CardView cardView_data;
    private EditText et_app_no;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        init();

        return view;
    }

    public void init() {
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

        btn_search = (Button) view.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        btn_verify = (Button) view.findViewById(R.id.btn_verify);
        btn_verify.setOnClickListener(this);
        cardView_data = (CardView) view.findViewById(R.id.cv_search_data);
        et_app_no = (EditText) view.findViewById(R.id.et_app_no);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                cardView_data.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_verify:
                break;
            default:
                return;
        }
    }

}
