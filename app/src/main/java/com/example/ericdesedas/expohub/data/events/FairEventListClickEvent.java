package com.example.ericdesedas.expohub.data.events;

import androidx.core.util.Pair;
import android.view.View;

import java.util.List;

public class FairEventListClickEvent {
    public String fairEventId;
    public List<Pair<View, String>> transitioningElements;
}
