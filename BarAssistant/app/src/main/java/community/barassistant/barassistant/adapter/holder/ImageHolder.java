package community.barassistant.barassistant.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import community.barassistant.barassistant.R;

/**
 * Created by EL on 11.06.2016.
 */
public class ImageHolder extends RecyclerView.ViewHolder {

    private ImageView image;
    private TextView description;

    public ImageHolder(View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.image);
        description = (TextView) itemView.findViewById(R.id.imageDescription);
    }

    public ImageView getImageView() {
        return image;
    }

    public TextView getDescriptionTextView() {
        return description;
    }
}