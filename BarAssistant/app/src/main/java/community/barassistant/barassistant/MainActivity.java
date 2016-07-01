package community.barassistant.barassistant;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import community.barassistant.barassistant.adapter.ViewPagerAdapter;
import community.barassistant.barassistant.fragment.ExcerciseFragment;
import community.barassistant.barassistant.fragment.HomeFragment;
import community.barassistant.barassistant.fragment.WorkoutFragment;

/**
 * @author Eugen Ljavin
 */

//TODO: IMPLEMENT MANAGER BEAN TO PREVENT QUERY DATABASE ALL THE TIME
public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ExcerciseFragment exercisesFragment;
    private WorkoutFragment workoutFragment;
    private FloatingActionButton mSharedFab;
    private View contentWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedFab = (FloatingActionButton) findViewById(R.id.fabMain);
        mSharedFab.setVisibility(View.INVISIBLE);
        contentWrapper = findViewById(R.id.content_wrapper);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_menu_white_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupViewPager(final ViewPager viewPager) {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        exercisesFragment = new ExcerciseFragment();
        workoutFragment = new WorkoutFragment();
        pagerAdapter.addFragment(exercisesFragment, getResources().getString(R.string.exercisesFragment));
        pagerAdapter.addFragment(workoutFragment, getResources().getString(R.string.workoutFragment));

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);
        workoutFragment.shareFab(mSharedFab); // To init the FAB

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
                                workoutFragment.shareFab(null);
                                exercisesFragment.shareFab(mSharedFab);
                                mSharedFab.show();
                                break;
                            case 1:
                            default:
                                exercisesFragment.shareFab(null);
                                workoutFragment.shareFab(mSharedFab); // Share FAB to new displayed fragment
                                mSharedFab.show();
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
        inflater.inflate(R.menu.toolbar_main, menu);
        return true;
    }

    public View getContentWrapper() {
        return contentWrapper;
    }

}