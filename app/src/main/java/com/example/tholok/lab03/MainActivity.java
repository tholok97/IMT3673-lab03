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
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    private ToneGenerator toneGenerator;

    private Vibrator vibrator;

    private SensorManager sensorManager;
    private Sensor sensor;
    private MyListener listener;

    private static String LOG_TAG = "BallTiltThing";

    float ballX;
    float ballY;
    float ballVelX;
    float ballVelY;
    float ballAccX;
    float ballAccY;
    float ballWidth;


    View drawingSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* SETUP SENSOR STUFF */

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        listener = new MyListener();

        // setup tonegenerator
        toneGenerator = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);

        // setup vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);



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



            // update x, y based on velocity
            ballX += ballVelX;
            ballY += ballVelY;

            // update velocity based on acc
            ballVelX += ballAccX;
            ballVelY += ballAccY;

            // dampening
            ballVelX *= 0.99f;
            ballVelY *= 0.99f;

            // react to hitting boundaries
            // play sound, reverse velocity and reposition ball upon hit
            if (ballX < ballWidth+10) {

                // THIS IS DEPRICATED (but using it for simplicity
                vibrator.vibrate(100);

                toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP);
                ballVelX *= -1;
                ballX = ballWidth+10+1;
            }
            if (ballX > getWidth() - ballWidth-10) {

                // THIS IS DEPRICATED (but using it for simplicity
                vibrator.vibrate(100);

                toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP);
                ballVelX *= -1;
                ballX = getWidth() - ballWidth - 10 - 1;
            }
            if (ballY < ballWidth+10) {

                // THIS IS DEPRICATED (but using it for simplicity
                vibrator.vibrate(100);

                toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP);
                ballVelY *= -1;
                ballY = ballWidth+10 + 1;
            }
            if (ballY > getHeight() - ballWidth-10) {

                // THIS IS DEPRICATED (but using it for simplicity
                vibrator.vibrate(100);

                toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP);
                ballVelY *= -1;
                ballY = getHeight() - ballWidth-10 - 1;
            }


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


            // move ball based on accelorometer data
            ballAccX = 0.2f * sensorEvent.values[1];
            ballAccY = 0.2f * sensorEvent.values[0];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    }
}
