package community.barassistant.barassistant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by EL on 01.06.2016.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_EXERCISES = "exercises";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EXERCISE_NAME = "exercise_name";
    public static final String COLUMN_EXERCISE_DESCRIPTION = "exercise_description";

    private static final String DATABASE_NAME = "commments.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + MySQLiteHelper.TABLE_EXERCISES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + MySQLiteHelper.COLUMN_EXERCISE_NAME
            + " text not null, " + MySQLiteHelper.COLUMN_EXERCISE_DESCRIPTION
            + " text not null);";


    public MySQLiteHelper(Context context) {
        super(context, MySQLiteHelper.DATABASE_NAME, null, MySQLiteHelper.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + MySQLiteHelper.TABLE_EXERCISES);
        onCreate(db);
    }
}
