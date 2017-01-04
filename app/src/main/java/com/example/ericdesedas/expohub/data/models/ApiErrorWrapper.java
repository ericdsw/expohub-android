package com.example.ericdesedas.expohub.data.models;

import com.squareup.moshi.Json;

import java.util.ArrayList;
import java.util.List;

public class ApiErrorWrapper {

    @Json(name = "errors") public List<Error> errorList;

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
     * Gets single first error
     *
     * @return the first {@link Error} on the list
     */
    public Error getUniqueError() {
        return errorList.get(0);
    }

    /**
     * Check if specified error exists
     *
     * @param title a {@link String} containing the desired error's title
     * @return whether the specified error exists
     */
    public boolean containsErrorForTitle(String title) {
        
        for (Error error : errorList) {
            if (error.title.equals(title)) {
                return true;
            }
        }
        return false;
    }

    public static class Error {

        @Json(name = "title")       public String title;
        @Json(name = "message")     public String message;
        @Json(name = "status")      public int status;

        public Error() {
            //
        }

        public Error(String title, String message, int status) {
            this.title      = title;
            this.message    = message;
            this.status     = status;
        }
    }
}
