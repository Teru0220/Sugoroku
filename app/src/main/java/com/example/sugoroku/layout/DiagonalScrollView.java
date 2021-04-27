package com.example.sugoroku.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class DiagonalScrollView extends ScrollView {
    public DiagonalScrollView(Context context) {
        super(context);
    }

    public DiagonalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DiagonalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
