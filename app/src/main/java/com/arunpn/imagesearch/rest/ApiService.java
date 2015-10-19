package com.arunpn.imagesearch.rest;

import com.arunpn.imagesearch.model.ResponseData;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by a1nagar on 10/18/15.
 */
public interface ApiService {

    @GET("/images")
    public void getImages(@Query("q") String searchQuery,
                          @Query("v") String apiVersion,
                          @Query("rsz") int resultSize,
                          @Query("start") int searchPage,
                          @Query("imgsz") String imageSize,
                          @Query("as_sitesearch") String searchSite,
                          @Query("imgc") String imageColor,
                          Callback<ResponseData> callback);
}

