package com.ascenttechnovation.geoecho.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by ADMIN on 27-06-2015.
 */
public class CustomButton extends Button {
    public CustomButton(Context context) {
        super(context);
        customText();
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        customText();
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        customText();
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        customText();
    }

    private void customText() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "opensans.ttf");
        setTypeface(tf);
    }
}
