package community.barassistant.barassistant.fragment;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.fabtransitionactivity.SheetLayout;

import community.barassistant.barassistant.AddExerciseToWorkoutActivity;
import community.barassistant.barassistant.R;
import community.barassistant.barassistant.model.Exercise;

/**
 * @author Eugen Ljavin
 */
public class AddExercisesToWorkoutFragment extends Fragment implements View.OnClickListener, SheetLayout.OnFabAnimationEndListener {

    private static final int REQUEST_CODE = 1;

    private FloatingActionButton mSharedFab;
    private SheetLayout mSheetLayout;

    public AddExercisesToWorkoutFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_recycler_view, container, false);
        setHasOptionsMenu(true);
        return rootView;
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
        startActivityForResult(intent,REQUEST_CODE);
        //mSheetLayout.expandFab();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {

            /*
            Exercise exercise = datasource.getLastAddedExercise();
            if (!exercises.contains(exercise)) {
                exercises.add(exercise);
                recyclerView.getAdapter().notifyDataSetChanged();
            }*/
            mSheetLayout.contractFab();
        }
    }

    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(getActivity(), AddExerciseToWorkoutActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }
}
