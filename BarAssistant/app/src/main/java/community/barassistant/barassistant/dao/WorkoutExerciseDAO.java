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
public class WorkoutExerciseDAO {

    //TODO: FIND A BETTER PLACE TO PLACE THE STATEMENTS
    //TODO: IMPLEMENT A GENERIC SQL STATEMENT GENERATOR
    private static final String SELECT_EXERCISE_BY_WORKOUT_ID =
            "SELECT * FROM " + CustomSQLiteHelper.TABLE_EXERCISE + " e "
                    + " LEFT JOIN " + CustomSQLiteHelper.TABLE_WORKOUT_EXERCISE + " we ON we." + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_EXERCISE_ID + " = e." + CustomSQLiteHelper.COLUMN_EXERCISE_ID
                    + " WHERE we." + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_WORKOUT_ID + " = ?;";

    private static final String SELECT_WORKOUT_BY_EXERCISE_ID =
            "SELECT * FROM " + CustomSQLiteHelper.TABLE_WORKOUT + " w "
                    + " LEFT JOIN " + CustomSQLiteHelper.TABLE_WORKOUT_EXERCISE + " we ON we." + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_WORKOUT_ID + " = w." + CustomSQLiteHelper.COLUMN_WORKOUT_ID
                    + " WHERE we." + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_EXERCISE_ID + " = ?;";

    private static final String SELECT_REPETITION_BY_PK =
            "SELECT we." + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_REPETITIONS + " FROM " + CustomSQLiteHelper.TABLE_WORKOUT_EXERCISE + " w "
                    + " WHERE we." + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_WORKOUT_ID + " = ? "
                    + " AND we." + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_EXERCISE_ID + " = ?;";

    private SQLiteDatabase database;
    private CustomSQLiteHelper dbHelper;
    private String[] allColumns = {
            CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_EXERCISE_ID,
            CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_WORKOUT_ID,
            CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_REPETITIONS
    };

    public WorkoutExerciseDAO(Context context) {
        dbHelper = new CustomSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long createWorkoutExercise(long workoutId, long exerciseId, int repetitions) {
        ContentValues values = new ContentValues();
        values.put(CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_WORKOUT_ID, workoutId);
        values.put(CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_EXERCISE_ID, exerciseId);
        values.put(CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_REPETITIONS, repetitions);
        return database.insert(CustomSQLiteHelper.TABLE_WORKOUT_EXERCISE, null, values);
    }

    public List<Exercise> getExercisesByWorkoutId(long wokoutId) {
        List<Exercise> exercises = new ArrayList<Exercise>();

        Cursor cursor = database.rawQuery(WorkoutExerciseDAO.SELECT_EXERCISE_BY_WORKOUT_ID, new String[] {String.valueOf(wokoutId)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Exercise exercise = cursorToExercise(cursor);
            exercises.add(exercise);
            cursor.moveToNext();
        }
        cursor.close();

        return new ArrayList<Exercise>();
    }

    //TODO: IMPLEMENT THIS METHOD WHEN YOU NEED IT
    public List<Workout> getWorkoutsByExerciseId(long exerciseId) {
        List<Workout> workouts = new ArrayList<Workout>();

        Cursor cursor = database.rawQuery(WorkoutExerciseDAO.SELECT_WORKOUT_BY_EXERCISE_ID, new String[] {String.valueOf(exerciseId)});
        cursor.close();

        return workouts;
    }

    public int getRepetitions(long workoutId, long exerciseId) {
        Cursor cursor = database.rawQuery(WorkoutExerciseDAO.SELECT_REPETITION_BY_PK, new String[] {String.valueOf(workoutId), String.valueOf(exerciseId)});
        int repetitions = cursor.getInt(0);
        cursor.close();

        return repetitions;
    }

    private Workout cursorToWorkout(Cursor cursor) {
        return new Workout(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5));
    }

    private Exercise cursorToExercise(Cursor cursor) {
        return new Exercise(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
    }


}
