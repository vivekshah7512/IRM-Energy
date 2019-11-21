package com.vbs.irmenergy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vbs.irmenergy.R;
import com.vbs.irmenergy.activity.CommissionProcessActivity;

public class CommissioningAdapter extends Adapter<CommissioningAdapter.MyViewHolder> {

    Context context;
    private LayoutInflater inflater = null;
    private String[] plan_name, application_no, app_id, app_name, app_address,
            app_area, workorder_no, workorder_contractor_name, contractor_id, meter_no;

    public CommissioningAdapter(Context context, String[] plan_name, String[] application_no,
                                String[] app_id, String[] app_name, String[] app_address,
                                String[] app_area, String[] workorder_no,
                                String[] workorder_contractor_name, String[] contractor_id,
                                String[] meter_no) {
        this.context = context;
        this.plan_name = plan_name;
        this.application_no = application_no;
        this.app_id = app_id;
        this.app_name = app_name;
        this.app_address = app_address;
        this.app_area = app_area;
        this.workorder_no = workorder_no;
        this.workorder_contractor_name = workorder_contractor_name;
        this.contractor_id = contractor_id;
        this.meter_no = meter_no;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_commission_list_item, parent, false));
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public void onBindViewHolder(MyViewHolder myViewHolder, final int position) {
        MyViewHolder holder = myViewHolder;

        holder.tv_app_no.setText(application_no[position]);
        holder.tv_cust_id.setText(workorder_no[position]);
        holder.tv_name.setText(app_name[position]);
        holder.tv_plan.setText(plan_name[position]);
        holder.tv_address.setText(app_address[position]);
        holder.tv_area.setText(app_area[position]);

        holder.btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommissionProcessActivity.class);
                intent.putExtra("app_no", application_no[position]);
                intent.putExtra("app_id", app_id[position]);
                intent.putExtra("customer_id", app_id[position]);
                intent.putExtra("contractor_id", contractor_id[position]);
                intent.putExtra("contractor_name", workorder_contractor_name[position]);
                intent.putExtra("meter_no", meter_no[position]);
                context.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return app_id.length;
    }

    public class MyViewHolder extends ViewHolder {
        TextView tv_app_no, tv_cust_id, tv_cust_type, tv_date, tv_name, tv_plan,
                tv_address, tv_area;
        Button btn_verify;

        public MyViewHolder(View view) {
            super(view);

            tv_app_no = (TextView) view.findViewById(R.id.tv_comm_application_no);
            tv_cust_id = (TextView) view.findViewById(R.id.tv_comm_cust_id);
            tv_cust_type = (TextView) view.findViewById(R.id.tv_comm_cust_type);
            tv_date = (TextView) view.findViewById(R.id.tv_comm_date);
            tv_name = (TextView) view.findViewById(R.id.tv_comm_name);
            tv_plan = (TextView) view.findViewById(R.id.tv_comm_plan);
            tv_address = (TextView) view.findViewById(R.id.tv_comm_address);
            tv_area = (TextView) view.findViewById(R.id.tv_comm_area);
            btn_verify = (Button) view.findViewById(R.id.btn_comm_verify);
        }
    }
}
