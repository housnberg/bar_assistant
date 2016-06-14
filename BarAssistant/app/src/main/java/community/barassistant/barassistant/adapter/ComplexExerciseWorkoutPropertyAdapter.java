package community.barassistant.barassistant.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import community.barassistant.barassistant.ImageLoaderSingleton;
import community.barassistant.barassistant.R;
import community.barassistant.barassistant.adapter.holder.ExerciseHolder;
import community.barassistant.barassistant.adapter.holder.WorkoutPropertyHolder;
import community.barassistant.barassistant.model.Exercise;
import community.barassistant.barassistant.model.Workout;

/**
 * Created by EL on 11.06.2016.
 */
//TODO: REFACTOR THIS CLASS -> SHOUD EXTEND FROM EXERCISEADAPTER BECAUSE THIS IS A SPECIALIZED EXERCISE ADAPTER
public class ComplexExerciseWorkoutPropertyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int EXERCISE = 0;
    private static final int WORKOUT = 1;

    private List<Object> items;
    private Workout workout;
    private Activity context;

    public ComplexExerciseWorkoutPropertyAdapter(Activity context, List<Object> items, Workout workout) {
        super();
        this.items = items;
        this.context = context;
        this.workout = workout;
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        if (EXERCISE == viewType) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_workout_exercise, viewGroup, false);
            viewHolder = new ExerciseHolder(itemView);
        } else if (WORKOUT == viewType) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_workout_property, viewGroup, false);
            viewHolder = new WorkoutPropertyHolder(itemView);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder.getItemViewType() == EXERCISE) {
            ExerciseHolder exerciseHolder = (ExerciseHolder) viewHolder;
            ImageLoaderSingleton instance = ImageLoaderSingleton.getInstance();
            Exercise exercise = (Exercise) items.get(position);
            exerciseHolder.getExerciseNameTextView().setText(exercise.getName());
            exerciseHolder.getExerciseDescriptionTextView().setText(exercise.getDescription());
            exerciseHolder.getRepsTextView().setText(String.valueOf(workout.getRepetitionByExerciseId(exercise.getId())));
            Bitmap image = null;
            //Only show the first saved image as title image
            if (exercise.getImagePaths() != null) {
                image = instance.loadImageFromStorage(exercise.getImagePaths().get(0));
                exerciseHolder.getExerciseTitleImageView().setImageBitmap(image);
                viewHolder.itemView.setTag(exercise);
            }
        } else if (viewHolder.getItemViewType() == WORKOUT) {
            WorkoutPropertyHolder workoutPropertyHolder = (WorkoutPropertyHolder) viewHolder;
            Workout workout = (Workout) items.get(position);
            if (workout.getDescription().trim() != "") {
                workoutPropertyHolder.getDescriptionTextView().setText(workout.getDescription());
                workoutPropertyHolder.getDescriptionTextView().clearAnimation();
                workoutPropertyHolder.getDescriptionTextView().clearFocus();
                workoutPropertyHolder.getDescriptionTextView().setVisibility(View.VISIBLE);
            }
            workoutPropertyHolder.getRoundsTextView().setText(String.valueOf(workout.getRounds()));
            workoutPropertyHolder.getPauseExercisesTextView().setText(String.valueOf(workout.getPauseExercises()));
            workoutPropertyHolder.getPauseRoundsTextView().setText(String.valueOf(workout.getPauseRounds()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Exercise) {
            return EXERCISE;
        } else if (items.get(position) instanceof Workout) {
            return WORKOUT;
        }
        return -1;
    }
}
