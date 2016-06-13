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
        ImageLoaderSingleton instance = ImageLoaderSingleton.getInstance();
        Exercise exercise = exercises.get(position);
        viewHolder.getExerciseNameTextView().setText(exercise.getName());
        viewHolder.getExerciseDescriptionTextView().setText(exercise.getDescription());
        Bitmap image = null;
        //Only show the first saved image as title image
        if (exercise.getImagePaths() != null) {
            image = instance.loadImageFromStorage(exercise.getImagePaths().get(0));
            viewHolder.getExerciseTitleImageView().setImageBitmap(image);
            viewHolder.itemView.setTag(exercise);
        }
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

}
