package com.example.ericdesedas.expohub.presentation.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

import com.example.ericdesedas.expohub.presentation.activities.EventsByFairActivity;
import com.example.ericdesedas.expohub.presentation.activities.FairDetailsActivity;
import com.example.ericdesedas.expohub.presentation.activities.LoginRegisterActivity;
import com.example.ericdesedas.expohub.presentation.activities.NewsByFairActivity;
import com.example.ericdesedas.expohub.presentation.activities.ProfileActivity;
import com.example.ericdesedas.expohub.presentation.activities.StandsByFairActivity;
import com.example.ericdesedas.expohub.presentation.presenters.StandsByFairPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Navigator class
 * Manages navigation between app components and transition animation logic between them
 */
public class Navigator {

    private Activity activity;
    private List<Pair<View, String>> transitioningElements;

    /**
     * Constructor
     * @param activity the {@link Activity} reference that will perform the navigation
     */
    public Navigator(Activity activity) {
        this.activity = activity;
        this.transitioningElements = new ArrayList<>();
    }

    // ======================================== Navigation ======================================== //

    public void navigateToFairDetailsActivity(String fairId) {

        Intent intent = new Intent(activity, FairDetailsActivity.class);
        intent.putExtra(FairDetailsActivity.KEY_FAIR_ID, fairId);

        executeNavigation(intent);
    }

    public void navigateToEventsByFairActivity(String fairId, String fairName) {

        Intent intent = new Intent(activity, EventsByFairActivity.class);
        intent.putExtra(EventsByFairActivity.KEY_FAIR_ID, fairId);
        intent.putExtra(EventsByFairActivity.KEY_FAIR_NAME, fairName);

        executeNavigation(intent);
    }

    public void navigateToNewsByFairActivity(String fairId, String fairName) {

        Intent intent = new Intent(activity, NewsByFairActivity.class);
        intent.putExtra(EventsByFairActivity.KEY_FAIR_ID, fairId);
        intent.putExtra(EventsByFairActivity.KEY_FAIR_NAME, fairName);

        executeNavigation(intent);
    }

    public void navigateToStandsByFairActivity(String fairId, String fairName) {

        Intent intent = new Intent(activity, StandsByFairActivity.class);
        intent.putExtra(StandsByFairActivity.KEY_FAIR_ID, fairId);
        intent.putExtra(StandsByFairActivity.KEY_FAIR_NAME, fairName);

        executeNavigation(intent);
    }

    public void navigateToLoginRegisterActivity() {

        Intent intent = new Intent(activity, LoginRegisterActivity.class);
        executeNavigation(intent);
    }

    public void navigateToProfileActivity() {

        Intent intent = new Intent(activity, ProfileActivity.class);
        executeNavigation(intent);
    }

    // ======================================== Transition ======================================== //

    /**
     * Adds an element to the transition animation logic
     * @param view the {@link View} reference to be transitioned
     * @param string the transition name
     * @return
     */
    public Navigator addTransitioningElement(View view, String string) {
        transitioningElements.add(new Pair<>(view, string));
        return this;
    }

    /**
     * Overwrites current transition element list
     * @param transitioningElements an {@link List<Pair<View, String>>} reference of transitioning elements.
     * @return
     */
    public Navigator setTransitioningElements(List<Pair<View, String>> transitioningElements) {
        this.transitioningElements = transitioningElements;
        return this;
    }

    // Private Methods

    /**
     * Executes required navigation with transition parameters
     * This method will only execute transitions on supported operative systems (v21 and above)
     * Finally, it clears the transitioningElements reference to avoid animation leaks
     *
     * @param intent the {@link Intent} to be executed
     */
    private void executeNavigation(Intent intent) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Pair<View, String>[] transitionPairs = (Pair[]) transitioningElements.toArray(new Pair[transitioningElements.size()]);

            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity, transitionPairs
            );
            activity.startActivity(intent, optionsCompat.toBundle());

        } else {
            activity.startActivity(intent);
        }

        transitioningElements.clear();
    }
}
