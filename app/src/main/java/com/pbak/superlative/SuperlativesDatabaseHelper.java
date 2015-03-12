package com.pbak.superlative;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by alexkao on 3/3/2015.
 */
public class SuperlativesDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static String DB_PATH = "/data/data/com.pbak.superlative/databases/";
    private static String DB_NAME = "superlatives.db";
    private static String DATABASE_NAME = "superlatives.db";
    private static int DATABASE_VERSION = 1;


    private SQLiteDatabase myDataBase;
    private final Context myContext;

    ArrayList<String> questions = new ArrayList<String>();

    public SuperlativesDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        myContext = context;
    }


    public SuperlativesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext = context;
    }


    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {

            Log.w(TAG, "Db exists!");
            Log.w(TAG, "Trying to copy database");
            this.getReadableDatabase();
//            copyDataBase();
            //do nothing - database already exist
        } else {

            Log.w(TAG, "Db does not exists!");
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                Log.w(TAG, "Trying to copy database");
                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

            Log.w(TAG, "Db exists! in checkDataBaseS");
        } catch (SQLiteException e) {

            Log.w(TAG, "Db does not exist yet! in checkDataBaseS");
            //database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        Log.w(TAG, "Trying to get assets");
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);


//        InputStream myInput = myContext.getAssets().open(DB_NAME);

        Log.w(TAG, "Finished getting assets");


        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        Log.w(TAG, "outFileName is: " + outFileName);

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.

}
