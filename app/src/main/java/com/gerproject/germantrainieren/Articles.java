package com.gerproject.germantrainieren;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import Models.ArticlesModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.gerproject.germantrainieren.CSVFile.articles;
import static com.gerproject.germantrainieren.CSVFile.singular;

public class Articles extends AppCompatActivity {

    private TextView _question_txt;
    private String _next_article;
    private int _random_index;
    private EditText _answer_txt;
    private Toast toastMsg;
    public static ArrayList<String> cpArticles, cpSingular;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_layout);

        textViewResult = findViewById(R.id.text_result);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://germanapi.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        //Call<List<ArticlesModel>> call = jsonPlaceHolderApi.GetAllFromArticles();

        /*call.enqueue(new Callback<List<ArticlesModel>>() {
            @Override
            public void onResponse(Call<List<ArticlesModel>> call, Response<List<ArticlesModel>> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText("Code: " + response.code() + response.message());
                    return;
                }
                List<ArticlesModel> allWords = response.body();

                for (ArticlesModel model : allWords ){
                    String content = "";
                    content += "ID: " + model.getId() + "\n";
                    content += "Article: " + model.getArticle() + "\n";
                    content += "Singular: " + model.getSingular() + "\n\n";

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<ArticlesModel>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });*/

        ArticlesModel newSingular = new ArticlesModel();
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
        });

        /*cpArticles = new ArrayList<String>(articles);
        cpSingular = new ArrayList<String>(singular);

        _question_txt = findViewById(R.id.question_txt);
        _answer_txt = findViewById(R.id.answer_txt);
        _random_index = getRandomNumberInRange(0, cpArticles.size() - 1);
        _next_article = "The article of " + cpSingular.get(_random_index) + " is : ";
        _question_txt.setText(_next_article);*/
    }

    /*@Override
    public void onClick(View v) {

        if(!cpArticles.isEmpty()){
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
        }
    }

    private static int getRandomNumberInRange(int min, int max) {

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }*/


}
