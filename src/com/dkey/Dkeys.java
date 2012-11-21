package com.dkey;


import java.io.IOException;
import java.util.HashMap;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Dkeys extends BaseActivity {

	private KeysAdapter adapter;
	private HashMap<Integer, String> _preloadedKeys;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dkglossary);
		LinearLayout layout = (LinearLayout) findViewById( R.id.glossarylayout);
		super.addtitleBar(layout, R.layout.dkeytitlebar,  "Select Dkey", false, R.id.informationID, R.id.backButtonID);
		ListView view = (ListView) findViewById(R.id.glossarylist);
		adapter = new KeysAdapter( this, R.id.glossaryitem);
		view.setAdapter(adapter);
		_preloadedKeys = DkeyApplication._controller.getDkeyModel().getPreLoadedKeys();
		for( Integer keyID : _preloadedKeys.keySet()) {
			KeyItem item = new KeyItem();
			item.keyID = keyID;
			item.keyName= _preloadedKeys.get(keyID);
			adapter.add(item);
		}
	}

	class KeysAdapter extends ArrayAdapter<KeyItem> {

		private Context _context;
		public KeysAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			_context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.glossarylistitem, null);
			}
			final KeyItem o =getItem(position);
			int currentActivity = DkeyApplication._controller.getCurrentKey();
			if( o.keyID == currentActivity) {
				o.keyName= o.keyName + "(Current Key)";
			}
			else {
				o.keyName= o.keyName.replace("(Current Key)","");
			}
			
			if (o != null) {
				TextView textView = (TextView) v.findViewById(R.id.glossaryitem);
				textView.setText(o.keyName);
			}
			v.setOnClickListener( new View.OnClickListener() {


				public void onClick(View v) {
					if( o.keyID != DkeyApplication._controller.getCurrentKey()) {
						try {
							DkeyApplication._controller.setCurrentKey( o.keyID );
							if(DkeyActivityGroup.group != null ) {
								DkeyActivityGroup.group.startOver();
								
							}
							if (DkeySpeciesActivityGroup.group != null ) {
								DkeySpeciesActivityGroup.group.startOver();
								DkeyApplication._controller.resetSpeciesList();
							}
							if (DkeyGlossaryGroup.group != null ) {
								DkeyGlossaryGroup.group.startOver();
								DkeyApplication._controller.resetGlossary();
							}
							DkeyApplication._controller.resetTabs();
							notifyDataSetChanged();
						} catch (SAXException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			});
			return v;

		}
	}

	class KeyItem {
		int keyID;
		String keyName;
	}

}
