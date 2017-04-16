package com.city_info.heartbeat.ityinfo.DownloadDate;


import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("/David-Haim/CountriesToCitiesJSON/master/countriesToCities.json")
    Call<Countries> getJSON();
}
