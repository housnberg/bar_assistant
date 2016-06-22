package community.barassistant.barassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;

import java.util.List;

import community.barassistant.barassistant.model.Exercise;
import community.barassistant.barassistant.model.Workout;

/**
 * Created by ivan on 14.06.2016.
 */
public class WorkoutExerciseActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button button;

    private Workout workout;
    private int currExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_exercise);

        imageView = (ImageView) findViewById(R.id.image);
        button = (Button) findViewById(R.id.borderlessButton);

        Bundle bundle  = getIntent().getExtras();
        workout = bundle.getParcelable("workout");

        List<Exercise> exerciseList = workout.getExercises();
        if(!exerciseList.isEmpty()){
            String imagePath = exerciseList.get(0).getImagePaths().get(0).getImagePath();
            imageView.setImageBitmap(ImageLoaderSingleton.getInstance().loadImageFromStorage(imagePath));
        }

    }

    public void onClick(View view){
        Intent intent = new Intent(this, CountdownActivity.class);
        intent.putExtra("workout", workout);
    }
}
