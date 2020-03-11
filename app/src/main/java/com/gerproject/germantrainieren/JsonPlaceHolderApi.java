package com.gerproject.germantrainieren;

import java.util.List;

import Models.ArticlesModel;
import Models.PluralsModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {

    @GET("api/words")
    Call<List<ArticlesModel>> GetAllFromArticles();

    @GET("api/words/plurals")
    Call<List<PluralsModel>> GetAllFromPlurals();

    @POST("api/words/newsingular")
    Call<List<ArticlesModel>> InsertSingular(@Body ArticlesModel newSingular);

    @POST("api/words/newplural")
    Call<List<PluralsModel>> InsertPlural(@Body PluralsModel newSingular);
}
