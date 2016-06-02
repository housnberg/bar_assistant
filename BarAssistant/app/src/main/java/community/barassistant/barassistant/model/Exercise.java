package community.barassistant.barassistant.model;

/**
 * Created by EL on 01.06.2016.
 */
public class Exercise {

    private long id;
    private String exerciseName;
    private String exerciseDescription;

    public String getExerciseDescription() {
        return exerciseDescription;
    }

    public void setExerciseDescription(String exerciseDescription) {
        this.exerciseDescription = exerciseDescription;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return exerciseName;
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
