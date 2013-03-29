package com.dkey;

import java.io.File;
import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Dkey extends BaseActivity {
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dkstart);
        RelativeLayout layout = (RelativeLayout) findViewById( R.id.startlayout);
        super.addtitleBar(layout,R.layout.dkeytitlebar, "Dichotomous key", false, R.id.informationID, R.id.backButtonID);
        Button button = (Button) findViewById(R.id.startbutton);
        DkeyApplication._controller.getIsLeafNodeList().add(false);
        super.setOrgID( new Integer( -1 ).toString() );
        button.setOnClickListener(new View.OnClickListener() {
			
			
			public void onClick(View v) {
				Intent intent = new Intent( v.getContext() ,DkeyNode.class);
				intent.putExtra("Node", "0");
				
		        // Create the view using FirstGroup's LocalActivityManager
		        View view = DkeyActivityGroup.group.getLocalActivityManager()
		        .startActivity("DkeyNode", intent
		        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
		        .getDecorView();

		        // Again, replace the view
		        DkeyActivityGroup.group.replaceView(view);
				
			}
		});
        
    }
    
    
    
}