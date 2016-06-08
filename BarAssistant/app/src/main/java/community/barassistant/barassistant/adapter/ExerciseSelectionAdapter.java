package community.barassistant.barassistant.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import community.barassistant.barassistant.ImageLoaderSingleton;
import community.barassistant.barassistant.R;
import community.barassistant.barassistant.model.Exercise;

/**
 * @author Eugen Ljavin
 */
public class ExerciseSelectionAdapter extends ArrayAdapter<Exercise> {

    private Activity context;
    private int resource;

    public ExerciseSelectionAdapter(Activity context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<Exercise> exercises) {
        super(context, resource, textViewResourceId, exercises);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Exercise exercise = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }
        TextView exerciseName = (TextView) convertView.findViewById(R.id.exerciseNameTextView);
        TextView exerciseDescription = (TextView) convertView.findViewById(R.id.exerciseDescriptionTextView);
        ImageView exerciseTitleImageView = (ImageView) convertView.findViewById(R.id.exerciseTitleImageView);
        exerciseName.setText(exercise.getName());
        exerciseDescription.setText(exercise.getDescription());
        //Only show the first saved image as title image
        exerciseTitleImageView.setImageBitmap(ImageLoaderSingleton.getInstance().loadImageFromStorage(exercise.getImagePaths().get(0)));

        return convertView;
    }
}