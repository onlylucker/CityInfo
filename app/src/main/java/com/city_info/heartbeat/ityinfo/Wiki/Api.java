package com.city_info.heartbeat.ityinfo.Wiki;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("wikipediaSearchJSON")
    Call<City> getInfo(@Query("q") String q,
                       @Query("maxRows") String maxRows,
                       @Query("username") String username);
}
