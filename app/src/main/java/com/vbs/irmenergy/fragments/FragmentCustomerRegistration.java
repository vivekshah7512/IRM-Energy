package com.vbs.irmenergy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vbs.irmenergy.R;
import com.vbs.irmenergy.activity.PaymentDetailActivity;
import com.vbs.irmenergy.utilities.Utility;

public class FragmentCustomerRegistration extends Fragment implements OnClickListener {

    private View view;
    private LinearLayout ll_reg1, ll_reg2, ll_reg3;
    private ImageView img_reg1, img_reg2, img_reg3;
    private Button btn_save;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_customer_registration, container, false);

        init();

        return view;
    }

    public void init() {
        Utility.setTitle(getActivity(), "Registration");

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
                break;
            default:
                return;
        }
    }

}
