package community.barassistant.barassistant.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eugen Ljavin
 */
public class Workout implements Parcelable {

    private long id;
    private String name;
    private String description;
    private int rounds;
    private int pauseExercises;
    private int pauseRounds;

    private List<Exercise> exercises;

    public Workout() {
        exercises = new ArrayList<Exercise>();
    }

    public Workout(long id, String name, String description, int rounds, int pauseExercises, int pauseRounds) {
        this();
        setId(id);
        setName(name);
        setDescription(description);
        setRounds(rounds);
        setPauseExercises(pauseExercises);
        setPauseRounds(pauseRounds);
    }

    private Workout(Parcel in){
        this();
        setId(in.readLong());
        setName(in.readString());
        setDescription(in.readString());
        setRounds(in.readInt());
        setPauseExercises(in.readInt());
        setPauseRounds(in.readInt());
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

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public int getPauseExercises() {
        return pauseExercises;
    }

    public void setPauseExercises(int pauseExercises) {
        this.pauseExercises = pauseExercises;
    }

    public int getPauseRounds() {
        return pauseRounds;
    }

    public void setPauseRounds(int pauseRounds) {
        this.pauseRounds = pauseRounds;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public boolean addExercise(Exercise exercise) {
        boolean isExerciseAdded = false;
        if (exercise != null) {
            exercises.add(exercise);
            isExerciseAdded = true;
        }
        return isExerciseAdded;
    }

    @Override
    public boolean equals(Object o) {
        boolean equals = false;

        if (o instanceof Workout) {
            Workout other = (Workout) o;
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
        dest.writeInt(rounds);
        dest.writeInt(pauseExercises);
        dest.writeInt(pauseRounds);
    }

    public static final Parcelable.Creator<Workout> CREATOR =
            new Parcelable.Creator<Workout>(){

                @Override
                public Workout createFromParcel(Parcel source) {
                    return new Workout(source);
                }

                @Override
                public Workout[] newArray(int size) {
                    return new Workout[size];
                }
            };
}
