package community.barassistant.barassistant;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import community.barassistant.barassistant.model.Exercise;
import community.barassistant.barassistant.services.CountdownTimerService;
import community.barassistant.barassistant.util.Constants;
import community.barassistant.barassistant.util.Helper;
import community.barassistant.barassistant.util.ImageControllerSingleton;

/**
 * Created by ivan on 19.06.2016.
 */
public class CountdownActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private CountdownTimerService countdownTimerService;
    private boolean isBound;
    private int time;
    private String currentRound;
    private BroadcastReceiver receiverTick;
    private BroadcastReceiver receiverFinish;
    private TextView countdownTextView;
    private TextView currentRoundTextView;
    private TextView exerciseName;
    private TextView exerciseDescription;
    private ImageView exerciseTitleImage;
    private View contentWrapper;

    private Exercise nextExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        tts = new TextToSpeech(this, this);

        contentWrapper = findViewById(R.id.content_wrapper);
        countdownTextView = (TextView) findViewById(R.id.countdownTextView);
        currentRoundTextView = (TextView) findViewById(R.id.current_round);
        exerciseName  = (TextView) findViewById(R.id.exercise_name);
        exerciseDescription = (TextView) findViewById(R.id.exercise_description);
        exerciseTitleImage = (ImageView) findViewById(R.id.exercise_title_image);

        Bundle bundle = getIntent().getExtras();
        time = (bundle.getInt("time") + 1) * 1000;
        currentRound = bundle.getString("round");
        nextExercise = bundle.getParcelable("data");


        currentRoundTextView.setText(currentRound);
        exerciseName.setText(nextExercise.getName());
        exerciseDescription.setText(nextExercise.getDescription());
        exerciseTitleImage.setImageBitmap(ImageControllerSingleton.instance.loadImageFromStorage(nextExercise.getImagePaths().get(0).getImagePath()));

        Intent intent = new Intent(this, CountdownTimerService.class);
        isBound = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        receiverTick = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String text = countdownTextView.getText().toString();
                int currTime = time / 1000;
                if(!text.equals("")){
                    currTime = Integer.valueOf(text);
                }

                currTime--;
                //TODO KONFIGURIERBAR MACHEN
                if (currTime % 10 == 0 || currTime <= 10 && currTime > 0) {
                    tts.speak(String.valueOf(currTime), TextToSpeech.QUEUE_FLUSH, null);
                }

                countdownTextView.setText(Integer.toString(currTime));
            }
        };

        receiverFinish = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                tts.speak("Go", TextToSpeech.QUEUE_FLUSH, null);
                finish();
            }
        };

    }

    @Override
    public void finish(){
        setResult(1);
        super.finish();
    }

    @Override
    protected void onStart(){
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverTick, new IntentFilter(CountdownTimerService.CTS_TICK));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverFinish, new IntentFilter(CountdownTimerService.CTS_FINISH));
    }

    @Override
    protected void onStop(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverTick);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverFinish);
        if(isBound){
            unbindService(serviceConnection);
        }
        super.onStop();

    }

    public void onClick(View view){
        if(countdownTimerService != null)
            countdownTimerService.cancel();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CountdownTimerService.LocalBinder binder = (CountdownTimerService.LocalBinder) service;
            countdownTimerService = binder.getService();
            countdownTimerService.countdownTimer(time);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA  || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    @Override
    public void onBackPressed() {
        Helper.createSnackbar(this, contentWrapper, R.string.snackbarNoBackPressAllowed, Constants.STATUS_INFO).show();
    }
}
