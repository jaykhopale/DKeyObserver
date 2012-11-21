package com.dkey.observe;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dkey.BaseActivity;
import com.dkey.DkeyApplication;
import com.dkey.R;
import com.dkey.R.layout;
import com.dkey.Interfaces.ILoadBigImageListener;
import com.dkey.Interfaces.ILoadPhotoListener;

public class DkeyViewPhotos extends BaseActivity implements ILoadPhotoListener, ILoadBigImageListener {
	private static final int IMAGE_PICK = 11;
	private Gallery gallery;
	private ImageView _bigImgView;
	private String currentBigImage;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		DkeyApplication._controller.registerLoadPhotoListener(this);
		DkeyApplication._controller.registerLoadBigImageListener(this);
		loadNoPhotoGallery();
		if (DkeyApplication._controller.getSnapCount() == 0) {
			loadNoPhotoGallery();
		} else {
			loadPhotos();
			loadBigImage(0);		
		}
	}
	
	private void loadNoPhotoGallery() {
		setContentView(R.layout.dkeynophoto);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.nophotolayout);
		super.addtitleBar(layout, R.layout.dkeyobservetitlebar,
				"Photo Gallery", true, R.id.informationID,
				R.id.doneButtonID);
		Button takepicture = (Button) layout.findViewById(R.id.takepicture);
		takepicture.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				loadCamera();
				
			}
		});
		
		/*
		takepicture.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				loadGallery();
				
			}
		});
		*/
				
	}
	
	
	protected void loadGallery() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, IMAGE_PICK);
		
	}

	protected void loadCamera() {
			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
		    //intent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );
			DkeyApplication._controller.setIsCameraOn(true);
		    getParent().startActivityForResult(intent,0);
		
	}
	
	public void loadBigImage( int pos) {
		this.getImageBitmap( _bigImgView,DkeyApplication._controller.getImages().elementAt(pos), true);
		currentBigImage = DkeyApplication._controller.getImages().elementAt(pos);
	}
	
	
	public void loadPhotos() {
		setContentView(R.layout.dkeyphotogallery);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.photogallery);
		super.addtitleBar(layout, R.layout.dkeyobservetitlebar,
				"Photo Gallery", true, R.id.informationID,
				R.id.doneButtonID);
		_bigImgView = (ImageView) findViewById(R.id.ImageView01);
		_bigImgView.setScaleType(ScaleType.CENTER_INSIDE);
		gallery = (Gallery) findViewById(R.id.examplegallery);
		Button deleteButton = (Button) findViewById( R.id.DeletePhoto);
		deleteButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				reloadPhotos();
			}
		});
		
		Button takepicture = (Button) layout.findViewById(R.id.takepicture);
		takepicture.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				loadCamera();
				
			}
		});
		
		
		gallery.setAdapter(new GalleryAdapter(this));

		gallery.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView parent, View v,
					int position, long id) {

				getImageBitmap(_bigImgView, DkeyApplication._controller.getImages().elementAt(position), true);
				currentBigImage = DkeyApplication._controller.getImages().elementAt(position);

			}

		});
	}
	
	public void reloadPhotos() {
		int currentBigImgIndex = DkeyApplication._controller.getImages().indexOf(currentBigImage);
		String deletedImage = currentBigImage;
		DkeyApplication._controller.getImages().remove(currentBigImage);
		if(currentBigImgIndex > 0) {
			loadPhotos();
			loadBigImage( currentBigImgIndex -1);
			
		}
		else {
			if( DkeyApplication._controller.getImages().size() > 0 ) {	
				loadPhotos();
				loadBigImage(0);
			}
			else {
				loadNoPhotoGallery();
			}
		}
		File pictureFile = new File(deletedImage);
		if( pictureFile.exists() ) {
			pictureFile.delete();
		}
	}

	
	public static Bitmap loadResizedBitmap( String filename, int width, int height, boolean exact ) {
	    Bitmap bitmap = null;
	    BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile( filename, options );
	    if ( options.outHeight > 0 && options.outWidth > 0 ) {
	        options.inJustDecodeBounds = false;
	        options.inSampleSize = 2;
	        while (    options.outWidth  / options.inSampleSize > width
	                && options.outHeight / options.inSampleSize > height ) {
	            options.inSampleSize++;
	        }
	        options.inSampleSize--;

	        bitmap = BitmapFactory.decodeFile( filename, options );
	        if ( bitmap != null && exact ) {
	            bitmap = Bitmap.createScaledBitmap( bitmap, width, height, false );
	        }
	    }
	    return bitmap;
	}
	 private void getImageBitmap( ImageView view, String url, boolean isBig) { 
         Bitmap bm = null; 
         Bitmap bmp = BitmapFactory.decodeFile(url);
		// Setting post rotate to 90
		Matrix mtx = new Matrix();
		mtx.postRotate(90);
		// Rotating Bitmap
		if( isBig) {
			bm = DkeyViewPhotos.loadResizedBitmap(url, bmp.getWidth()/2, bmp.getHeight()/2, true);
		}
		else {
			bm = DkeyViewPhotos.loadResizedBitmap(url, 70, 80, true);
		}
		 int w = bm.getWidth();
		 int h = bm.getHeight();
		 if( DkeyApplication._controller.ShouldRotate()) {
			 bm = Bitmap.createBitmap(bm, 0, 0, w, h, mtx, true); 
		 }
			 view.setImageBitmap(bm);
     } 

	class GalleryAdapter extends BaseAdapter {

		int GalItemBg;
		private Context cont;

		public GalleryAdapter(Context c) {

			cont = c;

			TypedArray typArray = obtainStyledAttributes(R.styleable.GalleryTheme);

			GalItemBg = typArray.getResourceId(
					R.styleable.GalleryTheme_android_galleryItemBackground, 1);

			typArray.recycle();

		}

		public int getCount() {
			
			return DkeyApplication._controller.getImages().size(); 
			
		}

		public Object getItem(int position) {

			return position;

		}

		public long getItemId(int position) {

			return position;

		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imgView = null;
			if(convertView == null) {
				imgView = new ImageView(cont);
				convertView = imgView;
			}
			else {
				imgView = (ImageView) convertView;
			}

			
			getImageBitmap(imgView , DkeyApplication._controller.getImages().elementAt(position), true);
			imgView.setScaleType(ImageView.ScaleType.FIT_XY);
			imgView.setLayoutParams(new Gallery.LayoutParams(150,120));
			imgView.setBackgroundResource(GalItemBg);
			return imgView;

		}

	}

}
