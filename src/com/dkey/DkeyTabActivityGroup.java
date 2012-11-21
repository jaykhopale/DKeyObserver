package com.dkey;

import java.util.ArrayList;
import com.dkey.DkeyController.DkeyTabs;
import com.dkey.Interfaces.ITabcontentChangeListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class DkeyTabActivityGroup extends BaseActivityGroup implements ITabcontentChangeListener{

	public static DkeyTabActivityGroup group;
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.history = new ArrayList< View >();
			group = this;
			changeCurrentActivityGroup( this );
			DkeyApplication._controller.registerTabContentChangeListener( DkeyTabs.dkeytab, this);
			// Start the root activity withing the group and get its view
			View view = DkeyTabActivityGroup.group.getLocalActivityManager().startActivity("DkeyTabs", new
					Intent(this,DkeyTabActivity.class)
			.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
			.getDecorView();
			// Replace the view of this ActivityGroup
			replaceView(view);
	        
	    }
	public void changeCurrentActivityGroup() {
		BaseActivityGroup.currentGroup = group;
		
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {	
    	Log.i( "MakeMachine", "resultCode: " + resultCode );
    	
    }
	
	
}
