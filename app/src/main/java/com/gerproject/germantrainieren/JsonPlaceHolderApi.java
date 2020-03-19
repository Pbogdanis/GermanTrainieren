package com.gerproject.germantrainieren;

import java.util.Collection;
import java.util.List;

import Models.ArticlesModel;
import Models.PluralsModel;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {

    @GET("api/words")
    Call<List<ArticlesModel>> GetAllFromArticles();

    @GET("api/words/plurals")
    Call<List<PluralsModel>> GetAllFromPlurals();

    @GET("api/login")
    Call<Boolean> GetLogin();

    @POST("api/words/newsingular")
    Call<String> InsertSingular(@Body ArticlesModel newSingular);

    @POST("api/words/newplural")
    Call<String> InsertPlural(@Body PluralsModel newSingular);
}
