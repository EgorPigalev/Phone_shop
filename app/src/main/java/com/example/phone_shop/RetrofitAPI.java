package com.example.phone_shop;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPI {

    @GET("Phones/{id}")
    Call<DataModal> getDATA(@Query("id") int id);

    @POST("Phones")
    Call<DataModal> createPost(@Body DataModal dataModal);

    @PUT("Phones/{id}")
    Call<DataModal> updateData(@Query("id") int id, @Body DataModal dataModal);

    @DELETE("Phones/{id}")
    Call<Void> deleteData(@Path("id") int id);

    @DELETE("Phones")
    Call<Void> deleteBasaData();
}
