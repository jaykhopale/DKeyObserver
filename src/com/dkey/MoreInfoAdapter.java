package com.dkey;

import android.app.Activity;
import android.content.Context;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class MoreInfoAdapter extends ArrayAdapter< MoreInfoRow > {

	Context context; 
	int layoutResourceId;    
	MoreInfoRow data[] = null;

	public MoreInfoAdapter(Context context, int layoutResourceId, MoreInfoRow[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		MoreInfoHolder holder = null;

		if(row == null)
		{
			LayoutInflater inflater =  ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);		            
			holder = new MoreInfoHolder();
			holder.keyTextView = (TextView)row.findViewById(R.id.key);
			holder.separator = (TextView)row.findViewById(R.id.separator);
			holder.valueTextView = (TextView)row.findViewById(R.id.value);
			row.setTag(holder);
		}
		else
		{
			holder = (MoreInfoHolder)row.getTag();
		}

		MoreInfoRow moreInfoRowItem = data[position];
		if( moreInfoRowItem != null ) {
			holder.keyTextView.setText(moreInfoRowItem.key);
			holder.separator.setText( ":" );
			holder.valueTextView.setText(moreInfoRowItem.value);
			Linkify.addLinks(holder.valueTextView, Linkify.WEB_URLS);

		}
		return row;
	}
	
	static class MoreInfoHolder
	{
		TextView keyTextView;
		TextView separator;
		TextView valueTextView;
	}

}

