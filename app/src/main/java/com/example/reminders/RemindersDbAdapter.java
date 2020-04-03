8package com.example.reminders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class RemindersDbAdapter {

    //these are the column names
    public static final String COL_ID = "_id";
    public static final String COL_CONTENT = "content";
    public static final String COL_IMPORTANT = "important";
    //these are the corresponding indices
    public static final int INDEX_ID = 0;
    public static final int INDEX_CONTENT = INDEX_ID + 1;
    public static final int INDEX_IMPORTANT = INDEX_ID + 2;
    //used for logging
    private static final String TAG = "RemindersDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_NAME = "db_reminders";
    private static final String TABLE_NAME = "tb_reminders";
    private static final int DATABASE_VERSION = 1;
    private final Context mCtx;
    //SQL statement used to create the database
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                    COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_CONTENT + " TEXT, " +
                    COL_IMPORTANT + " INTEGER );";


    public RemindersDbAdapter(Context ctx) {	this.mCtx = ctx;	}

    //open
    public void open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
    }

    //close
    public void close() {	if (mDbHelper != null)	mDbHelper.close();	}


    //take the name as the content of the reminder and boolean important. the id will be created for you automatically
    public void createReminder(String name, boolean important) {
	ContentValues contentValue = new ContentValues();
        contentValue.put(COL_CONTENT, name);
        contentValue.put(COL_IMPORTANT, important);
        mDb.insert(TABLE_NAME, null, contentValue);
    }

    //overloaded to take a reminder
    public long createReminder(Reminder reminder) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(COL_CONTENT, reminder.getContent());
        contentValue.put(COL_IMPORTANT, reminder.getImportant());
        mDb.insert(TABLE_NAME, null, contentValue);
    }

    //get a certain reminder given its id
    public Reminder fetchReminderById(int id) {
	String mContent;	int mImportant;		Cursor c;
    	try
    	{
        	c = mDb.rawQuery("SELECT * from " + TABLE_NAME + " where " + COL_ID + " = " + id, null);
		c.moveToFirst();
		mContent = c.getString(c.getColumnIndex(COL_CONTENT));
		mImportant = c.getInt(c.getColumnIndex(COL_IMPORTANT));
		c.close();
	}
    	catch(Exception e)
        	e.printStackTrace();

	return new Reminder(id,mContent,mImportant);
    }

    //Get all reminders
    public Cursor fetchAllReminders() {
	Cursor c;
	c = mDb.rawQuery("SELECT * from " + TABLE_NAME, null);
	c.moveToFirst();
	return c;
    }

    //Update a certain reminder
    public void updateReminder(Reminder reminder) {
	ContentValues cv = new ContentValues();
	cv.put(COL_ID,reminder.getId());
	cv.put(COL_CONTENT,reminder.getContent());
	cv.put(COL_IMPORTANT,reminder.getImportant());
	mDb.update(TABLE_NAME, cv, COL_ID + "=" + reminder.getId(), null);
    }
    //Delete a certain reminder given its id
    public void deleteReminderById(int nId) 	{	mDb.delete(TABLE_NAME, COL_ID + "=" + nId, null)	}

    //Delete all reminders
    public void deleteAllReminders() 		{	mDb.execSQL("delete * from "+ TABLE_NAME);		}


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) 	{	super(context, DATABASE_NAME, null, DATABASE_VERSION);	}

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }


}
