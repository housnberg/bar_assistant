package community.barassistant.barassistant.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eugen Ljavin
 */
public class Exercise {

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
}
