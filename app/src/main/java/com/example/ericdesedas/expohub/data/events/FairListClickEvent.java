package com.example.ericdesedas.expohub.data.events;

import androidx.core.util.Pair;
import android.view.View;

import java.util.List;

/**
 * Event used to propagate event list click from Fragment to MainActivity
 * Contains a reference to:
 *  - Selected fair id
 *  - List of transitioning elements
 */
public class FairListClickEvent {
    public String fairId;
    public List<Pair<View, String>> transitioningElements;
}
