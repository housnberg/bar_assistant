package community.barassistant.barassistant.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import community.barassistant.barassistant.model.Exercise;
import community.barassistant.barassistant.model.Image;
import community.barassistant.barassistant.model.Workout;

/**
 * Created by EL on 11.06.2016.
 */

//TODO: USE http://ormlite.com/ TO PERSIST DATA
public class DataAccessObject {

    private SQLiteDatabase database;
    private CustomSQLiteHelper dbHelper;
    private String[] allExerciseColumns = {
            CustomSQLiteHelper.COLUMN_EXERCISE_ID,
            CustomSQLiteHelper.COLUMN_EXERCISE_NAME,
            CustomSQLiteHelper.COLUMN_EXERCISE_DESCRIPTION
    };
    private String[] allImagePathColumns = {
            CustomSQLiteHelper.COLUMN_IMAGE_PATH_ID,
            CustomSQLiteHelper.COLUMN_IMAGE_PATH_EXERCISE_ID,
            CustomSQLiteHelper.COLUMN_IMAGE_PATH_DESCRIPTION,
            CustomSQLiteHelper.COLUMN_IMAGE_PATH_ORDER
    };
    private String[] allColumns = {
            CustomSQLiteHelper.COLUMN_WORKOUT_ID,
            CustomSQLiteHelper.COLUMN_WORKOUT_NAME,
            CustomSQLiteHelper.COLUMN_WORKOUT_DESCRIPTION,
            CustomSQLiteHelper.COLUMN_WORKOUT_ROUNDS,
            CustomSQLiteHelper.COLUMN_WORKOUT_PAUSE_EXERCISES,
            CustomSQLiteHelper.COLUMN_WORKOUT_PAUSE_ROUNDS
    };

    //TODO: FIND A BETTER PLACE TO PLACE THE STATEMENTS
    //TODO: IMPLEMENT A GENERIC SQL STATEMENT GENERATOR
    //TODO: REFACVTOR THIS CLASS -> ExerciseDAO, WorkoutDAO, etc.
    private static final String SELECT_IMAGE_PATH_BY_EXERCISE_ID =
            "SELECT * FROM " + CustomSQLiteHelper.TABLE_IMAGE_PATH + " ip"
                    + " WHERE ip." + CustomSQLiteHelper.COLUMN_IMAGE_PATH_EXERCISE_ID + " = ?"
                    + " ORDER BY ip." + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_ORDER + ";";

    private static final String SELECT_EXERCISE_BY_ID =
            "SELECT e." + CustomSQLiteHelper.COLUMN_EXERCISE_ID + " FROM " + CustomSQLiteHelper.TABLE_EXERCISE + " e"
                    + " WHERE e." + CustomSQLiteHelper.COLUMN_EXERCISE_ID + " = ?;";

    private static final String SELECT_EXERCISE_BY_WORKOUT_ID =
            "SELECT * FROM " + CustomSQLiteHelper.TABLE_EXERCISE + " e "
                    + " LEFT JOIN " + CustomSQLiteHelper.TABLE_WORKOUT_EXERCISE + " we ON we." + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_EXERCISE_ID + " = e." + CustomSQLiteHelper.COLUMN_EXERCISE_ID
                    + " WHERE we." + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_WORKOUT_ID + " = ?"
                    + " ORDER BY we." + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_ORDER + ";";

    private static final String SELECT_WORKOUT_BY_EXERCISE_ID =
            "SELECT * FROM " + CustomSQLiteHelper.TABLE_WORKOUT + " w "
                    + " LEFT JOIN " + CustomSQLiteHelper.TABLE_WORKOUT_EXERCISE + " we ON we." + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_WORKOUT_ID + " = w." + CustomSQLiteHelper.COLUMN_WORKOUT_ID
                    + " WHERE we." + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_EXERCISE_ID + " = ?;";

    private static final String SELECT_REPETITION_BY_PK =
            "SELECT we." + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_REPETITIONS + " FROM " + CustomSQLiteHelper.TABLE_WORKOUT_EXERCISE + " we "
                    + " WHERE we." + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_WORKOUT_ID + " = ? "
                    + " AND we." + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_EXERCISE_ID + " = ?;";

    private static final String SELECT_IS_REPEATABLE_BY_PK =
            "SELECT we." + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_IS_REPEATABLE + " FROM " + CustomSQLiteHelper.TABLE_WORKOUT_EXERCISE + " we "
                    + " WHERE we." + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_WORKOUT_ID + " = ? "
                    + " AND we." + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_EXERCISE_ID + " = ?;";

    private static final String SELECT_WORKOUT_BY_ID =
            "SELECT w." + CustomSQLiteHelper.COLUMN_WORKOUT_ID + " FROM " + CustomSQLiteHelper.TABLE_WORKOUT + " w"
                    + " WHERE w." + CustomSQLiteHelper.COLUMN_WORKOUT_ID + " = ?;";

    public DataAccessObject(Context context) {
        dbHelper = new CustomSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Exercise createExercise(String name, String description, List<Image> imagePaths) {
        ContentValues values = new ContentValues();
        values.put(CustomSQLiteHelper.COLUMN_EXERCISE_NAME, name);
        values.put(CustomSQLiteHelper.COLUMN_EXERCISE_DESCRIPTION, description);
        long insertId = database.insert(CustomSQLiteHelper.TABLE_EXERCISE, null, values);
        Cursor cursor = database.query(CustomSQLiteHelper.TABLE_EXERCISE, allExerciseColumns, CustomSQLiteHelper.COLUMN_EXERCISE_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Exercise newExercise = cursorToExercise(cursor);
        newExercise.setImagePaths(imagePaths);
        for (Image imagePath : imagePaths) {
            createImage(imagePath.getImagePath(), imagePath.getDescription(), newExercise.getId(), imagePath.getOrder());
        }
        cursor.close();
        return newExercise;
    }

    public void createImage(String imagePath, String descritpion, long exerciseId, int order) {
        ContentValues values = new ContentValues();
        values.put(CustomSQLiteHelper.COLUMN_IMAGE_PATH_ID, imagePath);
        values.put(CustomSQLiteHelper.COLUMN_IMAGE_PATH_DESCRIPTION, descritpion);
        values.put(CustomSQLiteHelper.COLUMN_IMAGE_PATH_EXERCISE_ID, exerciseId);
        values.put(CustomSQLiteHelper.COLUMN_IMAGE_PATH_ORDER, order);
        long insertId = database.insert(CustomSQLiteHelper.TABLE_IMAGE_PATH, null, values);
    }

    public List<Image> getImagePathByExerciseId(long exerciseId) {
        List<Image> imagePaths = new ArrayList<Image>();
        try {
            Cursor cursor = database.rawQuery(DataAccessObject.SELECT_IMAGE_PATH_BY_EXERCISE_ID, new String[] {String.valueOf(exerciseId)});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                imagePaths.add(cursorToImage(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {

        }
        return imagePaths;
    }

    public void deleteExercise(Exercise exercise) {
        long id = exercise.getId();
        database.delete(CustomSQLiteHelper.TABLE_EXERCISE, CustomSQLiteHelper.COLUMN_EXERCISE_ID + " = " + id, null);
    }

    public List<Exercise> getAllExercises() {
        List<Exercise> exercises = new ArrayList<Exercise>();
        List<Image> imagePaths = new ArrayList<Image>();

        Cursor cursor = database.query(CustomSQLiteHelper.TABLE_EXERCISE, allExerciseColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Exercise exercise = cursorToExercise(cursor);
            imagePaths = getImagePathByExerciseId(exercise.getId()); {
                for (Image imagePath : imagePaths) {
                    exercise.addImage(imagePath);
                }
            }
            exercises.add(exercise);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return exercises;
    }

    //TODO: Rewrite this as database statement to improve performance
    public Exercise getLastAddedExercise() {
        return getAllExercises().get(getAllExercises().size() - 1);
    }

    public Exercise getExerciseById(long exerciseId) {
        Exercise exercise = null;
        List<Image> imagePaths;
        try {
            Cursor cursor = database.rawQuery(DataAccessObject.SELECT_EXERCISE_BY_ID, new String[] {String.valueOf(exerciseId)});
            cursor.moveToFirst();
            imagePaths = getImagePathByExerciseId(exerciseId);
            exercise.setImagePaths(imagePaths);
            exercise = cursorToExercise(cursor);
            cursor.close();
        } catch (Exception e) {

        }
        return exercise;
    }

    private Exercise cursorToExercise(Cursor cursor) {
        return new Exercise(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
    }

    public long createWorkoutExercise(long workoutId, long exerciseId, int repetitions, boolean isRepeatable, int order) {
        ContentValues values = new ContentValues();
        values.put(CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_WORKOUT_ID, workoutId);
        values.put(CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_EXERCISE_ID, exerciseId);
        values.put(CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_REPETITIONS, repetitions);

        if (isRepeatable) {
            values.put(CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_IS_REPEATABLE, 1);
        } else {
            values.put(CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_IS_REPEATABLE, 0);
        }

        values.put(CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_ORDER, order);

        return database.insert(CustomSQLiteHelper.TABLE_WORKOUT_EXERCISE, null, values);
    }

    public List<Exercise> getExercisesByWorkoutId(long wokoutId) {
        List<Exercise> exercises = new ArrayList<Exercise>();
        List<Image> imagePaths;
        Cursor cursor = database.rawQuery(DataAccessObject.SELECT_EXERCISE_BY_WORKOUT_ID, new String[] {String.valueOf(wokoutId)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Exercise exercise = cursorToExercise(cursor);
            imagePaths = getImagePathByExerciseId(exercise.getId());
            exercise.setImagePaths(imagePaths);
            exercises.add(exercise);
            cursor.moveToNext();
        }
        cursor.close();

        return exercises;
    }

    //TODO: IMPLEMENT THIS METHOD WHEN YOU NEED IT
    public List<Workout> getWorkoutsByExerciseId(long exerciseId) {
        List<Workout> workouts = new ArrayList<Workout>();

        Cursor cursor = database.rawQuery(DataAccessObject.SELECT_WORKOUT_BY_EXERCISE_ID, new String[] {String.valueOf(exerciseId)});
        cursor.close();

        return workouts;
    }

    public int getRepetitions(long workoutId, long exerciseId) {
        Cursor cursor = database.rawQuery(DataAccessObject.SELECT_REPETITION_BY_PK, new String[] {String.valueOf(workoutId), String.valueOf(exerciseId)});
        cursor.moveToFirst();
        int repetitions = cursor.getInt(0);
        cursor.close();

        return repetitions;
    }

    public boolean getIsRepeatable(long workoutId, long exerciseId) {
        Cursor cursor = database.rawQuery(DataAccessObject.SELECT_IS_REPEATABLE_BY_PK, new String[] {String.valueOf(workoutId), String.valueOf(exerciseId)});
        cursor.moveToFirst();
        int repetitions = cursor.getInt(0);
        cursor.close();

        return repetitions != 0;
    }

    public Workout createWorkout(String exerciseName, String exerciseDescription, List<Exercise> exercises, List<Integer> repetitions, int rounds, int pauseExercises, int pauseRounds) {
        ContentValues values = new ContentValues();
        values.put(CustomSQLiteHelper.COLUMN_WORKOUT_NAME, exerciseName);
        values.put(CustomSQLiteHelper.COLUMN_WORKOUT_DESCRIPTION, exerciseDescription);
        values.put(CustomSQLiteHelper.COLUMN_WORKOUT_ROUNDS, rounds);
        values.put(CustomSQLiteHelper.COLUMN_WORKOUT_PAUSE_EXERCISES, pauseExercises);
        values.put(CustomSQLiteHelper.COLUMN_WORKOUT_PAUSE_ROUNDS, pauseRounds);
        long insertId = database.insert(CustomSQLiteHelper.TABLE_WORKOUT, null, values);
        Cursor cursor = database.query(CustomSQLiteHelper.TABLE_WORKOUT, allColumns, CustomSQLiteHelper.COLUMN_WORKOUT_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Workout newWorkout = cursorToWorkout(cursor);
        cursor.close();
        return newWorkout;
    }

    public void deleteWorkout(Workout workout) {
        long id = workout.getId();
        database.delete(CustomSQLiteHelper.TABLE_WORKOUT, CustomSQLiteHelper.COLUMN_WORKOUT_ID + " = " + id, null);
    }

    public List<Workout> getAllWorkouts() {
        List<Workout> workouts = new ArrayList<Workout>();

        Cursor cursor = database.query(CustomSQLiteHelper.TABLE_WORKOUT,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Workout workout = cursorToWorkout(cursor);
            workouts.add(workout);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return workouts;
    }

    public Workout getWorkoutById(long workoutId) {
        Workout workout = null;
        try {
            Cursor cursorWorkout = database.rawQuery(DataAccessObject.SELECT_WORKOUT_BY_ID, new String[] {String.valueOf(workoutId)});
            cursorWorkout.moveToFirst();
            workout = cursorToWorkout(cursorWorkout);
            cursorWorkout.close();
        } catch (Exception e) {

        }
        return workout;
    }

    //TODO: Rewrite this as database statement to improve performance
    public Workout getLastAddedWorkout() {
        return getAllWorkouts().get(getAllWorkouts().size() - 1);
    }

    private Workout cursorToWorkout(Cursor cursor) {
        return new Workout(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5));
    }

    private Image cursorToImage(Cursor cursor) {
        return new Image(cursor.getString(0), cursor.getInt(2), cursor.getString(1));
    }

}
