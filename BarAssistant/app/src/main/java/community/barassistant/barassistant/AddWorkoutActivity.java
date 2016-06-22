package community.barassistant.barassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.List;
import java.util.Map;

import community.barassistant.barassistant.adapter.ViewPagerAdapter;
import community.barassistant.barassistant.dao.DataAccessObject;
import community.barassistant.barassistant.fragment.AddExercisesToWorkoutFragment;
import community.barassistant.barassistant.fragment.AddPropertiesToWorkoutFragment;
import community.barassistant.barassistant.model.Exercise;
import community.barassistant.barassistant.model.Workout;
import community.barassistant.barassistant.util.Constants;
import community.barassistant.barassistant.util.Helper;

/**
 * @author Eugen Ljavin
 */
public class AddWorkoutActivity extends AppCompatActivity {

    private DataAccessObject datasource;

    private int result = RESULT_CANCELED;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AddExercisesToWorkoutFragment addExercisesToWorkoutFragment;
    private AddPropertiesToWorkoutFragment addPropertiesToWorkoutFragment;
    private FloatingActionButton mSharedFab;
    private View contentWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedFab = (FloatingActionButton) findViewById(R.id.fabMain);
        mSharedFab.setVisibility(View.INVISIBLE);

        datasource = new DataAccessObject(this);
        datasource.open();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        contentWrapper = findViewById(R.id.content_wrapper);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_close_white_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupViewPager(final ViewPager viewPager) {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        addExercisesToWorkoutFragment = new AddExercisesToWorkoutFragment();
        addPropertiesToWorkoutFragment = new AddPropertiesToWorkoutFragment();
        pagerAdapter.addFragment(addPropertiesToWorkoutFragment, getResources().getString(R.string.addPropertiesToWorkoutFragment));
        pagerAdapter.addFragment(addExercisesToWorkoutFragment, getResources().getString(R.string.addExercisesToWorkoutFragment));

        viewPager.setAdapter(pagerAdapter);

        addPropertiesToWorkoutFragment.shareFab(mSharedFab); // To init the FAB
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        switch (viewPager.getCurrentItem()) {
                            case 0:
                                addExercisesToWorkoutFragment.shareFab(null); // Remove FAB from fragment
                                addPropertiesToWorkoutFragment.shareFab(mSharedFab);
                                break;
                            case 1:
                                addPropertiesToWorkoutFragment.shareFab(null); // Remove FAB from fragment
                                addExercisesToWorkoutFragment.shareFab(mSharedFab);
                                break;
                        }
                        //mSharedFab.show(); // Show animation
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_save, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO: CHECK IF THE VIEWS ARE SET
        String workoutName = addPropertiesToWorkoutFragment.getWorkoutName().getText().toString();
        String workoutDescription = addPropertiesToWorkoutFragment.getWorkoutDescription().getText().toString();
        int workoutRounds = addPropertiesToWorkoutFragment.getRoundsWheeler().getSelectedPosition() + 1;
        int workoutPauseExercises = addPropertiesToWorkoutFragment.getPauseExercisesWheeler().getSelectedPosition() + 1;
        int workoutPauseRounds = addPropertiesToWorkoutFragment.getPauseRoundsWheeler().getSelectedPosition() + 1;
        List<Exercise> exercises = addExercisesToWorkoutFragment.getSelectedExercises();
        Map<Long, Integer> exerciseRepetitions = addExercisesToWorkoutFragment.getExerciseRepetitions();
        Map<Long, Boolean> exercisesRepeatable = addExercisesToWorkoutFragment.getExercisesRepeatable();

        if (item.getItemId() == R.id.actionSave) {
            if (workoutName.length() < 3) {
                Helper.createSnackbar(this, contentWrapper, R.string.snackbarInvalidName, Constants.STATUS_ERROR).show();
            } else if (exercises == null || exercises.isEmpty()) {
                Helper.createSnackbar(this, contentWrapper, R.string.snackbarNoExercisesSelected, Constants.STATUS_ERROR).show();
            } else {
                Workout workout = null;
                workout = datasource.createWorkout(workoutName, workoutDescription, null, null, workoutRounds, workoutPauseExercises, workoutPauseRounds);
                for (int order = 0; order < exercises.size(); order ++) {
                    Exercise exercise = exercises.get(order);
                    datasource.createWorkoutExercise(workout.getId(), exercise.getId(), exerciseRepetitions.get(exercise.getId()), exercisesRepeatable.get(exercise.getId()), order);
                }
                result = RESULT_OK;
                finish();
            }
        }
        return true;
    }

    public View getContentWrapper() {
        return contentWrapper;
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        setResult(result, intent);
        super.finish();
    }

}
