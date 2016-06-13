package community.barassistant.barassistant.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import community.barassistant.barassistant.model.Exercise;
import community.barassistant.barassistant.model.Workout;

/**
 * @author Eugen Ljavin
 */
public class WorkoutDAO {

    private SQLiteDatabase database;
    private CustomSQLiteHelper dbHelper;
    private String[] allColumns = {
            CustomSQLiteHelper.COLUMN_WORKOUT_ID,
            CustomSQLiteHelper.COLUMN_WORKOUT_NAME,
            CustomSQLiteHelper.COLUMN_WORKOUT_DESCRIPTION,
            CustomSQLiteHelper.COLUMN_WORKOUT_ROUNDS,
            CustomSQLiteHelper.COLUMN_WORKOUT_PAUSE_EXERCISES,
            CustomSQLiteHelper.COLUMN_WORKOUT_PAUSE_ROUNDS
    };

    private static final String SELECT_WORKOUT_BY_ID =
            "SELECT w." + CustomSQLiteHelper.COLUMN_WORKOUT_ID + " FROM " + CustomSQLiteHelper.TABLE_WORKOUT + " w"
                    + " WHERE w." + CustomSQLiteHelper.COLUMN_WORKOUT_ID + " = ?;";

    public WorkoutDAO(Context context) {
        dbHelper = new CustomSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
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
            Cursor cursorWorkout = database.rawQuery(WorkoutDAO.SELECT_WORKOUT_BY_ID, new String[] {String.valueOf(workoutId)});
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


}
