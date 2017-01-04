package com.example.ericdesedas.expohub.presentation.presenters;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;

import java.util.List;

public abstract class Presenter {

    /**
     * Code that should run when the Presenter starts
     */
    public void onStart() {
        // Empty
    }

    /**
     * Code that should run when the presenter stops
     */
    public void onStop() {
        // Empty
    }

    /**
     * Returns a single string containing all supplied error messages separated by a comma
     * @param errors the list of errors to be concatenated
     * @return the concatenated String
     */
    protected String concatenateErrorString(List<ApiErrorWrapper.Error> errors) {

        String errorString = "";
        for (ApiErrorWrapper.Error error : errors) {
            errorString += error.message + ", ";
        }
        errorString = errorString.substring(0, errorString.length() - 2);
        return errorString;
    }
}
