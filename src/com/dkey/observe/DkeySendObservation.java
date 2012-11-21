package com.dkey.observe;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.UUID;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dkey.BaseActivity;
import com.dkey.DkeyApplication;
import com.dkey.DkeyController;
import com.dkey.R;
import com.dkey.Interfaces.IGetNotesValuesListener;

public class DkeySendObservation extends BaseActivity {

	public static final int SEND_EMAIL = 2;
	private ObservationsDataSource datasource;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.dkeysend);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.sendlayout);
		super.addtitleBar(layout, R.layout.dkeyobservetitlebar,
				"Observation Deck", true, R.id.informationID, R.id.doneButtonID);
		Button sendEmail = (Button) findViewById(R.id.sendemail);
		Button saveObs = (Button) findViewById(R.id.saveobs);
		Button viewObsv = (Button) findViewById(R.id.viewobsv);

		viewObsv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						DkeyViewObservations.class);
				startActivity(i);

			}
		});

		saveObs.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * Uri uri = Uri.parse( "http://www.surveymonkey.com/s/GQRNM6Q"
				 * ); startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
				 */
				datasource = new ObservationsDataSource(getApplicationContext());
				datasource.open();
				datasource.addObservation();
				datasource.close();

				Toast.makeText(getApplicationContext(),
						"Observation saved :-)", Toast.LENGTH_SHORT).show();

			}
		});
		sendEmail.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				DkeyApplication._controller.updateAppConfig();
				if (!DkeyApplication._controller.isConfigIcomplete()) {
					String tempXmlFileName = Environment
							.getExternalStorageDirectory()
							+ "/"
							+ UUID.randomUUID().toString() + ".xml";

					File xmlFile = new File(tempXmlFileName);
					if (xmlFile.exists()) {
						xmlFile.delete();
					}
					DkeyApplication._controller.createXmlDoc(tempXmlFileName);
					Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
					i.setType("text/plain");
					SharedPreferences app_preferences = getApplicationContext()
							.getSharedPreferences("myPrefs",
									Context.MODE_WORLD_READABLE);
					;
					i.putExtra(Intent.EXTRA_EMAIL, new String[] {
							app_preferences.getString("ToEmailID", ""), "" });
					i.putExtra(Intent.EXTRA_SUBJECT,
							app_preferences.getString("Subject", ""));
					i.putExtra(Intent.EXTRA_TEXT,
							app_preferences.getString("Message", ""));
					ArrayList<Uri> uris = new ArrayList<Uri>();
					// convert from paths to Android friendly Parcelable Uri's
					if (DkeyApplication._controller.getImages() != null) {
						for (String file : DkeyApplication._controller
								.getImages()) {
							File fileIn = new File(file);
							Uri u = Uri.fromFile(fileIn);
							uris.add(u);
						}
					}
					File fileIn = new File(tempXmlFileName);
					Uri u = Uri.fromFile(fileIn);
					uris.add(u);

					i.setType("image/jpg;application/xml");
					i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
					try {
						getParent().startActivityForResult(
								Intent.createChooser(i, "Send mail..."),
								SEND_EMAIL);
					} catch (android.content.ActivityNotFoundException ex) {
						Toast.makeText(DkeySendObservation.this,
								"There are no email clients installed.",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					final AlertDialog.Builder b = new AlertDialog.Builder(
							getParent());
					b.setIcon(android.R.drawable.ic_dialog_alert);
					b.setTitle("Alert !!");
					b.setMessage("You have not configured your Email Settings.To do so click on OK");
					b.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									DkeyApplication._controller
											.setCurrentTabforObserveTabs(1);

								}
							});
					b.setNegativeButton(android.R.string.no, null);
					b.show();

				}
			}
		});

	}

}
