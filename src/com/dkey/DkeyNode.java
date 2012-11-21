package com.dkey;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class DkeyNode extends BaseActivity {

	/** Called when the activity is first created. */
	private Node currentNode;
	String leftChildId;
	private String rightChildId;
	private String isNextleafnode;
	private NodeList list;
	private String currentNodeId;
	private String orgId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dkeynode);
		RelativeLayout layout = (RelativeLayout) findViewById( R.id.branchnode);
        DkeyApplication._controller.getIsLeafNodeList().add(false);
        super.addtitleBar(layout,R.layout.dkeytitlebar, "Dichotomous Key", true, R.id.informationID, R.id.backButtonID );
        super.setOrgID( "-1" );
		Button topbutton = (Button) findViewById(R.id.topbutton);
		Button bottombutton = (Button) findViewById(R.id.bottombutton);
		Bundle bundle = getIntent().getExtras();
		currentNodeId = bundle.getString("Node");
		/**
		Button startoverButton  = (Button) findViewById(R.id.Startoverbutton);
		startoverButton.setOnClickListener(new View.OnClickListener(){
			
			public void onClick(View v) {
					

			}
		});
*/
		topbutton.setOnClickListener(new View.OnClickListener() {

			
			public void onClick(View v) {
				checkNextNode(leftChildId);
				if( isNextleafnode.equals("no")) {
					Intent intent = new Intent(v.getContext(), DkeyNode.class);
					intent.putExtra("Node", leftChildId);
					View view = DkeyActivityGroup.group.getLocalActivityManager()
					        .startActivity("DkeyNode", intent
					        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
					        .getDecorView();

					        // Again, replace the view
					        DkeyActivityGroup.group.replaceView(view);
					
				} else if(isNextleafnode.equals("yes") ) {
					Intent intent = new Intent(v.getContext(), DkeyLeafNode.class);
					intent.putExtra("Node", leftChildId);
					View view = DkeyActivityGroup.group.getLocalActivityManager()
					        .startActivity("DkeyLeafNode", intent
					        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
					        .getDecorView();

					        // Again, replace the view
					        DkeyActivityGroup.group.replaceView(view);
				}

			}
		});
		
		bottombutton.setOnClickListener(new View.OnClickListener() {

			
			public void onClick(View v) {
				checkNextNode(rightChildId);
				if( isNextleafnode.equals("no")) {
					Intent intent = new Intent(v.getContext(), DkeyNode.class);
					intent.putExtra("Node", rightChildId);
					View view = DkeyActivityGroup.group.getLocalActivityManager()
					        .startActivity("DkeyNode", intent
					        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
					        .getDecorView();

					        // Again, replace the view
					        DkeyActivityGroup.group.replaceView(view);
				}
				else if(isNextleafnode.equals("yes")) {
					Intent intent = new Intent(v.getContext(), DkeyLeafNode.class);
					intent.putExtra("Node", rightChildId);
					View view = DkeyActivityGroup.group.getLocalActivityManager()
					        .startActivity("DkeyLeafNode", intent
					        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
					        .getDecorView();

					        // Again, replace the view
					        DkeyActivityGroup.group.replaceView(view);
				}

			}
		});
		DkeyApplication app = (DkeyApplication) getApplication();
		list = app._controller.getDocBuilder().getElementsByTagName("Node");
		boolean found =false;
		for(int i = 0; i< list.getLength(); i++){     	
			NodeList children = list.item(i).getChildNodes();
			boolean rightNode = false;
			for(int j = 0; j < children.getLength(); j++){ 
				if( children.item(j).getNodeName().equals("nodeID")){
					if(children.item(j).getChildNodes().item(0).getNodeValue().equals(currentNodeId)) {
						currentNode = list.item(i);
						found=true;
						rightNode=true;
					}
				}
				if(rightNode){
					if(children.item(j).getNodeName().equals("leftChildDescription")){
						Node leftChildDescription = children.item(j);
						topbutton.setText(leftChildDescription.getChildNodes().item(0).getNodeValue());
					}
					else if(children.item(j).getNodeName().equals("rightChildDescription")){
						Node rightChildDescription = children.item(j);
						bottombutton.setText(rightChildDescription.getChildNodes().item(0).getNodeValue());
					}
					else if(children.item(j).getNodeName().equals("leftChildID")){
						Node leftChildIdNode = children.item(j);
						leftChildId = leftChildIdNode.getChildNodes().item(0).getNodeValue();
					}
					else if(children.item(j).getNodeName().equals("rightChildID")){
						Node rightChildIdNode = children.item(j);
						rightChildId = rightChildIdNode.getChildNodes().item(0).getNodeValue();
					}
					else if(children.item(j).getNodeName().equals("isLeafNode")){
						Node isLeafNode = children.item(j);
						isNextleafnode = isLeafNode.getChildNodes().item(0).getNodeValue();
					}
					

				}

				// Node parentattr = children.item(j).getAttributes().getNamedItem("parentID");

			}
			if (found){
				break;
			}
		}


	}

	private void checkNextNode(String nodeId){
		boolean found =false;
		isNextleafnode= "no";
		for(int i = 0; i< list.getLength(); i++){     	
			NodeList children = list.item(i).getChildNodes();
			boolean rightNode = false;
			for(int j = 0; j < children.getLength(); j++){ 
				if( children.item(j).getNodeName().equals("nodeID")){
					if(children.item(j).getChildNodes().item(0).getNodeValue().equals(nodeId)) {
						currentNode = list.item(i);
						found=true;
						rightNode=true;
					}
				}
				if(rightNode){
					if( children.item(j).getNodeName().equals("isLeafNode")){
						isNextleafnode=children.item(j).getChildNodes().item(0).getNodeValue();	
					}
				}
			}
			if (found){
				break;
			}
		}	
	}
}
