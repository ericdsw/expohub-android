package com.example.ericdesedas.expohub.presentation.activities;

import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.models.News;
import com.example.ericdesedas.expohub.presentation.adapters.NewsListAdapter;
import com.example.ericdesedas.expohub.presentation.adapters.RecyclerAdapterFactory;
import com.example.ericdesedas.expohub.presentation.fragments.RouteDialogFragment;
import com.example.ericdesedas.expohub.presentation.navigation.Navigator;
import com.example.ericdesedas.expohub.presentation.presenters.NewsByFairPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsByFairActivity extends BaseActivity implements
    NewsByFairPresenter.View,
    NewsListAdapter.Listener {

    public static final String KEY_FAIR_ID      = "fair_id";
    public static final String KEY_FAIR_NAME    = "fair_name";

    @BindView(R.id.toolbar)                 Toolbar toolbar;
    @BindView(R.id.recycler_view)           RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.network_progress)        ProgressBar networkProgressBar;
    @BindView(R.id.error_text)              TextView errorText;

    @Inject RecyclerAdapterFactory adapterFactory;
    @Inject NewsByFairPresenter presenter;
    @Inject Navigator navigator;

    private String fairId;
    private String fairName;
    private NewsListAdapter adapter;

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

        adapter = adapterFactory.createNewsListAdapter();
        setupUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_by_fair, menu);
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
                RouteDialogFragment routeDialogFragment = RouteDialogFragment.newInstance("GET", "/fairs/{id}/news");
                routeDialogFragment.show(getSupportFragmentManager(), "");
                break;
        }
        return true;
    }

    @Override
    public void toggleLoading(boolean showLoading) {

        if (showLoading) {
            if (! adapter.hasNews()) {
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
    public void updateList(News[] newses) {

        adapter.updateList(newses);
        adapter.notifyDataSetChanged();

        if (newses.length <= 0) {
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(getString(R.string.empty_news_error));
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
    public void onNewsCardClick(News news) {
        navigator.navigateToNewsDetailActivity(news.getId());
    }

    private void setupUI() {

        presenter.setView(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter.setListener(this);

        swipeRefreshLayout.setOnRefreshListener(() -> loadNews());

        loadNews();
    }

    private void loadNews() {
        errorText.setVisibility(View.GONE);
        presenter.onLoadEventsCommand(fairId);
    }
}
