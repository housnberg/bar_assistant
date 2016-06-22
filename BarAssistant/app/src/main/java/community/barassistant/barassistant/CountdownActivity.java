package community.barassistant.barassistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ivan on 19.06.2016.
 */
public class CountdownActivity extends AppCompatActivity {
    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        Bundle bundle = getIntent().getExtras();
        time = bundle.getInt("time");

    }
}
