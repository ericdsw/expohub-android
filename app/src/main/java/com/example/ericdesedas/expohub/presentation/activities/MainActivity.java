package com.example.ericdesedas.expohub.presentation.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.events.FairListClickEvent;
import com.example.ericdesedas.expohub.data.events.FairListRefreshEvent;
import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.presentation.adapters.RecyclerAdapterFactory;
import com.example.ericdesedas.expohub.presentation.adapters.TabAdapter;
import com.example.ericdesedas.expohub.presentation.fragments.FairListFragment;
import com.example.ericdesedas.expohub.presentation.fragments.MyEventsFragment;
import com.example.ericdesedas.expohub.presentation.navigation.Navigator;
import com.example.ericdesedas.expohub.presentation.presenters.MainScreenPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Application's main activity
 */
public class MainActivity extends BaseActivity implements
    MainScreenPresenter.View {

    // UI References
    @BindView(R.id.toolbar)         Toolbar toolbar;
    @BindView(R.id.tab_layout)      TabLayout tabLayout;
    @BindView(R.id.view_pager)      ViewPager viewPager;

    // Adapters
    TabAdapter tabAdapter;

    // Injected Dependencies

    @Inject
    MainScreenPresenter presenter;

    @Inject
    RecyclerAdapterFactory adapterFactory;

    @Inject
    EventBus eventBus;

    @Inject
    Navigator navigator;

    private FairListFragment fairListFragment;

    // Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Setup
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Injection
        getActivityComponent().inject(this);

        // UI Setup
        setSupportActionBar(toolbar);
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        setupUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
        presenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    }
}
