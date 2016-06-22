package community.barassistant.barassistant.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class CountdownTimerService extends Service {

    private long startTime = 0;

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
        new CountDownTimer(time,1000){

            public void onTick(long millisUntilFinished){
                System.out.println(millisUntilFinished /1000);
            }
            public void onFinish(){
                System.out.println("Ende");

            }
        }.start();

    }

    public void timer(){
        // Timer code here
    }

    public void mainTimer(){

    }

}