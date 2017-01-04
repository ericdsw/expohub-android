package com.example.ericdesedas.expohub.helpers.preferences;

import android.content.Context;

import com.example.ericdesedas.expohub.R;

/**
 * This class exposes all required network parameters
 * Parameters will be loaded via defined resources through the app's {@link Context} reference
 */
public class NetworkPreferenceHelper {

    private Context context;

    public NetworkPreferenceHelper(Context context) {
        this.context = context;
    }

    public String getBaseURL() {
        return context.getString(R.string.api_base_url);
    }

    public int getReadTimeout() {
        return context.getResources().getInteger(R.integer.read_timeout);
    }

    public int getWriteTimeout() {
        return context.getResources().getInteger(R.integer.write_timeout);
    }
}
