package com.example.ericdesedas.expohub.presentation.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;

import androidx.core.util.Pair;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.events.FairEventListClickEvent;
import com.example.ericdesedas.expohub.data.events.FairEventListRefreshEvent;
import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.presentation.adapters.EventListAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FairEventListFragment extends BaseFragment implements
        EventListAdapter.Listener {

    @BindView(R.id.swipe_refresh_layout)    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)           RecyclerView recyclerView;
    @BindView(R.id.network_progress)        ProgressBar networkProgress;
    @BindView(R.id.error_text)              TextView errorText;

    private EventListAdapter adapter;
    private EventBus eventBus;

    private boolean canUpdate = false;

    public static FairEventListFragment newInstance(EventListAdapter adapter, EventBus eventBus) {
        FairEventListFragment fragment = new FairEventListFragment();
        fragment.setAdapter(adapter);
        fragment.setEventBus(eventBus);
        return fragment;
    }

    public FairEventListFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view   = inflater.inflate(R.layout.fragment_fair_event_list, container, false);
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

    // EventListAdapter.Listener methods

    @Override
    public void onEventCellClick(FairEvent fairEvent, List<Pair<View, String>> transitioningElements) {

        FairEventListClickEvent event = new FairEventListClickEvent();
        event.fairEventId           = fairEvent.getId();
        event.transitioningElements = transitioningElements;
        eventBus.post(event);
    }

    // Public methods

    public void setAdapter(EventListAdapter eventListAdapter) {
        this.adapter = eventListAdapter;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void toggleLoading(boolean showLoading) {

        if (canUpdate) {
            if (! showLoading) {
                networkProgress.setVisibility(View.GONE);
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            } else if (! adapter.hasFairEvents()) {
                networkProgress.setVisibility(View.VISIBLE);
            }
        }
    }

    public void updateList(FairEvent[] fairEvents) {

        adapter.swapList(fairEvents);
        adapter.notifyDataSetChanged();

        if (fairEvents.length <= 0) {
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(getString(R.string.empty_events_error));
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

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                eventBus.post(new FairEventListRefreshEvent());
                errorText.setVisibility(View.GONE);
            }
        });
    }
}
