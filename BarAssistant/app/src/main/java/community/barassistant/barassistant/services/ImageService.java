package community.barassistant.barassistant.services;

import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;

/**
 * Created by ivan on 03.06.2016.
 */
public class ImageService extends Service {
    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder{
        public ImageService getService() {
            return ImageService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public String saveImageToStorage(Bitmap bm){
        String path = null;
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File dir = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(dir, genRandomName());
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(mypath);
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fos);
            fos.close();
            Toast.makeText(getApplicationContext(), "Image saved", Toast.LENGTH_SHORT).show();
            path = mypath.getAbsolutePath();
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Sorry, something went wrong when saving the image", Toast.LENGTH_SHORT).show();
        }
        return path;
    }

    public Bitmap loadImageFromStorage(String path){
        Bitmap bm = null;
        try{
            File f = new File(path);
            bm = BitmapFactory.decodeStream(new FileInputStream(f));
        }catch(Exception e){
            e.printStackTrace();
        }
        return bm;
    }

    //10000 possible names should provide a high enough chance for the name to be unique
    private String genRandomName(){
        return "bar"+new Random().nextInt(10000) + ".jpg";
    }
}
