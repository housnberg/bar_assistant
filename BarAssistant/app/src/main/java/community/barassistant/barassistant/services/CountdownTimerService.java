package community.barassistant.barassistant.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

public class CountdownTimerService extends Service {
    public static final String CTS_TICK = "TICK";
    public static final String CTS_FINISH = "FINISH";

    private CountDownTimer countDownTimer;

    private long startTime = 0;
    private LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);

    private final IBinder BINDER = new LocalBinder();
    public class LocalBinder extends Binder {
        public CountdownTimerService getService() {
            return CountdownTimerService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return BINDER;
    }

    public void countdownTimer(long time){
        countDownTimer = new CountDownTimer(time,1000){

            public void onTick(long millisUntilFinished){
                Intent intent = new Intent(CTS_TICK);
                broadcastManager.sendBroadcast(intent);
            }
            public void onFinish(){
                Intent intent = new Intent(CTS_FINISH);
                broadcastManager.sendBroadcast(intent);

            }
        }.start();

    }

    public void cancel(){
        countDownTimer.cancel();
        countDownTimer.onFinish();
    }

    public void timer(){
        // Timer code here
    }

    public void mainTimer(){

    }

}