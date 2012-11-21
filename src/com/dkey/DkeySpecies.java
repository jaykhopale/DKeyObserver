package com.dkey;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dkey.Interfaces.IResetSpeciesListListener;
import com.dkey.observe.Test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class DkeySpecies extends BaseActivity implements IResetSpeciesListListener {

	ListView dkeySpeciesList;
	SpeciesAdapter adapter;
	int currentKey;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dkeyspecieslist);
		LinearLayout layout = (LinearLayout) findViewById( R.id.dkeyspecieslist);
		super.addtitleBar(layout,R.layout.dkeytitlebar, "List of Species", false, R.id.informationID, R.id.backButtonID );
		//View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
		//listView1.addHeaderView(header);
		adapter = new SpeciesAdapter(this, R.layout.dkey_species_listitem );
		getSpecies();
		dkeySpeciesList.setAdapter(adapter);
		currentKey = DkeyApplication._controller.getCurrentKey();
		DkeyApplication._controller.registerResetSpeciesListListener(this);
	}

	private void getSpecies() {
		if(adapter != null) {
			adapter.clear();
		}
		dkeySpeciesList = (ListView) findViewById(R.id.listView1);
		NodeList list = DkeyApplication._controller.getDocBuilder().getElementsByTagName("organism");
		for(int i = 0; i< list.getLength(); i++){  
			Species species = new Species();
			NodeList children = list.item(i).getChildNodes();
			for(int j = 0; j < children.getLength(); j++){ 
				Node node = children.item(j);
				if( node.getNodeName().equals("image")){
					species.image =node.getChildNodes().item(0).getNodeValue();
				}
				else if( node.getNodeName().equals("genus")){
					species.common_name = node.getChildNodes().item(0).getNodeValue();
				}
				else if( node.getNodeName().equals("org_id")){
					species.org_id = node.getChildNodes().item(0).getNodeValue();
				}
			}
			adapter.add(species);
			Log.d("ORGANISM ID",species.org_id + "---------" +species.common_name);
		}
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if ( currentKey != DkeyApplication._controller.getCurrentKey()) {
			currentKey = DkeyApplication._controller.getCurrentKey();
			getSpecies();
		}
	}

	private class SpeciesAdapter extends ArrayAdapter<Species> {

		Context context; 
		int layoutResourceId;    
		public SpeciesAdapter(Context context, int layoutResourceId ) {
			super(context, layoutResourceId);
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			SpeciesHolder holder = null;
			final Species species = getItem(position);
			if(row == null)
			{
				LayoutInflater inflater = ((Activity)context).getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);

				holder = new SpeciesHolder();
				holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
				holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

				row.setTag(holder);
				
			}
			else
			{
				holder = (SpeciesHolder)row.getTag();
			}

			row.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					Intent intent = new Intent(v.getContext(), DkeyMoreInfo.class);
					intent.putExtra("orgID", species.org_id);
					View view = DkeySpeciesActivityGroup.group.getLocalActivityManager()
							.startActivity("DkeyMoreInfo", intent
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
									.getDecorView();

					// Again, replace the view
					DkeySpeciesActivityGroup.group.replaceView(view);

				}
			});
			holder.txtTitle.setText(species.common_name);
			Log.d("Species", species.common_name+ "----------------------------------------------");
			Log.d("image", species.image+ "----------------------------------------------");
			species.image = species.image.toLowerCase();
			species.image = species.image.split("\\.")[0];
			
			holder.imgIcon.setImageResource(getResources().getIdentifier(species.image,"drawable",getPackageName()));

			return row;
		}

	}

	static class SpeciesHolder
	{
		ImageView imgIcon;
		TextView txtTitle;
	}

	private class Species {
		String org_id;
		String image;
		String common_name;
	}

	public void resetSpeciesList() {
		getSpecies();
		
	}


}
