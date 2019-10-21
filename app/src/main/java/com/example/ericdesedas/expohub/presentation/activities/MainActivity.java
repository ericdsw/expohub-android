package com.example.ericdesedas.expohub.presentation.activities;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.events.FairEventListClickEvent;
import com.example.ericdesedas.expohub.data.events.FairEventListRefreshEvent;
import com.example.ericdesedas.expohub.data.events.FairListClickEvent;
import com.example.ericdesedas.expohub.data.events.FairListRefreshEvent;
import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.data.models.Session;
import com.example.ericdesedas.expohub.helpers.preferences.PreferenceHelper;
import com.example.ericdesedas.expohub.presentation.adapters.RecyclerAdapterFactory;
import com.example.ericdesedas.expohub.presentation.adapters.TabAdapter;
import com.example.ericdesedas.expohub.presentation.fragments.DisclaimerDialogFragment;
import com.example.ericdesedas.expohub.presentation.fragments.FairEventListFragment;
import com.example.ericdesedas.expohub.presentation.fragments.FairListFragment;
import com.example.ericdesedas.expohub.presentation.fragments.RouteDialogFragment;
import com.example.ericdesedas.expohub.presentation.navigation.Navigator;
import com.example.ericdesedas.expohub.presentation.presenters.MainScreenPresenter;
import com.example.ericdesedas.expohub.presentation.viewmodels.ProfileDrawerViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements
    MainScreenPresenter.View,
    ProfileDrawerViewModel.Listener {

    @BindView(R.id.toolbar)         Toolbar toolbar;
    @BindView(R.id.tab_layout)      TabLayout tabLayout;
    @BindView(R.id.view_pager)      ViewPager viewPager;
    @BindView(R.id.navigation_view) NavigationView navigationView;
    @BindView(R.id.drawer_layout)   DrawerLayout drawerLayout;
    @BindView(R.id.root_view)       View rootView;

    @Inject MainScreenPresenter presenter;
    @Inject RecyclerAdapterFactory adapterFactory;
    @Inject EventBus eventBus;
    @Inject Navigator navigator;
    @Inject PreferenceHelper preferenceHelper;

    private FairListFragment fairListFragment;
    private FairEventListFragment fairEventListFragment;
    private TabAdapter tabAdapter;
    private ProfileDrawerViewModel profileDrawerViewModel;

    private int currentFragmentOffset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getActivityComponent().inject(this);

        setSupportActionBar(toolbar);
        tabAdapter              = new TabAdapter(getSupportFragmentManager());
        profileDrawerViewModel  = new ProfileDrawerViewModel(navigationView.getHeaderView(0), this);
        setupUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    protected void onStart() {

        super.onStart();
        eventBus.register(this);
        presenter.onStart();

        presenter.onRefreshUserDataCommand();

        presenter.onLoadFairsCommand();
        presenter.onLoadTrendingEventsCommand();
    }

    @Override
    protected void onStop() {
        super.onStop();
        eventBus.unregister(this);
        presenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileDrawerViewModel.releaseViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_route_info:
                RouteDialogFragment routeDialogFragment = null;
                switch (currentFragmentOffset) {
                    case 0:
                         routeDialogFragment = RouteDialogFragment.newInstance("GET", "/fairs");
                        break;
                    case 1:
                        routeDialogFragment = RouteDialogFragment.newInstance("GET",
                                "/fairEvents<br/><span style='color:red;'>?sort=-attendance&include=eventType&page[cursor]=0&page[limit]=7</span>");
                        break;
                }
                if (routeDialogFragment != null) {
                    routeDialogFragment.show(getSupportFragmentManager(), "");
                }
                break;
        }
        return true;
    }

    // Interface callbacks

    @Override
    public void toggleLoading(boolean showLoading) {
        fairListFragment.toggleLoading(showLoading);
    }

    @Override
    public void toggleFairEventsLoading(boolean showLoading) {
        fairEventListFragment.toggleLoading(showLoading);
    }

    @Override
    public void updateFairList(Fair[] fairs) {
        fairListFragment.updateList(fairs);
    }

    @Override
    public void showError(int statusCode, String error) {
        fairListFragment.showError(statusCode, error);
    }

    @Override
    public void showFairEventsError(int code, String error) {
        fairEventListFragment.showError(code, error);
    }

    @Override
    public void showUnidentifiedUser() {
        profileDrawerViewModel.formatUnidentified();
    }

    @Override
    public void showIdentifiedUser(Session session) {
        profileDrawerViewModel.showSessionData(session);
    }

    @Override
    public void showTrendingFairEvents(FairEvent[] fairEvents) {
        fairEventListFragment.updateList(fairEvents);
    }

    // Events

    @Subscribe
    public void onFairListClickEvent(FairListClickEvent event) {
        navigator.setTransitioningElements(event.transitioningElements)
                .navigateToFairDetailsActivity(event.fairId);
    }

    @Subscribe
    public void onFairEventListClickEvent(FairEventListClickEvent eventListClickEvent) {
        navigator.setTransitioningElements(eventListClickEvent.transitioningElements)
                .navigateToFairEventDetailsActivity(eventListClickEvent.fairEventId);
    }

    @Subscribe
    public void onFairListRefreshEvent(FairListRefreshEvent event) {
        presenter.onLoadFairsCommand();
    }

    @Subscribe
    public void onFairEventRefreshEvent(FairEventListRefreshEvent event) {
        presenter.onLoadTrendingEventsCommand();
    }

    // Private Methods

    /**
     * Executes all required logic to initialize user interface dependencies.
     * This method should contain all UI logic other than binding, and should be called at onCreate()
     */
    private void setupUI() {

        fairListFragment = FairListFragment.newInstance(adapterFactory.createFairListAdapter(), eventBus);
        fairEventListFragment = FairEventListFragment.newInstance(adapterFactory.createEventListAdapter(), eventBus);

        presenter.setView(this);
        presenter.initialize();

        tabAdapter.addFragment(getString(R.string.title_fairs_fragment), fairListFragment);
        tabAdapter.addFragment(getString(R.string.title_trending), fairEventListFragment);

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener((MenuItem item) -> {
            drawerLayout.closeDrawers();
            handleDrawerSelection(item);

            return false;
        });

        // Check for current Fragment offset
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                currentFragmentOffset = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        // Check if disclaimer dialog must be shown
        if (! preferenceHelper.readBooleanPref(PreferenceHelper.DO_NOT_SHOW_DISCLAIMER, false)) {
            DisclaimerDialogFragment dialogFragment = DisclaimerDialogFragment.newInstance(preferenceHelper);
            dialogFragment.show(getSupportFragmentManager(), getString(R.string.disclaimer));
        }
    }

    /**
     * Handles menu selection
     * @param menuItem the selected {@link MenuItem} instance
     */
    private void handleDrawerSelection(MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.action_settings:
                navigator.navigateToSettingsActivity();
                break;

            case R.id.action_about:
                navigator.navigateToAboutActivity();
                break;

            default:
                break;
        }
    }

    @Override
    public void onProfileClick() {
        navigator.setTransitioningElements(profileDrawerViewModel.getTransitioningElements())
                .navigateToProfileActivity();
    }

    @Override
    public void onLoginButtonClick() {
        navigator.navigateToLoginRegisterActivity();
    }
}
