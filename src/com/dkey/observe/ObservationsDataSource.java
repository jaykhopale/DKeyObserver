package com.dkey.observe;

import java.util.ArrayList;
import java.util.List;

import com.dkey.DkeyApplication;
import com.dkey.observe.Observation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ObservationsDataSource {

	private SQLiteDatabase db;
	private MySQLiteHelper dbHelper;

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
				DkeyApplication._controller.getValue("common_name"));
		values.put(MySQLiteHelper.family,
				DkeyApplication._controller.getValue("family"));
		values.put(MySQLiteHelper.genus,
				DkeyApplication._controller.getValue("genus"));
		values.put(MySQLiteHelper.lat,
				DkeyApplication._controller.getValue("latitude"));
		values.put(MySQLiteHelper.longi,
				DkeyApplication._controller.getValue("longitude"));
		values.put(MySQLiteHelper.variety,
				DkeyApplication._controller.getValue("variety"));
		values.put(MySQLiteHelper.species,
				DkeyApplication._controller.getValue("species"));
		values.put(MySQLiteHelper.moreFields1,
				DkeyApplication._controller.getMoreFieldValue(0));
		values.put(MySQLiteHelper.moreFields2,
				DkeyApplication._controller.getMoreFieldValue(1));
		values.put(MySQLiteHelper.moreFields3,
				DkeyApplication._controller.getMoreFieldValue(2));

		db.insert(MySQLiteHelper.TABLE_OBS, null, values);

	}

	/*
	 * public List<Observation> getAllObservations() { List<Observation>
	 * observations = new ArrayList<Observation>(); String selectQuery =
	 * "SELECT * FROM " + MySQLiteHelper.TABLE_OBS;
	 * 
	 * Cursor cursor = db.rawQuery(selectQuery, null); cursor.moveToFirst();
	 * 
	 * while (!cursor.isAfterLast()) {
	 * 
	 * Observation obsnote = new Observation();
	 * obsnote.setId(Integer.toString(cursor.getInt(0)));
	 * obsnote.setCommon_name(cursor.getString(1));
	 * obsnote.setLatitude(cursor.getString(7));
	 * obsnote.setLongitude(cursor.getString(8));
	 * 
	 * observations.add(obsnote);
	 * 
	 * // observations.add(cursor.getString(1)); // Make sure to close the
	 * cursor cursor.moveToNext(); } cursor.close();
	 * 
	 * return observations;
	 * 
	 * }
	 */

	public Cursor getAllObservations() {

		Cursor resultCursor = db.query(MySQLiteHelper.TABLE_OBS, new String[] {
				MySQLiteHelper.COLUMN_ID, MySQLiteHelper.common_name,
				MySQLiteHelper.lat, MySQLiteHelper.longi }, null, null, null,
				null, null);

		if (resultCursor != null) {
			resultCursor.moveToFirst();

		}

		return resultCursor;

	}

	public Cursor getParticularObservation(int id) {

		Cursor resultCursor = db.query(MySQLiteHelper.TABLE_OBS, new String[] {
				MySQLiteHelper.COLUMN_ID, MySQLiteHelper.common_name,
				MySQLiteHelper.family, MySQLiteHelper.species,
				MySQLiteHelper.genus, MySQLiteHelper.comments,
				MySQLiteHelper.variety, MySQLiteHelper.lat,
				MySQLiteHelper.longi, MySQLiteHelper.moreFields1,
				MySQLiteHelper.moreFields2, MySQLiteHelper.moreFields3 },
				MySQLiteHelper.COLUMN_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (resultCursor != null) {
			resultCursor.moveToFirst();
		}

		return resultCursor;

	}
}