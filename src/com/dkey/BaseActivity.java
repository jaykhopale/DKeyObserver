package com.dkey;

import java.io.File;
import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dkey.DkeyController.DkeyTabs;



import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BaseActivity extends Activity {

	LayoutInflater inflater;
	protected ArrayList<View> history;
	public static BaseActivityGroup currentGroup;
	public static Activity _currentActivity;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.dkeytemplate);
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public void addtitleBar(ViewGroup view, int titleBarResourceID,
			String text, boolean isBackButtonVisible,
			int informationResourceID, int buttonID) {
		View titleBar = inflater.inflate(titleBarResourceID, null, false);
		view.addView(titleBar, 0);
		Button informationButton = (Button) titleBar
				.findViewById(informationResourceID);
		Button backButton = (Button) titleBar.findViewById(buttonID);
		backButton.setOnClickListener(new TitleOnClickListener());
		informationButton.setOnClickListener(new TitleOnClickListener());
		TextView titleText = (TextView) titleBar.findViewById(R.id.title);
		titleText.setText(text);
		informationButton.setVisibility(View.INVISIBLE);
		if (!isBackButtonVisible) {
			backButton.setVisibility(View.GONE);
			informationButton.setVisibility(View.INVISIBLE);
		} else {
			backButton.setVisibility(View.VISIBLE);
		}

	}

	protected void deleteOldPhotos() {
		for (int i = 0; i < 4; i++) {
			String filename = "picture_" + i + ".jpg";
			File pictureFile = new File(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/"
					+ filename);
			if (pictureFile.exists()) {
				pictureFile.delete();
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("xyz", "Hitting onactivityResult");
	}

	private class TitleOnClickListener implements View.OnClickListener {

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.backButtonID:
				currentGroup.back();
				if (currentGroup instanceof DkeyActivityGroup) {
					DkeyApplication._controller.getIsLeafNodeList().remove(
							(DkeyApplication._controller.getIsLeafNodeList()
									.size()) - 1);
					if (!DkeyApplication._controller.getIsLeafNodeList().get(
							DkeyApplication._controller.getIsLeafNodeList()
									.size() - 1)) {
						DkeyApplication._controller.setOrgID("-1");
						DkeyApplication._controller.processOrganismNode();
					}

				}
				break;
			case R.id.doneButtonID:
				final AlertDialog.Builder b = new AlertDialog.Builder(
						getParent());
				b.setIcon(android.R.drawable.ic_dialog_alert);
				b.setTitle("Alert !!");
				b.setMessage("Going Back would delete all the observations.Would you like to proceed?");
				b.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								BaseActivityGroup.currentGroup.back();
								DkeyApplication._controller
										.changeTabContent(DkeyTabs.dkeytab);
								DkeyApplication._controller
										.setSelectedTab(DkeyTabs.dkeytab);

							}
						});
				b.setNegativeButton(android.R.string.no, null);
				b.show();
				break;
			case R.id.informationID:
				final Dialog dialog = new Dialog(_currentActivity,
						R.style.infoDialog);
				dialog.setContentView(R.layout.dkeyinfo);
				dialog.setCancelable(true);
				RelativeLayout rlayout = (RelativeLayout) findViewById(R.id.dkeyinfo);
				Button doneButton = (Button) dialog
						.findViewById(R.id.doneButton);
				doneButton.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						dialog.cancel();
					}
				});
				NodeList list = DkeyApplication._controller.getDocBuilder()
						.getElementsByTagName("splash");
				ArrayList<MoreInfoRow> rows = new ArrayList<MoreInfoRow>();
				if (list != null && list.getLength() > 0) {
					NodeList children = list.item(0).getChildNodes();
					for (int j = 0; j < children.getLength(); j++) {
						Node organism = children.item(j);
						if (organism.getChildNodes().item(0) != null
								&& organism.getChildNodes().item(0)
										.getNodeValue() != null
								&& !organism.getChildNodes().item(0)
										.getNodeValue().equals("")) {
							MoreInfoRow row = new MoreInfoRow();
							row.key = children.item(j).getNodeName()
									.replace("_", " ");
							row.separator = ":";
							row.value = organism.getChildNodes().item(0)
									.getNodeValue();
							rows.add(row);
						}
					}

					int rowCount = 0;
					MoreInfoRow[] rowArray = new MoreInfoRow[rows.size()];
					for (MoreInfoRow moreInfoRow : rows) {
						rowArray[rowCount] = moreInfoRow;
						rowCount++;
					}
					MoreInfoAdapter adapter = new MoreInfoAdapter(getParent(),
							R.layout.dkeymoreinfo_row, rowArray);
					ListView moreInfoList = (ListView) dialog
							.findViewById(R.id.keyinfolist);
					moreInfoList.setAdapter(adapter);
				}
				dialog.show();
				break;
			}
		}
	}

	public void setOrgID(String i) {
		DkeyApplication._controller.setOrgID(i);
	}

	@Override
	public void onBackPressed() {
		Context context;
		if (getParent() instanceof ActivityGroup) {
			ActivityGroup grp = (ActivityGroup) getParent();
			context = grp.getParent();
		} else {
			context = getParent();
		}
		final AlertDialog.Builder b = new AlertDialog.Builder(context);
		b.setIcon(android.R.drawable.ic_dialog_alert);
		b.setTitle("Alert !!");
		b.setMessage("Do you  want to quit the Application?");
		b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();

			}
		});
		b.setNegativeButton(android.R.string.no, null);
		b.show();
	}

}
