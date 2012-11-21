
package com.dkey;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dkey.DkeySpecies.SpeciesHolder;
import com.dkey.R.id;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DkeyMoreInfo extends BaseActivity {

	private NodeList list;
	private String orgID;
	private LinearLayout moreinfotable;
	ListView moreInfoList;
	int moreInfoSize;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dkmoreinfo);
		moreinfotable = (LinearLayout) findViewById(R.id.moreinfo);
		super.addtitleBar(moreinfotable,R.layout.dkeytitlebar,  "More Information", true, R.id.informationID, R.id.backButtonID);
		Bundle bundle = getIntent().getExtras();
		orgID = bundle.getString("orgID");
		ArrayList<MoreInfoRow> rows = DkeyApplication._controller.processOrganismInformation( orgID);
		int rowCount = 0;
		MoreInfoRow[] rowArray = new MoreInfoRow[rows.size()];
		for(MoreInfoRow moreInfoRow : rows ) {
			rowArray[rowCount] = moreInfoRow;
			rowCount++;
		}
		MoreInfoAdapter adapter = new MoreInfoAdapter(this, R.layout.dkeymoreinfo_row, rowArray );
		moreInfoList = (ListView) findViewById(R.id.moreinfotable);
		moreInfoList.setAdapter(adapter);
	}


}