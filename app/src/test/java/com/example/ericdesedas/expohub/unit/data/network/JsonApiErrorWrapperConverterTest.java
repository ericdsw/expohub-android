package com.example.ericdesedas.expohub.unit.data.network;


import com.example.ericdesedas.expohub.data.models.ApiErrorWrapper;
import com.example.ericdesedas.expohub.helpers.FileReaderHelper;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JsonApiErrorWrapperConverterTest {

    private JsonAdapter<ApiErrorWrapper> errorAdapter;
    private FileReaderHelper fileReader;

    @Before
    public void setUp() {

        Moshi moshi     = new Moshi.Builder().build();
        errorAdapter    = moshi.adapter(ApiErrorWrapper.class);

        fileReader = new FileReaderHelper();
    }

    @Test
    public void it_correctly_parses_json_api_error_with_one_error() throws IOException {

        String jsonString               = fileReader.readFile("json/api_error_single.json");
        ApiErrorWrapper apiErrorWrapper = errorAdapter.fromJson(jsonString);

        assertThat("Checking that ApiErrorWrapper contains correct amount of errors", apiErrorWrapper.errorList.size(), is(1));
        assertThat("Checking that ApiErrorWrapper's first error title parsed correctly", apiErrorWrapper.errorList.get(0).title, is("foo"));
        assertThat("Checking that ApiErrorWrapper's first error message parsed correctly", apiErrorWrapper.errorList.get(0).message, is("bar"));
        assertThat("Checking that ApiErrorWrapper's first error status parsed correctly", apiErrorWrapper.errorList.get(0).status, is(400));
    }

    @Test
    public void it_correctly_parses_json_api_error_with_multiple_errors() throws IOException {

        String jsonString               = fileReader.readFile("json/api_error_list.json");
        ApiErrorWrapper apiErrorWrapper = errorAdapter.fromJson(jsonString);

        assertThat("Checking that ApiErrorWrapper contains correct amount of errors", apiErrorWrapper.errorList.size(), is(3));
    }
}
