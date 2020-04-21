package Helpers;

import com.gerproject.germantrainieren.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Models.ArticlesModel;

import static com.gerproject.germantrainieren.Articles._countCorrect;
import static com.gerproject.germantrainieren.Articles._countFalse;
import static com.gerproject.germantrainieren.Articles._remainingArticles;
import static com.gerproject.germantrainieren.Articles._allArticles;
import static com.gerproject.germantrainieren.MainActivity.editor;
import static com.gerproject.germantrainieren.MainActivity.mContext;
import static com.gerproject.germantrainieren.MainActivity.sharedPref;

public class Stats {

    public static void saveArticleValues(List<ArticlesModel> allArticles){
        //Save values in memory
        editor = sharedPref.edit();
        editor.putString(mContext.getString(R.string.correctLabel), String.valueOf(_countCorrect));
        editor.putString(mContext.getString(R.string.falseLabel), String.valueOf(_countFalse));
        editor.putString(mContext.getString(R.string.remainingLabel), String.valueOf(_remainingArticles));
        //Set the values
        Gson gson = new Gson();
        String json = gson.toJson(allArticles);
        editor.putString(mContext.getString(R.string.article), json);
        editor.apply();
    }

    public static void checkArticleValues(){

        Integer countCorrect = 0, countFalse = 0, remainingArticles = 0;
        List<ArticlesModel> allArticles = new ArrayList<>();
        _countCorrect = countCorrect;
        _countFalse = countFalse;
        _remainingArticles = remainingArticles;
        _allArticles = allArticles;

        if(sharedPref.contains(mContext.getString(R.string.correctLabel))){
            _countCorrect = Integer.valueOf(sharedPref.getString(mContext.getString(R.string.correctLabel),""));
        }

        if(sharedPref.contains(mContext.getString(R.string.falseLabel))){
            _countFalse = Integer.valueOf(sharedPref.getString(mContext.getString(R.string.falseLabel),""));
        }

        if(sharedPref.contains(mContext.getString(R.string.remainingLabel))){
            _remainingArticles = Integer.valueOf(sharedPref.getString(mContext.getString(R.string.remainingLabel),""));
        }

        Gson gson = new Gson();
        String json = sharedPref.getString(mContext.getString(R.string.article),"");
        Type type = new TypeToken<ArrayList<ArticlesModel>>() {}.getType();
        _allArticles = gson.fromJson(json, type);

        if(_allArticles == null){
            _allArticles = new ArrayList<>();
        }
    }

    public static void resetArticleValues(){
        Integer countCorrect = 0, countFalse = 0, remainingArticles = 0;
        List<ArticlesModel> allArticles = new ArrayList<>();
        _countCorrect = countCorrect;
        _countFalse = countFalse;
        _remainingArticles = remainingArticles;
        _allArticles = allArticles;
        saveArticleValues(_allArticles);
    }
}
