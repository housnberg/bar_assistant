package community.barassistant.barassistant;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import community.barassistant.barassistant.dao.ExercisesDAO;
import community.barassistant.barassistant.model.Exercise;

/**
 * Created by EL on 05.06.2016.
 */
public class AddExerciseToWorkoutActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton mSharedFab;
    //private FloatingActionButton mSecondaryFab;
    private ExercisesDAO datasource;
    private EditText exerciseName;
    private EditText exerciseDescription;
    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise_to_workout);

        exerciseName = (EditText) findViewById(R.id.name);
        exerciseDescription = (EditText) findViewById(R.id.description);
//
        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionSave) {
            if (exerciseName.getText().toString().length() < 3) {
                Toast.makeText(this, R.string.toastInvalidName, Toast.LENGTH_LONG).show();
            } else {
                Exercise exercise = null;
                exercise = datasource.createExercise(exerciseName.getText().toString(), exerciseDescription.getText().toString());
                finish();
            }
        }
        return true;
    }

}
