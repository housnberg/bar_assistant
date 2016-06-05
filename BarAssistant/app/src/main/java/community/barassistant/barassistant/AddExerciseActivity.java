package community.barassistant.barassistant;

import android.content.ComponentName;
import android.content.Context;
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

import community.barassistant.barassistant.model.Exercise;
import community.barassistant.barassistant.dao.ExercisesDAO;
import community.barassistant.barassistant.services.ImageService;

/**
 * @author Eugen Ljavin
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
    private ImageService imageService;
    private boolean bound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        mSharedFab = (FloatingActionButton) findViewById(R.id.fabMain);
        mSharedFab.setOnClickListener(this);
        //mSecondaryFab = (FloatingActionButton) findViewById(R.id.fabSecondary);
        //mSecondaryFab.setOnClickListener(this);

        datasource = new ExercisesDAO(this);
        datasource.open();

        exerciseName = (EditText) findViewById(R.id.name);
        exerciseDescription = (EditText) findViewById(R.id.description);

        initToolbar();
    }

    @Override
    protected void onStart(){
        super.onStart();
        // Bind ImageService
        Intent intent = new Intent(this, ImageService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
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
    protected void onStop(){
        super.onStop();
        //Unbind Service. Service gets destroyed when not bound by any activity.
        if(bound){
            unbindService(connection);
            bound = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            /* ImageView iv = (ImageView) findViewById(R.id.imageView);
            iv.setImageBitmap(imageService.loadImageFromStorage(imageService.saveImageToStorage(photo))); */
        }
    }

    //WANJA; USE THIS FOR YOUR CLASS
    public void saveImageToExternalStorage(Bitmap bm) {

        OutputStream fOut = null;
        String strDirectory = getAlbumStorageDir("bar_assistant").toString();

        File f = new File(strDirectory, "bar_assistant_test.jpg");
        try {
            fOut = new FileOutputStream(f);

            /**Compress image**/
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();

            /**Update image to gallery**/
            MediaStore.Images.Media.insertImage(getContentResolver(), f.getAbsolutePath(), f.getName(), f.getName());
            Toast.makeText(getApplicationContext(), "Image saved...", Toast.LENGTH_SHORT).show();
            System.out.println("image saved to: " + strDirectory);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            if (exerciseName.getText().toString().length() < 3) {
                Toast.makeText(this, R.string.toastInvalidName, Toast.LENGTH_LONG).show();
            } else {
                Exercise exercise = null;
                exercise = datasource.createExercise(exerciseName.getText().toString(), exerciseDescription.getText().toString());
                finish();
            }
        }
        return true;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
        System.out.println(file.getAbsolutePath());
        if (!file.mkdirs()) {
            //Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }

    // Get reference to the ImageService
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ImageService.LocalBinder binder = (ImageService.LocalBinder) service;
            imageService = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

}
