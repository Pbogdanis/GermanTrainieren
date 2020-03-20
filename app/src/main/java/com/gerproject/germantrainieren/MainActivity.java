package com.gerproject.germantrainieren;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import Helpers.DialogBuilder;

import static Helpers.DialogBuilder._password;
import static Helpers.DialogBuilder._username;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static Intent intent;
    public static Context mContext;
    public static SharedPreferences sharedPref;
    public static SharedPreferences.Editor editor;
    public static DialogFragment newDialog;
    public static String username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //new "play with articles" activity
            case R.id.article_btn:
                intent = new Intent(this, Articles.class);
                startAc();
                break;
            //new "play with plurals" activity
            case R.id.plurals_btn:
                intent = new Intent(this, Plurals.class);
                startAc();
                break;
            //new "save word" activity
            case R.id.saveBtn:
                intent = new Intent(this, SaveNewWord.class);

                //DialogBuilder builder = new DialogBuilder();
                //Check login status
                /*while (!DialogBuilder.isAuth){
                    // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor

                    showDialog();
                    if(DialogBuilder.isAuth){
                        break;
                    }
                }*/
                if(hasCred()) {
                    startAc();
                } else {
                    showDialog();
                }
                break;
            //new "delete word" activity
            case R.id.delete_btn:
                intent = new Intent(this, DeleteWord.class);
                startAc();
                break;
        }
    }

    public void showDialog() {
        newDialog = new DialogBuilder();
        newDialog.show(getSupportFragmentManager(),"Dialog");
    }

    public static void dismissDialog(){
        newDialog.dismiss();
    }

    public static void startAc(){
        mContext.startActivity(intent);
    }

    public boolean hasCred(){
        username = sharedPref.getString(getString(R.string.username),"");
        password = sharedPref.getString(getString(R.string.password),"");

        return ((username != null && !username.isEmpty()) && (password != null && !password.isEmpty()));
    }

    public static void saveCred(){
        //Save username and password to shared preferences
        editor = sharedPref.edit();
        editor.putString(mContext.getString(R.string.username), _username);
        editor.putString(mContext.getString(R.string.password), _password);
        editor.apply();
    }
}
