package community.barassistant.barassistant.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import community.barassistant.barassistant.MainActivity;
import community.barassistant.barassistant.R;
import community.barassistant.barassistant.dao.ExercisesDAO;
import community.barassistant.barassistant.model.Exercise;
import community.barassistant.barassistant.services.ImageService;

/**
 * @author Eugen Ljavin
 */
public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder> {

    private List<Exercise> exercises;
    private Activity context;
    private ExercisesDAO datasource;

    public ExerciseAdapter(Activity context, List<Exercise> exercises, ExercisesDAO datasource) {
        this.exercises = exercises;
        this.context = context;
        this.datasource = datasource;
    }

    @Override
    public ExerciseHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_exercise, viewGroup, false);
        return new ExerciseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExerciseHolder viewHolder, int position) {
        Exercise exercise = exercises.get(position);
        viewHolder.exerciseNameTextView.setText(exercise.getName());
        viewHolder.exerciseDescriptionTextView.setText(exercise.getDescription());
        Bitmap image = null;
        if (((MainActivity) context).getImageService() != null) {
            image = ((MainActivity) context).getImageService().loadImageFromStorage(datasource.getImagePathByExerciseId(exercise.getId()));
        }
        if (image != null) {
            viewHolder.exerciseTitleImageView.setImageBitmap(((MainActivity) context).getImageService().loadImageFromStorage(datasource.getImagePathByExerciseId(exercise.getId())));
        }
        viewHolder.itemView.setTag(exercise);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    class ExerciseHolder extends RecyclerView.ViewHolder {

        public TextView exerciseNameTextView;
        public TextView exerciseDescriptionTextView;
        public ImageView exerciseTitleImageView;

        public ExerciseHolder(View itemView) {
            super(itemView);
            exerciseNameTextView = (TextView) itemView.findViewById(R.id.exerciseNameTextView);
            exerciseDescriptionTextView = (TextView) itemView.findViewById(R.id.exerciseDescriptionTextView);
            exerciseTitleImageView = (ImageView) itemView.findViewById(R.id.exerciseTitleImageView);
        }
    }
}
