package com.dkey.observe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	private static final String TAG = MySQLiteHelper.class.getSimpleName();
	private static final String DB_NAME = "observenotes.db";
	private static final int DB_VERSION = 1;
	public static final String TABLE_OBS = "observations";
	public static final String COLUMN_ID = "_id";
	public static final String common_name = "common_name";
	public static final String family = "family";
	public static final String species = "species";
	public static final String genus = "genus";
	public static final String comments = "comments";
	public static final String variety = "variety";
	public static final String lat = "lat";
	public static final String longi = "longi";
	public static final String moreFields1 = "moreFields1";
	public static final String moreFields2 = "moreFields2";
	public static final String moreFields3 = "moreFields3";

	private static final String DATABASE_CREATE = "create table " + TABLE_OBS
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ common_name + " text, " + family + " text, " + species
			+ " text, " + genus + " text, " + comments + " text, " + variety
			+ " text, " + lat + " text, " + longi + " text, " + moreFields1
			+ " text, " + moreFields2 + " text, " + moreFields3 + " text);";

	public MySQLiteHelper(Context context) {

		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		db.execSQL(DATABASE_CREATE);

		Log.d(TAG, "Database sql on Create" + DATABASE_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		Log.d(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBS);
		this.onCreate(db);

	}

}
