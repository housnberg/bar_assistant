package community.barassistant.barassistant.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by thomasrasp on 07.06.16.
 */
public class TimerService extends Service {
    private final IBinder BINDER = new LocalBinder();
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


    public void countdownTimer(long time){
        new CountDownTimer(time,1000){

            public void onTick(long millisUntilFinished){
                // Ausgabe auf Display
                System.out.println(millisUntilFinished /1000);
            }
            public void onFinish(){
                System.out.println("Ende");

            }
        }.start();

    }
}