package com.example.ericdesedas.expohub.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.data.models.Speaker;
import com.example.ericdesedas.expohub.helpers.image.ImageDownloader;
import com.example.ericdesedas.expohub.presentation.fragments.RouteDialogFragment;
import com.example.ericdesedas.expohub.presentation.presenters.FairEventDetailsPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FairEventDetailsActivity extends BaseActivity implements
    FairEventDetailsPresenter.View {

    public static final String KEY_FAIR_EVENT_ID = "fair-event-id";

    @BindView(R.id.toolbar)             Toolbar toolbar;
    @BindView(R.id.fair_event_image)    ImageView fairEventImage;
    @BindView(R.id.fair_event_name)     TextView fairEventName;
    @BindView(R.id.fair_event_subtext)  TextView fairEventSubtext;
    @BindView(R.id.content_text)        TextView fairEventContentText;
    @BindView(R.id.speakers_area)       LinearLayout speakersArea;

    @BindView(R.id.main_content)        RelativeLayout mainContent;
    @BindView(R.id.loading_progress)    ProgressBar networkProgressBar;
    @BindView(R.id.error_wrapper)       LinearLayout errorWrapper;
    @BindView(R.id.error_text)          TextView errorText;

    @Inject FairEventDetailsPresenter presenter;
    @Inject ImageDownloader imageDownloader;

    private String fairEventId;
    private FairEvent currentFairEvent;
    private Menu menu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fair_event_details);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        getActivityComponent().inject(this);

        fairEventId = getIntent().getStringExtra(KEY_FAIR_EVENT_ID);

        setupUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_fair_event_details, menu);
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
                        "/fairEvents/{id}<br /><span style='color:red;'>?include=speakers</span>");
                routeDialogFragment.show(getSupportFragmentManager(), "");
                break;
            case R.id.action_favorite:
                if (presenter.onCheckForIsFavorite(currentFairEvent.getId())) {
                    presenter.onUnAttendFairEventCommand(currentFairEvent.getId());
                } else {
                    presenter.onAttendFairEventCommand(currentFairEvent.getId());
                }
                break;
        }

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
    public void toggleLoading(boolean showLoading) {

        if (showLoading) {
            errorWrapper.setVisibility(View.GONE);
            mainContent.setVisibility(View.GONE);
            networkProgressBar.setVisibility(View.VISIBLE);
        } else {
            networkProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateFairEvent(FairEvent fairEvent) {

        currentFairEvent = fairEvent;

        mainContent.setVisibility(View.VISIBLE);

        fairEventName.setText(fairEvent.title);
        fairEventSubtext.setText(fairEvent.parsedDate());
        fairEventContentText.setText(fairEvent.description);

        imageDownloader.setMaxImageSize(500)
                .setImage(fairEvent.image, fairEventImage);

        speakersArea.removeAllViews();

        if (presenter.onCheckForIsFavorite(fairEvent.getId())) {
            showAttending(true);
        } else {
            showAttending(false);
        }

        if (fairEvent.getSpeakers().isEmpty()) {
            View emptyView = LayoutInflater.from(this).inflate(R.layout.sbv_cell_speakers_empty, speakersArea, false);
            speakersArea.addView(emptyView);
        } else {
            for (Speaker speaker : fairEvent.getSpeakers()) {

                View view = LayoutInflater.from(this).inflate(R.layout.sbv_cell_speaker, speakersArea, false);

                ((TextView) view.findViewById(R.id.speaker_name)).setText(speaker.name);
                ((TextView) view.findViewById(R.id.speaker_description)).setText(speaker.description);

                imageDownloader.setMaxImageSize(148)
                        .setCircularImage(speaker.picture, ((ImageView) view.findViewById(R.id.speaker_image)));

                speakersArea.addView(view);
            }
        }
    }

    @Override
    public void showError(int code, String error) {

        errorWrapper.setVisibility(View.VISIBLE);
        if (code == 500) {
            errorText.setText(getString(R.string.generic_network_error));
        } else {
            errorText.setText(error);
        }
    }

    @Override
    public void showUnidentifiedError() {
        Toast.makeText(this, getString(R.string.unidentified_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showAttending(boolean isAttending) {
        if (isAttending) {
            menu.findItem(R.id.action_favorite).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp));
        } else {
            menu.findItem(R.id.action_favorite).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp));
        }
    }

    private void setupUI() {

        presenter.setView(this);
        presenter.initialize();

        presenter.onLoadFairEventCommand(fairEventId);
    }
}
