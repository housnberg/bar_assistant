package community.barassistant.barassistant.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Binder;

public class TimerService extends Service {

    public static final long NOTIFY_INTERVAL = 1000; // 1 second
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    private final IBinder BINDER = new LocalBinder();
    private long millis = 0;
    private long startTime = 0;

    public class LocalBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return BINDER;
    }


    public void startTimer(){
        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
            this.startTime = System.currentTimeMillis();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    @Override
    public void onDestroy() {
        if (mTimer != null) {
            mTimer = null;
        }
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    System.out.println(getTime());
                }

            });
        }

        public String getTime() {
            millis = System.currentTimeMillis() - startTime;
            return (new SimpleDateFormat("mm:ss")).format(new Date(millis));
        }

    }
}