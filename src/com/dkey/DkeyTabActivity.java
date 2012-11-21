package com.dkey;


import com.dkey.DkeyController.DkeyTabs;
import com.dkey.Interfaces.IResetTabListener;
import com.dkey.Interfaces.ITabSetChangeListener;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;

public class DkeyTabActivity extends TabActivity implements ITabSetChangeListener, IResetTabListener {

	private TabHost _tabHost;
	private int currentTab;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView( R.layout.dkeytemplate);
		_tabHost = getTabHost();  // The activity TabHost
		setupTabs();
		BaseActivity._currentActivity= this;
		DkeyApplication._controller.registerTabSetChangeListener(DkeyTabs.dkeytab, this);
		DkeyApplication._controller.registerResetTabListener(this);
		_tabHost.setOnTabChangedListener( new TabHost.OnTabChangeListener() {

			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				
				if( tabId.equals("Identify")) {
					DkeyApplication._controller.changeTabContent(DkeyTabs.key);
					currentTab = _tabHost.getCurrentTab();
				}
				else if (tabId.equals("organisms")) {
					DkeyApplication._controller.changeTabContent(DkeyTabs.species);
					currentTab = _tabHost.getCurrentTab();
				}
				else if (tabId.equals("Glossary")) {
					//yet to be written
					DkeyApplication._controller.changeTabContent(DkeyTabs.Glossary);
					currentTab = _tabHost.getCurrentTab();
				}
				else if (tabId.equals("Keys")) {
					//yet to be written
					currentTab = _tabHost.getCurrentTab();
				}
				else if (tabId.equals("Observe")) {
					//yet to be written
					DkeyApplication._controller.changeTabContent(DkeyTabs.dkeytab);
					DkeyApplication._controller.resetObserveTabSet();
				}
			}
			
		}); 
	}
	

	public void setupTabs() {

		_tabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
		TabHost.TabSpec spec;  // Resusable TabSpec for each tab
		setupTab( "Identify", DkeyActivityGroup.class );
		setupTab( "organisms", DkeySpeciesActivityGroup.class );
		setupTab( "Glossary", DkeyGlossaryGroup.class );
		setupTab( "Keys", Dkeys.class );
		setupTab( "Observe", DkeyObservation.class );
		_tabHost.setCurrentTab( 0 );
		currentTab = 0;

	}
	
	private void setupTab( final String tag, Class<?> activityClass) {
		View tabview = createTabView(_tabHost.getContext(), tag);
		TabSpec setContent = _tabHost.newTabSpec(tag).setIndicator(tabview).setContent(new Intent(this, activityClass));
		_tabHost.addTab(setContent);

	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.custom_tabs, null);
		//LayoutParams params = new LayoutParams(LayoutParams., 50);
		//view.setLayoutParams(params);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		tv.setTextSize(9);
		return view;
	}
	
	public void resetTabs() {
		_tabHost.setCurrentTab(0);
		_tabHost.clearAllTabs();
		setupTabs();
	}

		
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onRestart() {
		super.onRestart();
	}

	public void setPreviouslySelectedTab() {
		// TODO Auto-generated method stub
		_tabHost.setCurrentTab(currentTab);
		BaseActivity._currentActivity= this;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
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
