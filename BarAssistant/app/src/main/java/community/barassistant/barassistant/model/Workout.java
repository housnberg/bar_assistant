package community.barassistant.barassistant.model;

/**
 * @author Eugen Ljavin
 */
public class Workout {

    private long id;
    private String name;
    private String description;
    private int rounds;
    private int pauseExercises;
    private int pauseRounds;

    public Workout() {

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

    @Override
    public boolean equals(Object o) {
        boolean equals = false;

        if (o instanceof Workout) {
            Workout other = (Workout) o;
            equals = other.getId() == this.getId();
        }

        return equals;
    }
}
