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

import java.util.List;

import community.barassistant.barassistant.AddWorkoutActivity;
import community.barassistant.barassistant.MainActivity;
import community.barassistant.barassistant.R;
import community.barassistant.barassistant.adapter.ItemClickSupport;
import community.barassistant.barassistant.adapter.WorkoutAdapter;
import community.barassistant.barassistant.dao.DataAccessObject;
import community.barassistant.barassistant.model.Workout;
import community.barassistant.barassistant.WorkoutActivity;
import community.barassistant.barassistant.util.Helper;

/**
 * @author Eugen Ljavin
 */
public class WorkoutFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CODE_ADD_WORKOUT = 1;
    private static final int REQUEST_CODE_SHOW_WORKOUT = 2;

    private DataAccessObject datasource;

    private FloatingActionButton mSharedFab;
    private FloatingActionButton fabSecondary;
    private RecyclerView recyclerView;
    private MenuItem sortItem;
    private MenuItem filterItem;

    private List<Workout> workouts;
    private boolean isFabOpen = true;

    public WorkoutFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_recycler_view, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        datasource = new DataAccessObject(getActivity());
        setHasOptionsMenu(true);
        datasource.open();
        workouts = datasource.getAllWorkouts();
        setupRecyclerView();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fabSecondary = (FloatingActionButton) getView().getRootView().findViewById(R.id.fabSecondary);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new WorkoutAdapter(getActivity(), workouts));
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(getActivity(), WorkoutActivity.class);
                intent.putExtra("data", workouts.get(position));
                startActivityForResult(intent, REQUEST_CODE_SHOW_WORKOUT);
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
            fab.setImageResource(R.mipmap.ic_add_white_24dp);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), AddWorkoutActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_WORKOUT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSharedFab = null; // To avoid keeping/leaking the reference of the FAB
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_WORKOUT && getActivity().RESULT_OK == resultCode) {

            Workout workout = datasource.getLastAddedWorkout();
            if (!workouts.contains(workout)) {
                workouts.add(workout);
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            Helper.createSnackbar(getActivity(), ((MainActivity) getActivity()).getContentWrapper(), R.string.snackbarWorkoutAddedSuccessfully).show();
        }
    }

}
