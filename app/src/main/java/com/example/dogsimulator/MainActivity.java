package com.example.dogsimulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
  private float mAccel;
  private float mAccelCurrent;
  private float mAccelLast;
  MediaPlayer player;
  private SensorManager sensorManager;
  Sensor accelerometer;
  ImageView image;
  DogChanger dogChanger;
  VoiceRecognizer voiceRecognizer;
  ImageButton voiceButton;
  ImageView walkButton;
  ImageView helpButton;
  TextView helpBox;

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
    image = (ImageView) findViewById(R.id.dog);
    dogChanger = new DogChanger(image);
    voiceButton =(ImageButton)findViewById(R.id.vButton);
    helpButton =(ImageView)findViewById(R.id.help_button);
    walkButton=(ImageView)findViewById(R.id.walk_button);
    walkButton.setOnClickListener(new View.OnClickListener(){
      public void onClick(View v){
        step(v);
      }
    });
   helpButton.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
       Intent i = new Intent(getApplicationContext(), PopActivity.class);
       startActivity(i);
     }
   });
    voiceButton.setOnClickListener(new View.OnClickListener()   {
      public void onClick(View v)  {
        startListen();
      }
    });
    ActivityCompat.requestPermissions(MainActivity.this,
      new String[]{Manifest.permission.RECORD_AUDIO},
      1);
  }
  public void step(View view) {
    Intent intent = new Intent(this, StepCounter.class);
    startActivity(intent);
    sensorManager.unregisterListener(this);
    MainActivity.this.finish();
  }
  public void play(int sound) {
    if (player == null) {
      player = MediaPlayer.create(this, sound);
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
    if (mAccel > 10 &&mAccel <= 19){
      dogChanger.changeAnimation("bark");
      play(R.raw.bark2);
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int i) {

  }

  public void startListen() {
    voiceRecognizer = new VoiceRecognizer((TextView) findViewById(R.id.voiceDetector), SpeechRecognizer.createSpeechRecognizer(this), image, (ImageView) findViewById(R.id.alce));

  }
  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    switch (requestCode) {
      case 1: {
        if (grantResults.length > 0
          && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
          Toast.makeText(MainActivity.this, "Insufficient permissions", Toast.LENGTH_SHORT).show();
        }
        return;
      }
    }
  }
}




