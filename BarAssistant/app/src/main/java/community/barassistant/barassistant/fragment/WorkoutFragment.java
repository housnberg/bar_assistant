package community.barassistant.barassistant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.fabtransitionactivity.SheetLayout;

import java.util.List;

import community.barassistant.barassistant.AddWorkoutActivity;
import community.barassistant.barassistant.R;
import community.barassistant.barassistant.adapter.ExercisesAdapter;
import community.barassistant.barassistant.adapter.ItemClickSupport;
import community.barassistant.barassistant.model.Exercise;
import community.barassistant.barassistant.model.ExercisesDAO;

/**
 * Created by EL on 29.05.2016.
 */
public class WorkoutFragment extends Fragment implements View.OnClickListener, SheetLayout.OnFabAnimationEndListener {

    private static final int REQUEST_CODE = 1;

    private FloatingActionButton mSharedFab;
    private FloatingActionButton fabSecondary;
    private SheetLayout mSheetLayout;
    private RecyclerView recyclerView;
    private ExercisesDAO datasource;
    private MenuItem sortItem;
    private MenuItem filterItem;
    private List<Exercise> exercises;
    private boolean isFabOpen = true;

    public WorkoutFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_exercise, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        datasource = new ExercisesDAO(getActivity());
        setHasOptionsMenu(true);
        datasource.open();
        exercises = datasource.getAllExercises();
        setupRecyclerView();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSheetLayout = (SheetLayout) getView().getRootView().findViewById(R.id.bottom_sheet);
        fabSecondary = (FloatingActionButton) getView().getRootView().findViewById(R.id.fabSecondary);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ExercisesAdapter(getActivity(), exercises));
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Toast.makeText(getActivity(), position + "", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void shareFab(FloatingActionButton fab) {
        if (fab == null) { // When the FAB is shared to another Fragmen{t
            if (mSharedFab != null) {
                mSharedFab.setOnClickListener(null);
            }
            mSharedFab = null;
        }
        else {
            mSharedFab = fab;
            mSharedFab.setOnClickListener(this);
            mSheetLayout.setFab(mSharedFab);
            mSheetLayout.setFabAnimationEndListener(this);
            fab.setImageResource(R.mipmap.ic_add_white_24dp);
        }
    }

    @Override
    public void onClick(View view) {
        mSheetLayout.expandFab();
    }

    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(getActivity(), AddWorkoutActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSharedFab = null; // To avoid keeping/leaking the reference of the FAB
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {

            Exercise exercise = datasource.getLastAddedExercise();
            if (!exercises.contains(exercise)) {
                exercises.add(exercise);
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            mSheetLayout.contractFab();
        }
    }

    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
        filterItem = (MenuItem) getView().getRootView().findViewById(R.id.actionFilter);
        sortItem = (MenuItem) getView().getRootView().findViewById(R.id.actionSort);
        filterItem.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.actionFilter:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    */
}
