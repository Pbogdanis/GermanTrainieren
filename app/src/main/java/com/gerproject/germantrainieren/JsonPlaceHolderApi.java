package com.gerproject.germantrainieren;

import java.util.Collection;
import java.util.List;

import Models.ArticlesModel;
import Models.PluralsModel;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("api/words")
    Call<List<ArticlesModel>> GetAllFromArticles();

    @GET("api/words/plurals")
    Call<List<PluralsModel>> GetAllFromPlurals();

    @GET("api/login")
    Call<Boolean> GetLogin();

    // GET Plurals of singular word based on id
    @GET("api/words/plurals/{id}")
    Call<List<PluralsModel>> GetPluralsOfSingular(@Path("id")int id);

    @POST("api/words/newsingular")
    Call<String> InsertSingular(@Body ArticlesModel newSingular);

    @POST("api/words/newplural")
    Call<String> InsertPlural(@Body PluralsModel newSingular);

    // DELETE singular word
    @DELETE("api/words/deletesingular/{id}")
    Call<String> DeleteSingular(@Path("id")int id);

    // DELETE plural word
    @DELETE("api/words/deleteplural/{id}")
    Call<String> DeletePlural(@Path("id")int id);
}
