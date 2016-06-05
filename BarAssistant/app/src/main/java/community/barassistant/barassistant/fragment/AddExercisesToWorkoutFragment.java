package community.barassistant.barassistant.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import community.barassistant.barassistant.R;

/**
 * Created by EL on 04.06.2016.
 */
public class AddExercisesToWorkoutFragment extends Fragment {

    public AddExercisesToWorkoutFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_exercises_to_workout, container, false);
        setHasOptionsMenu(true);
        return rootView;
    }

}
