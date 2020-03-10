package com.gerproject.germantrainieren;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    private Toast _toastMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articles_layout);

        _wordFromList = findViewById(R.id.wordFromList);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://germanapi.azurewebsites.net/")
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




        /*ArticlesModel newSingular = new ArticlesModel();
        newSingular.setSingular("testFromAndroid01");
        newSingular.setArticle("testFromAndroid02");
        Call<List<ArticlesModel>> call = jsonPlaceHolderApi.InsertSingular(newSingular);
        call.enqueue(new Callback<List<ArticlesModel>>() {
            @Override
            public void onResponse(Call<List<ArticlesModel>> call, Response<List<ArticlesModel>> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText("Code: " + response.code() + response.message());
                    return;
                }
                String result = String.valueOf(response.code());
                textViewResult.setText(result);

            }

            @Override
            public void onFailure(Call<List<ArticlesModel>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });*/

        /*cpArticles = new ArrayList<String>(articles);
        cpSingular = new ArrayList<String>(singular);

        _question_txt = findViewById(R.id.question_txt);
        _answer_txt = findViewById(R.id.answer_txt);
        _random_index = getRandomNumberInRange(0, cpArticles.size() - 1);
        _next_article = "The article of " + cpSingular.get(_random_index) + " is : ";
        _question_txt.setText(_next_article);*/
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
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Wrong! The correct article is " + _allArticles.get(_random_index).getArticle(), Toast.LENGTH_SHORT).show();
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
            _toastMsg = Toast.makeText(this, "The quiz is over!", Toast.LENGTH_SHORT);
            _toastMsg.setGravity(Gravity.TOP| Gravity.CENTER, 0, 0);
            _toastMsg.show();

            finish();
        }

        /*if(!cpArticles.isEmpty()){
            if (_answer_txt.getText().toString().isEmpty()){
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
            } else {

                if (cpArticles.get(_random_index).equalsIgnoreCase(_answer_txt.getText().toString())) {
                    Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Wrong! The correct article is " + cpArticles.get(_random_index), Toast.LENGTH_SHORT).show();
                }

                //Remove word from the lists
                cpArticles.remove(_random_index);
                cpSingular.remove(_random_index);

                //Update view
                if(!cpArticles.isEmpty()){
                    //Generate new random index
                    _random_index = getRandomNumberInRange(0, cpArticles.size() - 1);
                    //Update view
                    _next_article = "The article of " + cpSingular.get(_random_index) + " is : ";
                    _question_txt.setText(_next_article);
                    _answer_txt.setText("");
                } else {
                   //Close activity and show MainActivity
                    toastMsg = Toast.makeText(this, "The quiz is over!", Toast.LENGTH_SHORT);
                    toastMsg.setGravity(Gravity.TOP| Gravity.CENTER, 0, 0);
                    toastMsg.show();

                    finish();
                }

            }
        }*/
    }


}
