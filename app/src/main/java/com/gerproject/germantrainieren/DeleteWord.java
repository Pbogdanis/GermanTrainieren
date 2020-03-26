package com.gerproject.germantrainieren;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import Helpers.ListViewAdapter;
import Helpers.RandomIndex;
import Models.ArticlesModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static Helpers.ListViewAdapter.FIRST_COLUMN;
import static Helpers.ListViewAdapter.SECOND_COLUMN;
import static Helpers.ListViewAdapter.THIRD_COLUMN;


public class DeleteWord extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private String wordForDeletion;
    private EditText filter;
    private ListViewAdapter listAdapter;
    private int _idForDeletion;
    private List<ArticlesModel> _allArticles;
    private ArrayList<HashMap<String, String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_word_layout);
        listView = findViewById(R.id.listview);

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


                list=new ArrayList<HashMap<String,String>>();

                for (ArticlesModel article: _allArticles){
                    HashMap<String,String> hashmap=new HashMap<String, String>();
                    hashmap.put(FIRST_COLUMN, String.valueOf(article.getId()));
                    hashmap.put(SECOND_COLUMN, article.getArticle());
                    hashmap.put(THIRD_COLUMN, article.getSingular());
                    list.add(hashmap);
                }

                listAdapter = new ListViewAdapter((Activity) MainActivity.mContext, list);

                filter = findViewById(R.id.filter);

                listView.setAdapter(listAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        listView.getSelector().setAlpha(255);
                        wordForDeletion = listView.getItemAtPosition(position).toString();
                        JSONObject json = null;
                        try {
                            json = new JSONObject(wordForDeletion);
                            String message = json.getString("Third");
                            wordForDeletion = message;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });

                filter.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        // When user changed the Text
                        //listAdapter.getFilter().filter(cs);
                        listView.clearFocus();
                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        String text = filter.getText().toString().toLowerCase(Locale.getDefault());
                        listAdapter.filter(text);
                        listView.getSelector().setAlpha(0);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<ArticlesModel>> call, Throwable t) {
                setContentView(R.layout.failure_layout);
            }
        });




    }

    @Override
    public void onClick(View v) {
        //Call delete

    }
}
