package com.example.ericdesedas.expohub.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.ericdesedas.expohub.data.models.News;
import com.example.ericdesedas.expohub.presentation.adapters.NewsDetailAdapter;
import com.example.ericdesedas.expohub.presentation.adapters.RecyclerAdapterFactory;
import com.example.ericdesedas.expohub.presentation.fragments.RouteDialogFragment;
import com.example.ericdesedas.expohub.presentation.presenters.NewsDetailPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsDetailActivity extends BaseActivity implements
        NewsDetailPresenter.View {

    public static final String KEY_NEWS_ID = "news-id";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.network_progress) ProgressBar networkProgressBar;
    @BindView(R.id.error_text) TextView errorText;

    @Inject NewsDetailPresenter newsDetailPresenter;
    @Inject RecyclerAdapterFactory recyclerAdapterFactory;

    private String newsId;
    private NewsDetailAdapter newsDetailAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        getActivityComponent().inject(this);

        newsId = getIntent().getStringExtra(KEY_NEWS_ID);

        setupUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_route_info:
                RouteDialogFragment routeDialogFragment = RouteDialogFragment.newInstance("GET",
                        "/news/{id}<br /><span style='color:red;'>?include=comments,comments.user</span>");
                routeDialogFragment.show(getSupportFragmentManager(), "");
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        newsDetailPresenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        newsDetailPresenter.onStop();
    }

    @Override
    public void toggleLoading(boolean showLoading) {
        if (showLoading) {
            networkProgressBar.setVisibility(View.VISIBLE);
        } else {
            networkProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateNews(News news) {
        newsDetailAdapter.setNews(news);
        newsDetailAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(int code, String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    private void setupUI() {

        newsDetailPresenter.setView(this);

        newsDetailAdapter = recyclerAdapterFactory.createNewsDetailAdapter();
        recyclerView.setAdapter(newsDetailAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        newsDetailPresenter.onLoadSingleNewsCommand(newsId);
    }
}
