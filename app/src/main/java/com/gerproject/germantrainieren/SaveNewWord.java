package com.gerproject.germantrainieren;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Helpers.BasicAuthInterceptor;
import Models.ArticlesModel;
import Models.PluralsModel;
import okhttp3.Credentials;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.gerproject.germantrainieren.MainActivity.password;
import static com.gerproject.germantrainieren.MainActivity.mContext;
import static com.gerproject.germantrainieren.MainActivity.refreshBtns;
import static com.gerproject.germantrainieren.MainActivity.username;


public class SaveNewWord extends AppCompatActivity implements View.OnClickListener {

    private EditText _new_singular, _new_plural;
    //private Spinner _new_article;
    private String _new_article_value;
    private Boolean saveIsValid = false, _isPlural;
    private String[] _article_values = new String[]{"Choose the article","DER","DIE","DAS"};
    private final List<String> _articlesList = new ArrayList<>(Arrays.asList(_article_values));
    //private Switch _wordType;
    private Call<String> _call;
    private JsonPlaceHolderApi _jsonPlaceHolderApi;
    private Button _singularSave, _pluralSave, _derBtn, _dieBtn, _dasBtn;

    public SaveNewWord(){
        //Authentication client
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(username, password))
                .build();

        //Make post call to API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mContext.getResources().getString(R.string.api_url))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        _jsonPlaceHolderApi = jsonPlaceHolderApi;

        refreshBtns();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_new_word_layout);

        //Get Items from the layout
        //_new_article = findViewById(R.id.art);
        _singularSave = findViewById(R.id.singularSave);
        _pluralSave = findViewById(R.id.pluralSave);
        _derBtn = findViewById(R.id.derBtn);
        _dieBtn = findViewById(R.id.dieBtn);
        _dasBtn = findViewById(R.id.dasBtn);

        _new_singular = findViewById(R.id.new_singular);
        _new_plural = findViewById(R.id.new_plural);
        _new_article_value = "";

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,_articlesList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);

    }

    @Override
    public void onClick(final View v) {
        _isPlural = false;
        SetSingularOrPlural(v);
        if(!_isPlural){
            SetArticle(v);
        }

        String _new_singular_value = _new_singular.getText().toString();
        String _new_plural_value = _new_plural.getText().toString();

        //Check if all values have been inserted
        //Singular
        if(!_isPlural){
            if(!_new_article_value.isEmpty() &&
                    !_new_singular_value.isEmpty()){
                saveIsValid = true;
                //Prepare insert singular call
                List<ArticlesModel> newSingular = new ArrayList<>();
                ArticlesModel newSingularModel = new ArticlesModel();

                newSingularModel.setArticle(_new_article_value);
                newSingularModel.setSingular(_new_singular_value);
                newSingular.add(newSingularModel);
                _call = _jsonPlaceHolderApi.InsertSingular(newSingularModel);
            }
        } else {
            //Plural
            if(!_new_singular_value.isEmpty() &&
                    !_new_plural_value.isEmpty()){
                saveIsValid = true;
                //Prepare insert plural call
                List<PluralsModel> newPlural = new ArrayList<>();
                PluralsModel newpluralModel = new PluralsModel();

                newpluralModel.setSingular(_new_singular_value);
                newpluralModel.setPlural(_new_plural_value);
                newPlural.add(newpluralModel);
                _call = _jsonPlaceHolderApi.InsertPlural(newpluralModel);
            }
        }

        if(v.getId() == R.id.new_word_save){

            //Animation on button click
            ((Button)findViewById(R.id.new_word_save)).setOnTouchListener(new View.OnTouchListener() {

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
                            onSave(v);
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
    }

    private void onSave(final View v){
        if(!saveIsValid){
            Snackbar.make(v, getString(R.string.allValues), Snackbar.LENGTH_SHORT).show();
        } else {

            _call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(!response.isSuccessful()){
                        setContentView(R.layout.failure_layout);
                    } else {
                        Snackbar.make(v, response.body(), Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    setContentView(R.layout.failure_layout);
                }
            });
        }
        saveIsValid = false;
    }

    private void SetSingularOrPlural(View v){
        if(v.getId()==R.id.singularSave){
            _singularSave.setBackground(mContext.getDrawable(R.drawable.buttonshapegreen));
            _pluralSave.setBackground(mContext.getDrawable(R.drawable.buttonshape));
            _isPlural = false;
            _new_plural.setEnabled(false);
            _derBtn.setClickable(true);
            _dieBtn.setClickable(true);
            _dasBtn.setClickable(true);
            _derBtn.setBackground(mContext.getDrawable(R.drawable.buttonshape));
            _dieBtn.setBackground(mContext.getDrawable(R.drawable.buttonshape));
            _dasBtn.setBackground(mContext.getDrawable(R.drawable.buttonshape));
        } else if (v.getId()==R.id.pluralSave){
            _singularSave.setBackground(mContext.getDrawable(R.drawable.buttonshape));
            _pluralSave.setBackground(mContext.getDrawable(R.drawable.buttonshapegreen));
            _isPlural = true;
            _new_plural.setEnabled(true);
            _derBtn.setClickable(false);
            _dieBtn.setClickable(false);
            _dasBtn.setClickable(false);
            _derBtn.setBackground(mContext.getDrawable(R.drawable.buttonshape));
            _dieBtn.setBackground(mContext.getDrawable(R.drawable.buttonshape));
            _dasBtn.setBackground(mContext.getDrawable(R.drawable.buttonshape));
        }
    }

    private void SetArticle(View v){
        switch (v.getId()){
            case R.id.derBtn:
                _derBtn.setBackground(mContext.getDrawable(R.drawable.buttonshapegreen));
                _dieBtn.setBackground(mContext.getDrawable(R.drawable.buttonshape));
                _dasBtn.setBackground(mContext.getDrawable(R.drawable.buttonshape));
                _new_article_value = _derBtn.getText().toString().toLowerCase();
                break;
            case R.id.dieBtn:
                _derBtn.setBackground(mContext.getDrawable(R.drawable.buttonshape));
                _dieBtn.setBackground(mContext.getDrawable(R.drawable.buttonshapegreen));
                _dasBtn.setBackground(mContext.getDrawable(R.drawable.buttonshape));
                _new_article_value = _dieBtn.getText().toString().toLowerCase();
                break;
            case R.id.dasBtn:
                _derBtn.setBackground(mContext.getDrawable(R.drawable.buttonshape));
                _dieBtn.setBackground(mContext.getDrawable(R.drawable.buttonshape));
                _dasBtn.setBackground(mContext.getDrawable(R.drawable.buttonshapegreen));
                _new_article_value = _dasBtn.getText().toString().toLowerCase();
                break;
        }
    }
}
