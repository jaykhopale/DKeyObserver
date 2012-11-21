package com.dkey;

import java.util.ArrayList;

import com.dkey.DkeyController.DkeyTabs;
import com.dkey.Interfaces.ITabChangeListener;
import com.dkey.Interfaces.ITabcontentChangeListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DkeyObservation extends BaseActivity implements ITabChangeListener{

	  /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        DkeyApplication._controller.registerTabChangeListener( this);
	        
	    }
	    
	    public void resetObserveTabSet() {
			initializeObserveTabSet();
			
		}
		
		private void initializeObserveTabSet() {
			View view = DkeyTabActivityGroup.group.getLocalActivityManager()
			        .startActivity("DkeyNode", new
							Intent(this,DkeyObserveTabActivity.class)
			        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
			        .getDecorView();

			        // Again, replace the view
			DkeyTabActivityGroup.group.replaceView(view);
			//DkeyActivityGroup.group.changeCurrentActivityGroup();
		}
}
