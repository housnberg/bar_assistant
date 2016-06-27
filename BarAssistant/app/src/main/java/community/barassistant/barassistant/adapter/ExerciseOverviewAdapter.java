package community.barassistant.barassistant.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import community.barassistant.barassistant.util.ImageControllerSingleton;
import community.barassistant.barassistant.R;
import community.barassistant.barassistant.adapter.holder.ExerciseHolder;
import community.barassistant.barassistant.model.Exercise;

/**
 * @author Eugen Ljavin
 */
public class ExerciseOverviewAdapter extends RecyclerView.Adapter<ExerciseHolder> {

    private List<Exercise> exercises;
    private Activity context;

    public ExerciseOverviewAdapter(Activity context, List<Exercise> exercises) {
        this.exercises = exercises;
        this.context = context;
    }

    @Override
    public ExerciseHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_exercise, viewGroup, false);
        return new ExerciseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExerciseHolder viewHolder, int position) {
        ImageControllerSingleton instance = ImageControllerSingleton.getInstance();
        Exercise exercise = exercises.get(position);
        viewHolder.getExerciseNameTextView().setText(exercise.getName());
        viewHolder.getExerciseDescriptionTextView().setText(exercise.getDescription());
        Bitmap image = null;
        //Only show the first saved image as title image
            image = instance.loadImageFromStorage(exercise.getImagePaths().get(0).getImagePath(), context);
            viewHolder.getExerciseTitleImageView().setImageBitmap(image);
            viewHolder.itemView.setTag(exercise);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
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
