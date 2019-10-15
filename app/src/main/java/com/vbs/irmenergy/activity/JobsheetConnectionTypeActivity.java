package com.vbs.irmenergy.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.vbs.irmenergy.R;
import com.vbs.irmenergy.adapter.ConnectionAdapter;
import com.vbs.irmenergy.utilities.ExpandableHeightListView;

import java.util.ArrayList;

public class JobsheetConnectionTypeActivity extends Activity implements View.OnClickListener {

    private Context mContext;
    private Button btn_comm_submit;
    private RadioGroup rg_connection;
    private ExpandableHeightListView listView;
    private ArrayList<String> arrayList;
    private ImageView img_back;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_jobsheet_connection_type);
        mContext = this;

        initUI();

    }

    private void initUI() {
        btn_comm_submit = (Button) findViewById(R.id.btn_jobsheet_submit);
        btn_comm_submit.setOnClickListener(this);
        rg_connection = (RadioGroup) findViewById(R.id.rg_connection);

        arrayList = new ArrayList<>();
        arrayList.add("GI Pipe");
        arrayList.add("PEO inside");
        arrayList.add("PEO outside");
        arrayList.add("PEB inside");
        arrayList.add("PEB outside");
        listView = (ExpandableHeightListView) findViewById(R.id.list_connection);
        listView.setAdapter(new ConnectionAdapter(mContext, arrayList));
        listView.setExpanded(true);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_jobsheet_submit:
                Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_sucess);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);

                Button btn_dashboard = (Button) dialog.findViewById(R.id.btn_dashboard);
                btn_dashboard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        Intent i = new Intent(mContext, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                });

                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.width = ActionBar.LayoutParams.MATCH_PARENT;
                wlp.height = ActionBar.LayoutParams.MATCH_PARENT;
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                window.setAttributes(wlp);
                dialog.show();
                break;
            default:
                break;
        }
    }
}
