package community.barassistant.barassistant;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import community.barassistant.barassistant.dao.WorkoutDAO;
import community.barassistant.barassistant.fragment.AddExercisesToWorkoutFragment;
import community.barassistant.barassistant.fragment.AddPropertiesToWorkoutFragment;
import community.barassistant.barassistant.model.Workout;

/**
 * @author Eugen Ljavin
 */
public class AddWorkoutActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton btnUp, btnDown;
    TextView textViewUp, textViewMid, textViewBottom;
    TextView txt_help_gest;

    int nStart = 5;
    int nEnd = 250;

    private WorkoutDAO datasource;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Fragment addExercisesToWorkoutFragment;
    private Fragment addPropertiesToWorkoutFragment;
    private FloatingActionButton mSharedFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedFab = (FloatingActionButton) findViewById(R.id.fabMain);
        mSharedFab.setVisibility(View.INVISIBLE);

        datasource = new WorkoutDAO(this);
        datasource.open();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

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

    private void setupViewPager(final ViewPager viewPager) {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        addPropertiesToWorkoutFragment = new AddPropertiesToWorkoutFragment();
        addExercisesToWorkoutFragment = new AddExercisesToWorkoutFragment();
        pagerAdapter.addFragment(addPropertiesToWorkoutFragment, getResources().getString(R.string.addPropertiesToWorkoutFragment));
        pagerAdapter.addFragment(addExercisesToWorkoutFragment, getResources().getString(R.string.addExercisesToWorkoutFragment));

        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
        String workoutName = ((AddPropertiesToWorkoutFragment) addPropertiesToWorkoutFragment).getWorkoutName().getText().toString();
        String workoutDescription = ((AddPropertiesToWorkoutFragment) addPropertiesToWorkoutFragment).getWorkoutDescription().getText().toString();
        int workoutRounds = ((AddPropertiesToWorkoutFragment) addPropertiesToWorkoutFragment).getRoundsWheeler().getSelectedPosition() + 1;
        int workoutPauseExercises = ((AddPropertiesToWorkoutFragment) addPropertiesToWorkoutFragment).getPauseExercisesWheeler().getSelectedPosition() + 1;
        int workoutPauseRounds = ((AddPropertiesToWorkoutFragment) addPropertiesToWorkoutFragment).getPauseRoundsWheeler().getSelectedPosition() + 1;

        if (item.getItemId() == R.id.actionSave) {
            if (workoutName.length() < 3) {
                Toast.makeText(this, R.string.toastInvalidName, Toast.LENGTH_LONG).show();
            } else {
                Workout workout = null;
                workout = datasource.createExercise(workoutName, workoutDescription, workoutRounds, workoutPauseExercises, workoutPauseRounds);
                System.out.println("Workout with the id " + workout.getId() + " set.");
                finish();
            }
        }
        return true;
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
    public void onClick(View v) {
//            String getString = String.valueOf(textViewMid.getText());
//            int curent = Integer.parseInt(getString);
//            if (v == btnUp) {
//                if (curent < nEnd) {
//                    curent++;
//                    textViewUp.setText(String.valueOf(curent - 1));
//                    textViewMid.setText(String.valueOf(curent));
//                    textViewBottom.setText(String.valueOf(curent + 1));
//                }
//
//            }
//            if (v == btnDown) {
//                if (curent > nStart) {
//                    curent--;
//                    textViewUp.setText(String.valueOf(curent - 1));
//                    textViewMid.setText(String.valueOf(curent));
//                    textViewBottom.setText(String.valueOf(curent + 1));
//                }
//            }

        if(txt_help_gest.isShown()){
            Fx.slide_up(this, txt_help_gest);
            txt_help_gest.setVisibility(View.GONE);
        }
        else{
            txt_help_gest.setVisibility(View.VISIBLE);
            Fx.slide_down(this, txt_help_gest);
        }

    }
}
