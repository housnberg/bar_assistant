package community.barassistant.barassistant;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.github.fabtransitionactivity.SheetLayout;

/**
 * Created by EL on 28.05.2016.
 */
public class ExcerciseFragment extends MyFragment implements View.OnClickListener, SheetLayout.OnFabAnimationEndListener {

    private FloatingActionButton mSharedFab;
    private FloatingActionButton fabSecondary;

    private SheetLayout mSheetLayout;

    private boolean isFabOpen = true;

    public ExcerciseFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        setupRecyclerView(recyclerView);
        return rootView;
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

    public void shareFab(FloatingActionButton fab) {
        if (fab == null) { // When the FAB is shared to another Fragmen{t
            if (mSharedFab != null) {
                mSharedFab.setOnClickListener(null);
            }
            mSharedFab = null;
        }
        else {
            mSharedFab = fab;
            mSharedFab.setOnClickListener(this);
            fab.setImageResource(R.mipmap.ic_add_white_24dp);
        }
    }

    @Override
    public void onClick(View view) {
        if(isFabOpen) {
            mSharedFab.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate_forward));
            mSheetLayout = (SheetLayout) getView().getRootView().findViewById(R.id.bottom_sheet);
            mSheetLayout.setFab(mSharedFab);
            mSheetLayout.setFabAnimationEndListener(this);
            mSheetLayout.expandFab();
            fabSecondary = (FloatingActionButton) getView().getRootView().findViewById(R.id.fabSecondary);
            fabSecondary.show();
        } else {
            mSharedFab.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate_backward));
            fabSecondary = (FloatingActionButton) getView().getRootView().findViewById(R.id.fabSecondary);
            fabSecondary.hide();
        }
        isFabOpen = !isFabOpen;
    }

    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(getActivity(), AddExerciseActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSharedFab = null; // To avoid keeping/leaking the reference of the FAB
    }

}
