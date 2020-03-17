package com.gerproject.germantrainieren;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Models.ArticlesModel;
import Models.PluralsModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SaveNewWord extends AppCompatActivity implements View.OnClickListener {

    private EditText _new_singular, _new_plural;
    private Spinner _new_article;
    private String _new_article_value;
    private Boolean saveIsValid = false;
    private String[] _article_values = new String[]{"Choose the article","der","die","das"};
    private final List<String> _articlesList = new ArrayList<>(Arrays.asList(_article_values));
    private Switch _wordType;
    private Call<String> _call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_new_word_layout);

        //Get Items from the layout
        _new_article = findViewById(R.id.new_article);
        _new_singular = findViewById(R.id.new_singular);
        _new_plural = findViewById(R.id.new_plural);
        _new_article_value = "";
        _wordType = findViewById(R.id.wordType);

        _wordType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Singular
                if(!isChecked){
                    //Enable items
                    _wordType.setText(R.string.singular);
                    _new_article.setEnabled(true);
                    _new_singular.setEnabled(true);
                    _new_plural.setEnabled(false);

                } else {
                    //Plural
                    _wordType.setText(R.string.plural);
                    _new_article.setEnabled(false);
                    _new_singular.setEnabled(true);
                    _new_plural.setEnabled(true);
                }
            }
        });



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
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        _new_article.setAdapter(spinnerArrayAdapter);

        _new_article.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    //Save the article chosen
                    _new_article_value = selectedItemText;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                _new_article_value = "";
            }
        });

    }

    @Override
    public void onClick(View v) {

        String _new_singular_value = _new_singular.getText().toString();
        String _new_plural_value = _new_plural.getText().toString();

        //Make post call to API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://germanapi.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi _jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        //Check if all values have been inserted
        //Singular
        if(!_wordType.isChecked()){
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

        if(!saveIsValid){
            Toast.makeText(this, "You must enter all the values", Toast.LENGTH_SHORT).show();
        } else {

            _call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(!response.isSuccessful()){
                        setContentView(R.layout.failure_layout);
                    } else {
                        Toast.makeText(getApplicationContext(), response.body() , Toast.LENGTH_SHORT).show();
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
}
