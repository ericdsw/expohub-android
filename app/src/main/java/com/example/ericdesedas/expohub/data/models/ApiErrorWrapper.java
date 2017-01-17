package com.example.ericdesedas.expohub.data.models;

import com.squareup.moshi.Json;

import java.util.ArrayList;
import java.util.List;

import moe.banana.jsonapi2.Error;

public class ApiErrorWrapper {

    @Json(name = "errors") public List<Error> errorList;

    /**
     * Constructor
     */
    public ApiErrorWrapper() {
        errorList = new ArrayList<>();
    }

    /**
     * @return whether the wrapper contains only one error
     */
    public boolean hasUniqueError() {
        return errorList.size() == 1;
    }

    /**
     * Gets a single error from the list
     * Used to abstract cases where it is expected that only one error will be generated
     *
     * @return the first {@link Error} on the list
     */
    public Error getUniqueError() {
        return errorList.get(0);
    }

    public void addError(Error error) {
        errorList.add(error);
    }

    public List<Error> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<Error> errorList) {
        this.errorList = errorList;
    }

    /**
     * Check if specified error exists
     *
     * @param title a {@link String} containing the desired error's title
     * @return whether the specified error exists
     */
    public boolean containsErrorForTitle(String title) {
        
        for (Error error : errorList) {
            if (error.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }
}
