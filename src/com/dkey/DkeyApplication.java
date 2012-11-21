package com.dkey;

import java.io.IOException;

import org.xml.sax.SAXException;

import android.app.Application;
import android.content.res.Resources;
import android.provider.Settings.Secure;
import android.widget.Toast;

public class DkeyApplication extends Application {

	public static DkeyController _controller;
	public static Resources _resources;
	public void onCreate() {
		super.onCreate();
		_resources = getResources();
		try {
			_controller = new DkeyController( this );
			DkeyController.DEVICE_ID = this.getDeviceID();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public String getDeviceID() {
		return Secure.getString(this.getContentResolver(),Secure.ANDROID_ID).toString();
	}
	
 
}
