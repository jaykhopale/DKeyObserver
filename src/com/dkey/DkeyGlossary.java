package com.dkey;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dkey.DkeyGlossary.GlossaryAdapter;
import com.dkey.Interfaces.IResetGlossaryListener;

public class DkeyGlossary extends BaseActivity implements IResetGlossaryListener, OnItemClickListener{
	private GlossaryAdapter adapter;
	List<GlossaryItem> li = new ArrayList<GlossaryItem>() ;
	private int currentKey;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dkglossary);
		LinearLayout layout = (LinearLayout) findViewById( R.id.glossarylayout);
		super.addtitleBar(layout, R.layout.dkeytitlebar,  "Glossary", false, R.id.informationID, R.id.backButtonID);
		ListView view = (ListView) layout.findViewById(R.id.glossarylist);
		loadGlossary();
		adapter = new GlossaryAdapter(this,li);
		view.setOnItemClickListener(this);
		view.setAdapter(adapter);
		currentKey = DkeyApplication._controller.getCurrentKey();
		DkeyApplication._controller.registerResetGlossaryListener(this);
		AdapterView w;
	}
	
	
	private void loadGlossary() {
		if(adapter != null) {
			adapter.clear();
		}
		NodeList list = DkeyApplication._controller.getDocBuilder().getElementsByTagName("GlossaryItem");
		for(int i = 0; i< list.getLength(); i++){     	
			NodeList children = list.item(i).getChildNodes();
			GlossaryItem item = new GlossaryItem();
			for(int j = 0; j < children.getLength(); j++){
				if(children.item(j).getNodeName().equals("itemID")){
					Node childNode = children.item(j);
					String key = childNode.getChildNodes().item(0).getNodeValue();
					item.id=key;
				}
				else if(children.item(j).getNodeName().equals("title")){
					Node childNode = children.item(j);
					String key = childNode.getChildNodes().item(0).getNodeValue();
					item.title=key;
				}
				else if(children.item(j).getNodeName().equals("description")){
					Node childNode = children.item(j);
					String key = childNode.getChildNodes().item(0).getNodeValue();
					item.desc=key;
				}
				else if(children.item(j).getNodeName().equals("itemImage")){
					Node childNode = children.item(j);
					String key = DkeyGlossaryDetails.NOIMAGE;
					if(childNode.getChildNodes().getLength() > 0 ) {
						key = childNode.getChildNodes().item(0).getNodeValue();
				}
				item.image=key;
				}
			}
			li.add(item);
		}

	}
	
	@Override
	public void onResume() {
		super.onResume();
		if ( currentKey != DkeyApplication._controller.getCurrentKey()) {
			currentKey = DkeyApplication._controller.getCurrentKey();
			loadGlossary();
		}
	}
	
	 class GlossaryAdapter extends ArrayAdapter<GlossaryItem> {

		private Context _context;
		private List<GlossaryItem> _values;
		public GlossaryAdapter(Context context, List<GlossaryItem> li) {
			super(context,R.layout.glossarylistitem, li);
			_context = context;
			_values = li;
		}
		
		

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			//if (v == null) {
				LayoutInflater vi = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.glossarylistitem, null);
			//}
			final GlossaryItem o =_values.get(position);
			if (o != null) {
				TextView textView = (TextView) v.findViewById(R.id.glossaryitem);
				textView.setText(o.title);
			}
			return v;
		}
	 }

	public void resetGlossary() {
		loadGlossary();
		
	}


	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		GlossaryItem o = li.get(arg2);
		Intent intent = new Intent(this, DkeyGlossaryDetails.class);
		intent.putExtra("glossaryID", o.id);
		intent.putExtra("title", o.title);
		intent.putExtra("desc", o.desc);
		intent.putExtra("image", o.image);
		View view = DkeyGlossaryGroup.group.getLocalActivityManager()
				.startActivity("DkeyGlossaryDetails", intent
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();

		// Again, replace the view
		DkeyGlossaryGroup.group.replaceView(view);
		
		/*
		AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity._currentActivity);
		builder.setTitle("Description");
		builder.setMessage(o.desc);
		builder.show();
		builder.setPositiveButton("Close",new DialogInterface.OnClickListener() {


			public void onClick(DialogInterface dialog, int which) {

				dialog.cancel();
			}
		});
	*/
		
	}
}
