package community.barassistant.barassistant;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
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

import java.util.ArrayList;
import java.util.List;

import community.barassistant.barassistant.model.Exercise;
import community.barassistant.barassistant.dao.ExercisesDAO;
import community.barassistant.barassistant.services.ImageService;
import community.barassistant.barassistant.services.CountdownTimerService;
import community.barassistant.barassistant.services.TimerService;

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
    private ImageService imageService;
    private CountdownTimerService CountdownTimerService;
    private TimerService TimerService;

    private boolean bound = false;
    private boolean boundCountdownTimer = false;
    private boolean boundTimer = false;

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
            //CountdownTimerService.countdownTimer(30000);
            TimerService.startTimer();
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
    protected void onStart(){
        super.onStart();
        // Bind ImageService
        Intent intent = new Intent(this, ImageService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        Intent intentCountdownTimer = new Intent(this, CountdownTimerService.class);
        bindService(intentCountdownTimer, connectionCountdownTimer, Context.BIND_AUTO_CREATE);

        Intent intentTimer = new Intent(this, TimerService.class);
        bindService(intentTimer,connectionTimer, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop(){
        super.onStop();
        //Unbind Service. Service gets destroyed when not bound by any activity.
        if(bound){
            unbindService(connection);
            bound = false;
        }
        if (boundCountdownTimer) {
            unbindService(connectionCountdownTimer);
            boundCountdownTimer = false;
        }
        if(boundTimer){
            unbindService(connectionTimer);
            boundTimer = false;
        }
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

    // Get reference to the CountdownTimerService
    private ServiceConnection connectionCountdownTimer = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CountdownTimerService.LocalBinder countdowntimebinder = (CountdownTimerService.LocalBinder) service;
            CountdownTimerService = countdowntimebinder.getService();
            boundCountdownTimer = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            boundCountdownTimer = false;
        }
    };

    // Get reference to the TimerService
    private ServiceConnection connectionTimer = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TimerService.LocalBinder timerbinder = (TimerService.LocalBinder) service;
            TimerService = timerbinder.getService();
            boundTimer = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            boundTimer = false;
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            ImageView iv = (ImageView) findViewById(R.id.imageView);
            imageUrls.add(imageService.saveImageToStorage(photo));
            iv.setImageBitmap(imageService.loadImageFromStorage(imageUrls.get(imageUrls.size() - 1)));
        }
    }
}