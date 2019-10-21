package com.example.ericdesedas.expohub.presentation.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
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
import com.example.ericdesedas.expohub.presentation.fragments.CreateCommentDialogFragment;
import com.example.ericdesedas.expohub.presentation.fragments.RouteDialogFragment;
import com.example.ericdesedas.expohub.presentation.presenters.NewsDetailPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsDetailActivity extends BaseActivity implements
        NewsDetailPresenter.View,
        CreateCommentDialogFragment.CreateCommentListener {

    public static final String KEY_NEWS_ID = "news-id";

    @BindView(R.id.toolbar)             Toolbar toolbar;
    @BindView(R.id.recycler_view)       RecyclerView recyclerView;
    @BindView(R.id.network_progress)    ProgressBar networkProgressBar;
    @BindView(R.id.error_text)          TextView errorText;

    @Inject NewsDetailPresenter newsDetailPresenter;
    @Inject RecyclerAdapterFactory recyclerAdapterFactory;

    private String newsId;
    private News news;
    private NewsDetailAdapter newsDetailAdapter;
    private ProgressDialog commentCreateProgressDialog;

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

        commentCreateProgressDialog = new ProgressDialog(this);
        commentCreateProgressDialog.setMessage(getString(R.string.message_creating_comment));

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
        if (showLoading && news == null) {
            networkProgressBar.setVisibility(View.VISIBLE);
        } else {
            networkProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void toggleCommentCreating(boolean isCreating) {
        if (isCreating) {
            if (! commentCreateProgressDialog.isShowing()) {
                commentCreateProgressDialog.show();
            }
        } else {
            if (commentCreateProgressDialog.isShowing()) {
                commentCreateProgressDialog.dismiss();
            }
        }
    }

    @Override
    public void showCommentCreationMessage() {
        Toast.makeText(this, getString(R.string.message_comment_added), Toast.LENGTH_SHORT).show();
        newsDetailPresenter.onLoadSingleNewsCommand(newsId);
    }

    @Override
    public void showCommentCreationErrorMessage(int code, String message) {
        if (code == 500) {
            Toast.makeText(this, getString(R.string.generic_network_error), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateNews(News news) {

        errorText.setVisibility(View.GONE);
        newsDetailAdapter.setNews(news);
        newsDetailAdapter.notifyDataSetChanged();

        this.news = news;
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

    @OnClick(R.id.add_comment_button)
    public void onAddCommentButtonClick() {
        if (newsDetailPresenter.canRequestNewComment()) {
            CreateCommentDialogFragment fragment = CreateCommentDialogFragment.newInstance(this);
            fragment.show(getSupportFragmentManager(), "");
        } else {
            Toast.makeText(this, getString(R.string.cannot_create_comment_auth), Toast.LENGTH_LONG).show();
        }
    }

    private void setupUI() {

        newsDetailPresenter.setView(this);

        newsDetailAdapter = recyclerAdapterFactory.createNewsDetailAdapter();
        recyclerView.setAdapter(newsDetailAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        newsDetailPresenter.onLoadSingleNewsCommand(newsId);
    }

    @Override
    public void onCreateComment(String commentText) {
        newsDetailPresenter.onCreateCommentCommand(commentText, newsId);
    }
}
