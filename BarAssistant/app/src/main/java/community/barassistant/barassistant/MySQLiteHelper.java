package community.barassistant.barassistant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Eugen Ljavin
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_EXERCISE = "exercise";
    public static final String COLUMN_EXERCISE_ID = "_id";
    public static final String COLUMN_EXERCISE_NAME = "name";
    public static final String COLUMN_EXERCISE_DESCRIPTION = "description";

    public static final String TABLE_WORKOUT = "workout";
    public static final String COLUMN_WORKOUT_ID = "_id";
    public static final String COLUMN_WORKOUT_NAME = "name";
    public static final String COLUMN_WORKOUT_DESCRIPTION = "description";
    public static final String COLUMN_WORKOUT_ROUNDS = "rounds";
    public static final String COLUMN_WORKOUT_PAUSE_EXERCISES = "pause_exercises";
    public static final String COLUMN_WORKOUT_PAUSE_ROUNDS = "pause_rounds";

    private static final String DATABASE_NAME = "barassistant.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    //TODO: Make a generic SQL Statement Generator
    private static final String DATABASE_CREATE_EXERCISE = "create table "
            + MySQLiteHelper.TABLE_EXERCISE
            + "("
            + MySQLiteHelper.COLUMN_EXERCISE_ID + " integer primary key autoincrement, "
            + MySQLiteHelper.COLUMN_EXERCISE_NAME + " text not null, "
            + MySQLiteHelper.COLUMN_EXERCISE_DESCRIPTION + " text not null "
            + ");";

    private static final String DATABASE_CREATE_WORKOUT = "create table "
            + MySQLiteHelper.TABLE_WORKOUT
            + "("
            + MySQLiteHelper.COLUMN_WORKOUT_ID + " integer primary key autoincrement, "
            + MySQLiteHelper.COLUMN_WORKOUT_NAME + " text not null, "
            + MySQLiteHelper.COLUMN_WORKOUT_DESCRIPTION + " text not null, "
            + MySQLiteHelper.COLUMN_WORKOUT_ROUNDS + " integer not null, "
            + MySQLiteHelper.COLUMN_WORKOUT_PAUSE_EXERCISES + " integer not null, "
            + MySQLiteHelper.COLUMN_WORKOUT_PAUSE_ROUNDS + " integer not null "
            + ");";


    public MySQLiteHelper(Context context) {
        super(context, MySQLiteHelper.DATABASE_NAME, null, MySQLiteHelper.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(MySQLiteHelper.DATABASE_CREATE_EXERCISE);
        database.execSQL(MySQLiteHelper.DATABASE_CREATE_WORKOUT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + MySQLiteHelper.TABLE_EXERCISE);
        db.execSQL("DROP TABLE IF EXISTS " + MySQLiteHelper.TABLE_WORKOUT);
        onCreate(db);
    }
}
