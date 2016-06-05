package community.barassistant.barassistant.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import community.barassistant.barassistant.model.Exercise;

/**
 * @author Eugen Ljavin
 */
public class ExercisesDAO {

    private SQLiteDatabase database;
    private CustomSQLiteHelper dbHelper;
    private String[] allColumns = {
            CustomSQLiteHelper.COLUMN_EXERCISE_ID,
            CustomSQLiteHelper.COLUMN_EXERCISE_NAME,
            CustomSQLiteHelper.COLUMN_EXERCISE_DESCRIPTION
    };

    public ExercisesDAO(Context context) {
        dbHelper = new CustomSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Exercise createExercise(String name, String description) {
        ContentValues values = new ContentValues();
        values.put(CustomSQLiteHelper.COLUMN_EXERCISE_NAME, name);
        values.put(CustomSQLiteHelper.COLUMN_EXERCISE_DESCRIPTION, description);
        long insertId = database.insert(CustomSQLiteHelper.TABLE_EXERCISE, null, values);
        Cursor cursor = database.query(CustomSQLiteHelper.TABLE_EXERCISE, allColumns, CustomSQLiteHelper.COLUMN_EXERCISE_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Exercise newExercise = cursorToExercise(cursor);
        cursor.close();
        return newExercise;
    }

    public void deleteExercise(Exercise exercise) {
        long id = exercise.getId();
        database.delete(CustomSQLiteHelper.TABLE_EXERCISE, CustomSQLiteHelper.COLUMN_EXERCISE_ID + " = " + id, null);
    }

    public List<Exercise> getAllExercises() {
        List<Exercise> exercises = new ArrayList<Exercise>();

        Cursor cursor = database.query(CustomSQLiteHelper.TABLE_EXERCISE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Exercise exercise = cursorToExercise(cursor);
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

    private Exercise cursorToExercise(Cursor cursor) {
        return new Exercise(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
    }
}
