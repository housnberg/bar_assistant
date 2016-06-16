package community.barassistant.barassistant.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;

/**
 * Created by EL on 14.06.2016.
 */
public class Image implements Parcelable {

    private Exercise exercise;
    private String imagePath;
    private int order;
    private String description;

    public Image() {

    }

    public Image(String imagePath, int order, String description) {
        this();
        this.imagePath = imagePath;
        this.order = order;
        this.description = description;
    }

    public Image(Exercise exercise, String imagePath, int order, String description) {
        this(imagePath, order, description);
        this.exercise = exercise;
    }

    private Image(Parcel in){
        this();
        setExercise((Exercise) in.readParcelable(Exercise.class.getClassLoader()));
        setImagePath(in.readString());
        setOrder(in.readInt());
        setDescription(in.readString());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(exercise, 1);
        dest.writeString(imagePath);
        dest.writeInt(order);
        dest.writeString(description);
    }

    public static final Parcelable.Creator<Image> CREATOR =
            new Parcelable.Creator<Image>(){

                @Override
                public Image createFromParcel(Parcel source) {
                    return new Image(source);
                }

                @Override
                public Image[] newArray(int size) {
                    return new Image[size];
                }
            };
}
