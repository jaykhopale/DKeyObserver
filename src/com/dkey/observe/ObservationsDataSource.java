package com.dkey.observe;

import java.util.ArrayList;
import java.util.List;

import com.dkey.DkeyApplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ObservationsDataSource {

	private SQLiteDatabase db;
	private MySQLiteHelper dbHelper;

	/*
	 * private String[] allColumns = { MySQLiteHelper.C_ID,
	 * MySQLiteHelper.comments, MySQLiteHelper.common_name,
	 * MySQLiteHelper.family, MySQLiteHelper.genus, MySQLiteHelper.lat,
	 * MySQLiteHelper.longi, MySQLiteHelper.species, MySQLiteHelper.variety,
	 * MySQLiteHelper.moreFields1, MySQLiteHelper.moreFields2,
	 * MySQLiteHelper.moreFields3
	 * 
	 * };
	 */

	public ObservationsDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() {
		db = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void addObservation() {
		System.out.println("Comments "
				+ DkeyApplication._controller.getValue("comments"));
		Log.d(ObservationsDataSource.class.getSimpleName(),
				DkeyApplication._controller.getValue("comments"));
		ContentValues values = new ContentValues();
		// values.put(MySQLiteHelper.COLUMN_ID,);
		values.put(MySQLiteHelper.comments,
				DkeyApplication._controller.getValue("comments"));
		values.put(MySQLiteHelper.common_name,
				DkeyApplication._controller.getValue("comments"));
		values.put(MySQLiteHelper.family,
				DkeyApplication._controller.getValue("comments"));
		values.put(MySQLiteHelper.genus,
				DkeyApplication._controller.getValue("comments"));
		values.put(MySQLiteHelper.genus,
				DkeyApplication._controller.getValue("comments"));
		values.put(MySQLiteHelper.lat,
				DkeyApplication._controller.getValue("comments"));
		values.put(MySQLiteHelper.longi,
				DkeyApplication._controller.getValue("comments"));
		values.put(MySQLiteHelper.species,
				DkeyApplication._controller.getValue("comments"));
		values.put(MySQLiteHelper.moreFields1,
				DkeyApplication._controller.getMoreFieldValue(0));
		values.put(MySQLiteHelper.moreFields2,
				DkeyApplication._controller.getMoreFieldValue(1));
		values.put(MySQLiteHelper.moreFields3,
				DkeyApplication._controller.getMoreFieldValue(2));

		db.insert(MySQLiteHelper.TABLE_OBS, null, values);

	}

	public List<String> getAllObservations() {
		List<String> observations = new ArrayList<String>();
		String selectQuery = "SELECT * FROM " + MySQLiteHelper.TABLE_OBS;

		Cursor cursor = db.rawQuery(selectQuery, null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {

			observations.add(cursor.getString(0));
			observations.add(cursor.getString(1));
			// Make sure to close the cursor
			cursor.moveToNext();
		}
		cursor.close();

		return observations;

	}
}