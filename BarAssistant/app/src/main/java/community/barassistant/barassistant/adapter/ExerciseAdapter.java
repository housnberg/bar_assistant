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
 * @author Eugen Ljavin
 */
public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder> {

    private List<Exercise> exercises;
    private Activity context;

    public ExerciseAdapter(Activity context, List<Exercise> exercises) {
        this.exercises = exercises;
        this.context = context;
    }

    @Override
    public ExerciseHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.exercise_list_item, viewGroup, false);
        return new ExerciseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExerciseHolder viewHolder, int position) {
        Exercise exercise = exercises.get(position);
        viewHolder.exerciseNameTextView.setText(exercise.getName());
        viewHolder.exerciseDescriptionTextView.setText(exercise.getDescription());
        viewHolder.itemView.setTag(exercise);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public class ExerciseHolder extends RecyclerView.ViewHolder {

        public TextView exerciseNameTextView;
        public TextView exerciseDescriptionTextView;

        public ExerciseHolder(View itemView) {
            super(itemView);
            exerciseNameTextView = (TextView) itemView.findViewById(R.id.exerciseNameTextView);
            exerciseDescriptionTextView = (TextView) itemView.findViewById(R.id.exerciseDescriptionTextView);
        }
    }
}
