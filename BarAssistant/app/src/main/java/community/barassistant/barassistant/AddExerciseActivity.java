package community.barassistant.barassistant;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import community.barassistant.barassistant.model.Exercise;
import community.barassistant.barassistant.dao.ExercisesDAO;
import community.barassistant.barassistant.services.ImageService;

/**
 * @author Eugen Ljavin
 * @author Johann Andrejtschik
 */
public class AddExerciseActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String APP_PATH_SD_CARD = "/DesiredSubfolderName/";
    public final static String APP_THUMBNAIL_PATH_SD_CARD = "thumbnails";

    private static final int CAMERA_REQUEST = 1888;

    private Toolbar toolbar;
    private FloatingActionButton mSharedFab;
    //private FloatingActionButton mSecondaryFab;
    private ExercisesDAO datasource;
    private EditText exerciseName;
    private EditText exerciseDescription;
    private Bitmap photo;

    private boolean bound = false;

    private List<String> imageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        mSharedFab = (FloatingActionButton) findViewById(R.id.fabMain);
        mSharedFab.setOnClickListener(this);

        imageUrls = new ArrayList<String>();

        datasource = new ExercisesDAO(this);
        datasource.open();

        exerciseName = (EditText) findViewById(R.id.name);
        exerciseDescription = (EditText) findViewById(R.id.description);

        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
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
                Toast.makeText(this, R.string.toastInvalidName, Toast.LENGTH_LONG).show();
            } else {
                Exercise exercise = null;
                exercise = datasource.createExercise(exerciseName.getText().toString().trim(), exerciseDescription.getText().toString().trim());
                for (String path : imageUrls) {
                    datasource.createImagePath(path, exercise.getId());
                }
                finish();
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            ImageLoaderSingleton instance = ImageLoaderSingleton.getInstance();
            photo = (Bitmap) data.getExtras().get("data");
            ImageView iv = (ImageView) findViewById(R.id.imageView);
            imageUrls.add(instance.saveImageToStorage(photo, new ContextWrapper(getApplicationContext())));
            iv.setImageBitmap(instance.loadImageFromStorage(imageUrls.get(imageUrls.size() - 1)));
        }
    }
}
