package com.dkey.observe;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkey.BaseActivity;
import com.dkey.DkeyApplication;
import com.dkey.R;
import com.dkey.Interfaces.IUpdateAppConfiguration;
import com.dkey.Interfaces.IUpdateAppConfiguration;;

public class DkeyConfig extends BaseActivity implements IUpdateAppConfiguration {

	EditText toemailid;
	EditText fromemailid;
	EditText subject;
	EditText message;
	EditText maximages;
	EditText username;
	boolean inComplete;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dkeyconfig);
		LinearLayout layout = (LinearLayout) findViewById( R.id.config);
        super.addtitleBar(layout,R.layout.dkeyobservetitlebar, "Configuration", true, R.id.informationID, R.id.doneButtonID );
        DkeyApplication._controller.registerAppConfigurer( this );
        DkeyApplication._controller.setIsConfigModified(true);
        loadConfigValues();
	}

	private void loadConfigValues() {
		
		toemailid = (EditText) findViewById(R.id.toemailid);
		toemailid.setText(getString("ToEmailID"));
		
		fromemailid = (EditText) findViewById(R.id.fromemailid);
		fromemailid.setText(getString("FromEmailID"));
		
		subject = (EditText) findViewById(R.id.subject);
		subject.setText(getString("Subject"));
		
		message = (EditText) findViewById(R.id.message);
		message.setText(getString("Message"));
		
		maximages = (EditText) findViewById(R.id.maximages);
		maximages.setText(getString("MaxImages"));
		
		username = (EditText) findViewById(R.id.username);
		username.setText(getString("Username"));
		
		
	}


	private String getString(String key) {
		String value = DkeyApplication._controller.getSharedPrefs().getString(key, null);
		if( value == null || value.equals("") ) {
		  if( !DkeyApplication._controller.isConfigIcomplete()) {
			  DkeyApplication._controller.setConfigIncomplete(true);
		  }
		}
		return value;
	}
	
	public void UpdateConfigValues( String key, EditText view) {
		Editor editor = DkeyApplication._controller.getSharedPrefs().edit();
		editor.putString(key, getStringFromView(view));
		editor.commit();
	}

	public void updateData() {
			DkeyApplication._controller.setConfigIncomplete(false);
			UpdateConfigValues("ToEmailID",toemailid);
			UpdateConfigValues("FromEmailID",fromemailid);
			UpdateConfigValues("Subject",subject);
			UpdateConfigValues("Message",message);
			UpdateConfigValues("MaxImages",maximages);
			UpdateConfigValues("Username",username);
	}
	
	public String getStringFromView( EditText view ) {
		String value = view.getText().toString();
		if( value == null || value.equals("") ) {
		  if( !DkeyApplication._controller.isConfigIcomplete()) {
			  DkeyApplication._controller.setConfigIncomplete(true);
		  }
		}
		return value;
	}
}
