package community.barassistant.barassistant.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import community.barassistant.barassistant.R;

/**
 * Created by EL on 11.06.2016.
 */
public class WorkoutPropertyHolder  extends RecyclerView.ViewHolder {

    private TextView description;
    private TextView rounds;
    private TextView pauseExercises;
    private TextView pauseRounds;

    public WorkoutPropertyHolder(View itemView) {
        super(itemView);
        description = (TextView) itemView.findViewById(R.id.workoutDescription);
        rounds = (TextView) itemView.findViewById(R.id.rounds);
        pauseExercises = (TextView) itemView.findViewById(R.id.pauseExercises);
        pauseRounds = (TextView) itemView.findViewById(R.id.pauseRounds);
    }

    public TextView getPauseRoundsTextView() {
        return pauseRounds;
    }

    public TextView getPauseExercisesTextView() {
        return pauseExercises;
    }

    public TextView getRoundsTextView() {
        return rounds;
    }

    public TextView getDescriptionTextView() {
        return description;
    }
}