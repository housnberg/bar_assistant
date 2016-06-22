package community.barassistant.barassistant.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import community.barassistant.barassistant.R;

/**
 * Created by EL on 11.06.2016.
 */
public class ImageHolderSecond extends RecyclerView.ViewHolder {

    private ImageView image;
    private TextView order;
    private TextView description;

    public ImageHolderSecond(View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.image);

        order = (TextView) itemView.findViewById(R.id.order);
        description = (TextView) itemView.findViewById(R.id.description);
    }

    public ImageView getImageView() {
        return image;
    }

    public TextView getDescription() {
        return description;
    }

    public TextView getOrder() {
        return order;
    }
}