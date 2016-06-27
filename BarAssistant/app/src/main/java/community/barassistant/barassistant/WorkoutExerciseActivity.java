package community.barassistant.barassistant;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import community.barassistant.barassistant.dao.DataAccessObject;
import community.barassistant.barassistant.model.Exercise;
import community.barassistant.barassistant.model.Workout;
import community.barassistant.barassistant.services.CountdownTimerService;
import community.barassistant.barassistant.util.Constants;
import community.barassistant.barassistant.util.Helper;
import community.barassistant.barassistant.util.ImageControllerSingleton;

/**
 * Created by ivan on 14.06.2016.
 */
public class WorkoutExerciseActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    private Button button;
    private View contentWrapper;

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

        receiverTick = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String text = textView.getText().toString();
                int currTime = reps / 1000;
                if(!text.equals("")){
                    currTime = Integer.valueOf(text);
                }
                currTime--;
                textView.setText(Integer.toString(currTime));
            }
        };

        receiverFinish = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //textView.setText("0");
                button.setEnabled(true);
            }
        };

        contentWrapper = findViewById(R.id.content_wrapper);
        textView = (TextView) findViewById(R.id.workoutExerciseTextView);
        imageView = (ImageView) findViewById(R.id.image);
        button = (Button) findViewById(R.id.borderlessButton);

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

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverTick);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverFinish);
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
                button.setEnabled(false);
                countdownTimerService.countdownTimer((reps+1)*1000);

            }
        }else{
            time = System.currentTimeMillis() - time;
            Intent intentFinish = new Intent(this, FinishActivity.class);
            intentFinish.putExtra("workoutTime", time);
            startActivity(intentFinish);
        }

    }

    private void setImage(){
        String imagePath = exercises.get(currExercise).getImagePaths().get(0).getImagePath();
        imageView.setImageBitmap(ImageControllerSingleton.getInstance().loadImageFromStorage(imagePath));
    }

    private void setReps(){
        long key = exercises.get(currExercise).getId();
        reps = repetitionsPerExercise.get(key);
        textView.setText(Integer.toString(reps));
    }

    public void onClick(View view){
        Intent intent = new Intent(this, CountdownActivity.class);
        if(currRound < workout.getRounds()){
            if(currExercise < (exercises.size()-1)){
                currExercise++;
                intent.putExtra("time", workout.getPauseExercises());
                intent.putExtra("round", (currRound + 1) + "/" + workout.getRounds());
            }else {
                currExercise = 0;
                currRound++;
                intent.putExtra("time", workout.getPauseRounds());
                intent.putExtra("round", currRound + "/" + workout.getRounds() + " finished!");
            }
            intent.putExtra("data", exercises.get(currExercise));

            startActivityForResult(intent, 1);

        }
    }


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CountdownTimerService.LocalBinder binder = (CountdownTimerService.LocalBinder) service;
            countdownTimerService = binder.getService();
            button.setEnabled(false);
            countdownTimerService.countdownTimer((reps+1)*1000);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            countdownTimerService = null;
        }
    };

    @Override
    public void onBackPressed() {
        Helper.createSnackbar(this, contentWrapper, R.string.snackbarNoBackPressAllowed, Constants.STATUS_INFO).show();
    }


}
