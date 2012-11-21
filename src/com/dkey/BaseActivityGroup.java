package com.dkey;

import java.util.ArrayList;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

public class BaseActivityGroup extends ActivityGroup{

	protected ArrayList< View > history;
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
		if(history.size() > 0) {
			history.remove(history.size()-1);
			if( history.size() > 0 ) {
				setContentView(history.get( history.size()-1));
				DkeyApplication._controller.destroyAllSessionContent();
			}
		
		}
	}
	
	public void startOver() {
		while(history.size() > 1) {
			history.remove(history.size()-1);
		}
		if(history.size() == 1) {
			setContentView(history.get( history.size()-1));
		}
	}

	public void changeCurrentActivityGroup(BaseActivityGroup baseActivity) {
		currentGroup = baseActivity;
		BaseActivity.currentGroup = currentGroup;

	}
	
	@Override
	public void onBackPressed() {
		final AlertDialog.Builder b = new AlertDialog.Builder(getParent());
		b.setIcon(android.R.drawable.ic_dialog_alert);
		b.setTitle("Alert !!");
		b.setMessage("Do you  want to quit the Application?");
		b.setPositiveButton("Yes",
			    new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
			            finish();
			            
			        }
			    });
		b.setNegativeButton(android.R.string.no, null);
		b.show();
	}
	
	

}

