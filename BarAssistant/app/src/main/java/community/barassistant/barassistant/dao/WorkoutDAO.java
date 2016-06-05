package community.barassistant.barassistant.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import community.barassistant.barassistant.MySQLiteHelper;
import community.barassistant.barassistant.model.Workout;

/**
 * @author Eugen Ljavin
 */
public class WorkoutDAO {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {
            MySQLiteHelper.COLUMN_WORKOUT_ID,
            MySQLiteHelper.COLUMN_WORKOUT_NAME,
            MySQLiteHelper.COLUMN_WORKOUT_DESCRIPTION,
            MySQLiteHelper.COLUMN_WORKOUT_ROUNDS,
            MySQLiteHelper.COLUMN_WORKOUT_PAUSE_EXERCISES,
            MySQLiteHelper.COLUMN_WORKOUT_PAUSE_ROUNDS
    };

    public WorkoutDAO(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Workout createExercise(String exerciseName, String exerciseDescription, int rounds, int pauseExercises, int pauseRounds) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_WORKOUT_NAME, exerciseName);
        values.put(MySQLiteHelper.COLUMN_WORKOUT_DESCRIPTION, exerciseDescription);
        values.put(MySQLiteHelper.COLUMN_WORKOUT_ROUNDS, rounds);
        values.put(MySQLiteHelper.COLUMN_WORKOUT_PAUSE_EXERCISES, pauseExercises);
        values.put(MySQLiteHelper.COLUMN_WORKOUT_PAUSE_ROUNDS, pauseRounds);
        long insertId = database.insert(MySQLiteHelper.TABLE_WORKOUT, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_WORKOUT, allColumns, MySQLiteHelper.COLUMN_WORKOUT_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Workout newWorkout = cursorToWorkout(cursor);
        cursor.close();
        return newWorkout;
    }

    public void deleteWorkout(Workout workout) {
        long id = workout.getId();
        database.delete(MySQLiteHelper.TABLE_WORKOUT, MySQLiteHelper.COLUMN_WORKOUT_ID + " = " + id, null);
    }

    public List<Workout> getAllWorkouts() {
        List<Workout> workouts = new ArrayList<Workout>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_WORKOUT,
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

    //TODO: Rewrite this as database statement to improve performance
    public Workout getLastAddedWorkout() {
        return getAllWorkouts().get(getAllWorkouts().size() - 1);
    }

    private Workout cursorToWorkout(Cursor cursor) {
        return new Workout(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5));
    }


}
