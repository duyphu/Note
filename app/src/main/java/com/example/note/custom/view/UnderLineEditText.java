package com.example.note.custom.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by phund on 3/3/2016.
 */
public class UnderLineEditText extends EditText {

    private Rect mRect;
    private Paint mPaint;

    public UnderLineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        int aColor = Color.parseColor("#000000");
        mPaint.setColor(aColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int line_height = getLineHeight();
        int count = height / line_height;
        if (getLineCount() > count) {
            count = getLineCount();
        }
        Rect r = mRect;
        Paint paint = mPaint;
        int baseline = getLineBounds(0, r);
        for (int i = 0; i < count; i++) {
            canvas.drawLine(0, baseline + 1, getRight(), baseline + 1, paint);
            baseline += getLineHeight();
        }
        super.onDraw(canvas);
    }
}