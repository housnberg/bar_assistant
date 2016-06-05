package community.barassistant.barassistant.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import community.barassistant.barassistant.R;
import community.barassistant.barassistant.model.Workout;

/**
 * @author Eugen Ljavin
 */
public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutHolder> {

    private List<Workout> workouts;
    private Activity context;

    public WorkoutAdapter(Activity context, List<Workout> workouts) {
        this.workouts = workouts;
        this.context = context;
    }

    @Override
    public WorkoutHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_workout, viewGroup, false);
        return new WorkoutHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WorkoutHolder viewHolder, int position) {
        Workout workout = workouts.get(position);
        viewHolder.workoutNameTextView.setText(workout.getName());
        viewHolder.workoutDescriptionTextView.setText(workout.getDescription());
        viewHolder.itemView.setTag(workout);
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public class WorkoutHolder extends RecyclerView.ViewHolder {

        public TextView workoutNameTextView;
        public TextView workoutDescriptionTextView;

        public WorkoutHolder(View itemView) {
            super(itemView);
            workoutNameTextView = (TextView) itemView.findViewById(R.id.workoutNameTextView);
            workoutDescriptionTextView = (TextView) itemView.findViewById(R.id.workoutDescriptionTextView);
        }
    }
}
