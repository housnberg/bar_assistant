package community.barassistant.barassistant;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import community.barassistant.barassistant.fragment.ExcerciseFragment;
import community.barassistant.barassistant.fragment.HomeFragment;
import community.barassistant.barassistant.fragment.WorkoutFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private HomeFragment homeFragment;
    private ExcerciseFragment exercisesFragment;
    private WorkoutFragment workoutFragment;
    private FloatingActionButton mSharedFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedFab = (FloatingActionButton) findViewById(R.id.fabMain);
        mSharedFab.setVisibility(View.INVISIBLE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_menu_white_24dp);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(final ViewPager viewPager) {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        homeFragment = new HomeFragment();
        exercisesFragment = new ExcerciseFragment();
        workoutFragment = new WorkoutFragment();
        pagerAdapter.addFragment(homeFragment, getResources().getString(R.string.homeFragment));
        pagerAdapter.addFragment(exercisesFragment, getResources().getString(R.string.exercisesFragment));
        pagerAdapter.addFragment(workoutFragment, getResources().getString(R.string.workoutFragment));

        viewPager.setAdapter(pagerAdapter);

        homeFragment.shareFab(mSharedFab); // To init the FAB
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
                                exercisesFragment.shareFab(null); // Remove FAB from fragment
                                workoutFragment.shareFab(null);
                                homeFragment.shareFab(mSharedFab);
                                break;
                            case 1:
                                homeFragment.shareFab(null); // Remove FAB from fragment
                                workoutFragment.shareFab(null);
                                exercisesFragment.shareFab(mSharedFab);
                                mSharedFab.show();
                                break;
                            case 2:
                            default:
                                homeFragment.shareFab(null); // Remove FAB from fragment
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

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}