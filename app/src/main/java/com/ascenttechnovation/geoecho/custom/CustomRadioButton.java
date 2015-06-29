package com.ascenttechnovation.geoecho.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by ADMIN on 29-06-2015.
 */
public class CustomRadioButton extends RadioButton {
    public CustomRadioButton(Context context) {
        super(context);
        customText();
    }

    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        customText();
    }

    public CustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        customText();
    }

    public CustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        customText();
    }

    private void customText() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "opensans.ttf");
        setTypeface(tf);
    }
}
