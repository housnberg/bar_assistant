package community.barassistant.barassistant;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;

/**
 * Created by Johann Andrejtschik on 07.06.2016.
 */
public class ImageLoaderSingleton {

    public static ImageLoaderSingleton instance;

    private ImageLoaderSingleton(){}

    public static ImageLoaderSingleton getInstance(){
        if(instance == null){
            instance = new ImageLoaderSingleton();
        }
        return instance;
    }

    public String saveImageToStorage(Bitmap bm, ContextWrapper cw){
        String path = null;
        File dir = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(dir, genRandomName());
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(mypath);
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fos);
            fos.close();
            path = mypath.getAbsolutePath();
        }catch(Exception e){
            e.printStackTrace();
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
