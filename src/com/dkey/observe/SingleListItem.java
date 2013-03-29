package com.dkey.observe;

import java.text.DecimalFormat;
import java.util.List;

import com.dkey.BaseActivity;
import com.dkey.R;
import com.dkey.observe.DkeyNotes.MyLocationListener;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SingleListItem extends BaseActivity {

	private ObservationsDataSource datasource;
	// private CustomCursorAdapter dataAdapter;

	private static String sLat;
	private static String sLong;
	private static Location locNav;
	LocationManager mlocManager;
	LocationListener mlocListener;

	private void enableLocationSettings() {
		Intent settingsIntent = new Intent(
				Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(settingsIntent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_item_list_view);
		LinearLayout singleItemLayout = (LinearLayout) findViewById(R.id.singleitemid);

		super.addtitleBar(singleItemLayout, R.layout.dkeyobservetitlebar,
				"Details", false, R.id.informationID, R.id.doneButtonID);

		final TextView latdisp = (TextView) findViewById(R.id.latdisp);
		final TextView longdisp = (TextView) findViewById(R.id.longdisp);
		TextView famdisp = (TextView) findViewById(R.id.famdisp);
		TextView gendisp = (TextView) findViewById(R.id.gendisp);
		TextView specdisp = (TextView) findViewById(R.id.specdisp);
		TextView vardisp = (TextView) findViewById(R.id.vardisp);
		TextView namdisp = (TextView) findViewById(R.id.namdisp);
		TextView comdisp = (TextView) findViewById(R.id.comdisp);
		TextView exfd1 = (TextView) findViewById(R.id.exfd1);
		TextView exfd2 = (TextView) findViewById(R.id.exfd2);
		TextView exfd3 = (TextView) findViewById(R.id.exfd3);
		Button nav = (Button) findViewById(R.id.nav);

		Intent getDetail = getIntent();

		String rowId = getDetail.getStringExtra("row");

		int id = Integer.valueOf(rowId);
		datasource = new ObservationsDataSource(getApplicationContext());
		datasource.open();
		Cursor cursor = datasource.getParticularObservation(id);

		latdisp.setText("Latitude :" + cursor.getString(7));
		longdisp.setText("Longitude :" + cursor.getString(8));
		famdisp.setText("Family : " + cursor.getString(2));
		gendisp.setText("Genus : " + cursor.getString(4));
		specdisp.setText("Species : " + cursor.getString(3));
		vardisp.setText("Variety : " + cursor.getString(6));
		namdisp.setText("Common Name : " + cursor.getString(1));
		comdisp.setText("Comments : " + cursor.getString(5));
		exfd1.setText("ExtraField 1 : " + cursor.getString(9));
		exfd2.setText("ExtraField 2 : " + cursor.getString(10));
		exfd3.setText("ExtraField 3 : " + cursor.getString(11));

		datasource.close();

		String latDest = latdisp.getText().toString();
		String longDest = longdisp.getText().toString();

		final String finLat = latDest.substring(latDest.indexOf(":") + 1);
		final String finLong = longDest.substring(longDest.indexOf(":") + 1);

		nav.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				final boolean gpsEnabled = mlocManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
				mlocListener = new MyLocationListener();

				if (!gpsEnabled) {

					AlertDialog.Builder builder = new AlertDialog.Builder(SingleListItem.this);
					Log.d("Alert", "Inside alert dialog");
					builder.setTitle("Enable Location!");
					builder.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) { // TODO Auto-generated
														// method stub

									enableLocationSettings();

								}
							});

					builder.setNegativeButton("No",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) { // TODO Auto-generated
														// method stub //
									dialog.dismiss();

								}
							});
					AlertDialog dialog = builder.create();
					dialog.show();

				}

				/*
				 * locNav = mlocManager
				 * .getLastKnownLocation(LocationManager.GPS_PROVIDER);
				 */

				List<String> providers = mlocManager.getProviders(true);

				for (int i = providers.size() - 1; i >= 0; i--) {
					locNav = mlocManager.getLastKnownLocation(providers.get(i));
					if (locNav != null)
						break;
				}

				mlocManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 10000, 10,
						mlocListener);

				if (locNav != null || gpsEnabled) {
					sLat = Double.valueOf((locNav.getLatitude())).toString();

					sLong = Double.valueOf((locNav.getLongitude())).toString();
				}
				
				if(gpsEnabled){
				if (sLat != null && sLong != null) {
					Intent intent = new Intent(
							android.content.Intent.ACTION_VIEW,
							Uri.parse("https://maps.google.com/maps?saddr="
									+ sLat + "," + sLong + "&daddr=" + finLat
									+ "," + finLong));

					SingleListItem.this.startActivity(intent);
				}
				}
			}

		});

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mlocListener != null)
			mlocManager.removeUpdates(mlocListener);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		return;
	}

	class MyLocationListener implements LocationListener

	{

		public void onLocationChanged(Location loc)

		{

			sLat = Double.valueOf(loc.getLatitude()).toString();
			sLong = Double.valueOf(loc.getLongitude()).toString();

		}

		public void onProviderDisabled(String provider)

		{

			/*Toast.makeText(getApplicationContext(),

			"Please enable GPS in Settings",

			Toast.LENGTH_SHORT).show();*/
			
						

		}

		public void onProviderEnabled(String provider)

		{

			/*
			 * Toast.makeText(getApplicationContext(),
			 * 
			 * "Gps Enabled",
			 * 
			 * Toast.LENGTH_SHORT).show();
			 */
		}

		public void onStatusChanged(String provider, int status, Bundle extras)

		{

		}

	}

}
