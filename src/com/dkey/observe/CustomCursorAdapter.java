package com.dkey.observe;

import com.dkey.R;
import com.dkey.observe.SingleListItem.MyLocationListener;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CustomCursorAdapter extends SimpleCursorAdapter {

	public CustomCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		super.bindView(view, context, cursor);

		final Context ctx = context;
		final Cursor crsr = cursor;

		TextView tvId = (TextView) view.getTag(R.id.rowID);
		TextView tvname = (TextView) view.getTag(R.id.rowTitle);
		TextView tvlat = (TextView) view.getTag(R.id.lat_text);
		TextView tvlong = (TextView) view.getTag(R.id.long_text);
		/*
		 * ImageButton dirButton = (ImageButton) view
		 * .getTag(R.id.imageButtondir);
		 */

		tvId.setText(crsr.getString(0));
		tvname.setText("Name:" + crsr.getString(1));
		tvlat.setText("Lat:" + crsr.getString(2));
		tvlong.setText("Long:" + crsr.getString(3));

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup group) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.simplelistrow, null);
		view.setTag(R.id.rowID, view.findViewById(R.id.rowID));
		view.setTag(R.id.rowTitle, view.findViewById(R.id.rowTitle));
		view.setTag(R.id.lat_text, view.findViewById(R.id.lat_text));
		view.setTag(R.id.long_text, view.findViewById(R.id.long_text));
		// view.setTag(R.id.imageButtondir,
		// view.findViewById(R.id.imageButtondir));
		return view;

	}

}
