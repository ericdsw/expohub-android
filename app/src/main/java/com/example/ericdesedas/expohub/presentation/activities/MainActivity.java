package com.example.ericdesedas.expohub.presentation.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.events.FairListClickEvent;
import com.example.ericdesedas.expohub.data.events.FairListRefreshEvent;
import com.example.ericdesedas.expohub.data.events.SuccessfulAuthEvent;
import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.data.models.Session;
import com.example.ericdesedas.expohub.presentation.adapters.RecyclerAdapterFactory;
import com.example.ericdesedas.expohub.presentation.adapters.TabAdapter;
import com.example.ericdesedas.expohub.presentation.fragments.FairListFragment;
import com.example.ericdesedas.expohub.presentation.fragments.MyEventsFragment;
import com.example.ericdesedas.expohub.presentation.navigation.Navigator;
import com.example.ericdesedas.expohub.presentation.presenters.MainScreenPresenter;
import com.example.ericdesedas.expohub.presentation.viewmodels.ProfileDrawerViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements
    MainScreenPresenter.View,
    ProfileDrawerViewModel.Listener {

    @BindView(R.id.toolbar)         Toolbar toolbar;
    @BindView(R.id.tab_layout)      TabLayout tabLayout;
    @BindView(R.id.view_pager)      ViewPager viewPager;
    @BindView(R.id.add_fair_button) FloatingActionButton addFairButton;
    @BindView(R.id.navigation_view) NavigationView navigationView;
    @BindView(R.id.drawer_layout)   DrawerLayout drawerLayout;

    @Inject MainScreenPresenter presenter;
    @Inject RecyclerAdapterFactory adapterFactory;
    @Inject EventBus eventBus;
    @Inject Navigator navigator;

    private FairListFragment fairListFragment;
    private TabAdapter tabAdapter;
    private ProfileDrawerViewModel profileDrawerViewModel;

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
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
        presenter.onStart();
        presenter.onRefreshUserDataCommand();
    }

    @Override
    protected void onStop() {
        super.onStop();
        eventBus.unregister(this);
        presenter.onStop();
    }

    // Interface callbacks

    @Override
    public void toggleLoading(boolean showLoading) {
        fairListFragment.toggleLoading(showLoading);
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
    public void showUnidentifiedUser() {
        profileDrawerViewModel.formatUnidentified();
    }

    @Override
    public void showIdentifiedUser(Session session) {
        profileDrawerViewModel.showSessionData(session);
    }

    // Clicks

    @OnClick(R.id.add_fair_button)
    public void onAddFairClick() {
        Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show();
    }

    // Events

    @Subscribe
    public void onFairListClickEvent(FairListClickEvent event) {
        navigator.setTransitioningElements(event.transitioningElements)
                .navigateToFairDetailsActivity(event.fairId);
    }

    @Subscribe
    public void onFairListRefreshEvent(FairListRefreshEvent event) {
        presenter.onLoadFairsCommand();
    }

    // Private Methods

    /**
     * Executes all required logic to initialize user interface dependencies.
     * This method should contain all UI logic other than binding, and should be called at onCreate()
     */
    private void setupUI() {

        fairListFragment = FairListFragment.newInstance(adapterFactory.createFairListAdapter(), eventBus);

        presenter.setView(this);
        presenter.initialize();

        tabAdapter.addFragment(getString(R.string.title_fairs_fragment), fairListFragment);
        tabAdapter.addFragment(getString(R.string.title_my_events_fragment), MyEventsFragment.newInstance());

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        presenter.onLoadFairsCommand();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    addFairButton.animate().scaleX(1).scaleY(1).start();
                } else {
                    addFairButton.animate().scaleX(0).scaleY(0).start();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                drawerLayout.closeDrawers();
                handleDrawerSelection(item);

                return false;
            }
        });
    }

    /**
     * Handles menu selection
     * @param menuItem the selected {@link MenuItem} instance
     */
    private void handleDrawerSelection(MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.action_trending:
                break;

            case R.id.action_settings:
                break;

            case R.id.action_about:
                break;

            default:
                break;
        }
    }

    @Override
    public void onProfileClick() {

    }

    @Override
    public void onLoginButtonClick() {
        navigator.navigateToLoginRegisterActivity();
    }
}
