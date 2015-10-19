package com.arunpn.imagesearch.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.Date;

import retrofit.RestAdapter;

/**
 * Created by a1nagar on 10/18/15.
 */
public class RestClient {
    public static final String ENDPOINT = "https://ajax.googleapis.com/ajax/services/search";
    ApiService apiService;

    public RestClient() {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .disableHtmlEscaping()
                .create();

        RestAdapter restAdapter = new RestAdapter
                .Builder()
                .setEndpoint(ENDPOINT)
                .setConverter( new DefaultGSONConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        this.apiService = restAdapter.create(ApiService.class);

    }

    public ApiService getApiService() { return  apiService; }
}
