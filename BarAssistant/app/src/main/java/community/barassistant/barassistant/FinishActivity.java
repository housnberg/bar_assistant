package community.barassistant.barassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import community.barassistant.barassistant.model.Workout;
import community.barassistant.barassistant.util.Constants;
import community.barassistant.barassistant.util.Helper;

/**
 * Created by ivan on 24.06.2016.
 */
public class FinishActivity extends AppCompatActivity {

    private TextView textView;
    private long time;
    private View contentWrapper;
    private Toolbar toolbar;
    private Workout workout;

    @Override
    protected void onCreate(Bundle savedBundleInstance){
        super.onCreate(savedBundleInstance);
        setContentView(R.layout.activity_finish);

        textView = (TextView) findViewById(R.id.finishViewTime);
        Bundle bundle  = getIntent().getExtras();
        time = bundle.getLong("workoutTime");
        workout = bundle.getParcelable("data");
        contentWrapper = findViewById(R.id.content_wrapper);
        String timeFormatted = (new SimpleDateFormat("mm:ss")).format(new Date(time));
        textView.append(timeFormatted);

        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(workout.getName());
        setSupportActionBar(toolbar);
    }

    public void onClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Helper.createSnackbar(this, contentWrapper, R.string.snackbarNoBackPressAllowed, Constants.STATUS_INFO).show();
    }
}
