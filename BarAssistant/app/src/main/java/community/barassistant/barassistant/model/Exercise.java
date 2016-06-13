package community.barassistant.barassistant.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eugen Ljavin
 */
public class Exercise implements Parcelable {

    private long id;
    private String name;
    private String description;
    private List<String> imagePaths;

    public Exercise() {
        imagePaths = new ArrayList<String>();
    }

    public Exercise(long id, String name, String description) {
        this();
        setId(id);
        setName(name);
        setDescription(description);
    }

    private Exercise(Parcel in){
        this();
        setId(in.readLong());
        setName(in.readString());
        setDescription(in.readString());
        setImagePaths(in.createStringArrayList());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public boolean addImagePath(String imagePath) {
        boolean isImagePathAdded = false;
        if (imagePath != null) {
            imagePaths.add(imagePath);
            isImagePathAdded = true;
        }
        return isImagePathAdded;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        boolean equals = false;

        if (o instanceof Exercise) {
            Exercise other = (Exercise) o;
            equals = other.getId() == this.getId();
        }

        return equals;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeStringList(imagePaths);
    }

    public static final Parcelable.Creator<Exercise> CREATOR =
        new Parcelable.Creator<Exercise>(){

            @Override
            public Exercise createFromParcel(Parcel source) {
                return new Exercise(source);
            }

            @Override
            public Exercise[] newArray(int size) {
                return new Exercise[size];
            }
        };

}
