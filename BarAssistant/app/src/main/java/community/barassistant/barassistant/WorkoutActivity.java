package community.barassistant.barassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import community.barassistant.barassistant.adapter.ComplexExerciseWorkoutPropertyAdapter;
import community.barassistant.barassistant.dao.DataAccessObject;
import community.barassistant.barassistant.model.Exercise;
import community.barassistant.barassistant.model.Workout;

/**
 * Created by EL on 11.06.2016.
 */
public class WorkoutActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Intent shareIntent;

    private TextView description;
    private TextView rounds;
    private TextView pauseExercises;
    private TextView pauseRounds;

    private DataAccessObject datasource;
    private Workout workout;
    private List<Exercise> exercises;
    private Map<Long, Integer> repetitionsPerExercise;
    private Map<Long, Boolean> exercisesRepeatable;
    private FloatingActionButton fabMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workout = getIntent().getExtras().getParcelable("data");
        setContentView(R.layout.activity_workout);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        description = (TextView) findViewById(R.id.workoutDescription);
        fabMain = (FloatingActionButton) findViewById(R.id.fabMain);
        //fabMain.setOnClickListener(this);
        rounds = (TextView) findViewById(R.id.rounds);
        pauseExercises = (TextView) findViewById(R.id.pauseExercises);
        pauseRounds = (TextView) findViewById(R.id.pauseRounds);

        if (workout.getDescription() != null && !workout.getDescription().trim().equals("")) {
            description.setVisibility(View.VISIBLE);
            description.setText(workout.getDescription());
        }
        rounds.setText(String.valueOf(workout.getRounds()));
        pauseExercises.setText(String.valueOf(workout.getPauseExercises()));
        pauseRounds.setText(String.valueOf(workout.getPauseRounds()));

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



        shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("*/*");
        // Add data to the intent, the receiving app will decide what to do with it.
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Text");
//        startActivity(Intent.createChooser(shareIntent, "How do you want to share?"));
//        mSharedFab = (FloatingActionButton) findViewById(R.id.fabMain);
//        mSharedFab.setVisibility(View.INVISIBLE);
//
//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        setupViewPager(viewPager);
//
//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
//
        initToolbar();

        setupRecyclerView(recyclerView);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
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

    private void setupRecyclerView(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ComplexExerciseWorkoutPropertyAdapter(this, exercises, workout));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolar_share, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == fabMain.getId()) {

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionShare) {
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            // Add data to the intent, the receiving app will decide what to do with it.
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, workout.getName() + " is a hell of a Workout!");
            shareIntent.putExtra(Intent.EXTRA_TEXT, buildShareMessage());
            startActivity(Intent.createChooser(shareIntent, "How do you want to share?"));

        }
        return true;
    }

    private String buildShareMessage() {

        StringBuilder shareMessage = new StringBuilder("Try this out!\n");

        for (Exercise exercise : exercises) {
            shareMessage.append(repetitionsPerExercise.get(exercise.getId()) + " Reps of " + exercise.getName() + "\n");
        }
        shareMessage.append("\nDo this " + workout.getRounds() + " rounds with " + workout.getPauseExercises() + " sec pause between exercsises and " + workout.getPauseRounds() + " sec pause between rounds!");
        return shareMessage.toString();
    }

    public void startWorkout(View view){
        System.out.println("startWorkout");
        Intent intent = new Intent(this, WorkoutExerciseActivity.class);
        intent.putExtra("workout", workout);
        startActivity(intent);
    }

}
