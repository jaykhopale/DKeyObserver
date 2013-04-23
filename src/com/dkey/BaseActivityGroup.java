package com.dkey;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

public class BaseActivityGroup extends ActivityGroup {

	protected ArrayList<View> history;
	public static BaseActivityGroup currentGroup;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.dkeytemplate);
	}

	public void replaceView(View v) {
		// Adds the old one to history
		history.add(v);
		// Changes this Groups View to the new View.
		setContentView(v);
	}

	public void back() {
		if (history.size() > 0) {
			history.remove(history.size() - 1);
			if (history.size() > 0) {
				setContentView(history.get(history.size() - 1));
				DkeyApplication._controller.destroyAllSessionContent();
			}

		}
		else {
			final AlertDialog.Builder b = new AlertDialog.Builder(getParent());
			b.setIcon(android.R.drawable.ic_dialog_alert);
			b.setTitle("Alert !!");
			b.setMessage("Do you  want to quit the Application?");
			b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					finish();

				}
			});
			b.setNegativeButton(android.R.string.no, null);
			b.show();
		}
	}

	public void startOver() {
		while (history.size() > 1) {
			history.remove(history.size() - 1);
		}
		if (history.size() == 1) {
			setContentView(history.get(history.size() - 1));
		}
	}

	public void changeCurrentActivityGroup(BaseActivityGroup baseActivity) {
		currentGroup = baseActivity;
		BaseActivity.currentGroup = currentGroup;

	}

	@Override
	public void onBackPressed() {// {
		//back();
		
		  Log.d(BaseActivityGroup.class.getName(), "Back pressed in base activity group");
		  final AlertDialog.Builder b = new AlertDialog.Builder(getParent());
		  b.setIcon(android.R.drawable.ic_dialog_alert);
		  b.setTitle("Alert !!");
		  b.setMessage("Do you  want to quit the Application?");
		  b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int which) { finish();
		  
		  } }); b.setNegativeButton(android.R.string.no, null); b.show();
		 
		// }
		//Log.d(BaseActivityGroup.class.getName(), "Back pressed in base activity group");
		/*if (currentGroup.history.size() == 1) {
			final AlertDialog.Builder b = new AlertDialog.Builder(getParent());
			b.setIcon(android.R.drawable.ic_dialog_alert);
			b.setTitle("Alert !!");
			b.setMessage("Do you  want to quit the Application?");
			b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					finish();

				}
			});
			b.setNegativeButton(android.R.string.no, null);
			b.show();
		} else {
			currentGroup.back();
			if (currentGroup instanceof DkeyActivityGroup) {
				DkeyApplication._controller.getIsLeafNodeList()
						.remove((DkeyApplication._controller
								.getIsLeafNodeList().size()) - 1);
				if (!DkeyApplication._controller.getIsLeafNodeList()
						.get(DkeyApplication._controller.getIsLeafNodeList()
								.size() - 1)) {
					DkeyApplication._controller.setOrgID("-1");
					DkeyApplication._controller.processOrganismNode();
				}
			}

		}*/
	}
	
	 public boolean onKeyDown(int keyCode, KeyEvent event)  
	  {  
	         //replaces the default 'Back' button action
		 Activity current = getLocalActivityManager().getCurrentActivity();
		        
	         if(keyCode==KeyEvent.KEYCODE_BACK)  
	         {  
	        	 Log.d(BaseActivityGroup.class.getName(), "Key back pressed");
	        	 current.onBackPressed();
	        	 return true;

	         }  
	         
	         return false;
	   }

}
