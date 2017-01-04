package com.example.ericdesedas.expohub.presentation.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.ericdesedas.expohub.R;

public class AspectRatioImageView extends ImageView {

	private float aspectRatio;

	/**
	 * Default Constructor
	 * Custom parameters will be passed inside AttributeSet
	 *
	 * @param context 	the {@link Context} that will be used to fetch the parameters
	 * @param attrs		the {@link AttributeSet} required by the ImageView Widget
	 */
	public AspectRatioImageView(Context context, AttributeSet attrs) {

		super(context, attrs);
		TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView, 0, 0);

		try {
			aspectRatio = array.getFloat(R.styleable.AspectRatioImageView_heightRatio, 0);
		} finally {
			array.recycle();
		}
	}

	@Override
	public void onMeasure(int widthSpec, int heightSpec) {
		int height = Math.round(MeasureSpec.getSize(widthSpec) * aspectRatio);
		int calculatedHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
		super.onMeasure(widthSpec, calculatedHeightSpec);
	}
}
