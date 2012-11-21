package com.dkey;

import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DkeyGlossaryDetails extends BaseActivity {
 
	public static final String NOIMAGE = "ni";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dkeyglossarydetails);
		RelativeLayout rLayout = (RelativeLayout) findViewById( R.id.glossarydetails);
		super.addtitleBar( rLayout, R.layout.dkeytitlebar,  "Glossary Details", true, R.id.informationID, R.id.backButtonID);
		TextView glossTitle = (TextView) rLayout.findViewById(R.id.glosstitle);
		TextView glossDesc = (TextView) rLayout.findViewById(R.id.glossdesc);
		ImageView glossImg = (ImageView) rLayout.findViewById(R.id.glossaryimage);
		Bundle bundle = getIntent().getExtras();
		glossTitle.setText(bundle.getString("title"));
		glossDesc.setText(bundle.getString("desc"));
		String imageName = bundle.getString("image");
		imageName = imageName.toLowerCase();
		imageName = imageName.split("\\.")[0];
		if ( !imageName.equals(NOIMAGE) ) {
			glossImg.setImageResource(getResources().getIdentifier( imageName,"drawable",getPackageName()));
		}
		else {
			glossImg.setVisibility(View.GONE);
		}
	}

}
