package com.dkey.observe;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.dkey.BaseActivity;
import com.dkey.CameraView;
import com.dkey.DkeyApplication;
import com.dkey.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
public class CameraDemo extends BaseActivity implements OnClickListener, Camera.PictureCallback {
	CameraView cameraView;
	Camera _camera;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
		super.deleteOldPhotos();
		setContentView(R.layout.dkeycamera);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.cameraLayout);
		super.addtitleBar(layout, R.layout.dkeytitlebar,  "Camera", false, R.id.informationID, R.id.backButtonID);
		Button pictureButton = (Button)layout.findViewById(R.id.Button01);
		cameraView = (CameraView) this.findViewById(R.id.CameraView01);
		android.widget.ZoomControls zoomControls = (android.widget.ZoomControls) findViewById(R.id.CAMERA_ZOOM_CONTROLS);
		cameraView.setZoomControls(zoomControls );
		Button viewPhotoButton = (Button)layout.findViewById(R.id.ViewPhotos);
		viewPhotoButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				DkeyApplication._controller.setCurrentTabforObserveTabs(2);
				
			}
		});
		pictureButton.setOnClickListener(this);

	}
	
	public void onClick(View v)
	{
		if(DkeyApplication._controller.getSnapCount() <= 3) {
			
			cameraView.takePicture(null, null, null, this);
			
		}
		else {
			//show dialog and ask user to delete photos in view photos
		}
	}
	// From the Camera.PictureCallback
	public void onPictureTaken(byte[] data, Camera camera)
	{
		_camera = camera;
		Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
		int w = bmp.getWidth();
		int h = bmp.getHeight();
		// Setting post rotate to 90
		Matrix mtx = new Matrix();
		mtx.postRotate(90);
		// Rotating Bitmap
		Bitmap rotatedBMP = Bitmap.createBitmap(bmp, 0, 0, w, h, mtx, true);
		//imv.setImageBitmap(bmp);    
		String filename = "picture_"+DkeyApplication._controller.getSnapCount() + 1 + ".jpg";
		File pictureFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename);
		if( pictureFile.exists() ) {
			pictureFile.delete();
		}
		DkeyApplication._controller.setSnapCount(DkeyApplication._controller.getSnapCount() + 1);
		DkeyApplication._controller.addImages(pictureFile.getAbsolutePath());
		try
		{
			FileOutputStream pfos = new FileOutputStream(pictureFile);
			rotatedBMP.compress(CompressFormat.JPEG, 75, pfos);
			pfos.flush();
			pfos.close();
			
			camera.startPreview();
			DkeyApplication._controller.loadPhotos();
			/*
			FileOutputStream outStream = new FileOutputStream(filename);
			outStream.write(data);
			outStream.close();
			*/
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

	}
}