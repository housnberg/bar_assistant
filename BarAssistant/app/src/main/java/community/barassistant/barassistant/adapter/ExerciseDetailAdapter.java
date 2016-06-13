package community.barassistant.barassistant.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import community.barassistant.barassistant.R;
import community.barassistant.barassistant.model.Workout;

/**
 * Created by EL on 13.06.2016.
 */
public class ExerciseDetailAdapter extends RecyclerView.Adapter<ExerciseDetailAdapter.ExerciseImageHolder> {

    private List<Bitmap> images;
    private Activity context;
    private int resource;

    public ExerciseDetailAdapter(Activity context, @LayoutRes int resource, List<Bitmap> workouts) {
        this.images = workouts;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public ExerciseImageHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);
        return new ExerciseImageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExerciseImageHolder viewHolder, int position) {
        Bitmap image = images.get(position);
        viewHolder.image.setImageBitmap(image);
        viewHolder.order.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ExerciseImageHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView order;
        private TextView description;

        public ExerciseImageHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            order = (TextView) itemView.findViewById(R.id.order);
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }
}
