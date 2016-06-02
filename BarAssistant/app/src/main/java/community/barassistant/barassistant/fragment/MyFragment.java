package community.barassistant.barassistant.fragment;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import community.barassistant.barassistant.R;

/**
 * Created by EL on 29.05.2016.
 */
public class MyFragment extends Fragment {

    private FloatingActionButton mSharedFab;

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
        }
    }

    public FloatingActionButton getFloatingActionButton() {
        return mSharedFab;
    }
}
