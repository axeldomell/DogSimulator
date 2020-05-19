package com.example.dogsimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class StepCounter extends AppCompatActivity {
    private TextView textView;
    private TextView textView3;
    private double MagnitudePrevious = 0;
    private Integer stepCount = 0;
    private Double distance = 0.0;
    private ImageView dogStill;
    private pl.droidsonroids.gif.GifImageView dogMoving;
    private Handler handler;
    private boolean walking;
    private Runnable walk;
    private Runnable still;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);

        dogStill = findViewById(R.id.dog_walking_still);
        dogMoving = findViewById(R.id.dog_walking_gif);
        textView = findViewById(R.id.textView);
        textView3 = findViewById(R.id.textView3);


        this.handler = new Handler();
        this.walking = false;


        this.walk = new Runnable() {
            @Override
            public void run() {
                dogStill.setVisibility(View.INVISIBLE);
                dogMoving.setVisibility(View.VISIBLE);
                walking = true;
            }
        };

        this.still = new Runnable() {
            @Override
            public void run() {
                dogMoving.setVisibility(View.INVISIBLE);
                dogStill.setVisibility(View.VISIBLE);
                walking = false;
            }
        };

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        SensorEventListener stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(sensorEvent != null) {
                    float x_acceleration = sensorEvent.values[0];
                    float y_acceleration = sensorEvent.values[1];
                    float z_acceleration = sensorEvent.values[2];

                    double Magnitude = Math.sqrt(x_acceleration*x_acceleration + y_acceleration*y_acceleration + z_acceleration*z_acceleration);
                    double MagnitudeDelta = Magnitude - MagnitudePrevious;
                    MagnitudePrevious = Magnitude;

                    if(MagnitudeDelta > 6) {
                        stepCount++;
                        toggleWalkingState();
                    }
                    distance = (double) ((stepCount * 78) / 100);

                    textView.setText(stepCount.toString());
                    textView3.setText(distance.toString() + " " + "meter");

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void toggleWalkingState(){
        if (walking) {
            handler.removeCallbacks(still);
        } else {
            handler.post(walk);
        }
        handler.postDelayed(still, 3000);


    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        StepCounter.this.finish();
    }


    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount", stepCount);
        editor.apply();
    }


    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount", stepCount);
        editor.apply();
    }


    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        stepCount = sharedPreferences.getInt("stepCount", 0);
    }


    public void reset(View view) {
        stepCount = 0;
        distance = 0.0;
    }

}
