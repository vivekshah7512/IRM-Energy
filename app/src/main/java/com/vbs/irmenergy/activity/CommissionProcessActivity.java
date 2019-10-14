package com.vbs.irmenergy.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kyanogen.signatureview.SignatureView;
import com.vbs.irmenergy.R;
import com.vbs.irmenergy.utilities.Utility;

public class CommissionProcessActivity extends Activity implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Context mContext;
    private Button btn_comm_submit;
    private LinearLayout ll_take_photo, ll_take_sign;
    private ImageView img_capture, img_sign;
    private TextView tv_img_title;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_commission_process);
        mContext = this;

        initUI();

    }

    private void initUI() {
        btn_comm_submit = (Button) findViewById(R.id.btn_comm_submit);
        btn_comm_submit.setOnClickListener(this);
        ll_take_photo = (LinearLayout) findViewById(R.id.ll_take_photo);
        ll_take_photo.setOnClickListener(this);
        ll_take_sign = (LinearLayout) findViewById(R.id.ll_take_sign);
        ll_take_sign.setOnClickListener(this);
        img_capture = (ImageView) findViewById(R.id.img_meter);
        img_sign = (ImageView) findViewById(R.id.img_sign);
        tv_img_title = (TextView) findViewById(R.id.tv_meter_title);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_take_photo:
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            img_capture.setImageBitmap(photo);
            tv_img_title.setText(Utility.getTimeStamp() + ".jpg");
        }
    }
}
