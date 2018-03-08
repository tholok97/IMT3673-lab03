package com.example.tholok.lab03;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by tholok on 08.03.18.
 */

public class MyView extends View {

    Paint myPaint;

    public MyView(Context context) {
        super(context);

        myPaint = new Paint();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.d("draw", "xsize: " + getWidth());
        Log.d("draw", "drawing");

        canvas.drawCircle(getWidth()/2, getHeight()/2, getWidth() * 0.05f, myPaint);
    }

}
