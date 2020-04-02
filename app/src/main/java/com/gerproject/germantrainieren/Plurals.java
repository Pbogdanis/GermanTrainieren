package com.gerproject.germantrainieren;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.concurrent.TimeUnit;

import Models.PluralsModel;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static Helpers.RandomIndex.getRandomNumberInRange;
import static com.gerproject.germantrainieren.MainActivity.mContext;
import static com.gerproject.germantrainieren.MainActivity.refreshBtns;

public class Plurals extends AppCompatActivity implements View.OnClickListener {

    private String _next_plural;
    private int _random_index;
    private EditText _answer_txt;
    private String _answer;
    private boolean _isCorrect;
    private TextView _pluralFromList;
    private List<PluralsModel> _allPlurals;
    private JsonPlaceHolderApi _jsonPlaceHolderApi;

    public Plurals(){
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
        setContentView(R.layout.plurals_layout);

        _answer_txt = findViewById(R.id.answer_txt);
        _pluralFromList = findViewById(R.id.pluralFromList);

        Call<List<PluralsModel>> call = _jsonPlaceHolderApi.GetAllFromPlurals();

        call.enqueue(new Callback<List<PluralsModel>>() {
            @Override
            public void onResponse(Call<List<PluralsModel>> call, Response<List<PluralsModel>> response) {
                if(!response.isSuccessful()){
                    setContentView(R.layout.failure_layout);
                    return;
                }
                _allPlurals = response.body();

                //Do all the code here, after the response//
                _random_index = getRandomNumberInRange(0, _allPlurals.size() - 1);
                _pluralFromList.setText(_allPlurals.get(_random_index).getSingular());
            }

            @Override
            public void onFailure(Call<List<PluralsModel>> call, Throwable t) {
                setContentView(R.layout.failure_layout);
            }
        });
    }

    @Override
    public void onClick(View v) {

        _isCorrect = false;
        _answer = _answer_txt.getText().toString();

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.top_coordinator);
        Snackbar snack= Snackbar.make(coordinatorLayout, "Text", Snackbar.LENGTH_LONG);
        View view = snack.getView();
        CoordinatorLayout.LayoutParams params=(CoordinatorLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        //snackbar.show();

        if(!_allPlurals.isEmpty()){
            if (_answer.isEmpty()){
                snack = Snackbar.make(coordinatorLayout, getString(R.string.selectAnAnswer), Snackbar.LENGTH_SHORT);
                snack.show();
            } else {

                //Check for correct answer//
                if ( _allPlurals.get(_random_index).getPlural().equalsIgnoreCase(_answer)){
                    _isCorrect = true;
                }

                if(_isCorrect){
                    snack = Snackbar.make(coordinatorLayout, getString(R.string.correct), Snackbar.LENGTH_SHORT);
                    snack.show();
                } else {
                    snack = Snackbar.make(coordinatorLayout, getString(R.string.wrongPlural) + " " +  _allPlurals.get(_random_index).getPlural(), Snackbar.LENGTH_SHORT);
                    snack.show();
                }

                //Remove word from list//
                _allPlurals.remove(_allPlurals.get(_random_index));

                //Update view
                if(!_allPlurals.isEmpty()){
                    //Generate new random index
                    _random_index = getRandomNumberInRange(0, _allPlurals.size() - 1);
                    //Update view
                    _next_plural = _allPlurals.get(_random_index).getSingular();
                    _pluralFromList.setText(_next_plural);
                    _answer_txt.setText("");
                } else {
                    //Close activity and show MainActivity
                    snack = Snackbar.make(coordinatorLayout, getString(R.string.over), Snackbar.LENGTH_SHORT);
                    snack.show();

                    finish();
                }

            }
        }
    }
}
