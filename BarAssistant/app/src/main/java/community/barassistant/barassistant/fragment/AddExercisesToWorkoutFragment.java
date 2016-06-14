package community.barassistant.barassistant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.fabtransitionactivity.SheetLayout;

import java.util.ArrayList;
import java.util.List;

import community.barassistant.barassistant.AddExerciseToWorkoutActivity;
import community.barassistant.barassistant.R;
import community.barassistant.barassistant.adapter.ComplexExerciseWorkoutPropertyAdapter;
import community.barassistant.barassistant.adapter.ExerciseOverviewAdapter;
import community.barassistant.barassistant.adapter.ExerciseTouchHelper;
import community.barassistant.barassistant.adapter.ItemClickSupport;
import community.barassistant.barassistant.dao.DataAccessObject;
import community.barassistant.barassistant.model.Exercise;

/**
 * @author Eugen Ljavin
 */
public class AddExercisesToWorkoutFragment extends Fragment implements View.OnClickListener, SheetLayout.OnFabAnimationEndListener {

    private static final int REQUEST_CODE = 1;

    private FloatingActionButton mSharedFab;
    private SheetLayout mSheetLayout;
    private RecyclerView recyclerView;

    private DataAccessObject datasource;

    private List<Exercise> exercises;

    public AddExercisesToWorkoutFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_recycler_view, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        datasource = new DataAccessObject(getActivity());
        datasource.open();

        if (getActivity().getIntent().getExtras() != null) {
            //TODO: LOAD SELECTED EXERCISESY WORKOUT ID
            //exercises = datasource.getExerciseById();
        } else {
            exercises = new ArrayList<Exercise>();
        }

        setHasOptionsMenu(true);
        setupRecyclerView(recyclerView);
        return rootView;
    }

    private void setupRecyclerView(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ExerciseOverviewAdapter(getActivity(), exercises));
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Toast.makeText(getActivity(), position + "", Toast.LENGTH_LONG).show();
            }
        });
        ItemTouchHelper.Callback callback = new ExerciseTouchHelper((ExerciseOverviewAdapter) recyclerView.getAdapter());
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSheetLayout = (SheetLayout) getView().getRootView().findViewById(R.id.bottom_sheet);
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
            //mSheetLayout.setFab(mSharedFab);
            //mSheetLayout.setFabAnimationEndListener(this);
            fab.setImageResource(R.mipmap.ic_add_white_24dp);
            mSharedFab.show();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), AddExerciseToWorkoutActivity.class);
        intent.putParcelableArrayListExtra("data", (ArrayList<Exercise>) exercises);
        startActivityForResult(intent, REQUEST_CODE);
        //mSheetLayout.expandFab();
    }

    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(getActivity(), AddExerciseToWorkoutActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == getActivity().RESULT_OK) {

            List<Exercise> selectedExercises = data.getParcelableArrayListExtra("data");
            for (Exercise selectedExercise : selectedExercises) {
                if (!exercises.contains(selectedExercise)) {
                    exercises.add(selectedExercise);
                }
            }
            recyclerView.getAdapter().notifyDataSetChanged();
            mSheetLayout.contractFab();
        }
    }

    public List<Exercise> getSelectedExercises() {
        return exercises;
    }

}
