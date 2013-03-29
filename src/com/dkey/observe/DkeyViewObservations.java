package com.dkey.observe;

import java.util.List;

import com.dkey.BaseActivity;
import com.dkey.R;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.Toast;

public class DkeyViewObservations extends BaseActivity {

	private ObservationsDataSource datasource;
	//protected static LayoutInflater inflater;

	private CustomCursorAdapter dataAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dkeylistview);
		LinearLayout layout = (LinearLayout) findViewById(R.id.devobs);
		super.addtitleBar(layout, R.layout.dkeyobservetitlebar,
				"Device Observations", false, R.id.informationID,
				R.id.doneButtonID);

		datasource = new ObservationsDataSource(getApplicationContext());
		datasource.open();
		
		Cursor cursor = datasource.getAllObservations();
		String[] columns = new String[] { MySQLiteHelper.COLUMN_ID,
				MySQLiteHelper.common_name, MySQLiteHelper.lat,
				MySQLiteHelper.longi };

		int[] to = new int[] { R.id.rowID, R.id.rowTitle, R.id.lat_text,
				R.id.long_text };

		dataAdapter = new CustomCursorAdapter(this, R.layout.simplelistrow,
				cursor, columns, to);

		ListView mainListView = (ListView) findViewById(R.id.listViewObs);
		mainListView.setAdapter(dataAdapter);

		mainListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> mainListView, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				TextView rowId = (TextView) view.findViewById(R.id.rowID);
				String row = rowId.getText().toString();
				
				Intent launchDetails = new Intent(getApplicationContext(), SingleListItem.class);
				
				launchDetails.putExtra("row", row);
				startActivity(launchDetails);

			}

		});

		datasource.close();
		
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		return;

	}

	
	
	
	
	
	
}
