package community.barassistant.barassistant;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import community.barassistant.barassistant.services.CountdownTimerService;

/**
 * Created by ivan on 19.06.2016.
 */
public class CountdownActivity extends AppCompatActivity {
    private CountdownTimerService countdownTimerService;
    private boolean isBound;
    private int time;
    private BroadcastReceiver receiverTick;
    private BroadcastReceiver receiverFinish;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        textView = (TextView) findViewById(R.id.countdownTextView);

        Bundle bundle = getIntent().getExtras();
        time = bundle.getInt("time") * 1000;

        Intent intent = new Intent(this, CountdownTimerService.class);
        isBound = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        receiverTick = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String text = textView.getText().toString();
                int currTime = time / 1000;
                if(!text.equals("")){
                    currTime = Integer.valueOf(text);
                }
                currTime--;
                textView.setText(Integer.toString(currTime));
            }
        };

        receiverFinish = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };

    }

    @Override
    public void finish(){
        setResult(1);
        super.finish();
    }

    @Override
    protected void onStart(){
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverTick, new IntentFilter(CountdownTimerService.CTS_TICK));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverFinish, new IntentFilter(CountdownTimerService.CTS_FINISH));
    }

    @Override
    protected void onStop(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverTick);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverFinish);
        if(isBound){
            unbindService(serviceConnection);
        }
        super.onStop();

    }

    public void onClick(View view){
        if(countdownTimerService != null)
            countdownTimerService.cancel();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CountdownTimerService.LocalBinder binder = (CountdownTimerService.LocalBinder) service;
            countdownTimerService = binder.getService();
            countdownTimerService.countdownTimer(time);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
