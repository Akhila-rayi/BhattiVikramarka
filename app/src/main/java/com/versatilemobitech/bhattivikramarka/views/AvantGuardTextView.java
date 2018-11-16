package com.versatilemobitech.bhattivikramarka.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.versatilemobitech.bhattivikramarka.R;


public class AvantGuardTextView extends TextView {
    private int font;

    public AvantGuardTextView(Context context) {
        super(context);
    }

    public AvantGuardTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
//         initFont(context, attrs);
        initView(context, attrs);
    }

    public AvantGuardTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // initFont(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        try {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AvantGuardTextView);
            String str = a.getString(R.styleable.AvantGuardTextView_font_type);
            if (str == null)
                str = "1";
            switch (Integer.parseInt(str)) {
                case 1:
                    str = "AvantGuardRegular.ttf";
                    break;
                default:
                    str = "AvantGuardRegular.ttf";
                    break;
            }
            setTypeface(FontManager.getInstance(getContext()).loadFont(str));
            a.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}