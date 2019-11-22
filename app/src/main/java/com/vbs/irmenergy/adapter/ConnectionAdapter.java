package com.vbs.irmenergy.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.vbs.irmenergy.R;
import com.vbs.irmenergy.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConnectionAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater inflater = null;
    private ArrayList<String> id;
    private JSONArray jsonArray, jsonArray2;
    private String[] workorder_id, workorder_name, material_id, material_name, material_amount;
    private AddItem addItem;

    public ConnectionAdapter(Context context, ArrayList<String> id, JSONArray jsonArray,
                             JSONArray jsonArray2) {
        this.context = context;
        this.id = id;
        this.jsonArray = jsonArray;
        this.jsonArray2 = jsonArray2;
        addItem = (AddItem) (Activity) context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return id.size();
    }

    public Object getItem(int position) {
        return id.get(position);
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

            Utility.setSpinnerAdapter1(context, holder.sp_connection_type, new String[]
                    {"Select", "Geyser Point", "Kitchen Point"});

            try {
                JSONObject jsonObjectMessage;
                int lenth = jsonArray.length() + 1;
                workorder_id = new String[lenth];
                workorder_name = new String[lenth];
                for (int a = 0; a < lenth; a++) {
                    if (a == 0) {
                        workorder_id[0] = "0";
                        workorder_name[0] = "Select Work Type";
                    } else {
                        jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                        workorder_id[a] = jsonObjectMessage.getString("workorder_id");
                        workorder_name[a] = jsonObjectMessage.getString("workorder_name");
                    }
                }
                Utility.setSpinnerAdapter1(context, holder.sp_work_type, workorder_name);

                int lenth2 = jsonArray2.length() + 1;
                material_id = new String[lenth2];
                material_name = new String[lenth2];
                material_amount = new String[lenth2];
                for (int a = 0; a < lenth2; a++) {
                    if (a == 0) {
                        material_id[0] = "0";
                        material_name[0] = "Select Material Name";
                        material_amount[0] = "0";
                    } else {
                        jsonObjectMessage = jsonArray2.getJSONObject(a - 1);
                        material_id[a] = jsonObjectMessage.getString("material_id");
                        material_name[a] = jsonObjectMessage.getString("material_name");
                        material_amount[a] = jsonObjectMessage.getString("material_amount");
                    }
                }
                Utility.setSpinnerAdapter1(context, holder.sp_material_name, material_name);
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.sp_work_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    getMaterial.selectWO(workorder_id[i]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.img_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position + 1 == id.size()) {
                    // Add More
                    addItem.add(0, id.size());
                } else {
                    // Remove
                    addItem.add(1, position);
                }
            }
        });


        if (position + 1 == id.size()) {
            holder.img_remove.setImageResource(R.drawable.plus);
            holder.img_remove.setColorFilter(ContextCompat.getColor(context, R.color.colorGreen));
        } else {
            holder.img_remove.setImageResource(R.drawable.minus);
            holder.img_remove.setColorFilter(ContextCompat.getColor(context, R.color.colorRed));
        }

        return convertView;
    }

    public interface AddItem {
        void add(int type, int pos);
    }

    public class Holder {
        Spinner sp_work_type, sp_connection_type, sp_material_name;
        ImageView img_remove;
    }
}
