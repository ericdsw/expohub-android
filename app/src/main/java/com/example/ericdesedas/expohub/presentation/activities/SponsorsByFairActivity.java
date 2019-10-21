package com.example.ericdesedas.expohub.presentation.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.models.Sponsor;
import com.example.ericdesedas.expohub.presentation.adapters.RecyclerAdapterFactory;
import com.example.ericdesedas.expohub.presentation.adapters.SponsorListAdapter;
import com.example.ericdesedas.expohub.presentation.fragments.RouteDialogFragment;
import com.example.ericdesedas.expohub.presentation.presenters.SponsorsByFairPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SponsorsByFairActivity extends BaseActivity implements
    SponsorsByFairPresenter.View,
    SponsorListAdapter.Listener {

    public static final String KEY_FAIR_ID      = "fair_id";
    public static final String KEY_FAIR_NAME    = "fair_name";

    @BindView(R.id.toolbar)                 Toolbar toolbar;
    @BindView(R.id.recycler_view)           RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.network_progress)        ProgressBar networkProgressBar;
    @BindView(R.id.error_text)              TextView errorText;

    @Inject RecyclerAdapterFactory adapterFactory;
    @Inject SponsorsByFairPresenter presenter;

    private String fairId;
    private String fairName;
    private SponsorListAdapter sponsorListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fair_sponsors);

        ButterKnife.bind(this);

        fairId      = getIntent().getStringExtra(KEY_FAIR_ID);
        fairName    = getIntent().getStringExtra(KEY_FAIR_NAME);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(fairName);

        getActivityComponent().inject(this);

        sponsorListAdapter = adapterFactory.createSponsorListAdapter();
        setupUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sponsors_by_fair, menu);
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
            case R.id.action_route_info:
                RouteDialogFragment routeDialogFragment = RouteDialogFragment.newInstance("GET",
                        "/fairs/{id}/sponsors<br/><span style='color:red;'>?include=sponsorRank&sort=sponsor_rank_id</span>");
                routeDialogFragment.show(getSupportFragmentManager(), "");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupUI() {

        presenter.setView(this);

        recyclerView.setAdapter(sponsorListAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        sponsorListAdapter.setListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadSponsors();
            }
        });

        loadSponsors();
    }

    @Override
    public void onSponsorCellClick(Sponsor sponsor) {

    }

    @Override
    public void toggleLoading(boolean showLoading) {
        if (showLoading) {
            if (! sponsorListAdapter.hasSponsors()) {
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
    public void updateList(Sponsor[] sponsors) {

        sponsorListAdapter.updateList(sponsors);
        sponsorListAdapter.notifyDataSetChanged();

        if (sponsors.length <= 0) {
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(getString(R.string.empty_sponsors_error));
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

    private void loadSponsors() {
        errorText.setVisibility(View.GONE);
        presenter.onLoadSponsorsCommand(fairId);
    }
}
