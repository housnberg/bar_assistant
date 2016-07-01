package community.barassistant.barassistant.util;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

/**
 * Created by EL on 01.07.2016.
 */
public class TextToSpeechController {

    private TextToSpeech textToSpeech;
    private Context context;

    private boolean isLoaded = false;

    public TextToSpeechController(Context context) {
        try {
            this.context = context;
            textToSpeech = new TextToSpeech(context, onInitListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TextToSpeech.OnInitListener onInitListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.getDefault());
                if (result == TextToSpeech.LANG_MISSING_DATA  || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TextToSpeech", "This Language is not supported");
                } else {
                    isLoaded = true;
                }
            } else {
                Log.e("TextToSpeech", "Initilization Failed!");
            }
        }
    };

    public void speak(String text) {
        if (isLoaded)
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        else
            Log.e("error", "TTS Not Initialized");
    }

    public void shutDown() {
        if (!textToSpeech.isSpeaking()) {
            textToSpeech.shutdown();
        }
    }

    public void stop() {
        textToSpeech.stop();
    }


}
