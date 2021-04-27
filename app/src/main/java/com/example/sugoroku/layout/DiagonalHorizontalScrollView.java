package com.example.sugoroku.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class DiagonalHorizontalScrollView extends HorizontalScrollView {
    public DiagonalHorizontalScrollView(Context context) {
        super(context);
    }

    public DiagonalHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DiagonalHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
