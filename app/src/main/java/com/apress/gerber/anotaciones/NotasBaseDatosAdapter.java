package com.apress.gerber.anotaciones;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Blob;


public class NotasBaseDatosAdapter {

    //Nombres de la columna de la base de datos

    public static final String COL_ID = "_id";
    public static final String COL_CONTENT = "content";
    public static final String COL_IMPORTANT = "important";
    //Correspondientes indices

    public static final int INDEX_ID = 0;
    public static final int INDEX_CONTENT = INDEX_ID + 1;
    public static final int INDEX_IMPORTANT = INDEX_ID + 2;

    //Utilizado para el registro
    private static final String TAG = "NotasBaseDatosAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_NAME = "DATA_BASE_ANOTACIONES";
    private static final String TABLE_NAME = "TABLA_ANOTACIONES";
    private static final int DATABASE_VERSION = 1;
    private final Context mCtx;

    //Creamos la base de datos SQL
    private static final String DATABASE_CREATE = "CREATE TABLE if not exists "+TABLE_NAME+" ( "+COL_ID+" INTEGER PRIMARY KEY autoincrement, "+COL_CONTENT+" TEXT, "+COL_IMPORTANT +" INTEGER );";


    //Constructor
    public NotasBaseDatosAdapter(Context ctx) {
        this.mCtx = ctx; }



    //-----------------METODOS---------------------------------

    //open
    public void open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
           mDb = mDbHelper.getWritableDatabase();
    }

     //close
    public void close() {
            if (mDbHelper != null)
               mDbHelper.close();
    }


    //CREATE
    public void createReminder(String name, float important) {
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT, name);
        values.put(COL_IMPORTANT, important);
        mDb.insert(TABLE_NAME, null, values);
      }


     //Crear notas igual que el metodo anterior esta sobrecargado
     public long createReminder(Notas notas) {
          ContentValues values = new ContentValues();
          values.put(COL_CONTENT, notas.getContent());
          values.put(COL_IMPORTANT, notas.getImportant());
          return mDb.insert(TABLE_NAME, null, values);
     }

    //READ
    public Notas fetchReminderById(int id) {
          Cursor cursor = mDb.query(TABLE_NAME, new String[]{COL_ID,COL_CONTENT, COL_IMPORTANT}, COL_ID + "=?",new String[]{String.valueOf(id)}, null, null, null, null);
          if (cursor != null)
              cursor.moveToFirst();

            return new Notas(cursor.getInt(INDEX_ID),cursor.getString(INDEX_CONTENT),cursor.getFloat(INDEX_IMPORTANT));
    }


    public Cursor fetchAllReminders() {
        Cursor mCursor = mDb.query(TABLE_NAME, new String[]{COL_ID,COL_CONTENT, COL_IMPORTANT},null, null, null, null, null);
        if (mCursor != null)
            mCursor.moveToFirst();

        return mCursor;
    }


    //UPDATE
    public void updateReminder(Notas notas) {
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT, notas.getContent());
        values.put(COL_IMPORTANT, notas.getImportant());
        mDb.update(TABLE_NAME, values,COL_ID + "=?", new String[]{String.valueOf(notas.getId())});
    }

    //DELETE
    public void deleteReminderById(int nId) {

        mDb.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(nId)});
    }

    public void deleteAllReminders() {
        mDb.delete(TABLE_NAME, null, null);
    }





   //Clase interna que es la que tiene la base de datos

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }


}
