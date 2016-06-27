package community.barassistant.barassistant;

import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import community.barassistant.barassistant.adapter.ImageAdapter;
import community.barassistant.barassistant.adapter.ImageTouchHelper;
import community.barassistant.barassistant.adapter.ItemClickSupport;
import community.barassistant.barassistant.dao.DataAccessObject;
import community.barassistant.barassistant.model.Exercise;
import community.barassistant.barassistant.model.Image;
import community.barassistant.barassistant.util.Constants;
import community.barassistant.barassistant.util.Helper;
import community.barassistant.barassistant.util.ImageControllerSingleton;

/**
 * @author Eugen Ljavin
 * @author Johann Andrejtschik
 */
public class AddExerciseActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 1888;

    private int result = RESULT_CANCELED;

    private Toolbar toolbar;
    private FloatingActionButton mSharedFab;
    //private FloatingActionButton mSecondaryFab;
    private DataAccessObject datasource;
    private EditText exerciseName;
    private EditText exerciseDescription;
    private RecyclerView recyclerView;
    private View contentWrapper;

    private Bitmap photo;

    private List<Image> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        mSharedFab = (FloatingActionButton) findViewById(R.id.fabMain);
        mSharedFab.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        contentWrapper = findViewById(R.id.content_wrapper);

        images = new ArrayList<Image>();

        datasource = new DataAccessObject(this);
        datasource.open();

        exerciseName = (EditText) findViewById(R.id.name);
        exerciseDescription = (EditText) findViewById(R.id.description);

        setupRecyclerView(recyclerView);
        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_close_white_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupRecyclerView(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ImageAdapter(this, images, R.layout.list_item_image));

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showLocationDialog(position);
            }
        });

        ItemTouchHelper.Callback callback = new ImageTouchHelper((ImageAdapter) recyclerView.getAdapter());
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fabMain) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionSave) {
            if (exerciseName.getText().toString().trim().length() < 3) {
                Helper.createSnackbar(this, contentWrapper, R.string.snackbarInvalidName, Constants.STATUS_ERROR).show();
            } else {
                Exercise exercise = null;
                ImageControllerSingleton instance = ImageControllerSingleton.getInstance();
                List<Image> images = new ArrayList<Image>();
                for (int order = 0; order < this.images.size(); order++) {
                    String imagePath = instance.saveImageToStorage(this.images.get(order).getBitmap(), new ContextWrapper(getApplicationContext()));
                    System.out.println("+++++++" + imagePath);
                    images.add(new Image(imagePath, order, this.images.get(order).getDescription()));
                }
                exercise = datasource.createExercise(exerciseName.getText().toString().trim(), exerciseDescription.getText().toString().trim(), images);
                result = RESULT_OK;
                finish();
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            Image image = new Image();
            image.setBitmap(photo);
            images.add(image);
            recyclerView.getAdapter().notifyDataSetChanged();
        }

    }

    private void showLocationDialog(final int imagePosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Image Description");
        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_description, null);

        final EditText imageDescritpion = (EditText) view.findViewById(R.id.imageDescription);

        builder.setView(view);

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        images.get(imagePosition).setDescription(imageDescritpion.getText().toString());
                        recyclerView.getAdapter().notifyDataSetChanged();
                        Helper.createSnackbar(AddExerciseActivity.this, contentWrapper, R.string.snackbarSuccessfullyAddedImageDescription, Constants.STATUS_INFO).show();

                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        setResult(result, intent);
        super.finish();
    }
}