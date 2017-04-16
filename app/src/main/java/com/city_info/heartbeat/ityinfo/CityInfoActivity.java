package com.city_info.heartbeat.ityinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.city_info.heartbeat.ityinfo.Wiki.Api;
import com.city_info.heartbeat.ityinfo.Wiki.City;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Activity для отображения краткой информации про выбранный город
 */

public class CityInfoActivity extends AppCompatActivity {


    private String city;
    private TextView text_info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_info);

        city = getIntent().getStringExtra("CITY");
        final TextView textView = (TextView) findViewById(R.id.city_info);

        Retrofit retrofit;
        Api api;

        retrofit = new Retrofit.Builder()
                .baseUrl("http://api.geonames.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);

        api.getInfo(city, "10", "heartbeat").enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {
                try{
                textView.setText(response.body().getGeonames().get(0).getSummary());}
                catch (Exception e){
                    textView.setText("Nothing found");
                }

            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {
                textView.setText("Nothing found");
            }
        });

    }
}
