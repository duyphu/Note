package com.example.note.custom.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by phund on 3/3/2016.
 */
public class MyLayout extends LinearLayout {
    public MyLayout(Context context) {
        super(context);
    }

    public MyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
