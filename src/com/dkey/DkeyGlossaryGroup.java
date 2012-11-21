package com.dkey;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dkey.DkeyController.DkeyTabs;
import com.dkey.Interfaces.ITabcontentChangeListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DkeyGlossaryGroup extends BaseActivityGroup implements ITabcontentChangeListener{
	
	public static DkeyGlossaryGroup group;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.history = new ArrayList< View >();
		group = this;
		changeCurrentActivityGroup( group );
		DkeyApplication._controller.registerTabContentChangeListener(DkeyTabs.Glossary , this);
		View view = getLocalActivityManager().startActivity("DkeyGlossary", new
				Intent(this,DkeyGlossary.class)
		.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
		.getDecorView();
		// Replace the view of this ActivityGroup
		replaceView(view);
	}
	public void changeCurrentActivityGroup() {
		BaseActivity.currentGroup = group;
		
	}
	
}
