package com.gerproject.germantrainieren;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Models.ArticlesModel;
import Helpers.RandomIndex;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.gerproject.germantrainieren.MainActivity.mContext;
import static com.gerproject.germantrainieren.MainActivity.refreshBtns;

public class Articles extends AppCompatActivity implements View.OnClickListener {


    private TextView _wordFromList;
    private List<ArticlesModel> _allArticles;
    private Integer _remainingArticles, _countCorrect, _countFalse;
    private Integer _random_index;
    private String _btnPressedTxt;
    private JsonPlaceHolderApi _jsonPlaceHolderApi;
    private Button _nextQst, _answerBtn, _correctIfFalse, _derBtn, _dieBtn, _dasBtn;
    private Button[] _articleBtnList;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private TextView _correctValue, _falseValue, _remainingValue;

    public Articles(){
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mContext.getResources().getString(R.string.api_url))
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        _jsonPlaceHolderApi = jsonPlaceHolderApi;

        refreshBtns();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articles_layout);

        _wordFromList = findViewById(R.id.wordFromList);
        _nextQst = findViewById(R.id.nextQuestion);
        _derBtn = findViewById(R.id.derBtn);
        _dieBtn = findViewById(R.id.dieBtn);
        _dasBtn = findViewById(R.id.dasBtn);
        _correctValue = findViewById(R.id.correctValue);
        _falseValue = findViewById(R.id.falseValue);
        _remainingValue = findViewById(R.id.remainingValue);
        _articleBtnList = new Button[3];
        _articleBtnList[0] = _derBtn;
        _articleBtnList[1] = _dieBtn;
        _articleBtnList[2] = _dasBtn;
        _allArticles = new ArrayList<>();
        //Initialize counters
        _countCorrect = 0;
        _countFalse = 0;
        _remainingArticles = 0;
        _correctValue.setText(_countCorrect.toString());
        _falseValue.setText(_countFalse.toString());
        _remainingValue.setText(_remainingArticles.toString());

        Call<List<ArticlesModel>> call = _jsonPlaceHolderApi.GetAllFromArticles();

        call.enqueue(new Callback<List<ArticlesModel>>() {
            @Override
            public void onResponse(Call<List<ArticlesModel>> call, Response<List<ArticlesModel>> response) {
                if(!response.isSuccessful()){
                    setContentView(R.layout.failure_layout);
                    return;
                }
                _allArticles = response.body();
                _remainingArticles = _allArticles.size();
                _remainingValue.setText(_remainingArticles.toString());

                //Do all the code here, after the response//
                _random_index = RandomIndex.getRandomNumberInRange(0, _allArticles.size() - 1);
                _wordFromList.setText(_allArticles.get(_random_index).getSingular());
            }

            @Override
            public void onFailure(Call<List<ArticlesModel>> call, Throwable t) {
                setContentView(R.layout.failure_layout);
            }
        });

        ((Button)findViewById(R.id.nextQuestion)).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        Button view = (Button) v;
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                        // Your action here on button click
                        //Call Next Question method
                        onNextQuestion(v);
                    case MotionEvent.ACTION_CANCEL: {
                        Button view = (Button) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.derBtn:
                _btnPressedTxt = getString(R.string.derTxt);
                checkAnswer(v);
                break;
            case R.id.dieBtn:
                _btnPressedTxt = getString(R.string.dieTxt);
                checkAnswer(v);
                break;
            case R.id.dasBtn:
                _btnPressedTxt = getString(R.string.dastxt);
                checkAnswer(v);
                break;
        }
    }

    public void onNextQuestion(View v){

        //Remove word from list
        _allArticles.remove(_allArticles.get(_random_index));
        _remainingArticles =  _allArticles.size();
        _remainingValue.setText(_remainingArticles.toString());
        //Update view
        if(!_allArticles.isEmpty()){
            //Generate new random index
            _random_index = RandomIndex.getRandomNumberInRange(0, _allArticles.size() - 1);
            //Update view
            _wordFromList.setText(_allArticles.get(_random_index).getSingular());
            //Disable NEXT btn
            _nextQst.setVisibility(View.INVISIBLE);
            //Reset btn color
            for (Button articleBtn : _articleBtnList) {
               articleBtn.setBackground(getResources().getDrawable(R.drawable.buttonshape));
            }
        } else {
            //Close activity and show MainActivity
            Snackbar.make(v, getString(R.string.over), Snackbar.LENGTH_SHORT).show();
            finish();
        }

    }

    public void checkAnswer(View v){
        _answerBtn = findViewById(v.getId());

        if(!_allArticles.isEmpty()){
            if( _allArticles.get(_random_index).getArticle().equalsIgnoreCase(_btnPressedTxt) ){
                //Change answerBtn color to green
                _answerBtn.setBackground(getResources().getDrawable(R.drawable.buttonshapegreen));
                //Update correct counter
                _countCorrect += 1;
                _correctValue.setText(_countCorrect.toString());
            } else {
                //Change answerBtn color to red
                _answerBtn.setBackground(getResources().getDrawable(R.drawable.buttonshapered));
                //Update false counter
                _countFalse += 1;
                _falseValue.setText(_countFalse.toString());
                for (Button correctBtn : _articleBtnList) {
                    //Change correctAnswerBtn color to green
                    if (correctBtn.getText().toString().equalsIgnoreCase(_allArticles.get(_random_index).getArticle())){
                        correctBtn.setBackground(getResources().getDrawable(R.drawable.buttonshapegreen));
                        break;
                    }
                }
            }
            //Enable NEXT btn
            _nextQst.setVisibility(View.VISIBLE);

        } else {
            Snackbar.make(v, getString(R.string.waitForServer), Snackbar.LENGTH_SHORT).show();
        }
    }
}
