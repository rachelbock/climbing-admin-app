package com.rage.clamberadmin;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Handles Retrofit Implementation
 */
public class ApiManager {

    private static final String API_URL = "http://104.154.102.192:8080/";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private static final AdminClamberService clamberService = retrofit.create(AdminClamberService.class);

    public static AdminClamberService getClamberService(){
        return clamberService;
    }
    public static String getImageUrl(String imageUrlSuffix) {
        return API_URL + imageUrlSuffix;
    }


}
