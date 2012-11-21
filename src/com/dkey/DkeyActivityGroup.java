package com.dkey;

import java.util.ArrayList;

import com.dkey.DkeyController.DkeyTabs;
import com.dkey.Interfaces.ITabcontentChangeListener;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class DkeyActivityGroup extends BaseActivityGroup implements ITabcontentChangeListener {

	  /** Called when the activity is first created. */
	public static DkeyActivityGroup group;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.history = new ArrayList< View >();
		group = this;
		changeCurrentActivityGroup( group );
		DkeyApplication._controller.registerTabContentChangeListener( DkeyTabs.key, this);
		// Start the root activity withing the group and get its view
		View view = getLocalActivityManager().startActivity("Dkey", new
				Intent(this,Dkey.class)
		.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
		.getDecorView();
		// Replace the view of this ActivityGroup
		replaceView(view);
        
    }
    
    public void changeCurrentActivityGroup( ) {
    	BaseActivity.currentGroup = group;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {	
    	Log.i( "MakeMachine", "resultCode: " + resultCode );
    	
    }
    
}
