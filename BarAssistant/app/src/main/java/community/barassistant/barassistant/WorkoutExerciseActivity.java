package community.barassistant.barassistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;

/**
 * Created by ivan on 14.06.2016.
 */
public class WorkoutExerciseActivity extends AppCompatActivity {

    private ImageView imageView;
    private Chronometer chronometer;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_exercise);

        imageView = (ImageView) findViewById(R.id.w_e_imageView);
        chronometer = (Chronometer) findViewById(R.id.chronometerMain);
        button = (Button) findViewById(R.id.borderlessButton);
    }
}
