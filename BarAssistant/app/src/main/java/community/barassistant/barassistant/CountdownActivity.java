package community.barassistant.barassistant;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Locale;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import community.barassistant.barassistant.model.Exercise;
import community.barassistant.barassistant.services.CountdownTimerService;
import community.barassistant.barassistant.util.Constants;
import community.barassistant.barassistant.util.Helper;
import community.barassistant.barassistant.util.ImageControllerSingleton;
import community.barassistant.barassistant.util.TextToSpeechController;

/**
 * Created by ivan on 19.06.2016.
 */
public class CountdownActivity extends AppCompatActivity {

    private TextToSpeechController ttsc;
    private Toolbar toolbar;
    private CountdownTimerService countdownTimerService;
    private boolean isBound;
    private int time;
    private int currentRound;
    private int rounds;
    private BroadcastReceiver receiverTick;
    private BroadcastReceiver receiverFinish;
    private TextView nextExerciseTextView;
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

        ttsc = new TextToSpeechController(getApplicationContext());

        contentWrapper = findViewById(R.id.content_wrapper);
        nextExerciseTextView = (TextView) findViewById(R.id.next_exercise);
        countdownTextView = (TextView) findViewById(R.id.countdownTextView);
        currentRoundTextView = (TextView) findViewById(R.id.current_round);
        exerciseName  = (TextView) findViewById(R.id.exercise_name);
        exerciseDescription = (TextView) findViewById(R.id.exercise_description);
        exerciseTitleImage = (ImageView) findViewById(R.id.exercise_title_image);

        Bundle bundle = getIntent().getExtras();
        time = (bundle.getInt("time") + 1) * 1000;
        currentRound = bundle.getInt("round");
        String text = getResources().getString(R.string.getReady);
        nextExercise = bundle.getParcelable("data");
        if (currentRound != 0) {
            rounds = bundle.getInt("rounds");
            text = getResources().getString(R.string.round) + " " + currentRound + "/" + rounds;
            if (bundle.getInt("exercise") == 0) {
                text = text + " finished";
            }
        }

        initToolbar(bundle.getString("workout_name"));

        nextExerciseTextView.setText(getResources().getString(R.string.nextExercise) + " (" + (bundle.getInt("exercise") + 1) + "/" + bundle.getInt("amount_exercises") + ")");
        currentRoundTextView.setText(text);
        exerciseName.setText(nextExercise.getName());
        exerciseDescription.setText(nextExercise.getDescription());
        exerciseTitleImage.setImageBitmap(ImageControllerSingleton.instance.loadImageFromStorage(nextExercise.getImagePaths().get(0).getImagePath(), this));

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
                if ((currTime % 10 == 0 && currTime > 0) || (currTime <= 10 && currTime > 0)) {
                    ttsc.speak(String.valueOf(currTime));
                } else if (currTime == 0) {
                }

                countdownTextView.setText(Integer.toString(currTime));
            }
        };

        receiverFinish = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ttsc.speak("Go");
                ttsc.shutDown();
                finish();
            }
        };

    }

    @Override
    public void finish(){
        ttsc.shutDown();
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
        ttsc.shutDown();
        if(isBound){
            unbindService(serviceConnection);
        }
        super.onStop();

    }

    public void onClick(View view){
        cancelCountdownTimerService();
    }

    private void cancelCountdownTimerService() {
        if (countdownTimerService != null) {
            countdownTimerService.cancel();
        }
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

    private void initToolbar(String workoutName) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setTitle(workoutName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_stop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_stop) {
            showDialog();
        }
        return true;
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.quit);
        builder.setMessage(R.string.areYouSure);
        builder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelCountdownTimerService();
                        Intent intent = new Intent(CountdownActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

        String negativeText = getString(android.R.string.no);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        Helper.createSnackbar(this, contentWrapper, getResources().getString(R.string.snackbarNoBackPressAllowed)  + ". " + getResources().getString(R.string.snkackbarQuit), Constants.STATUS_INFO).show();
    }
}
