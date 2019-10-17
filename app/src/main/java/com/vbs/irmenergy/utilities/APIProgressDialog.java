package com.vbs.irmenergy.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.vbs.irmenergy.R;

public class APIProgressDialog extends ProgressDialog {
    GifView gv;
    ImageView Progress_iv;

    public static APIProgressDialog ctor(Context context) {
        APIProgressDialog dialog = new APIProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        return dialog;
    }

    public APIProgressDialog(Context context) {
        super(context);
    }

    public APIProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_dialog);

        try {
            gv = (GifView) findViewById(R.id.progress_iv);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}

