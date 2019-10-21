package com.example.ericdesedas.expohub.helpers.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.io.File;

/**
 * Image downloader that uses Picasso library for the image handling
 */
public class PicassoImageDownloader implements ImageDownloader {

	private Picasso picasso;
	private int maxImageSize 			= 100;
	private boolean shouldResizeImage 	= false;

	public PicassoImageDownloader(Context context) {
		picasso = new Picasso.Builder(context)
				.build();
	}

	@Override
	public void setImage(String url, ImageView imageView) {
		try {
			prepareParameters(picasso.load(url))
					.into(imageView);
		}
		catch (IllegalArgumentException exception) {

		}

		// Reset resizing
		shouldResizeImage = false;
	}

	@Override
	public void setCircularImage(String url, ImageView imageView) {
		try {
			prepareParameters(picasso.load(url))
					.transform(new CircleTransform())
					.into(imageView);
		}
		catch (IllegalArgumentException exception) {

		}

		// Reset resizing
		shouldResizeImage = false;
	}

	@Override
	public void setCircularImage(String url, ImageView imageView, int placeholderImageId) {
		try {
			prepareParameters(picasso.load(url))
					.transform(new CircleTransform())
					.placeholder(placeholderImageId)
					.error(placeholderImageId)
					.into(imageView);
		}
		catch (IllegalArgumentException exception) {

		}

		// Reset resizing
		shouldResizeImage = false;
	}

	@Override
	public void setCircularImage(File file, ImageView imageView) {

		try {
			prepareParameters(picasso.load(file))
					.transform(new CircleTransform())
					.into(imageView);
		}
		catch (IllegalArgumentException exception) {

		}

		// Reset resizing
		shouldResizeImage = false;
	}

	@Override
	public void setImage(String url, final Callback callback) {
		prepareParameters(picasso.load(url))
				.into(new Target() {
					@Override
					public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
						callback.onLoaded(bitmap);
					}

					@Override
					public void onBitmapFailed(Exception exception, Drawable errorDrawable) {
						Log.e("PicassoImageDownloader", "Bitmap failed");
					}

					@Override
					public void onPrepareLoad(Drawable placeHolderDrawable) {

					}
				});

		// Reset resizing
		shouldResizeImage = false;
	}

	@Override
	public ImageDownloader setMaxImageSize(int imageSize) {
		this.maxImageSize = imageSize;
		shouldResizeImage = true;

		return this;
	}

	/**
	 * Appends common parameters to picasso's request creator
	 * @param requestCreator a {@link RequestCreator} instance
	 * @return a configured {@link RequestCreator}
	 */
	private RequestCreator prepareParameters(RequestCreator requestCreator) {

		if(shouldResizeImage) {
			requestCreator = requestCreator.resize(maxImageSize, maxImageSize)
					.centerInside();
		}

		return requestCreator;
	}
}
