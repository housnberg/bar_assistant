package community.barassistant.barassistant;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import community.barassistant.barassistant.adapter.ExerciseSelectionAdapter;
import community.barassistant.barassistant.adapter.ItemClickSupport;
import community.barassistant.barassistant.dao.ExercisesDAO;
import community.barassistant.barassistant.model.Exercise;

/**
 * Created by EL on 05.06.2016.
 */
public class AddExerciseToWorkoutActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton mSharedFab;
    private EditText exerciseName;
    private EditText exerciseDescription;
    private Bitmap photo;
    private ListView exerciseSelectionListView;
    private CheckBox choice;

    private ExercisesDAO datasource;
    private List<Exercise> exercises;
    private List<Long> selectedExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise_to_workout);

        exerciseSelectionListView = (ListView) findViewById(R.id.exerciseSelectionListView);

        selectedExercises = new ArrayList<Long>();

        datasource = new ExercisesDAO(this);
        datasource.open();
        exercises = datasource.getAllExercises();

        ArrayAdapter<Exercise> adapter = new ExerciseSelectionAdapter(this, R.layout.list_select_exercises, R.id.exerciseNameTextView, exercises);
        exerciseSelectionListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        exerciseSelectionListView.setAdapter(adapter);

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
        inflater.inflate(R.menu.toolbar_filter_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionSave) {
            int selectedExercisesAmount = exerciseSelectionListView.getCount();
            SparseBooleanArray sparseBooleanArray = exerciseSelectionListView.getCheckedItemPositions();

            for (int i = 0; i < selectedExercisesAmount; i++) {
                if (sparseBooleanArray.get(i)) {
                    selectedExercises.add(((Exercise) exerciseSelectionListView.getItemAtPosition(i)).getId());
                }
            }
            finish();
        }
        return true;
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("data", selectedExercises.toArray(new Long[selectedExercises.size()]));
        setResult(RESULT_OK, intent);
        super.finish();
        //intent.putParcelableArrayListExtra("data", selectedExercises);
    }

}
