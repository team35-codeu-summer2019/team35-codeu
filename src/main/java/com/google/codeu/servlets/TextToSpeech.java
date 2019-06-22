package com.google.codeu.servlets;

import com.google.protobuf.ByteString;
import javax.servlet.http.HttpServlet;
import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;

import org.jsoup.safety.Whitelist;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import javax.servlet.ServletOutputStream;
import java.io.InputStream;

import java.io.IOException;

/**
 * Takes requests that contain text and responds with an MP3 file speaking that
 * text.
 */
@WebServlet("/a11y/tts")
public class TextToSpeech extends HttpServlet {

  private TextToSpeechClient ttsClient;

  @Override
  public void init() {
    try {
      ttsClient = TextToSpeechClient.create();
    } catch (IOException exception) {
      System.out.println(exception);
    }
  }

  /** Responds with an MP3 bytestream from the Google Cloud Text-to-Speech API */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String text = Jsoup.clean(request.getParameter("text"), Whitelist.none());
    // Text to Speech API
    SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

    VoiceSelectionParams voice = VoiceSelectionParams.newBuilder().setLanguageCode("en-US")
        .setSsmlGender(SsmlVoiceGender.NEUTRAL).build();

    AudioConfig audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();

    SynthesizeSpeechResponse responseFromAPI = ttsClient.synthesizeSpeech(input, voice, audioConfig);

    ByteString audioContents = responseFromAPI.getAudioContent();

    response.setContentType("audio/mpeg");
    try (ServletOutputStream out = response.getOutputStream()) {
      out.write(audioContents.toByteArray());
    }
  }
}