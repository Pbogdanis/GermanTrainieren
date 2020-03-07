package com.gerproject.germantrainieren;

import java.util.List;

import Models.ArticlesModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {

    @GET("api/words")
    Call<List<ArticlesModel>> GetAllFromArticles();

    @POST("api/words/newsingular")
    Call<List<ArticlesModel>> InsertSingular(@Body ArticlesModel newSingular);
}
