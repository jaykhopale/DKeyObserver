package com.dkey;
/*
Copyright (c) 2010 Nandeesh C Rajashekar, Marguerite C. Murphy & Christopher D. Smith, SFSU

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:
	
	The above copyright notice and this permission notice shall be included in
	all copies or substantial portions of the Software.
	
	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	THE SOFTWARE.
	
*/
import java.util.ArrayList;

import com.dkey.DkeyController.DkeyTabs;
import com.dkey.Interfaces.IObserveTabChangeListener;
import com.dkey.observe.DkeyConfig;
import com.dkey.observe.DkeyNotes;
import com.dkey.observe.DkeySendObservation;
import com.dkey.observe.DkeyViewPhotos;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class DkeyObserveTabActivity extends TabActivity implements
		IObserveTabChangeListener {

	private TabHost _tabHost;
	private int currentTab;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dkeytemplate);
		DkeyApplication._controller.registerObserveTabChangeListener(this);
		_tabHost = getTabHost();
		setupTabs();
		BaseActivity._currentActivity = this;
		_tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				currentTab = _tabHost.getCurrentTab();
				if (tabId.equals("key")) {
					DkeyApplication._controller.changeTabContent(DkeyTabs.key);
				} else if (tabId.equals("species")) {
					DkeyApplication._controller
							.changeTabContent(DkeyTabs.species);
				} else if (tabId.equals("Glossary")) {
					// yet to be written
				} else if (tabId.equals("Keys")) {
					// yet to be written
				}

			}
		});
	}

	public void setupTabs() {
		_tabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);	
		_tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		setupTab("Field Notes",DkeyNotes.class);
		setupTab("Config", DkeyConfig.class);		
		// addTab( "Camera", R.drawable.icon, CameraDemo.class );
		setupTab("View Photos", DkeyViewPhotos.class);
		setupTab("Send", DkeySendObservation.class);
		_tabHost.setCurrentTab(0);
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
		tv.setTextSize(12);
		return view;
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		DkeyApplication._controller.setIsCameraOn(false);
		if (data != null) {
			if (requestCode == 11) {

				Log.d("fgd", "vdf");
			}
			if( requestCode == DkeySendObservation.SEND_EMAIL) {
				if( data != null ) {
					Toast toast = Toast.makeText(this," The email was sent successfully",1000);
					toast.show();
				}
			} else {

				Cursor cursor = null;
				int datacolumn;
				if (data.getData() == null) {
					// for samsung phones
					Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
					String[] proj = { MediaStore.Images.Media.DATA };
					final String selection = MediaStore.Images.Media.BUCKET_ID
							+ "=?";
					String CAMERA_BUCKET_NAME = Environment
							.getExternalStorageDirectory().toString()
							+ "/DCIM/Camera";
					final String CAMERA_BUCKET_ID = getBucketID(CAMERA_BUCKET_NAME);
					final String[] selectionArgs = { CAMERA_BUCKET_ID };
					cursor = this.getContentResolver().query(uri, proj,
							selection, selectionArgs, null);
					DkeyApplication._controller.setShouldRotateImage(true);
				} else {

					Uri uri1 = data.getData();
					String[] proj1 = { MediaStore.Images.Media.DATA };
					cursor = managedQuery(uri1, proj1, null, null, null);
					datacolumn = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					DkeyApplication._controller.setShouldRotateImage(false);
				}

				if (cursor.moveToLast()) {
					if (cursor.isLast()) {
						datacolumn = cursor
								.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						String s = cursor.getString(datacolumn);
						Log.d("Finally", s);
						DkeyApplication._controller.addImages(s);
						DkeyApplication._controller.loadPhotos();
						DkeyApplication._controller.loadBigImage();
					}
				}
			}
		}

	}

	private String getBucketID(String path) {
		return String.valueOf(path.toLowerCase().hashCode());
	}

	public void addTab(String tag, int drawableId, Class<?> activityClass) {

		TabHost.TabSpec spec = _tabHost.newTabSpec(tag);
		View view = LayoutInflater.from(this).inflate(R.layout.tabindicator,
				getTabWidget(), false);
		ImageView icon = (ImageView) view.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		TextView textView = (TextView) view.findViewById(R.id.tabtitle);

		/** The text view is null in landscape mode, We display only Image view. */
		if (textView != null) {
			textView.setText(tag);
		}
		spec.setIndicator(view);

		Intent intent = new Intent(this, activityClass);
		spec.setContent(intent);

		/**
		 * State used to decide the UI of the child activity. Currently used in
		 * meeting activity
		 */

		if (_tabHost.getChildCount() > 0) {
			// add separator
			_tabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
		}
		_tabHost.addTab(spec);

	}

	public void setPreviouslySelectedTab() {
		// TODO Auto-generated method stub
		_tabHost.setCurrentTab(currentTab);
	}

	public void changeCurrentObserveTab(int i) {
		_tabHost.setCurrentTab(i);

	}
	
		
}
