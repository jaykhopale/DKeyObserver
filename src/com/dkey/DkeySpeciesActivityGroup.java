package com.dkey;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dkey.DkeyController.DkeyTabs;
import com.dkey.Interfaces.ITabcontentChangeListener;

import android.app.Activity;
import android.app.ActivityGroup;
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


public class DkeySpeciesActivityGroup extends BaseActivityGroup implements ITabcontentChangeListener{
	
	public static DkeySpeciesActivityGroup group;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dkeyspecieslist);
		this.history = new ArrayList< View >();
		group = this;
		changeCurrentActivityGroup( group );
		DkeyApplication._controller.registerTabContentChangeListener(DkeyTabs.species, this);
		View view = getLocalActivityManager().startActivity("DkeySpecies", new
				Intent(this,DkeySpecies.class)
		.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
		.getDecorView();
		// Replace the view of this ActivityGroup
		replaceView(view);
	}
	public void changeCurrentActivityGroup() {
		BaseActivity.currentGroup = group;
		
	}
		
}
