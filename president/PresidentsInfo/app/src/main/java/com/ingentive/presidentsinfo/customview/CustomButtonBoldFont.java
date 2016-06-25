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
public class CustomButtonBoldFont extends Button {

    public CustomButtonBoldFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CustomButtonBoldFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public CustomButtonBoldFont(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomViewBoldFont);
            String fontName = a.getString(R.styleable.CustomViewBoldFont_boldFont);
            if (fontName!=null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/"+fontName);
                setTypeface(myTypeface);
            }
            a.recycle();
        }
    }

}