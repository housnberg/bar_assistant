package community.barassistant.barassistant.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import community.barassistant.barassistant.R;

/**
 * Created by EL on 11.06.2016.
 */
public class ExerciseHolder extends RecyclerView.ViewHolder {

    private TextView exerciseNameTextView;
    private TextView exerciseDescriptionTextView;
    private ImageView exerciseTitleImageView;
    private TextView repsTextView;
    private TextView genericText;

    public ExerciseHolder(View itemView) {
        super(itemView);
        exerciseNameTextView = (TextView) itemView.findViewById(R.id.exerciseNameTextView);
        exerciseDescriptionTextView = (TextView) itemView.findViewById(R.id.exerciseDescriptionTextView);
        exerciseTitleImageView = (ImageView) itemView.findViewById(R.id.exerciseTitleImageView);
        repsTextView = (TextView) itemView.findViewById(R.id.repsTextView);
        genericText = (TextView) itemView.findViewById(R.id.generic_text);
    }

    public ImageView getExerciseTitleImageView() {
        return exerciseTitleImageView;
    }

    public TextView getExerciseDescriptionTextView() {
        return exerciseDescriptionTextView;
    }

    public TextView getExerciseNameTextView() {
        return exerciseNameTextView;
    }

    public TextView getRepsTextView() {
        return repsTextView;
    }

    public TextView getGenericTextView() {
        return genericText;
    }
}