package com.vbs.irmenergy.utilities;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.vbs.irmenergy.R;


public class CustomButton extends Button {
    private Context mContext;
    private String mFont;

    public CustomButton(Context context) {
        super(context, null);
        this.mContext = context;
        init();
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTextview, 0, 0);
        try {
            this.mFont = a.getString(0);
            init();
        } finally {
            a.recycle();
        }
    }

    public void init() {
        if (this.mFont != null) {
            Typeface tf;
            if (this.mFont.equals("POPPINS-BOLD.ttf")) {
                tf = Typeface.createFromAsset(this.mContext.getAssets(), Constant.fontPath1);
            } else if (this.mFont.equals("POPPINS-REGULAR.ttf")) {
                tf = Typeface.createFromAsset(this.mContext.getAssets(), Constant.fontPath2);
            } else if (this.mFont.equals("POPPINS-LIGHT.ttf")) {
                tf = Typeface.createFromAsset(this.mContext.getAssets(), Constant.fontPath4);
            } else {
                tf = Typeface.createFromAsset(this.mContext.getAssets(), Constant.fontPath5);
            }
            setTypeface(tf, Typeface.NORMAL);
        }
    }
}
