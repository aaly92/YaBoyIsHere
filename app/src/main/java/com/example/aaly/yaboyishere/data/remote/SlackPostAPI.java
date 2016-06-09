package com.example.aaly.yaboyishere.data.remote;


import com.example.aaly.yaboyishere.SlackPost;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SlackPostAPI {
    String API_URL = "https://hooks.slack.com/services/T026B13VA/B1F7H2L9Y/";

    @POST("cFSUDGUSrprLm4lbAuTAE9yo")
    Call<Void> postToSlack(@Body SlackPost slackPost);

    class Factory {
        private static SlackPostAPI service;

        public static SlackPostAPI getInstance() {
            if(service == null){
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(API_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                service = retrofit.create(SlackPostAPI.class);
            }
            return service;
        }
    }
}

