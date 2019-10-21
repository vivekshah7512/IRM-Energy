package com.vbs.irmenergy.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.vbs.irmenergy.R;
import com.vbs.irmenergy.utilities.Utility;

import java.util.ArrayList;

public class ConnectionAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater inflater = null;
    private ArrayList<String> name;

    public ConnectionAdapter(Context context, ArrayList<String> name) {
        this.context = context;
        this.name = name;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return name.size();
    }

    public Object getItem(int position) {
        return name.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.custom_connection_list_item, null);
            holder = new Holder();
            holder.sp_work_type = (Spinner) convertView.findViewById(R.id.sp_work_type);
            holder.sp_connection_type = (Spinner) convertView.findViewById(R.id.sp_conn_type);
            holder.sp_material_name = (Spinner) convertView.findViewById(R.id.sp_material_name);
            holder.img_remove = (ImageView) convertView.findViewById(R.id.img_remove);

            Utility.setSpinnerAdapter1(context, holder.sp_work_type, new String[]
                    {"Select Type", "Normal Conn.", "Extra Conn."});

            Utility.setSpinnerAdapter1(context, holder.sp_connection_type, new String[]
                    {"Select", "Geyser Point", "Kitchen Point"});

            Utility.setSpinnerAdapter1(context, holder.sp_material_name, new String[]
                    {"Select Material Name", "20mm PE shifting", "20mm PE shifting", "PEB"});

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        if (position + 1 == name.size()) {
            holder.img_remove.setImageResource(R.drawable.plus);
            holder.img_remove.setColorFilter(ContextCompat.getColor(context, R.color.colorGreen));
        } else {
            holder.img_remove.setImageResource(R.drawable.minus);
            holder.img_remove.setColorFilter(ContextCompat.getColor(context, R.color.colorRed));
        }

        return convertView;
    }

    public class Holder {
        Spinner sp_work_type, sp_connection_type, sp_material_name;
        ImageView img_remove;
    }
}
