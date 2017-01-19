package com.example.ericdesedas.expohub.data.network;

import com.example.ericdesedas.expohub.data.models.Session;
import com.example.ericdesedas.expohub.data.models.User;
import com.example.ericdesedas.expohub.data.network.contracts.SessionManager;
import com.example.ericdesedas.expohub.helpers.preferences.PreferenceHelper;
import com.squareup.moshi.Moshi;

import java.io.IOException;

public class SessionManagerImpl implements SessionManager {

    private PreferenceHelper preferenceHelper;
    private Moshi moshi;

    public SessionManagerImpl(PreferenceHelper preferenceHelper, Moshi moshi) {
        this.preferenceHelper   = preferenceHelper;
        this.moshi              = moshi;
    }

    @Override
    public boolean isLoggedIn() {
        return ! preferenceHelper.readStringPref(PreferenceHelper.SESSION_DATA).equals("");
    }

    @Override
    public Session getLoggedUser() throws IOException {
        Session session = getSessionFromPreference();
        return session;
    }

    @Override
    public void login(User user, String authKey) {

        Session session = new Session();
        session.id      = user.getId();
        session.name    = user.name;
        session.token   = authKey;

        saveSession(session);
    }

    @Override
    public void logout() {
        preferenceHelper.writeStringPref(PreferenceHelper.SESSION_DATA, "");
    }

    @Override
    public void refreshUserData(User user) throws IOException {

        Session session = getSessionFromPreference();
        session.id      = user.getId();
        session.name    = user.name;

        saveSession(session);
    }

    /**
     * Gets saved session data
     *
     * @return a {@link Session} instance
     * @throws IOException if the preference's string has invalid format
     */
    private Session getSessionFromPreference() throws IOException {

        String sessionString = preferenceHelper.readStringPref(PreferenceHelper.SESSION_DATA);
        return moshi.adapter(Session.class).fromJson(sessionString);
    }

    /**
     * Updates session data
     *
     * @param session a {@link Session} containing the updated information
     */
    private void saveSession(Session session) {
        String sessionString = moshi.adapter(Session.class).toJson(session);
        preferenceHelper.writeStringPref(PreferenceHelper.SESSION_DATA, sessionString);
    }
}
