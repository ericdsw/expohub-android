package com.example.ericdesedas.expohub.presentation.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.helpers.image.ImageDownloader;
import com.example.ericdesedas.expohub.presentation.navigation.Navigator;
import com.example.ericdesedas.expohub.presentation.presenters.FairDetailsPresenter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.joda.time.format.DateTimeFormat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FairDetailsActivity extends BaseActivity implements
        FairDetailsPresenter.View,
        OnMapReadyCallback {

    public static final String KEY_FAIR_ID = "fair_id";

    @BindView(R.id.fair_image)          ImageView fairImage;
    @BindView(R.id.fair_name)           TextView fairName;
    @BindView(R.id.fair_description)    TextView fairDescription;
    @BindView(R.id.fair_dates)          TextView fairDates;
    @BindView(R.id.location_name)       TextView locationName;
    @BindView(R.id.location_map)        View locationMap;
    @BindView(R.id.map_card_view)       CardView mapCardView;
    @BindView(R.id.options_card_view)   CardView optionsCardView;
    @BindView(R.id.error_text)          TextView errorTextView;
    @BindView(R.id.toolbar)             Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)  CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.loading_progress)    ProgressBar loadingProgress;
    @BindView(R.id.error_wrapper)       View errorWrapper;
    @BindView(R.id.content_options)     View contentOptionsWrapper;

    @Inject FairDetailsPresenter presenter;
    @Inject ImageDownloader imageDownloader;
    @Inject Navigator navigator;

    private String fairId;
    private SupportMapFragment mapFragment;
    private Fair currentFair;

    private boolean isMapReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fair_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getActivityComponent().inject(this);

        fairId = getIntent().getStringExtra(KEY_FAIR_ID);

        setupUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fair_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_map:
                break;
            default:
                return super.onOptionsItemSelected(item);
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

    // Click Listeners

    @OnClick(R.id.events_button)
    public void onEventsButtonClick() {
        navigator.navigateToEventsByFairActivity(currentFair.getId(), currentFair.name);
    }

    @OnClick(R.id.news_button)
    public void onNewsButtonClick() {
        navigator.navigateToNewsByFairActivity(currentFair.getId(), currentFair.name);
    }

    @OnClick(R.id.stands_button)
    public void onStandsButtonClick() {
        navigator.navigateToStandsByFairActivity(currentFair.getId(), currentFair.name);
    }

    @OnClick(R.id.retry_button)
    public void onRetryButtonClick() {
        presenter.onLoadFairCommand(fairId);
    }

    @OnClick(R.id.map_card_view)
    public void onMapClick() {

    }

    @OnClick(R.id.sponsors_button)
    public void onSponsorsClick() {
        navigator.navigateToSponsorsByFairActivity(currentFair.getId(), currentFair.name);
    }

    // View callback methods

    @Override
    public void toggleLoading(boolean showLoading) {

        if (showLoading) {
            errorWrapper.setVisibility(View.GONE);
            contentOptionsWrapper.setVisibility(View.GONE);
            mapCardView.setVisibility(View.GONE);
            optionsCardView.setVisibility(View.GONE);
            loadingProgress.setVisibility(View.VISIBLE);
        } else {
            loadingProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateFair(Fair fair) {

        currentFair = fair;

        contentOptionsWrapper.setVisibility(View.VISIBLE);

        mapCardView.setVisibility(View.VISIBLE);
        optionsCardView.setVisibility(View.VISIBLE);

        String startingDateString = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
                .parseDateTime(fair.startingDate)
                .toString("MMM dd, yyyy");

        String endingDateString = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
                .parseDateTime(fair.endingDate)
                .toString("MMM dd, yyyy");

        fairName.setText(fair.name);
        fairDescription.setText(fair.description);
        fairDates.setText(String.format(getString(R.string.label_dates), startingDateString, endingDateString));
        locationName.setText(fair.address);

        imageDownloader.setImage(fair.getImage(), fairImage);

        mapFragment = createMapFragment();
        placeMap();
    }

    @Override
    public void showError(int code, String error) {

        errorWrapper.setVisibility(View.VISIBLE);
        if (code == 500) {
            errorTextView.setText(getString(R.string.generic_network_error));
        } else {
            errorTextView.setText(error);
        }
    }

    // Google Maps Methods

    @Override
    public void onMapReady(GoogleMap googleMap) {
        isMapReady = true;
        googleMap.addMarker(new MarkerOptions()
            .position(new LatLng(currentFair.latitude, currentFair.longitude))
        );
    }

    // Private Methods

    private void setupUI() {

        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, android.R.color.white));

        presenter.setView(this);
        presenter.initialize();

        presenter.onLoadFairCommand(fairId);
    }

    private SupportMapFragment createMapFragment() {

        LatLng locationLatLng = new LatLng(currentFair.latitude, currentFair.longitude);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(locationLatLng)
                .zoom(14.0f)
                .build();

        GoogleMapOptions options = new GoogleMapOptions()
                .mapType(GoogleMap.MAP_TYPE_NORMAL)
                .compassEnabled(false)
                .liteMode(true)
                .mapToolbarEnabled(false)
                .camera(cameraPosition);

        return SupportMapFragment.newInstance(options);
    }

    private void placeMap() {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.location_map, mapFragment);
        fragmentTransaction.commit();

        mapFragment.getMapAsync(this);
    }
}
