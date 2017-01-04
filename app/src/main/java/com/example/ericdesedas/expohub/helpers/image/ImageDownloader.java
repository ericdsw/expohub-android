package com.example.ericdesedas.expohub.helpers.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.File;

/**
 * Abstracts the management of images that are downloaded from the net
 */
public interface ImageDownloader {

	/**
	 * Sets image from url to specified imageView
	 * @param url
	 * @param imageView
	 */
	void setImage(String url, ImageView imageView);

	/**
	 * Sets image from url to specified imageView
	 * crops the result inside a circle
	 * @param url
	 * @param imageView
	 */
	void setCircularImage(String url, ImageView imageView);

	/**
	 * Sets image from url to specified imageView
	 * crops the result inside a circle
	 * Will display placeholder image resource while request is executing
	 * @param url
	 * @param imageView
	 * @param placeholderImageId
	 */
	void setCircularImage(String url, ImageView imageView, int placeholderImageId);

	/**
	 * Sets image from local file to imageView
	 * Crops the result inside a circle
	 * @param file
	 * @param imageView
	 */
	void setCircularImage(File file, ImageView imageView);

	/**
	 * Loads the image from the network and fires specified callback
	 * @param url
	 * @param callback
	 */
	void setImage(String url, Callback callback);

	/**
	 * Limits max image size load on memory
	 * @param imageSize
	 */
	ImageDownloader setMaxImageSize(int imageSize);

	interface Callback {
		void onLoaded(Bitmap bitmap);
	}
}
