package com.versatilemobitech.bhattivikramarka.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.versatilemobitech.bhattivikramarka.R;


public class CenturyGothicTextView extends TextView {
    private int font;

    public CenturyGothicTextView(Context context) {
        super(context);
    }

    public CenturyGothicTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
//         initFont(context, attrs);
        initView(context, attrs);
    }

    public CenturyGothicTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // initFont(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        try {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CenturyGothicTextView);
            String str = a.getString(R.styleable.CenturyGothicTextView_font_type1);
            if (str == null)
                str = "1";
            switch (Integer.parseInt(str)) {
                case 1:
                    str = "CenturyGothicRegular.ttf";
                    break;
                default:
                    str = "CenturyGothicRegular.ttf";
                    break;
            }
            setTypeface(FontManager.getInstance(getContext()).loadFont(str));
            a.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}