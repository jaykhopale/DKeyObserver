package com.dkey;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ZoomControls;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {
	SurfaceHolder mHolder;
	int width;
	int height;
	ZoomControls _zoomControls;
	int currentZoomLevel = 0, maxZoomLevel = 0;

	Camera mCamera;

	public CameraView(Context context, AttributeSet attrs) {
		super(context, attrs);
		holderCreation();
	}

	public CameraView(Context context) {
		super(context);
		holderCreation();
	}

	public void setZoomControls(ZoomControls zoomControls) {
		_zoomControls = zoomControls;
	}

	public void takePicture(Camera.ShutterCallback shutter,
			Camera.PictureCallback raw, Camera.PictureCallback postview,
			Camera.PictureCallback jpeg) {
		mCamera.takePicture(shutter, raw, postview, jpeg);
	}

	public void holderCreation() {
		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, acquire the camera and tell it where to
		// draw.
		mCamera = Camera.open();
		Parameters params = mCamera.getParameters();
		mCamera.setDisplayOrientation(90);
		params.set("orientation", "portait");
		// If we aren't landscape (the default), tell the camera we want
		// portrait mode

		if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
			params.set("orientation", "portrait"); // "landscape"
			// And Rotate the final picture if possible
			// This works on 2.0 and higher only
			// params.setRotation(90);
			// Use reflection to see if it exists and to call it so you can
			// support older versions
			try {
				Method rotateSet = Camera.Parameters.class.getMethod(
						"setRotation", new Class[] { Integer.TYPE });
				Object arguments[] = new Object[] { new Integer(90) };
				rotateSet.invoke(params, arguments);
			} catch (NoSuchMethodException nsme) {
				// Older Device
				Log.v("CAMERAVIEW", "No Set Rotation");
			} catch (IllegalArgumentException e) {
				Log.v("CAMERAVIEW", "Exception IllegalArgument");
			} catch (IllegalAccessException e) {
				Log.v("CAMERAVIEW", "Illegal Access Exception");
			} catch (InvocationTargetException e) {
				Log.v("CAMERAVIEW", "Invocation Target Exception");
			}
		}
		mCamera.setParameters(params);
		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException exception) {
			mCamera.release();
			mCamera = null;
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return, so stop the preview.
		// Because the CameraDevice object is not a shared resource, it's very
		// important to release it when the activity is paused.
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		width = w;
		height = h;
		// Now that the size is known, set up the camera parameters and begin
		// the preview.
		Camera.Parameters parameters = mCamera.getParameters();
		// parameters.setPreviewSize(w, h);
		mCamera.setParameters(parameters);
		mCamera.startPreview();

		if (parameters.isZoomSupported()) {
			maxZoomLevel = parameters.getMaxZoom();

			_zoomControls.setIsZoomInEnabled(true);
			_zoomControls.setIsZoomOutEnabled(true);

			_zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (currentZoomLevel < 500) {
						currentZoomLevel++;
						mCamera.startSmoothZoom(currentZoomLevel);
					}
				}
			});

			_zoomControls.setOnZoomOutClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (currentZoomLevel > 0) {
						currentZoomLevel--;
						mCamera.startSmoothZoom(currentZoomLevel);
					}
				}
			});

		}
		else {
			_zoomControls.hide();
		}
	}
}