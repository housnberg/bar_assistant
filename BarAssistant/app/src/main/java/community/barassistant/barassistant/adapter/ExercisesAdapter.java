package community.barassistant.barassistant.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import community.barassistant.barassistant.R;
import community.barassistant.barassistant.model.Exercise;

/**
 * Created by EL on 02.06.2016.
 */
public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ExercisesHolder> {

    private List<Exercise> exercises;
    private Activity context;

    public ExercisesAdapter(Activity context, List<Exercise> exercises) {
        this.exercises = exercises;
        this.context = context;
    }

    @Override
    public ExercisesHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ExercisesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExercisesHolder viewHolder, int position) {
        Exercise exercise = exercises.get(position);
        viewHolder.exerciseNameTextView.setText(exercise.getExerciseName());
        viewHolder.exerciseDescriptionTextView.setText(exercise.getExerciseDescription());
        viewHolder.itemView.setTag(exercise);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public class ExercisesHolder extends RecyclerView.ViewHolder {

        public TextView exerciseNameTextView;
        public TextView exerciseDescriptionTextView;

        public ExercisesHolder(View itemView) {
            super(itemView);
            exerciseNameTextView = (TextView) itemView.findViewById(R.id.exerciseNameTextView);
            exerciseDescriptionTextView = (TextView) itemView.findViewById(R.id.exerciseDescriptionTextView);
        }
    }
}
