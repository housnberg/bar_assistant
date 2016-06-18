package community.barassistant.barassistant.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import community.barassistant.barassistant.ImageLoaderSingleton;
import community.barassistant.barassistant.R;
import community.barassistant.barassistant.adapter.holder.ExerciseHolder;
import community.barassistant.barassistant.model.Exercise;
import community.barassistant.barassistant.model.Workout;

/**
 * Created by EL on 11.06.2016.
 */
//TODO: REFACTOR THIS CLASS -> SHOUD EXTEND FROM EXERCISEADAPTER BECAUSE THIS IS A SPECIALIZED EXERCISE ADAPTER
public class ComplexExerciseWorkoutPropertyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Exercise> exercises;
    private Workout workout = null;
    private Activity context;
    private Map<Long, Integer> exerciseRepetitions;

    public ComplexExerciseWorkoutPropertyAdapter(Activity context, List<Exercise> items, Workout workout) {
        super();
        this.exercises = items;
        this.context = context;
        this.workout = workout;
    }

    public ComplexExerciseWorkoutPropertyAdapter(Activity context, List<Exercise> items, Map<Long, Integer> exerciseRepetitions) {
        super();
        this.exercises = items;
        this.context = context;
        this.exerciseRepetitions = exerciseRepetitions;
    }

    @Override
    public int getItemCount() {
        return this.exercises.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_workout_exercise, viewGroup, false);
            viewHolder = new ExerciseHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            ExerciseHolder exerciseHolder = (ExerciseHolder) viewHolder;
            ImageLoaderSingleton instance = ImageLoaderSingleton.getInstance();
            Exercise exercise = (Exercise) exercises.get(position);
            exerciseHolder.getExerciseNameTextView().setText(exercise.getName());
            exerciseHolder.getExerciseDescriptionTextView().setText(exercise.getDescription());
            TextView repsTextView = exerciseHolder.getRepsTextView();
            if (workout == null) {
                //TODO: THIS IS THE DEFAULT VALUE FOR THE REPS. FIND A BETTER PLACE FOR THIS INFORMATION
                repsTextView.setText(String.valueOf(exerciseRepetitions.get(exercise.getId())));
            } else {
                repsTextView.setText(String.valueOf(workout.getRepetitionByExerciseId(exercise.getId())));
            }
            Bitmap image = null;
            //Only show the first saved image as title image
            if (exercise.getImagePaths() != null) {
                image = instance.loadImageFromStorage(exercise.getImagePaths().get(0).getImagePath());
                exerciseHolder.getExerciseTitleImageView().setImageBitmap(image);
                viewHolder.itemView.setTag(exercise);
            }
    }

    public void onItemDismiss(int position) {
        exercises.remove(position);
        notifyItemRemoved(position);
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(exercises, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(exercises, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }
}
