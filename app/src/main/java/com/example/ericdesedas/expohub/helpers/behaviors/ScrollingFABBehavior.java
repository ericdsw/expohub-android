package com.example.ericdesedas.expohub.helpers.behaviors;


import android.content.Context;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * ScrollingFABBehavior class
 * External dependency obtained from the following post: http://stackoverflow.com/a/39366576/2049873
 * Credit to StackOverflow user Abdul Rizwan
 */
public class ScrollingFABBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    private static final String TAG = "ScrollingFABBehavior";
    Handler mHandler;

    public ScrollingFABBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, final FloatingActionButton child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);

        if (mHandler == null)
            mHandler = new Handler();


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                child.animate().scaleX(1).scaleY(1).start();
                Log.d("FabAnim","startHandler()");
            }
        },1000);

    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        //child -> Floating Action Button
        if (dyConsumed > 0) {
            Log.d("Scrolling","Up");
//            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
//            int fab_bottomMargin = layoutParams.bottomMargin;
            child.animate().scaleX(0).scaleY(0).start();
        } else if (dyConsumed < 0) {
            Log.d("Scrolling","down");
            child.animate().scaleX(1).scaleY(1).start();
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        if(mHandler!=null) {
            mHandler.removeMessages(0);
            Log.d("Scrolling","stopHandler()");
        }
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }
}
