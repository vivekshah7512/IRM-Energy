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
import com.vbs.irmenergy.activity.JobsheetDetailsActivity;

public class JobsheetAdapter extends Adapter<JobsheetAdapter.MyViewHolder> {

    Context context;
    private LayoutInflater inflater = null;
    private String[] workorder_id, workorder_refno, workorder_date, workorder_type,
            workorder_type_id, plan_name, application_no, workorder_contractorID,
            app_id, customer_name,
            customer_address, customer_area;

    public JobsheetAdapter(Context context, String[] workorder_id, String[] workorder_refno,
                           String[] workorder_date, String[] workorder_type,
                           String[] workorder_type_id, String[] plan_name,
                           String[] application_no, String[] workorder_contractorID,
                           String[] app_id, String[] customer_name,
                           String[] customer_address, String[] customer_area) {
        this.context = context;
        this.workorder_id = workorder_id;
        this.workorder_refno = workorder_refno;
        this.workorder_date = workorder_date;
        this.workorder_type = workorder_type;
        this.workorder_type_id = workorder_type_id;
        this.plan_name = plan_name;
        this.application_no = application_no;
        this.workorder_contractorID = workorder_contractorID;
        this.app_id = app_id;
        this.customer_name = customer_name;
        this.customer_address = customer_address;
        this.customer_area = customer_area;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_jobsheet_list_item, parent, false));
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public void onBindViewHolder(MyViewHolder myViewHolder, final int position) {
        MyViewHolder holder = myViewHolder;

        holder.tv_app_no.setText(application_no[position]);
        holder.tv_wo_no.setText(workorder_refno[position]);
        holder.tv_wo_date.setText(workorder_date[position]);
        holder.tv_wo_type.setText(workorder_type[position]);
        holder.tv_plan.setText(plan_name[position]);
        holder.tv_cust_name.setText(customer_name[position]);
        holder.tv_address.setText(customer_address[position]);
        holder.tv_area.setText(customer_area[position]);

        holder.btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, JobsheetDetailsActivity.class);
                intent.putExtra("woType", workorder_type_id[position]);
                intent.putExtra("app_no", application_no[position]);
                intent.putExtra("workorder_id", workorder_id[position]);
                context.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return workorder_id.length;
    }

    public class MyViewHolder extends ViewHolder {
        TextView tv_app_no, tv_wo_no, tv_wo_date, tv_wo_type, tv_plan, tv_cust_name,
                tv_address, tv_area;
        Button btn_verify;

        public MyViewHolder(View view) {
            super(view);
            tv_app_no = (TextView) view.findViewById(R.id.tv_application_no);
            tv_wo_no = (TextView) view.findViewById(R.id.tv_job_wo_no);
            tv_wo_date = (TextView) view.findViewById(R.id.tv_job_wo_date);
            tv_wo_type = (TextView) view.findViewById(R.id.tv_job_wo_type);
            tv_plan = (TextView) view.findViewById(R.id.tv_job_wo_plan);
            tv_cust_name = (TextView) view.findViewById(R.id.tv_customer_name);
            tv_address = (TextView) view.findViewById(R.id.tv_address);
            tv_area = (TextView) view.findViewById(R.id.tv_area);
            btn_verify = (Button) view.findViewById(R.id.btn_verify);
        }
    }
}
