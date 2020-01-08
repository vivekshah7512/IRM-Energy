package com.vbs.irmenergy.utilities;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.vbs.irmenergy.R;

public class PrefixEditText extends EditText {
    float mOriginalLeftPadding = -1;
    private Context mContext;
    private String mFont;

    public PrefixEditText(Context context) {
        super(context, null);
        mContext = context;
        init();
    }

    public PrefixEditText(Context context, AttributeSet attrs) {
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

    public PrefixEditText(Context context, AttributeSet attrs,
                          int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,
                             int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calculatePrefix();
    }

    private void calculatePrefix() {
        if (mOriginalLeftPadding == -1) {
            String prefix = (String) getTag();
            float[] widths = new float[prefix.length()];
            getPaint().getTextWidths(prefix, widths);
            float textWidth = 0;
            for (float w : widths) {
                textWidth += w;
            }
            mOriginalLeftPadding = getCompoundPaddingLeft();
            setPadding((int) (textWidth + mOriginalLeftPadding),
                    getPaddingRight(), getPaddingTop(),
                    getPaddingBottom());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String prefix = (String) getTag();
        canvas.drawText(prefix, mOriginalLeftPadding,
                getLineBounds(0, null), getPaint());
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
