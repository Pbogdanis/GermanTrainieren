package Helpers;

import java.io.IOException;
import java.util.List;

import Models.ArticlesModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetQueries {

    private static List<ArticlesModel> _allArticles;

    public static List<ArticlesModel> getAllWords(Call<List<ArticlesModel>> call){
        /*call.enqueue(new Callback<List<ArticlesModel>>() {
            @Override
            public void onResponse(Call<List<ArticlesModel>> call, Response<List<ArticlesModel>> response) {
                if(!response.isSuccessful()){
                    //textViewResult.setText("Code: " + response.code() + response.message());
                    return;
                }
                _allArticles = response.body();
                //return _allArticles;
            }

            @Override
            public void onFailure(Call<List<ArticlesModel>> call, Throwable t) {
                //textViewResult.setText(t.getMessage());
                //return null;
            }
        });*/
        try {
            _allArticles = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if ( !_allArticles.isEmpty()){
            return _allArticles;
        } else {
            return null;
        }
    }
}
