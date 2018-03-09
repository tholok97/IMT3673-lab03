package com.example.tholok.lab03;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {


    private SensorManager sensorManager;
    private Sensor sensor;
    private MyListener listener;

    private static String LOG_TAG = "BallTiltThing";

    int ballX;
    int ballY;
    float ballWidth;


    View drawingSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* SETUP SENSOR STUFF */

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        listener = new MyListener();



        //Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        MyView view = new MyView(this);

        setContentView(view);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listener);
    }

    private class MyView extends View {


        Paint blackPaint;
        Paint whitePaint;

        public MyView(Context context) {
            super(context);

            blackPaint = new Paint();
            whitePaint = new Paint();
            whitePaint.setColor(0xffffffff);
        }

        /**
         *  When size changes -> reposition ball to middle and set size appropriately
         * @param w
         * @param h
         * @param oldw
         * @param oldh
         */
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            ballX = w/2;
            ballY = h/2;
            ballWidth = w * 0.05f;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);


            // draw bouding rect
            canvas.drawRect(new Rect(10, 10, getWidth()-10, getHeight()-10), blackPaint);

            // draw the circle
            canvas.drawCircle(ballX, ballY, ballWidth, whitePaint);

            // hacky solution (??) to continuously draw the screen
            invalidate();
        }

    }

    private class MyListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            Log.e(LOG_TAG, "X: " + Float.toString(sensorEvent.values[0]) +
                    ", Y: " + Float.toString(sensorEvent.values[1]) +
                    ", Z: " + Float.toString(sensorEvent.values[2]));

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    }
}
