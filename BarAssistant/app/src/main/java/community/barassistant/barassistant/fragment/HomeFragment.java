package community.barassistant.barassistant.fragment;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import community.barassistant.barassistant.R;

/**
 * Created by EL on 28.05.2016.
 */
public class HomeFragment extends Fragment {

    private FloatingActionButton mSharedFab;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View drawer = inflater.inflate(R.layout.fragment_main_exercise, container, false);
        RecyclerView recyclerView = (RecyclerView) drawer.findViewById(R.id.recyclerView);
        setupRecyclerView(recyclerView);
        return drawer;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecyclerView.Adapter() {

            private final static int DUMMY_ITEM_COUNT = 30;

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
                View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
                return new TextHolder(itemView);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
                // We are too lazy for this by now ;-)
            }

            @Override
            public int getItemCount() {
                return DUMMY_ITEM_COUNT;
            }


            class TextHolder extends RecyclerView.ViewHolder {

                public TextHolder(View itemView) {
                    super(itemView);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSharedFab = null; // To avoid keeping/leaking the reference of the FAB
    }

    public void shareFab(FloatingActionButton fab) {
        if (fab == null) { // When the FAB is shared to another Fragment
            if (mSharedFab != null) {
                mSharedFab.setOnClickListener(null);
            }
            mSharedFab = null;
        }
        else {
            mSharedFab = fab;
            mSharedFab.hide();
        }
    }
}
