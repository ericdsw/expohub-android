package com.example.ericdesedas.expohub.presentation.presenters;

import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;

import java.util.ArrayList;
import java.util.List;

import moe.banana.jsonapi2.Document;
import moe.banana.jsonapi2.Error;
import moe.banana.jsonapi2.ResourceIdentifier;

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
    protected String concatenateErrorString(List<Error> errors) {

        String errorString = "";
        for (Error error : errors) {
            errorString += error.getDetail() + ", ";
        }
        errorString = errorString.substring(0, errorString.length() - 2);
        return errorString;
    }

    protected <T extends ResourceIdentifier> List<T> generateArrayFromDocument(Document<T> document) {

        List<T> list = new ArrayList<>();
        for(T element : document) {
            list.add(element);
        }

        return list;
    }
}
