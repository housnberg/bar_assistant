package community.barassistant.barassistant;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import community.barassistant.barassistant.dao.DataAccessObject;
import community.barassistant.barassistant.model.Exercise;
import community.barassistant.barassistant.model.Workout;
import community.barassistant.barassistant.services.CountdownTimerService;
import community.barassistant.barassistant.util.Constants;
import community.barassistant.barassistant.util.Helper;
import community.barassistant.barassistant.util.ImageControllerSingleton;
import community.barassistant.barassistant.util.TextToSpeechController;

/**
 * Created by ivan on 14.06.2016.
 */
public class WorkoutExerciseActivity extends AppCompatActivity {

    private TextToSpeechController ttsc;

    private Toolbar toolbar;
    private ImageView imageView;
    private TextView timeTextView;
    private TextView repsSecTextView;
    private Button button;
    private View contentWrapper;
    private TextView exerciseName;

    private int currExercise = 0;
    private int currRound = 0;

    private Workout workout;
    private DataAccessObject datasource;
    private ArrayList<Exercise> exercises;
    private HashMap<Long, Integer> repetitionsPerExercise;
    private HashMap<Long, Boolean> exercisesRepeatable;

    private CountdownTimerService countdownTimerService;
    private boolean isBound;
    private BroadcastReceiver receiverTick;
    private BroadcastReceiver receiverFinish;

    private int reps;
    private long time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_exercise);

        ttsc = new TextToSpeechController(getApplicationContext());

            receiverTick = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String text = timeTextView.getText().toString();
                    int currTime = reps / 1000;
                    if(!text.equals("")){
                        currTime = Integer.valueOf(text);
                    }
                    currTime--;
                    //TODO KONFIGURIERBAR MACHEN
                    if(!exercisesRepeatable.get(exercises.get(currExercise).getId())) {
                        if ((currTime % 10 == 0 && currTime > 0) || (currTime <= 10 && currTime > 0)) {
                            ttsc.speak(String.valueOf(currTime));
                        } else if (currTime == 0) {
                            changeButton(true);
                            ttsc.speak("Stop");
                        }
                    }

                    timeTextView.setText(Integer.toString(currTime));
                    repsSecTextView.setText(R.string.unitSeconds);
                }
            };

            receiverFinish = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    //timeTextView.setText("0");
                }
            };

        contentWrapper = findViewById(R.id.content_wrapper);
        repsSecTextView = (TextView) findViewById(R.id.reps_sec);
        timeTextView = (TextView) findViewById(R.id.time);
        imageView = (ImageView) findViewById(R.id.image);
        button = (Button) findViewById(R.id.borderlessButton);
        exerciseName = (TextView) findViewById(R.id.exercise_name);

        Bundle bundle  = getIntent().getExtras();
        workout = bundle.getParcelable("workout");

        datasource = new DataAccessObject(this);
        datasource.open();

        exercises = new ArrayList<Exercise>();
        exercises.addAll(datasource.getExercisesByWorkoutId(workout.getId()));

        repetitionsPerExercise = new HashMap<Long, Integer>();
        exercisesRepeatable = new HashMap<Long, Boolean>();

        for (Object exercise : exercises) {
            repetitionsPerExercise.put(((Exercise) exercise).getId(), datasource.getRepetitions(workout.getId(), ((Exercise) exercise).getId()));
            exercisesRepeatable.put(((Exercise) exercise).getId(), datasource.getIsRepeatable(workout.getId(), ((Exercise) exercise).getId()));
        }
        workout.setRepetitionsPerExercise(repetitionsPerExercise);
        workout.setExercisesRepeatable(exercisesRepeatable);


        setReps();


        if(!exercises.isEmpty()){
            setImage();
        }

        time = System.currentTimeMillis();

        initToolbar();

    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiverTick),
                new IntentFilter(CountdownTimerService.CTS_TICK)
        );
        LocalBroadcastManager.getInstance(this).registerReceiver((receiverFinish),
                new IntentFilter(CountdownTimerService.CTS_FINISH)
        );
        long key = exercises.get(currExercise).getId();
        if(!exercisesRepeatable.get(key)){
            Intent intent = new Intent(this, CountdownTimerService.class);
            if(!isBound)
                isBound = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setTitle(workout.getName());
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
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverTick);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverFinish);
        ttsc.shutDown();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){

        if(currRound < workout.getRounds()){
           /* if(currExercise < (exercises.size()-1)){
                currExercise++;
            }else{
                currRound++;
                currExercise = 0;
            }*/

            setImage();
            setReps();

            long key = exercises.get(currExercise).getId();
            if(!exercisesRepeatable.get(key)){
                changeButton(false);
                button.setTextColor(getResources().getColor(R.color.colorSecondaryText));
                button.setBackground(getResources().getDrawable(android.R.color.transparent));
                countdownTimerService.countdownTimer((reps + 1) * 1000);
            }
        } else {
            time = System.currentTimeMillis() - time;
            Intent intentFinish = new Intent(this, FinishActivity.class);
            intentFinish.putExtra("workoutTime", time);
            intentFinish.putExtra("data", workout);
            startActivity(intentFinish);
        }
        exerciseName.setText(exercises.get(currExercise).getName());

    }

    private void setImage(){
        String imagePath = exercises.get(currExercise).getImagePaths().get(0).getImagePath();
        imageView.setImageBitmap(ImageControllerSingleton.getInstance().loadImageFromStorage(imagePath, this));
    }

    private void setReps(){
        long key = exercises.get(currExercise).getId();
        reps = repetitionsPerExercise.get(key);
        timeTextView.setText(Integer.toString(reps));
        repsSecTextView.setText(R.string.unitRepetitions);
    }

    public void onClick(View view){
        Intent intent = new Intent(this, CountdownActivity.class);
        if(currRound < workout.getRounds()){
            if(currExercise < (exercises.size()-1)){
                currExercise++;
                intent.putExtra("time", workout.getPauseExercises());
                intent.putExtra("round", (currRound + 1));
            } else {
                currExercise = 0;
                currRound++;
                intent.putExtra("time", workout.getPauseRounds());
                intent.putExtra("round", currRound);
            }
            intent.putExtra("amount_exercises", exercises.size());
            intent.putExtra("exercise", currExercise);
            intent.putExtra("workout_name", workout.getName());
            intent.putExtra("rounds", workout.getRounds());
            intent.putExtra("data", exercises.get(currExercise));

            startActivityForResult(intent, 1);

        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CountdownTimerService.LocalBinder binder = (CountdownTimerService.LocalBinder) service;
            countdownTimerService = binder.getService();
            changeButton(false);
            countdownTimerService.countdownTimer((reps + 1)*1000);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            countdownTimerService = null;
        }
    };

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
                        Intent intent = new Intent(WorkoutExerciseActivity.this, MainActivity.class);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_stop, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Helper.createSnackbar(this, contentWrapper, getResources().getString(R.string.snackbarNoBackPressAllowed)  + ". " + getResources().getString(R.string.snkackbarQuit), Constants.STATUS_INFO).show();
    }

    private void cancelCountdownTimerService() {
        if (countdownTimerService != null) {
            countdownTimerService.cancel();
        }
    }

    private void changeButton(boolean enable) {
        button.setEnabled(enable);
        if (enable) {
            button.setTextColor(getResources().getColor(R.color.backgroundListItem));
            button.setBackground(getResources().getDrawable(R.drawable.material_button));
        } else {
            button.setTextColor(getResources().getColor(R.color.colorSecondaryText));
            button.setBackground(getResources().getDrawable(android.R.color.transparent));
        }
    }

}
