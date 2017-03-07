package com.example.ericdesedas.expohub.presentation.activities;

import android.os.Bundle;
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
import android.widget.Toast;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.models.Stand;
import com.example.ericdesedas.expohub.helpers.image.ImageDownloader;
import com.example.ericdesedas.expohub.presentation.adapters.RecyclerAdapterFactory;
import com.example.ericdesedas.expohub.presentation.adapters.StandListAdapter;
import com.example.ericdesedas.expohub.presentation.fragments.RouteDialogFragment;
import com.example.ericdesedas.expohub.presentation.fragments.StandDetailsDialogFragment;
import com.example.ericdesedas.expohub.presentation.presenters.StandsByFairPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StandsByFairActivity extends BaseActivity implements
    StandsByFairPresenter.View,
    StandListAdapter.Listener {

    public static final String KEY_FAIR_ID      = "fair_id";
    public static final String KEY_FAIR_NAME    = "fair_name";

    @BindView(R.id.toolbar)                 Toolbar toolbar;
    @BindView(R.id.recycler_view)           RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.network_progress)        ProgressBar networkProgressBar;
    @BindView(R.id.error_text)              TextView errorText;

    @Inject RecyclerAdapterFactory adapterFactory;
    @Inject StandsByFairPresenter presenter;
    @Inject ImageDownloader imageDownloader;

    private String fairId;
    private String fairName;
    private StandListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fair_news);

        ButterKnife.bind(this);

        fairId      = getIntent().getStringExtra(KEY_FAIR_ID);
        fairName    = getIntent().getStringExtra(KEY_FAIR_NAME);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(fairName);

        getActivityComponent().inject(this);

        adapter = adapterFactory.createStandListAdapter();
        setupUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stands_by_fair, menu);
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
                RouteDialogFragment routeDialogFragment = RouteDialogFragment.newInstance("GET", "/fairs/{id}/stands");
                routeDialogFragment.show(getSupportFragmentManager(), "");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStandCardClick(Stand stand) {
        StandDetailsDialogFragment fragment = StandDetailsDialogFragment.newInstance(stand, imageDownloader);
        fragment.show(getSupportFragmentManager(), "");
    }

    @Override
    public void toggleLoading(boolean showLoading) {

        if (showLoading) {
            if (! adapter.hasStands()) {
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
    public void updateList(Stand[] stands) {

        adapter.updateList(stands);
        adapter.notifyDataSetChanged();

        if (stands.length <= 0) {
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(getString(R.string.empty_stands_error));
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

    private void setupUI() {

        presenter.setView(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter.setListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadStands();
            }
        });

        loadStands();
    }

    private void loadStands() {
        errorText.setVisibility(View.GONE);
        presenter.onLoadStandsCommand(fairId);
    }
}
