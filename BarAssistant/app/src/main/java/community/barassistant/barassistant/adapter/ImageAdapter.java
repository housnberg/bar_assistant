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

import community.barassistant.barassistant.ImageLoaderSingleton;
import community.barassistant.barassistant.R;
import community.barassistant.barassistant.adapter.holder.ImageHolder;
import community.barassistant.barassistant.adapter.holder.ImageHolderSecond;
import community.barassistant.barassistant.model.Exercise;
import community.barassistant.barassistant.model.Workout;

/**
 * Created by EL on 11.06.2016.
 */
//TODO: REFACTOR THIS CLASS -> SHOUD EXTEND FROM EXERCISEADAPTER BECAUSE THIS IS A SPECIALIZED EXERCISE ADAPTER
public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int IMAGE_ADD_EXERCISE = 0;
    private static final int IMAGE_SHOW_EXERCISE = 1;

    private List<Object> items;
    private Activity context;
    private int resource;

    public ImageAdapter(Activity context, List<Object> items, @LayoutRes int resource) {
        super();
        this.items = items;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        if (IMAGE_ADD_EXERCISE == viewType) {
            View itemView = inflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);
            viewHolder = new ImageHolder(itemView);
        } else if(IMAGE_SHOW_EXERCISE == viewType) {
            View itemView = inflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);
            viewHolder = new ImageHolderSecond(itemView);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder.getItemViewType() == IMAGE_ADD_EXERCISE) {
            ImageHolder imageHolder = (ImageHolder) viewHolder;
            Bitmap image = (Bitmap) items.get(position);
            imageHolder.getImageView().setImageBitmap(image);
        } else if (viewHolder.getItemViewType() == IMAGE_SHOW_EXERCISE) {
            ImageHolderSecond imageHolder = (ImageHolderSecond) viewHolder;
            //Exercise image = (Bitmap) items.get(position);
            //imageHolder.getImageView().setImageBitmap(image);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Exercise) {
            return IMAGE_SHOW_EXERCISE;
        } else if (items.get(position) instanceof Bitmap) {
            return IMAGE_ADD_EXERCISE;
        }
        return -1;
    }

    public void onItemDismiss(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(items, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(items, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }
}
