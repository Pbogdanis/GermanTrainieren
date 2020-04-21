package Helpers;

import com.gerproject.germantrainieren.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Models.ArticlesModel;
import Models.PluralsModel;

import static com.gerproject.germantrainieren.Articles._countCorrect;
import static com.gerproject.germantrainieren.Articles._countFalse;
import static com.gerproject.germantrainieren.Articles._remainingArticles;
import static com.gerproject.germantrainieren.Articles._allArticles;
import static com.gerproject.germantrainieren.Plurals._countCorrectPlural;
import static com.gerproject.germantrainieren.Plurals._countFalsePlural;
import static com.gerproject.germantrainieren.Plurals._remainingPlurals;
import static com.gerproject.germantrainieren.Plurals._allPlurals;
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

        _countCorrect = 0;
        _countFalse = 0;
        _remainingArticles = 0;
        _allArticles =  new ArrayList<>();

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
        _countCorrect = 0;
        _countFalse = 0;
        _remainingArticles = 0;
        _allArticles = new ArrayList<>();
        saveArticleValues(_allArticles);
    }

    public static void savePluralValues(List<PluralsModel> allPlurals){
        //Save values in memory
        editor = sharedPref.edit();
        editor.putString(mContext.getString(R.string.correctLabelPlural), String.valueOf(_countCorrectPlural));
        editor.putString(mContext.getString(R.string.falseLabelPlural), String.valueOf(_countFalsePlural));
        editor.putString(mContext.getString(R.string.remainingLabelPlural), String.valueOf(_remainingPlurals));
        //Set the values
        Gson gson = new Gson();
        String json = gson.toJson(allPlurals);
        editor.putString(mContext.getString(R.string.plural), json);
        editor.apply();
    }

    public static void checkPluralValues(){

        _countCorrectPlural = 0;
        _countFalsePlural = 0;
        _remainingPlurals = 0;
        _allPlurals =  new ArrayList<>();

        if(sharedPref.contains(mContext.getString(R.string.correctLabelPlural))){
            _countCorrectPlural = Integer.valueOf(sharedPref.getString(mContext.getString(R.string.correctLabelPlural),""));
        }

        if(sharedPref.contains(mContext.getString(R.string.falseLabelPlural))){
            _countFalsePlural = Integer.valueOf(sharedPref.getString(mContext.getString(R.string.falseLabelPlural),""));
        }

        if(sharedPref.contains(mContext.getString(R.string.remainingLabelPlural))){
            _remainingPlurals = Integer.valueOf(sharedPref.getString(mContext.getString(R.string.remainingLabelPlural),""));
        }

        Gson gson = new Gson();
        String json = sharedPref.getString(mContext.getString(R.string.plural),"");
        Type type = new TypeToken<ArrayList<PluralsModel>>() {}.getType();
        _allPlurals = gson.fromJson(json, type);

        if(_allPlurals == null){
            _allPlurals = new ArrayList<>();
        }
    }

    public static void resetPluralValues(){
        _countCorrectPlural = 0;
        _countFalsePlural = 0;
        _remainingPlurals = 0;
        _allPlurals = new ArrayList<>();
        savePluralValues(_allPlurals);
    }
}
