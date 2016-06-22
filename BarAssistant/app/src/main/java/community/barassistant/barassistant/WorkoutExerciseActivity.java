package community.barassistant.barassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import community.barassistant.barassistant.dao.DataAccessObject;
import community.barassistant.barassistant.model.Exercise;
import community.barassistant.barassistant.model.Workout;
import community.barassistant.barassistant.util.ImageLoaderSingleton;

/**
 * Created by ivan on 14.06.2016.
 */
public class WorkoutExerciseActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button button;

    private int currExercise;
    private int currRound;

    private Workout workout;
    private DataAccessObject datasource;
    private ArrayList<Exercise> exercises;
    private HashMap<Long, Integer> repetitionsPerExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_exercise);

        imageView = (ImageView) findViewById(R.id.image);
        button = (Button) findViewById(R.id.borderlessButton);

        Bundle bundle  = getIntent().getExtras();
        workout = bundle.getParcelable("workout");

        datasource = new DataAccessObject(this);
        datasource.open();

        exercises = new ArrayList<Exercise>();
        exercises.addAll(datasource.getExercisesByWorkoutId(workout.getId()));

        repetitionsPerExercise = new HashMap<Long, Integer>();
        HashMap<Long, Boolean> exercisesRepeatable = new HashMap<Long, Boolean>();

        for (Object exercise : exercises) {
            repetitionsPerExercise.put(((Exercise) exercise).getId(), datasource.getRepetitions(workout.getId(), ((Exercise) exercise).getId()));
            exercisesRepeatable.put(((Exercise) exercise).getId(), datasource.getIsRepeatable(workout.getId(), ((Exercise) exercise).getId()));
        }
        workout.setRepetitionsPerExercise(repetitionsPerExercise);
        workout.setExercisesRepeatable(exercisesRepeatable);

        if(!exercises.isEmpty()){
            String imagePath = exercises.get(0).getImagePaths().get(0).getImagePath();
            imageView.setImageBitmap(ImageLoaderSingleton.getInstance().loadImageFromStorage(imagePath));
            currExercise = 0;
        }

    }

    public void onClick(View view){
        Intent intent = new Intent(this, CountdownActivity.class);
        if(currRound < workout.getRounds()){
            if(currExercise < (exercises.size()-1)){
                intent.putExtra("time", workout.getPauseExercises());
            }else {
                intent.putExtra("time", workout.getPauseRounds());
            }
            startActivityForResult(intent, 1);
        }
    }


}
