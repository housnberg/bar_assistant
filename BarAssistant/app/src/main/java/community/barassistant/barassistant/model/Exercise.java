package community.barassistant.barassistant.model;

/**
 * @author Eugen Ljavin
 */
public class Exercise {

    private long id;
    private String name;
    private String description;

    public Exercise() {

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
