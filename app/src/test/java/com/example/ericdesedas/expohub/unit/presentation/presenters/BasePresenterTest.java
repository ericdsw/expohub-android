package com.example.ericdesedas.expohub.unit.presentation.presenters;

import moe.banana.jsonapi2.Error;

public abstract class BasePresenterTest {

    protected Error createError(String title, String detail, String code, String status) {
        Error error = new Error();
        error.setTitle(title);
        error.setDetail(detail);
        error.setCode(code);
        error.setStatus(status);

        return error;
    }
}
