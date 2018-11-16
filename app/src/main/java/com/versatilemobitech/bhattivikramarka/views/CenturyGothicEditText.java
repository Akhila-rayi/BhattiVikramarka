package com.versatilemobitech.bhattivikramarka.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;

import com.versatilemobitech.bhattivikramarka.R;


@SuppressLint("AppCompatCustomView")
public class CenturyGothicEditText extends EditText {

    public CenturyGothicEditText(Context context) {
        super(context);
    }

    public CenturyGothicEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, R.attr.font_type1);
    }

    public CenturyGothicEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs, defStyle);
    }

    private void initView(Context context, AttributeSet attrs, int defStyle) {
        if (isInEditMode())
            return;
        try {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CenturyGothicTextView);

            String str = a.getString(R.styleable.CenturyGothicTextView_font_type1);
            if (str == null)
                str = "1";
            a.recycle();
            switch (Integer.parseInt(str)) {
                case 1:
                    str = "CenturyGothicRegular.ttf";
                    break;
                default:
                    str = "CenturyGothicRegular.ttf";
                    break;
            }
            setTypeface(FontManager.getInstance(getContext()).loadFont(str));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

