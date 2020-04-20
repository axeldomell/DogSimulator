package com.example.dogsimulator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class VoiceRecognizer implements RecognitionListener {

  protected TextView result;
  protected Intent intent;
  protected SpeechRecognizer recognizer=null;
  ImageView image;

  public VoiceRecognizer(TextView text, SpeechRecognizer speechRecognizer, ImageView image){

    result = text;
    this.image =image;
    intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
    intent.putExtra(RecognizerIntent.EXTRA_WEB_SEARCH_ONLY, "false");
    intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, "3000");

    recognizer =  speechRecognizer;
    recognizer.setRecognitionListener(this);
    recognizer.startListening(intent);
  }

  @Override
  public void onReadyForSpeech(Bundle params) {
    result.setText("Listening...");
  }

  @Override
  public void onBeginningOfSpeech() {

  }

  @Override
  public void onRmsChanged(float rmsdB) {

  }

  @Override
  public void onBufferReceived(byte[] buffer) {

  }

  @Override
  public void onEndOfSpeech() {

  }

  @Override
  public void onError(int error) {
    String message;
    switch (error) {
      case SpeechRecognizer.ERROR_AUDIO:
        message = "Audio error";
        break;
      case SpeechRecognizer.ERROR_CLIENT:
        message = "Client error";
        break;
      case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
        message = "Insufficient permissions";
        break;
      case SpeechRecognizer.ERROR_NETWORK:
        message = "Network error";
        break;
      case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
        message = "Network timeout";
        break;
      case SpeechRecognizer.ERROR_NO_MATCH:
        message = "No match";
        break;
      case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
        message = "Speech Recognizer is busy";
        break;
      case SpeechRecognizer.ERROR_SERVER:
        message = "Server error";
        break;
      case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
        message = "No speech input";
        break;
      default:
        message = "Speech Recognizer cannot understand you";
        break;
    }
    result.setText(message);
  }

  @Override
  public void onResults(Bundle results) {
    ArrayList<String> words = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

    String text = "";

    for (String word : words) {
      if (word.equals("lie down")){
        image.setImageResource(R.drawable.dog_sleeping);
      }
    }

    result.setText(text);
  }

  @Override
  public void onPartialResults(Bundle partialResults) {

  }

  @Override
  public void onEvent(int eventType, Bundle params) {

  }
  private void requestRecordAudioPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      String requiredPermission = Manifest.permission.RECORD_AUDIO;

    }
  }
}
