package com.example.ericdesedas.expohub.presentation.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.ericdesedas.expohub.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.go_to_repo_button)
    public void onGoToRepoCLick() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.repo_url)));
        startActivity(browserIntent);
    }
}
