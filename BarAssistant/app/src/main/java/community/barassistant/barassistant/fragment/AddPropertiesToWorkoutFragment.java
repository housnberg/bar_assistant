package community.barassistant.barassistant.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.lantouzi.wheelview.WheelView;

import java.util.ArrayList;
import java.util.List;

import community.barassistant.barassistant.R;

/**
 * @author Eugen Ljavin
 */
public class AddPropertiesToWorkoutFragment extends Fragment {

    private WheelView roundsWheeler;
    private WheelView pauseExercisesWheeler;
    private WheelView pauseRoundsWheeler;

    private EditText workoutName;
    private EditText workoutDescription;

    private List<String> possibleRounds;
    private List<String> possiblePauseExercises;
    private List<String> possiblePauseRounds;

    public AddPropertiesToWorkoutFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_properties_to_workout, container, false);
        setHasOptionsMenu(true);
        possibleRounds = new ArrayList<String>();
        possiblePauseExercises = new ArrayList<String>();
        possiblePauseRounds = new ArrayList<String>();
        for (int i = 1; i <= 15; i++) {
            possibleRounds.add(String.valueOf(i));
        }
        for (int i = 1; i <= 90; i++) {
            possiblePauseExercises.add(String.valueOf(i));
            possiblePauseRounds.add(String.valueOf(i));
        }

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        if (item.getItemId() == R.id.actionSave) {
            if (exerciseName.getText().toString().length() < 3) {
                Toast.makeText(this, R.string.toastInvalidName, Toast.LENGTH_LONG).show();
            } else {
                Exercise exercise = null;
                exercise = datasource.createExercise(exerciseName.getText().toString(), exerciseDescription.getText().toString());
                finish();
            }
        }
        */
        Toast.makeText(getActivity(), "TUT", Toast.LENGTH_LONG).show();
        return true;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View rootView = getView().getRootView();

        workoutName = (EditText) rootView.findViewById(R.id.name);
        workoutDescription = (EditText) rootView.findViewById(R.id.description);

        roundsWheeler = (WheelView) rootView.findViewById(R.id.roundsWheeler);
        roundsWheeler.setItems(possibleRounds);
        roundsWheeler.selectIndex(4);
        pauseExercisesWheeler = (WheelView) rootView.findViewById(R.id.pauseExercisesWheeler);
        pauseExercisesWheeler.setItems(possiblePauseExercises);
        pauseExercisesWheeler.selectIndex(19);
        pauseRoundsWheeler = (WheelView) rootView.findViewById(R.id.pauseRoundsWheeler);
        pauseRoundsWheeler.setItems(possiblePauseRounds);
        pauseRoundsWheeler.selectIndex(59);
    }

    public WheelView getPauseExercisesWheeler() {
        return pauseExercisesWheeler;
    }

    public WheelView getPauseRoundsWheeler() {
        return pauseRoundsWheeler;
    }

    public WheelView getRoundsWheeler() {
        return roundsWheeler;
    }

    public EditText getWorkoutDescription() {
        return workoutDescription;
    }

    public EditText getWorkoutName() {
        return workoutName;
    }
}