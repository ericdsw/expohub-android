package com.example.ericdesedas.expohub.presentation.activities;

import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.presentation.adapters.EventListAdapter;
import com.example.ericdesedas.expohub.presentation.adapters.RecyclerAdapterFactory;
import com.example.ericdesedas.expohub.presentation.fragments.RouteDialogFragment;
import com.example.ericdesedas.expohub.presentation.navigation.Navigator;
import com.example.ericdesedas.expohub.presentation.presenters.EventsByFairPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventsByFairActivity extends BaseActivity implements
    EventsByFairPresenter.View,
    EventListAdapter.Listener {

    public static final String KEY_FAIR_ID      = "fair_id";
    public static final String KEY_FAIR_NAME    = "fair_name";

    @BindView(R.id.toolbar)                 Toolbar toolbar;
    @BindView(R.id.recycler_view)           RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.network_progress)        ProgressBar networkProgressBar;
    @BindView(R.id.error_text)              TextView errorText;

    @Inject RecyclerAdapterFactory recyclerAdapterFactory;
    @Inject EventsByFairPresenter presenter;
    @Inject Navigator navigator;

    private String fairId;
    private String fairName;
    private EventListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fair_events);

        ButterKnife.bind(this);

        fairId      = getIntent().getStringExtra(KEY_FAIR_ID);
        fairName    = getIntent().getStringExtra(KEY_FAIR_NAME);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(fairName);

        // Injection
        getActivityComponent().inject(this);

        adapter = recyclerAdapterFactory.createEventListAdapter();
        setupUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_events_by_fair, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_route_info:
                RouteDialogFragment routeDialogFragment = RouteDialogFragment.newInstance("GET",
                        "/fairs/{id}/fairEvents<br /><span style='color:red;'>?include=eventType</span>");
                routeDialogFragment.show(getSupportFragmentManager(), "");
                break;
        }
        return true;
    }

    @Override
    public void toggleLoading(boolean showLoading) {

        if (showLoading) {
            if (! adapter.hasFairEvents()) {
                networkProgressBar.setVisibility(View.VISIBLE);
            }
        } else {
            networkProgressBar.setVisibility(View.GONE);
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    @Override
    public void updateEventList(FairEvent[] fairEvents) {

        adapter.swapList(fairEvents);
        adapter.notifyDataSetChanged();

        if (fairEvents.length <= 0) {
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(getString(R.string.empty_events_error));
        }
    }

    @Override
    public void showError(int code, String error) {

        errorText.setVisibility(View.VISIBLE);
        if (code == 500) {
            errorText.setText(getString(R.string.generic_network_error));
        } else {
            errorText.setText(error);
        }
    }

    @Override
    public void onEventCellClick(FairEvent fairEvent, List<Pair<View, String>> transitioningElements) {
        navigator.setTransitioningElements(transitioningElements)
                .navigateToFairEventDetailsActivity(fairEvent.getId());
    }

    private void setupUI() {

        presenter.setView(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter.setListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFairs();
            }
        });

        loadFairs();
    }

    private void loadFairs() {
        errorText.setVisibility(View.GONE);
        presenter.onLoadEventsCommand(fairId);
    }
}
