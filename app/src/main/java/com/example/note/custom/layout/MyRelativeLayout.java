package com.example.note.custom.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.ResourceBundle;

/**
 * Created by phund on 3/3/2016.
 */
public class MyRelativeLayout extends RelativeLayout {
    public MyRelativeLayout(Context context) {
        super(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
