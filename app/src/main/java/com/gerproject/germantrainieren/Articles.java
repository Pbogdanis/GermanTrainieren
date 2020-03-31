package com.gerproject.germantrainieren;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import Models.ArticlesModel;
import Helpers.RandomIndex;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Articles extends AppCompatActivity implements View.OnClickListener {


    private TextView _wordFromList;
    private List<ArticlesModel> _allArticles;
    private Integer _random_index;
    private String _btnPressedTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articles_layout);

        _wordFromList = findViewById(R.id.wordFromList);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<ArticlesModel>> call = jsonPlaceHolderApi.GetAllFromArticles();

        call.enqueue(new Callback<List<ArticlesModel>>() {
            @Override
            public void onResponse(Call<List<ArticlesModel>> call, Response<List<ArticlesModel>> response) {
                if(!response.isSuccessful()){
                    setContentView(R.layout.failure_layout);
                    return;
                }
                _allArticles = response.body();

                //Do all the code here, after the response//
                _random_index = RandomIndex.getRandomNumberInRange(0, _allArticles.size() - 1);
                _wordFromList.setText(_allArticles.get(_random_index).getSingular());
            }

            @Override
            public void onFailure(Call<List<ArticlesModel>> call, Throwable t) {
                setContentView(R.layout.failure_layout);
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.derBtn:
                _btnPressedTxt = getString(R.string.derTxt);
                break;
            case R.id.dieBtn:
                _btnPressedTxt = getString(R.string.dieTxt);
                break;
            case R.id.dasBtn:
                _btnPressedTxt = getString(R.string.dastxt);
                break;
        }


        if( _allArticles.get(_random_index).getArticle().equalsIgnoreCase(_btnPressedTxt) ){
            Snackbar.make(v, getString(R.string.correct), Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(v, getString(R.string.wrong)+ " " + _allArticles.get(_random_index).getArticle(), Snackbar.LENGTH_SHORT).show();
        }

        //Remove word from list//
        _allArticles.remove(_allArticles.get(_random_index));

        //Update view
        if(!_allArticles.isEmpty()){
            //Generate new random index
            _random_index = RandomIndex.getRandomNumberInRange(0, _allArticles.size() - 1);
            //Update view
            _wordFromList.setText(_allArticles.get(_random_index).getSingular());
        } else {
            //Close activity and show MainActivity
            Snackbar.make(v, getString(R.string.over), Snackbar.LENGTH_SHORT).show();

            finish();
        }
    }


}
