package community.barassistant.barassistant.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import community.barassistant.barassistant.adapter.holder.ImageHolder;
import community.barassistant.barassistant.adapter.holder.ImageHolderSecond;
import community.barassistant.barassistant.model.Exercise;
import community.barassistant.barassistant.model.Image;

/**
 * Created by EL on 11.06.2016.
 */
//TODO: REFACTOR THIS CLASS -> SHOUD EXTEND FROM EXERCISEADAPTER BECAUSE THIS IS A SPECIALIZED EXERCISE ADAPTER
public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int IMAGE_ADD_EXERCISE = 0;
    private static final int IMAGE_SHOW_EXERCISE = 1;

    private List<Image> images;
    private Activity context;
    private int resource;

    public ImageAdapter(Activity context, List<Image> items, @LayoutRes int resource) {
        super();
        this.images = items;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public int getItemCount() {
        return this.images.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ImageHolder(LayoutInflater.from(viewGroup.getContext()).from(viewGroup.getContext()).inflate(resource, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ImageHolder imageHolder = (ImageHolder) viewHolder;
        Bitmap image = images.get(position).getBitmap();
        imageHolder.getImageView().setImageBitmap(image);
        imageHolder.getDescriptionTextView().setText(images.get(position).getDescription());
    }

    public void onItemDismiss(int position) {
        images.remove(position);
        notifyItemRemoved(position);
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(images, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(images, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }
}
