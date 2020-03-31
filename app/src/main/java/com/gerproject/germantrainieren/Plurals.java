package com.gerproject.germantrainieren;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import Models.PluralsModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static Helpers.RandomIndex.getRandomNumberInRange;

public class Plurals extends AppCompatActivity implements View.OnClickListener {

    private String _next_plural;
    private int _random_index;
    private EditText _answer_txt;
    private String _answer;
    private Toast toastMsg;
    private boolean _isCorrect;
    private TextView _pluralFromList;
    private List<PluralsModel> _allPlurals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plurals_layout);

        _answer_txt = findViewById(R.id.answer_txt);
        _pluralFromList = findViewById(R.id.pluralFromList);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<PluralsModel>> call = jsonPlaceHolderApi.GetAllFromPlurals();

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

        if(!_allPlurals.isEmpty()){
            if (_answer.isEmpty()){
                Snackbar.make(v, getString(R.string.selectAnAnswer), Snackbar.LENGTH_SHORT).show();
            } else {

                //Check for correct answer//
                if ( _allPlurals.get(_random_index).getPlural().equalsIgnoreCase(_answer)){
                    _isCorrect = true;
                }

                if(_isCorrect){
                    Snackbar.make(v, getString(R.string.correct), Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(v, getString(R.string.wrongPlural) + " " +  _allPlurals.get(_random_index).getPlural(), Snackbar.LENGTH_SHORT).show();
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
                    Snackbar.make(v, getString(R.string.over), Snackbar.LENGTH_SHORT).show();

                    finish();
                }

            }
        }
    }
}
