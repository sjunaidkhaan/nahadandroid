package com.ingentive.presidentsinfo.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.ingentive.presidentsinfo.R;

/**
 * Created by PC on 26-05-2016.
 */
public class CustomButtonRomanFont extends Button {

    public CustomButtonRomanFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CustomButtonRomanFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public CustomButtonRomanFont(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomViewRomanFont);
            String fontName = a.getString(R.styleable.CustomViewRomanFont_romanFont);
            if (fontName != null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
                setTypeface(myTypeface);
            }
            a.recycle();
        }
    }
}