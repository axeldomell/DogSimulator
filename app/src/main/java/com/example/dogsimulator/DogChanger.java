package com.example.dogsimulator;

import android.widget.ImageView;
import android.view.View;
import java.util.Stack;

public class DogChanger {
  private Stack<String> states;

  public DogChanger(){
    this.states = new Stack<>();
  }

  // changes the currentAnimation based on the mood parameter
  public void changeAnimation(String mood, ImageView image){
    switch (mood){
      case "bark":
        image.setImageResource(R.drawable.dog_sitting_tounge);
        states.push(mood);
        break;
      case "sad":
        //change picture...
        states.push(mood);
      break;
    }
  }
  public String getPreviousMood(){
    return states.pop();
  }
}
