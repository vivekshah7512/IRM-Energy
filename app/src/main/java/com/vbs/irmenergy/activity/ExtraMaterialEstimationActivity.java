package com.vbs.irmenergy.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.kyanogen.signatureview.SignatureView;
import com.vbs.irmenergy.R;
import com.vbs.irmenergy.adapter.ConnectionAdapter;
import com.vbs.irmenergy.adapter.MaterialEstimationAdapter;
import com.vbs.irmenergy.utilities.ExpandableHeightListView;
import com.vbs.irmenergy.utilities.Utility;

import java.util.ArrayList;

public class ExtraMaterialEstimationActivity extends Activity implements View.OnClickListener {

    private Context mContext;
    private Button btn_comm_submit;
    private ExpandableHeightListView listView;
    private ArrayList<String> arrayList;
    private LinearLayout ll_take_sign;
    private ImageView img_sign;
    private ImageView img_back;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_extra_material_estimation);
        mContext = this;

        initUI();

    }

    private void initUI() {
        btn_comm_submit = (Button) findViewById(R.id.btn_material_submit);
        btn_comm_submit.setOnClickListener(this);
        ll_take_sign = (LinearLayout) findViewById(R.id.ll_take_sign);
        ll_take_sign.setOnClickListener(this);
        img_sign = (ImageView) findViewById(R.id.img_sign);

        arrayList = new ArrayList<>();
        arrayList.add("GI Pipe");
        arrayList.add("PEO inside");
        arrayList.add("PEO outside");
        arrayList.add("PEB inside");
        arrayList.add("PEB outside");
        listView = (ExpandableHeightListView) findViewById(R.id.list_material);
        listView.setAdapter(new MaterialEstimationAdapter(mContext, arrayList));
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
            case R.id.btn_material_submit:
                Dialog dialog1 = new Dialog(mContext);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.setContentView(R.layout.dialog_sucess);
                dialog1.setCanceledOnTouchOutside(false);
                dialog1.setCancelable(false);

                Button btn_dashboard = (Button) dialog1.findViewById(R.id.btn_dashboard);
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

                Window window1 = dialog1.getWindow();
                WindowManager.LayoutParams wlp1 = window1.getAttributes();
                wlp1.width = ActionBar.LayoutParams.MATCH_PARENT;
                wlp1.height = ActionBar.LayoutParams.MATCH_PARENT;
                window1.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                window1.setAttributes(wlp1);
                dialog1.show();
                break;
            case R.id.ll_take_sign:
                Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_sign);
                dialog.setCanceledOnTouchOutside(false);

                Button btn_clear = (Button) dialog.findViewById(R.id.btn_sign_cancel);
                Button btn_save = (Button) dialog.findViewById(R.id.btn_sign_submit);
                ImageView img_cancel = (ImageView) dialog.findViewById(R.id.img_cancel);
                SignatureView signatureView = (SignatureView) dialog.findViewById(R.id.signature_view);

                btn_clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (signatureView != null)
                            signatureView.clearCanvas();
                    }
                });

                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (signatureView.getSignatureBitmap() != null) {
                            dialog.dismiss();
                            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(signatureView.getSignatureBitmap(), 100, 100);
                            img_sign.setImageBitmap(ThumbImage);
                        } else {
                            Utility.toast("Draw your signature.", mContext);
                        }
                    }
                });

                img_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER;
                wlp.width = ActionBar.LayoutParams.MATCH_PARENT;
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                window.setAttributes(wlp);
                dialog.show();
                break;
            default:
                break;
        }
    }
}
