package com.example.dogsimulator;

import android.content.Intent;
import android.os.Handler;
import android.widget.ImageView;


public class DogChanger {

  private final Integer BLINK_INTERVAL = 3000;

  private String state;
  private ImageView image;
  private Handler handler;

  public DogChanger(ImageView image){
    this.image = image;
    this.handler = new Handler();

    //Default state
    this.state = "sitting";

    enableBlinking(handler);
  }

  // changes the currentAnimation based on the mood parameter
  // Moods work like toggles. Activating the same mood again will revert the mood to the previous.
  public void changeAnimation(String mood){
    switch (mood){

      // Blink case
      case "blink":
        switch (state){
          case "sitting_blink":
            image.setImageResource(R.drawable.dog_sitting);
            state = "sitting";
            break;
          case "sitting_blink_tounge":
            image.setImageResource(R.drawable.dog_sitting_tounge);
            state = "sitting_tounge";
            break;
          case "sitting_tounge":
            image.setImageResource(R.drawable.dog_sitting_blink_tounge);
            state = "sitting_blink_tounge";
            break;
          case "sitting":
            image.setImageResource(R.drawable.dog_sitting_blink);
            state = "sitting_blink";
            break;
        }
        break;

      // Bark case
      case "bark":
        switch (state){
          case "sitting_blink":
            image.setImageResource(R.drawable.dog_sitting_blink_tounge);
            state = "sitting_blink_tounge";
            break;
          case "sitting_blink_tounge":
            image.setImageResource(R.drawable.dog_sitting_blink);
            state = "sitting_blink";
            break;
          case "sitting_tounge":
            image.setImageResource(R.drawable.dog_sitting);
            state = "sitting";
            break;
          case "sitting":
            image.setImageResource(R.drawable.dog_sitting_tounge);
            state = "sitting_tounge";
            break;
        }
        break;

      // Sleep case
      case "sleep":
        switch (state) {
          case "sleeping":
            image.setImageResource(R.drawable.dog_sitting);
            state = "sitting";
            break;
          default:
            image.setImageResource(R.drawable.dog_sleeping);
            state = "sleeping";
            break;
        }
        break;
    }
  }

  public String getCurrentMood(){
    return this.state;
  }

  private void enableBlinking(Handler handler){
    recursiveBlinking(handler, BLINK_INTERVAL);
  }

  private void recursiveBlinking(final Handler handler, final Integer blinkInterval){
    handler.postDelayed(new Runnable() {
      @Override
      public void run(){
        changeAnimation("blink");
        if (blinkInterval > 500){
          recursiveBlinking(handler, 500);
        } else recursiveBlinking(handler, BLINK_INTERVAL);
      }
    }, blinkInterval);
  }
}
