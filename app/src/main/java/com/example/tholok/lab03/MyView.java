package com.example.tholok.lab03;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MyView extends View {

    int ballX;
    int ballY;
    float ballWidth;

    Paint blackPaint;
    Paint whitePaint;

    public MyView(Context context) {
        super(context);

        blackPaint = new Paint();
        whitePaint = new Paint();
        whitePaint.setColor(0xffffffff);

    }

    public void init() {
        ballX = getWidth() / 2;
        ballY = getHeight() / 2;
        ballWidth = getWidth() * 0.05f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        init();

        // draw bouding rect
        canvas.drawRect(new Rect(10, 10, getWidth()-10, getHeight()-10), blackPaint);

        // draw the circle
        canvas.drawCircle(ballX, ballY, ballWidth, whitePaint);

        // hacky solution (??) to continuously draw the screen
        invalidate();
    }

}
