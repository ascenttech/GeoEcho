package com.ascenttechnovation.geoecho.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by ADMIN on 27-06-2015.
 */
public class CustomEditText extends EditText {
    public CustomEditText(Context context) {
        super(context);
        customText();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        customText();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        customText();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        customText();
    }

    private void customText() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "opensans.ttf");
        setTypeface(tf);
    }

}
