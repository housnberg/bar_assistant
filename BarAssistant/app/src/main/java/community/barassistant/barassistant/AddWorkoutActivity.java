package community.barassistant.barassistant;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import community.barassistant.barassistant.model.ExercisesDAO;

/**
 * Created by EL on 02.06.2016.
 */
public class AddWorkoutActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 1888;
    private final static String ACTIVITY_NAME = "Add Workout";

    private Toolbar toolbar;
    private FloatingActionButton mSharedFab;
    private FloatingActionButton mSecondaryFab;
    private ExercisesDAO datasource;
    private EditText exerciseName;
    private EditText exerciseDescription;
    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        mSharedFab = (FloatingActionButton) findViewById(R.id.fabMain);
        mSharedFab.setOnClickListener(this);
        mSecondaryFab = (FloatingActionButton) findViewById(R.id.fabSecondary);
        mSecondaryFab.setOnClickListener(this);

        datasource = new ExercisesDAO(this);
        datasource.open();

        exerciseName = (EditText) findViewById(R.id.exerciseName);
        exerciseDescription = (EditText) findViewById(R.id.exerciseDescription);

        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_close_white_24dp);
        toolbar.setTitle(AddWorkoutActivity.ACTIVITY_NAME);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(View v) {

    }
}
