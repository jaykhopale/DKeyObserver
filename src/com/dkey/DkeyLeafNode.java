package com.dkey;

import java.io.File;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DkeyLeafNode extends BaseActivity {

	private NodeList list;
	private String node;
	private String orgID;
	DkeyApplication app;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dkleafnode);
		DkeyApplication._controller.getIsLeafNodeList().add(true);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.leafnode);
		super.addtitleBar(layout, R.layout.dkeytitlebar, "Dichotomous key",
				true, R.id.informationID, R.id.backButtonID);
		ImageView view = (ImageView) findViewById(R.id.plantimage);
		view.setImageResource(R.drawable.oak);
		Bundle bundle = getIntent().getExtras();
		node = bundle.getString("Node");
		app = (DkeyApplication) getApplication();
		list = app._controller.getDocBuilder().getElementsByTagName("Node");
		for (int i = 0; i < list.getLength(); i++) {
			NodeList children = list.item(i).getChildNodes();
			boolean rightNode = false;
			for (int j = 0; j < children.getLength(); j++) {
				if (children.item(j).getNodeName().equals("nodeID")) {
					if (children.item(j).getChildNodes().item(0).getNodeValue()
							.equals(node)) {
						rightNode = true;
					}
				}
				if (rightNode) {
					if (children.item(j).getNodeName().equals("organism_id")) {
						Node organism = children.item(j);
						orgID = organism.getChildNodes().item(0).getNodeValue();
						super.setOrgID(orgID);
						DkeyApplication._controller.processOrganismNode();
						break;
					}
				}
			}
		}

		Button moreinfobutton = (Button) findViewById(R.id.moreinfobutton);
		moreinfobutton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), DkeyMoreInfo.class);
				intent.putExtra("orgID", orgID);
				View view = DkeyActivityGroup.group
						.getLocalActivityManager()
						.startActivity("DkeyMoreInfo",
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
								.getDecorView();

				// Again, replace the view
				DkeyActivityGroup.group.replaceView(view);
			}

		});

		loadOrganism();
	}

	private void loadOrganism() {
		list = app._controller.getDocBuilder().getElementsByTagName("organism");
		boolean found = false;
		for (int i = 0; i < list.getLength(); i++) {
			NodeList children = list.item(i).getChildNodes();
			boolean rightNode = false;
			boolean isImageDone = false;
			String currentOrgID = null;
			for (int j = 0; j < children.getLength(); j++) {
				if (!rightNode && children.item(j).getNodeName().equals("org_id")) {
					Node organismIDNode = children.item(j);
					currentOrgID = organismIDNode.getChildNodes().item(0)
							.getNodeValue();
					if (currentOrgID.equals(orgID)) {
						rightNode = true;
					}
				}

				if ( rightNode && !isImageDone) {
					if (children.item(j).getNodeName().equals("image")) {
						isImageDone = true;
						Node image = children.item(j);
						String imageName = image.getChildNodes().item(0).getNodeValue();
						imageName = imageName.toLowerCase();
						imageName = imageName.split("\\.")[0];
						ImageView plantimage = (ImageView) findViewById(R.id.plantimage);
						try {
							plantimage.setImageResource(getResources().getIdentifier(imageName,"drawable",getPackageName()));
						}
						catch( Exception ex) {
							plantimage.setImageResource(R.drawable.oak);
						}
						finally {
							
						}
					}
				}

				if (rightNode) {
					if (children.item(j).getNodeName().equals("genus")) {
						Node common_name = children.item(j);
						String name = common_name.getChildNodes().item(0).getNodeValue();
						TextView imagetext = (TextView) findViewById(R.id.imagetext);
						imagetext.setText(name);
						//break;
					}
				}
			}
		}
	}
}
