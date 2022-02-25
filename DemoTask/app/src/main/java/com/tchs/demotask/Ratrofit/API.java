package com.tchs.demotask.Ratrofit;

import com.tchs.demotask.Response.CarResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface API {

    @GET("{url}")
    Call<CarResponse> getApiCall(@Path("url") String url
    );

}
