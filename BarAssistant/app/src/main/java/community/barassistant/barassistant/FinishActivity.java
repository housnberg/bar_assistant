package community.barassistant.barassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ivan on 24.06.2016.
 */
public class FinishActivity extends AppCompatActivity {
    private TextView textView;
    private long time;

    @Override
    protected void onCreate(Bundle savedBundleInstance){
        super.onCreate(savedBundleInstance);
        setContentView(R.layout.activity_finish);

        textView = (TextView) findViewById(R.id.finishViewTime);
        Bundle bundle  = getIntent().getExtras();
        time = bundle.getLong("workoutTime");

        String timeFormatted = (new SimpleDateFormat("mm:ss")).format(new Date(time));
        textView.append(timeFormatted);
    }

    public void onClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
