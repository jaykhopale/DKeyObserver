package com.dkey.observe;

import java.util.List;

import com.dkey.BaseActivity;
import com.dkey.R;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;

import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class DkeyViewObservations extends BaseActivity {

	private ObservationsDataSource datasource;
	protected static LayoutInflater inflater;
	private ListView mainListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dkeylistview);
		LinearLayout layout = (LinearLayout) findViewById(R.id.devobs);
		super.addtitleBar(layout, R.layout.dkeyobservetitlebar,
				"Device Observations", false, 0,
				0);

		datasource = new ObservationsDataSource(getApplicationContext());
		datasource.open();
		mainListView = (ListView) findViewById(R.id.listViewObs);

		List<String> values = datasource.getAllObservations();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.simplelistrow, values);
		
		
		mainListView.setAdapter(adapter);
		datasource.close();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		return;

	}

}
