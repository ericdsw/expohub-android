package com.example.ericdesedas.expohub.presentation.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.events.FairEventListClickEvent;
import com.example.ericdesedas.expohub.data.events.FairEventListRefreshEvent;
import com.example.ericdesedas.expohub.data.events.FairListClickEvent;
import com.example.ericdesedas.expohub.data.events.FairListRefreshEvent;
import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.data.models.User;
import com.example.ericdesedas.expohub.presentation.adapters.FairListAdapter;
import com.example.ericdesedas.expohub.presentation.adapters.RecyclerAdapterFactory;
import com.example.ericdesedas.expohub.presentation.adapters.TabAdapter;
import com.example.ericdesedas.expohub.presentation.fragments.FairEventListFragment;
import com.example.ericdesedas.expohub.presentation.fragments.FairListFragment;
import com.example.ericdesedas.expohub.presentation.navigation.Navigator;
import com.example.ericdesedas.expohub.presentation.presenters.ProfilePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends BaseActivity implements
    AppBarLayout.OnOffsetChangedListener,
    ProfilePresenter.View {

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;

    @BindView(R.id.toolbar)     Toolbar toolbar;
    @BindView(R.id.user_image)  ImageView userImage;
    @BindView(R.id.name)        TextView userName;
    @BindView(R.id.username)    TextView userUsername;
    @BindView(R.id.tab_layout)  TabLayout tabLayout;
    @BindView(R.id.viewpager)   ViewPager viewPager;
    @BindView(R.id.appbar)      AppBarLayout appBarLayout;

    @Inject ProfilePresenter presenter;
    @Inject RecyclerAdapterFactory recyclerAdapterFactory;
    @Inject EventBus eventBus;
    @Inject Navigator navigator;

    private ProgressDialog fetchingDataDialog;
    private ProgressDialog logoutProgressDialog;
    private FairListFragment fairListFragment;
    private FairEventListFragment fairEventListFragment;
    private boolean isAvatarShown = true;
    private int maxScrollSize;
    private TabAdapter tabAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        getActivityComponent().inject(this);

        tabAdapter = new TabAdapter(getSupportFragmentManager());

        fetchingDataDialog = new ProgressDialog(this);
        fetchingDataDialog.setCancelable(false);
        fetchingDataDialog.setMessage(getString(R.string.fetching_user_data));

        logoutProgressDialog = new ProgressDialog(this);
        logoutProgressDialog.setCancelable(false);
        logoutProgressDialog.setMessage(getString(R.string.logging_out_message));

        appBarLayout.addOnOffsetChangedListener(this);

        setupUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout:
                presenter.onLogoutCommand();
                break;
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
        presenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        eventBus.unregister(this);
        presenter.onStop();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (maxScrollSize == 0)
            maxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(verticalOffset)) * 100 / maxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && isAvatarShown) {
            isAvatarShown = false;
            userImage.animate().scaleY(0).scaleX(0).setDuration(200).start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !isAvatarShown) {
            isAvatarShown = true;

            userImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }

    @Override
    public void toggleLoading(boolean showLoading) {

        if (showLoading) {
            if (! fetchingDataDialog.isShowing()) {
                fetchingDataDialog.show();
            }
        } else {
            if (fetchingDataDialog.isShowing()) {
                fetchingDataDialog.dismiss();
            }
        }
    }

    @Override
    public void showError(int statusCode, String error) {

        if (statusCode == 500) {
            error = getString(R.string.generic_network_error);
        }

        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showUser(User user) {

        userName.setText(user.name);
        userUsername.setText(String.format(getString(R.string.label_username_profile), user.username));
        presenter.onFetchUserFairsCommand();
        presenter.onFetchUserAttendingFairEventsCommand();
    }

    @Override
    public void closeForInvalidUser() {

        Toast.makeText(this, getString(R.string.internal_auth_error), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void toggleLoggingOutLoading(boolean showLoading) {

        if (showLoading) {
            if (! logoutProgressDialog.isShowing()) {
                logoutProgressDialog.show();
            }
        } else {
            if (logoutProgressDialog.isShowing()) {
                logoutProgressDialog.dismiss();
            }
        }
    }

    @Override
    public void loggedOut() {
        Toast.makeText(this, getString(R.string.logged_out_message), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void toggleFairLoading(boolean showLoading) {
        fairListFragment.toggleLoading(showLoading);
    }

    @Override
    public void toggleFairEventsLoading(boolean showLoading) {
        fairEventListFragment.toggleLoading(showLoading);
    }

    @Override
    public void showUserFairs(Fair[] fairs) {
        fairListFragment.updateList(fairs);
    }

    @Override
    public void showAttendingFairEvents(FairEvent[] fairEvents) {
        fairEventListFragment.updateList(fairEvents);
    }

    // Subscribe

    @Subscribe
    public void onFairListRefreshEvent(FairListRefreshEvent event) {
        presenter.onFetchUserFairsCommand();
    }

    @Subscribe
    public void onFairEventListRefreshEvent(FairEventListRefreshEvent event) {
        presenter.onFetchUserAttendingFairEventsCommand();
    }

    @Subscribe
    public void onFairEventListClickEvent(FairEventListClickEvent event) {
        navigator.setTransitioningElements(event.transitioningElements)
                .navigateToFairEventDetailsActivity(event.fairEventId);
    }

    @Subscribe
    public void onFairListClickEvent(FairListClickEvent event) {
        navigator.setTransitioningElements(event.transitioningElements)
                .navigateToFairDetailsActivity(event.fairId);
    }

    @OnClick(R.id.add_fair_button)
    public void onAddFairButtonClick() {
        Toast.makeText(this, getString(R.string.not_implemented), Toast.LENGTH_LONG).show();
    }

    // Private methods

    private void setupUI() {

        presenter.setView(this);
        presenter.initialize();

        fairListFragment = FairListFragment.newInstance(recyclerAdapterFactory.createFairListAdapter(),
                eventBus, FairListAdapter.VIEW_TYPE_CONDENSED);

        fairEventListFragment = FairEventListFragment.newInstance(recyclerAdapterFactory.createEventListAdapter(), eventBus);

        tabAdapter.addFragment(getString(R.string.title_my_fairs_fragment), fairListFragment);
        tabAdapter.addFragment(getString(R.string.title_my_attending_events), fairEventListFragment);

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        presenter.onFetchUserDataCommand();
    }
}
