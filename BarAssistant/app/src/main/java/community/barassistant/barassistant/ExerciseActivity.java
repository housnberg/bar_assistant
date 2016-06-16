package community.barassistant.barassistant;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import community.barassistant.barassistant.adapter.ComplexExerciseWorkoutPropertyAdapter;
import community.barassistant.barassistant.adapter.ExerciseDetailAdapter;
import community.barassistant.barassistant.adapter.ExerciseOverviewAdapter;
import community.barassistant.barassistant.adapter.ExerciseTouchHelper;
import community.barassistant.barassistant.adapter.ImageAdapter;
import community.barassistant.barassistant.dao.DataAccessObject;
import community.barassistant.barassistant.model.Exercise;
import community.barassistant.barassistant.model.Image;
import community.barassistant.barassistant.model.Workout;

/**
 * Created by EL on 11.06.2016.
 */
public class ExerciseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView description;

    private DataAccessObject datasource;
    private Exercise exercise;
    private List<Bitmap> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exercise = getIntent().getExtras().getParcelable("data");
        setContentView(R.layout.activity_exercise);

        description = (TextView) findViewById(R.id.description);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        System.out.println("DESCRIPTION:" + exercise.getDescription());

        if (exercise.getDescription() != null && !exercise.getDescription().trim().equals("")) {
            description.setVisibility(View.VISIBLE);
            description.setText(exercise.getDescription().trim());
        }

        datasource = new DataAccessObject(this);
        datasource.open();

        items = new ArrayList<Bitmap>();
        for (Image imagePath : exercise.getImagePaths()) {
            items.add(ImageLoaderSingleton.getInstance().loadImageFromStorage(imagePath.getImagePath()));
        }

        initToolbar();

        setupRecyclerView(recyclerView);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_navigate_before_white_24dp);
        toolbar.setTitle(exercise.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerView(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ExerciseDetailAdapter(this, R.layout.list_item_exercise_detail, items));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolar_share, menu);
        return true;
    }

}
