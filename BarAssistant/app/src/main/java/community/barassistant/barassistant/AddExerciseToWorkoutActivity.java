package community.barassistant.barassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import community.barassistant.barassistant.adapter.ExerciseSelectionAdapter;
import community.barassistant.barassistant.dao.DataAccessObject;
import community.barassistant.barassistant.model.Exercise;

/**
 * Created by EL on 05.06.2016.
 */
public class AddExerciseToWorkoutActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 1;

    private Toolbar toolbar;
    private ListView exerciseSelectionListView;
    private FloatingActionButton mSharedFab;

    private DataAccessObject datasource;
    private List<Exercise> exercises;
    private ArrayList<Exercise> selectableExercises;
    ArrayAdapter<Exercise> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise_to_workout);

        exerciseSelectionListView = (ListView) findViewById(R.id.exerciseSelectionListView);
        mSharedFab = (FloatingActionButton) findViewById(R.id.fabMain);
        mSharedFab.setOnClickListener(this);

        datasource = new DataAccessObject(this);
        datasource.open();
        exercises = datasource.getAllExercises();

        selectableExercises = new ArrayList<Exercise>();

        //TODO: ONLY SHOW THE EXERCISES WHICH ARE NOT SELECTED
        adapter = new ExerciseSelectionAdapter(this, R.layout.list_select_exercises, R.id.exerciseNameTextView, exercises);
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
                    selectableExercises.add((Exercise) exerciseSelectionListView.getItemAtPosition(i));
                }
            }
            finish();
        }
        return true;
    }

    @Override
    public void finish() {
        //TODO: INCLUDE A LIST OF WORKOUTS INSTEAF OF ONLY THE IDS
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("data", selectableExercises);
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fabMain) {
            Intent intent = new Intent(this, AddExerciseActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            selectableExercises.add(datasource.getLastAddedExercise());
            adapter.notifyDataSetChanged();
        }
    }
}
