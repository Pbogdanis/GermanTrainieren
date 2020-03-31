package com.gerproject.germantrainieren;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import Helpers.BasicAuthInterceptor;
import Helpers.ListViewAdapter;
import Models.ArticlesModel;
import Models.PluralsModel;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static Helpers.ListViewAdapter.FIRST_COLUMN;
import static Helpers.ListViewAdapter.SECOND_COLUMN;
import static Helpers.ListViewAdapter.THIRD_COLUMN;
import static com.gerproject.germantrainieren.MainActivity.mContext;
import static com.gerproject.germantrainieren.MainActivity.password;
import static com.gerproject.germantrainieren.MainActivity.username;


public class DeleteWord extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private String wordForDeletion;
    private Integer _singularIdForDeletion;
    private EditText filter;
    private ListViewAdapter listAdapter;
    private List<ArticlesModel> _allArticles;
    private ArrayList<HashMap<String, String>> list;
    private JsonPlaceHolderApi _jsonPlaceHolderApi;
    private List<PluralsModel> _pluralsOfSingular;

    public DeleteWord(){
        //Authentication client
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(username, password))
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mContext.getResources().getString(R.string.api_url))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        _jsonPlaceHolderApi = jsonPlaceHolderApi;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_word_layout);

        listView = findViewById(R.id.listview);
        _singularIdForDeletion = null;

        Call<List<ArticlesModel>> call = _jsonPlaceHolderApi.GetAllFromArticles();

        call.enqueue(new Callback<List<ArticlesModel>>() {
            @Override
            public void onResponse(Call<List<ArticlesModel>> call, Response<List<ArticlesModel>> response) {
                if(!response.isSuccessful()){
                    setContentView(R.layout.failure_layout);
                    return;
                }
                _allArticles = response.body();

                list=new ArrayList<>();

                for (ArticlesModel article: _allArticles){
                    HashMap<String,String> hashmap=new HashMap<>();
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
                            _singularIdForDeletion = json.getInt("First");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                filter.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        // When user changed the Text
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
    public void onClick(final View v) {
        //Get selected item id
        if (_singularIdForDeletion == null){
            Snackbar.make(v, getString(R.string.selectFromList), Snackbar.LENGTH_SHORT).show();
        } else {
            //Call GetPluralsFromSingular from database
            Retrofit retrofit02 = new Retrofit.Builder()
                    .baseUrl(mContext.getResources().getString(R.string.api_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            JsonPlaceHolderApi jsonPlaceHolderApi02 = retrofit02.create(JsonPlaceHolderApi.class);
            Call<List<PluralsModel>> call = jsonPlaceHolderApi02.GetPluralsOfSingular(_singularIdForDeletion);

            call.enqueue(new Callback<List<PluralsModel>>() {
                @Override
                public void onResponse(Call<List<PluralsModel>> call, Response<List<PluralsModel>> response) {
                    if (!response.isSuccessful()) {
                        setContentView(R.layout.failure_layout);
                        Snackbar.make(v, response.message(), Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    _pluralsOfSingular = response.body();
                    Snackbar.make(v, response.message(), Snackbar.LENGTH_SHORT).show();

                    //Foreach plural in list, call delete plural
                    for (PluralsModel pluralFromList : _pluralsOfSingular) {
                        Call<String> call02 = _jsonPlaceHolderApi.DeletePlural(pluralFromList.getId());

                        call02.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call02, Response<String> response) {
                                if (!response.isSuccessful()) {
                                    setContentView(R.layout.failure_layout);
                                    Snackbar.make(v, response.message(), Snackbar.LENGTH_SHORT).show();
                                    return;
                                }
                                Snackbar.make(v, response.body(), Snackbar.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<String> call02, Throwable t) {
                                setContentView(R.layout.failure_layout);
                            }
                        });
                    }

                    //Call delete singular word from Database
                    Call<String> call03 = _jsonPlaceHolderApi.DeleteSingular(_singularIdForDeletion);

                    call03.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call03, Response<String> response) {
                            if (!response.isSuccessful()) {
                                setContentView(R.layout.failure_layout);
                                Snackbar.make(v, response.message(), Snackbar.LENGTH_SHORT).show();
                                return;
                            }
                            Snackbar.make(v, response.body() + " " + getString(R.string.fromSingulars), Snackbar.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<String> call03, Throwable t) {
                            setContentView(R.layout.failure_layout);
                        }
                    });

                    //RefreshListAdapter after all calls have been executed
                    RefreshListAdapter();
                }

                @Override
                public void onFailure(Call<List<PluralsModel>> call, Throwable t) {
                    setContentView(R.layout.failure_layout);
                }
            });

        }
    }

    private void RefreshListAdapter(){
        //Refresh listview after successfull deletion
        Call<List<ArticlesModel>> callForRefresh = _jsonPlaceHolderApi.GetAllFromArticles();

        callForRefresh.enqueue(new Callback<List<ArticlesModel>>() {
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
                            _singularIdForDeletion = json.getInt("First");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });

                filter.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        // When user changed the Text
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
                //End of refresh list after deletion
            }

            @Override
            public void onFailure(Call<List<ArticlesModel>> call, Throwable t) {
                setContentView(R.layout.failure_layout);
            }
        });
    }
}
