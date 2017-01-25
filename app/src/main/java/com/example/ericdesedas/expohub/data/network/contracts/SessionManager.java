package com.example.ericdesedas.expohub.data.network.contracts;

import com.example.ericdesedas.expohub.data.models.Session;
import com.example.ericdesedas.expohub.data.models.User;

import java.io.IOException;

public interface SessionManager {

    /**
     * @return boolean indicating if the user is logged inside the application
     */
    boolean isLoggedIn();

    /**
     * Login the user to the application
     * @param user the {@link User} instance that will be logged
     * @param authKey the {@link String} reference containing the auth key
     */
    void login(User user, String authKey);

    /**
     * Removes all user credentials from the application
     */
    void logout();

    /**
     * Gets user data
     *
     * @return a {@link Session} instance containing updated data
     * @throws IOException when the existing user data is corrupted
     */
    Session getLoggedUser() throws IOException;

    /**
     * Refreshes user data
     *
     * @param user a {@link User} user instance with updated data
     * @throws IOException when the existing user data is corrupted
     */
    void refreshUserData(User user) throws IOException;
}
