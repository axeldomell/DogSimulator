package com.example.dogsimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
  private float mAccel;
  private float mAccelCurrent;
  private float mAccelLast;
  MediaPlayer player;
  private SensorManager sensorManager;
  Sensor accelerometer;
  private ImageView image;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    mAccel =10f;
    mAccelCurrent = SensorManager.GRAVITY_EARTH;
    mAccelLast = SensorManager.GRAVITY_EARTH;
    image = (ImageView) findViewById(R.id.status_text);
  }
  public void play() {
    if (player == null) {
      player = MediaPlayer.create(this, R.raw.bark2);
    }
    player.start();
  }
  public void stop(){
  if(player!=null){
    player.release();
    player=null;

  }
}
  @Override
  public void onSensorChanged(SensorEvent sensorEvent) {
    float x = sensorEvent.values[0];
    float y = sensorEvent.values[1];
    float z = sensorEvent.values[2];
    mAccelLast = mAccelCurrent;
    mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
    float delta = mAccelCurrent - mAccelLast;
    mAccel = mAccel * 0.9f + delta;
    if (mAccel > 12) {
      image.setImageResource(R.drawable.dog_sitting_tounge);
      play();
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int i) {

  }
}
