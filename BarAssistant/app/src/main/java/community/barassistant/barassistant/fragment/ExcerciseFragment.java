package community.barassistant.barassistant.fragment;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import community.barassistant.barassistant.AddExerciseActivity;
import community.barassistant.barassistant.ExerciseActivity;
import community.barassistant.barassistant.MainActivity;
import community.barassistant.barassistant.R;
import community.barassistant.barassistant.adapter.ExerciseOverviewAdapter;
import community.barassistant.barassistant.adapter.ItemClickSupport;
import community.barassistant.barassistant.dao.DataAccessObject;
import community.barassistant.barassistant.model.Exercise;
import community.barassistant.barassistant.util.Constants;
import community.barassistant.barassistant.util.Helper;

/**
 * @author Eugen Ljavin
 */
public class ExcerciseFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CODE = 1;

    private FloatingActionButton mSharedFab;
    private FloatingActionButton fabSecondary;
    private RecyclerView recyclerView;

    private DataAccessObject datasource;
    private List<Exercise> exercises;

    private boolean isFabOpen = true;

    public ExcerciseFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_recycler_view, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        datasource = new DataAccessObject(getActivity());
        datasource.open();
        exercises = datasource.getAllExercises();

        setHasOptionsMenu(true);
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
        recyclerView.setAdapter(new ExerciseOverviewAdapter(getActivity(), exercises));
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(getActivity(), ExerciseActivity.class);
                intent.putExtra("data", exercises.get(position));
                startActivityForResult(intent, REQUEST_CODE);
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
        Intent intent = new Intent(getActivity(), AddExerciseActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == getActivity().RESULT_OK) {

            Exercise exercise = datasource.getLastAddedExercise();
            if (!exercises.contains(exercise)) {
                exercises.add(exercise);
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            Helper.createSnackbar(getActivity(), ((MainActivity) getActivity()).getContentWrapper(), R.string.snackbarExerciseAddedSuccessfully).show();
        }
    }

}
