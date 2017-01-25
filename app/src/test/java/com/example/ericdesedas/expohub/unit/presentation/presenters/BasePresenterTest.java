package com.example.ericdesedas.expohub.unit.presentation.presenters;

import moe.banana.jsonapi2.Error;

public abstract class BasePresenterTest {

    /**
     * Creates a new error instance
     *
     * @param title     a {@link String} reference with the error title
     * @param detail    a {@link String} reference with the error details
     * @param code      a {@link String} reference with the error code
     * @param status    a {@link String} reference with the error status
     * @return an {@link Error} instance
     */
    protected Error createError(String title, String detail, String code, String status) {

        Error error = new Error();
        error.setTitle(title);
        error.setDetail(detail);
        error.setCode(code);
        error.setStatus(status);

        return error;
    }
}
