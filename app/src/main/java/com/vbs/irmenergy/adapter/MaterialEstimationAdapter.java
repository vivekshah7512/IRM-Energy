package com.vbs.irmenergy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vbs.irmenergy.R;

import java.util.ArrayList;

public class MaterialEstimationAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater inflater = null;
    private ArrayList<String> name;

    public MaterialEstimationAdapter(Context context, ArrayList<String> name) {
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
            convertView = this.inflater.inflate(R.layout.custom_material_list_item, null);
            holder = new Holder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_material);
            holder.img_remove = (ImageView) convertView.findViewById(R.id.img_remove);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.tv_name.setText(name.get(position));

        if (position + 1 == name.size()) {
            holder.img_remove.setImageResource(R.drawable.plus);
        } else {
            holder.img_remove.setImageResource(R.drawable.minus);
        }

        return convertView;
    }

    public class Holder {
        TextView tv_name;
        ImageView img_remove;
    }
}
