package com.dkey.observe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dkey.BaseActivity;
import com.dkey.DkeyApplication;
import com.dkey.MoreInfoRow;
import com.dkey.R;
import com.dkey.Interfaces.IGetNotesValuesListener;

public class DkeyNotes extends BaseActivity implements IGetNotesValuesListener{

	EditText family;
	EditText genus;
	EditText species;
	EditText commonname;
	EditText comments;
	EditText key1;
	EditText key2;
	EditText key3;
	EditText value1;
	EditText value2;
	EditText value3;
	EditText variety;
	EditText longiValue;
	EditText latValue;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.dkeynotes);
		LinearLayout layout = (LinearLayout) findViewById(R.id.notesid);
		super.addtitleBar(layout, R.layout.dkeyobservetitlebar,
				"Notes", true, R.id.informationID,
				R.id.doneButtonID);
		DkeyApplication._controller.registerGetNotesValuesListener( this );
		Button gps = (Button) findViewById(R.id.gpsbutton);
		family = (EditText) findViewById(R.id.family);
		genus = (EditText) findViewById(R.id.genus);
		species = (EditText) findViewById(R.id.species);
		variety = (EditText) findViewById(R.id.variety);
		commonname = (EditText) findViewById(R.id.commonname);
		comments = (EditText) findViewById(R.id.comments);
		key1 = (EditText) findViewById(R.id.morekey1);
		key2 = (EditText) findViewById(R.id.morekey2);
		key3 = (EditText) findViewById(R.id.morekey3);
		value1 = (EditText) findViewById(R.id.morevalue1);
		value2 = (EditText) findViewById(R.id.morevalue2);
		value3 = (EditText) findViewById(R.id.morevalue3);
		loadData();
		latValue = (EditText) findViewById(R.id.latvalue);
		longiValue = (EditText) findViewById(R.id.longivalue);
		gps.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
				LocationListener mlocListener = new MyLocationListener();
				mlocManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
				
			}
		});
	}

	private void loadData() {
		
		ArrayList<MoreInfoRow> rows = DkeyApplication._controller.getMoreinfoRows();
		if(rows != null ) {
			for( MoreInfoRow row : rows ) {
				 if ( row.key.equals("family")) {
					 family.setText(row.value);
				 }
				 if ( row.key.equals("species")) {
					 species.setText(row.value);
				 }
				 if ( row.key.equals("genus")) {
					 genus.setText(row.value);
				 }
				 
				 if ( row.key.equals("common_name")) {
					 commonname.setText(row.value);
				 }
			}
		}
		
	}

	/* Class My Location Listener */

	class MyLocationListener implements LocationListener

	{


		public void onLocationChanged(Location loc)

		{

			DecimalFormat twoDForm = new DecimalFormat("#.##########");
			latValue.setText(Double.valueOf(twoDForm.format(loc.getLatitude())).toString());

			longiValue.setText(Double.valueOf(twoDForm.format(loc.getLongitude())).toString());

		}


		public void onProviderDisabled(String provider)

		{

			Toast.makeText( getApplicationContext(),

					"Gps Disabled",

					Toast.LENGTH_SHORT ).show();

		}


		public void onProviderEnabled(String provider)

		{

			Toast.makeText( getApplicationContext(),

					"Gps Enabled",

					Toast.LENGTH_SHORT).show();

		}


		public void onStatusChanged(String provider, int status, Bundle extras)

		{


		}

	}

	
	public Hashtable<String, String> NotesValues() {
	
		Hashtable<String, String> notesMap = new Hashtable<String, String>();
		notesMap.put("common_name",commonname.getText().toString());
		notesMap.put("family",family.getText().toString());
		notesMap.put("species", species.getText().toString());
		notesMap.put("genus", genus.getText().toString());
		notesMap.put("comments",comments.getText().toString());
		notesMap.put("latitude",latValue.getText().toString());
		notesMap.put("longitude",longiValue.getText().toString());
		notesMap.put("variety",variety.getText().toString());
		return notesMap;
	}
	
	public ArrayList<String> moreFieldValues() {
		ArrayList<String> moreFields = new ArrayList<String>();
		moreFields.add(key1.getText().toString() + " : " + value1.getText().toString());
		moreFields.add(key2.getText().toString() + " : " + value2.getText().toString());
		moreFields.add(key3.getText().toString() + " : " + value3.getText().toString());
		return moreFields;
	}
}
