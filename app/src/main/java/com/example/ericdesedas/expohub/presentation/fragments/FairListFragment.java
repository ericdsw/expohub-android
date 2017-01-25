package com.example.ericdesedas.expohub.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.events.FairListClickEvent;
import com.example.ericdesedas.expohub.data.events.FairListRefreshEvent;
import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.presentation.adapters.FairListAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FairListFragment extends Fragment implements
    FairListAdapter.Listener {

    @BindView(R.id.swipe_refresh_layout)    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)           RecyclerView recyclerView;
    @BindView(R.id.network_progress)        ProgressBar networkProgress;
    @BindView(R.id.error_text)              TextView errorText;

    private Unbinder unbinder;
    private FairListAdapter adapter;
    private EventBus eventBus;

    private boolean canUpdate   = false;
    private int adapterViewType = FairListAdapter.VIEW_TYPE_LARGE;

    public FairListFragment() {
        // Required empty constructor
    }

    public static FairListFragment newInstance(FairListAdapter adapter, EventBus eventBus) {
        FairListFragment fragment = new FairListFragment();
        fragment.setAdapter(adapter);
        fragment.setEventBus(eventBus);
        return fragment;
    }

    public static FairListFragment newInstance(FairListAdapter adapter, EventBus eventBus, int adapterViewType) {
        FairListFragment fragment = new FairListFragment();
        fragment.setAdapter(adapter);
        fragment.setEventBus(eventBus);
        fragment.setAdapterViewType(adapterViewType);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view   = inflater.inflate(R.layout.fragment_fair_list, container, false);
        unbinder    = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupUI();
    }

    @Override
    public void onStart() {
        super.onStart();
        canUpdate = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        canUpdate = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // Public Methods

    public void setAdapter(FairListAdapter adapter) {
        this.adapter = adapter;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void setAdapterViewType(int adapterViewType) {
        this.adapterViewType = adapterViewType;
    }

    public void toggleLoading(boolean showLoading) {

        if (canUpdate) {
            if (! showLoading) {
                networkProgress.setVisibility(View.GONE);
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            } else if (! adapter.hasFairs()) {
                networkProgress.setVisibility(View.VISIBLE);
            }
        }
    }

    public void updateList(Fair[] fairs) {

        adapter.updateFairList(fairs);
        adapter.notifyDataSetChanged();

        if (fairs.length <= 0) {
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(getString(R.string.empty_fairs_error));
        }
    }

    public void showError(int statusCode, String error) {

        errorText.setVisibility(View.VISIBLE);
        if (statusCode == 500) {
            errorText.setText(getString(R.string.generic_network_error));
        } else {
            errorText.setText(error);
        }
    }

    // Private Methods

    private void setupUI() {

        adapter.setListener(this);
        adapter.setViewSize(this.adapterViewType);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (adapterViewType == FairListAdapter.VIEW_TYPE_CONDENSED) {
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                eventBus.post(new FairListRefreshEvent());
                errorText.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onFairCardClick(Fair fair, List<Pair<View, String>> transitioningElements) {

        FairListClickEvent event = new FairListClickEvent();

        event.fairId                = fair.getId();
        event.transitioningElements = transitioningElements;

        eventBus.post(event);
    }
}
