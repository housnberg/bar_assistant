package community.barassistant.barassistant.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Eugen Ljavin
 */
public class CustomSQLiteHelper extends SQLiteOpenHelper {

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

    public static final String TABLE_WORKOUT_EXERCISE = "workout_exercise";
    public static final String COLUMN_WORKOUT_EXERCISE_WORKOUT_ID = "id_workout";
    public static final String COLUMN_WORKOUT_EXERCISE_EXERCISE_ID = "id_exercise";
    public static final String COLUMN_WORKOUT_EXERCISE_REPETITIONS = "repetitions";
    //TODO: DIESE INFO GEHOERT ZUR UEBUNG!!!
    public static final String COLUMN_WORKOUT_EXERCISE_IS_REPEATABLE = "is_repeatable";
    public static final String COLUMN_WORKOUT_EXERCISE_ORDER = "orders";

    public static final String TABLE_IMAGE_PATH = "image_path";
    public static final String COLUMN_IMAGE_PATH_ID = "_path";
    public static final String COLUMN_IMAGE_PATH_EXERCISE_ID = "id_exercise";
    public static final String COLUMN_IMAGE_PATH_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE_PATH_ORDER = "orders";

    private static final String DATABASE_NAME = "barassistant.db";
    private static final int DATABASE_VERSION = 1;

    private Context context;
    private InputStream in;

    // Database creation sql statement
    //TODO: Make a generic SQL Statement Generator
    private static final String CREATE_TABLE_EXERCISE = "create table "
            + CustomSQLiteHelper.TABLE_EXERCISE
            + "("
            + CustomSQLiteHelper.COLUMN_EXERCISE_ID + " integer primary key autoincrement, "
            + CustomSQLiteHelper.COLUMN_EXERCISE_NAME + " text not null, "
            + CustomSQLiteHelper.COLUMN_EXERCISE_DESCRIPTION + " text not null "
            + ");";

    private static final String CREATE_TABLE_WORKOUT = "create table "
            + CustomSQLiteHelper.TABLE_WORKOUT
            + "("
            + CustomSQLiteHelper.COLUMN_WORKOUT_ID + " integer primary key autoincrement, "
            + CustomSQLiteHelper.COLUMN_WORKOUT_NAME + " text not null, "
            + CustomSQLiteHelper.COLUMN_WORKOUT_DESCRIPTION + " text not null, "
            + CustomSQLiteHelper.COLUMN_WORKOUT_ROUNDS + " integer not null, "
            + CustomSQLiteHelper.COLUMN_WORKOUT_PAUSE_EXERCISES + " integer not null, "
            + CustomSQLiteHelper.COLUMN_WORKOUT_PAUSE_ROUNDS + " integer not null "
            + ");";

    private static final String CREATE_TABLE_WORKOUT_EXERCISE = "create table "
            + CustomSQLiteHelper.TABLE_WORKOUT_EXERCISE
            + "("
            + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_WORKOUT_ID + " integer not null references " + CustomSQLiteHelper.TABLE_EXERCISE + "(" + CustomSQLiteHelper.COLUMN_EXERCISE_ID + ") on delete cascade on update cascade, "
            + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_EXERCISE_ID + " integer not null references " + CustomSQLiteHelper.TABLE_WORKOUT + "(" + CustomSQLiteHelper.COLUMN_WORKOUT_ID + ") on delete cascade on update cascade, "
            + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_REPETITIONS + " integer not null default 10, "
            + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_IS_REPEATABLE + " integer not null default 1, "
            + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_ORDER + " integer not null, "
            + "primary key (" + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_WORKOUT_ID + "," + CustomSQLiteHelper.COLUMN_WORKOUT_EXERCISE_EXERCISE_ID + ")"
            + ");";

    private static final String CREATE_TABLE_IMAGE_PATH = "create table "
            + CustomSQLiteHelper.TABLE_IMAGE_PATH
            + "("
            + CustomSQLiteHelper.COLUMN_IMAGE_PATH_ID + " text primary key, "
            + CustomSQLiteHelper.COLUMN_IMAGE_PATH_DESCRIPTION + " text not null, "
            + CustomSQLiteHelper.COLUMN_IMAGE_PATH_ORDER + " integer not null, "
            + CustomSQLiteHelper.COLUMN_IMAGE_PATH_EXERCISE_ID + " integer not null references " + CustomSQLiteHelper.TABLE_EXERCISE + "(" + CustomSQLiteHelper.COLUMN_EXERCISE_ID + ") on delete cascade on update cascade "
            + ");";

    private static final String ENABLE_FOREIGN_KEY_SUPPORT = "PRAGMA foreign_keys = ON;";

    public CustomSQLiteHelper(Context context) {
        super(context, CustomSQLiteHelper.DATABASE_NAME, null, CustomSQLiteHelper.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CustomSQLiteHelper.ENABLE_FOREIGN_KEY_SUPPORT);
        database.execSQL(CustomSQLiteHelper.CREATE_TABLE_EXERCISE);
        database.execSQL(CustomSQLiteHelper.CREATE_TABLE_WORKOUT);
        database.execSQL(CustomSQLiteHelper.CREATE_TABLE_WORKOUT_EXERCISE);
        database.execSQL(CustomSQLiteHelper.CREATE_TABLE_IMAGE_PATH);
        initDatabase(database);
    }

    private void initDatabase(final SQLiteDatabase database) {
        try {
            in = context.getAssets().open("init_database.sql");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            do {
                line = reader.readLine();
                database.execSQL(line);
                Log.e("Insert Statement",  line);
            } while (line != null);

        } catch (Exception e) {

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(CustomSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + CustomSQLiteHelper.TABLE_EXERCISE);
        db.execSQL("DROP TABLE IF EXISTS " + CustomSQLiteHelper.TABLE_WORKOUT);
        db.execSQL("DROP TABLE IF EXISTS " + CustomSQLiteHelper.TABLE_WORKOUT_EXERCISE);
        db.execSQL("DROP TABLE IF EXISTS " + CustomSQLiteHelper.TABLE_IMAGE_PATH);
        onCreate(db);
    }
}
