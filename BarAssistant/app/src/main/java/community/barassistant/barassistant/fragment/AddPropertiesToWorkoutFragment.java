package community.barassistant.barassistant.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lantouzi.wheelview.WheelView;

import java.util.ArrayList;
import java.util.List;

import community.barassistant.barassistant.R;

/**
 * Created by EL on 04.06.2016.
 */
public class AddPropertiesToWorkoutFragment extends Fragment {

    private WheelView roundsWheeler;
    private WheelView pauseExercisesWheeler;
    private WheelView pauseRoundsWheeler;

    List<String> possibleRounds;
    List<String> possiblePauseExercises;
    List<String> possiblePauseRounds;

    public AddPropertiesToWorkoutFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_properties_to_workout, container, false);
        setHasOptionsMenu(true);possibleRounds = new ArrayList<String>();
        possiblePauseExercises= new ArrayList<String>();
        possiblePauseRounds = new ArrayList<String>();
        for (int i = 1; i <= 15; i ++) {
            possibleRounds.add(String.valueOf(i));
        }
        for (int i = 1; i <= 90; i ++) {
            possiblePauseExercises.add(String.valueOf(i));
            possiblePauseRounds.add(String.valueOf(i));
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        roundsWheeler = (WheelView) getView().getRootView().findViewById(R.id.roundsWheeler);
        roundsWheeler.setItems(possibleRounds);
        roundsWheeler.selectIndex(4);
        pauseExercisesWheeler = (WheelView) getView().getRootView().findViewById(R.id.pauseExercisesWheeler);
        pauseExercisesWheeler.setItems(possiblePauseExercises);
        pauseExercisesWheeler.selectIndex(21);
        pauseRoundsWheeler = (WheelView) getView().getRootView().findViewById(R.id.pauseRoundsWheeler);
        pauseRoundsWheeler.setItems(possiblePauseRounds);
        pauseRoundsWheeler.selectIndex(61);
        roundsWheeler.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onWheelItemChanged(WheelView wheelView, int position) {
                Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_SHORT);
            }

            @Override
            public void onWheelItemSelected(WheelView wheelView, int position) {

            }
        });
    }
}
