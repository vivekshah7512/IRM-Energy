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

public class EstimationAdapter extends Adapter<EstimationAdapter.MyViewHolder> {

    Context context;
    private LayoutInflater inflater = null;
    private String[] estworkorder_refno, estworkorder_id, estworkorder_date,
            estworkorder_type_id, estworkorder_type_name, estplan_name,
            estapplication_no, estapp_id;

    public EstimationAdapter(Context context, String[] estworkorder_refno, String[] estworkorder_id,
                             String[] estworkorder_date, String[] estworkorder_type_id,
                             String[] estworkorder_type_name,
                             String[] estplan_name, String[] estapplication_no, String[] estapp_id) {
        this.context = context;
        this.estworkorder_refno = estworkorder_refno;
        this.estworkorder_id = estworkorder_id;
        this.estworkorder_date = estworkorder_date;
        this.estworkorder_type_id = estworkorder_type_id;
        this.estworkorder_type_name = estworkorder_type_name;
        this.estplan_name = estplan_name;
        this.estapplication_no = estapplication_no;
        this.estapp_id = estapp_id;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_estimation_list_item, parent, false));
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public void onBindViewHolder(MyViewHolder myViewHolder, final int position) {
        MyViewHolder holder = myViewHolder;

//        holder.tv_app_no.setText(application_no[position]);
//        holder.tv_cust_id.setText(workorder_no[position]);
//        holder.tv_cust_type.setText(app_id[position]);
//        holder.tv_date.setText(app_id[position]);
//        holder.tv_name.setText(app_name[position]);
//        holder.tv_plan.setText(plan_name[position]);
//        holder.tv_address.setText(app_address[position]);
//        holder.tv_area.setText(app_area[position]);
//
//        holder.btn_verify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, CommissionProcessActivity.class);
//                intent.putExtra("app_no", application_no[position]);
//                context.startActivity(intent);
//            }
//        });
    }

    public int getItemCount() {
        return estworkorder_refno.length;
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
