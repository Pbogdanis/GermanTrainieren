package com.gerproject.germantrainieren;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.View;
import android.widget.Button;

import static Helpers.CustomDialog._password;
import static Helpers.CustomDialog._username;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static Intent intent;
    public static Context mContext;
    public static SharedPreferences sharedPref;
    public static SharedPreferences.Editor editor;
    public static DialogFragment newDialog;
    public static String username, password;
    private static Button _articlesBtn, _pluralsBtn, _saveBtn, _deleteBtn;
    private static Button[] _btnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        _articlesBtn = findViewById(R.id.article_btn);
        _pluralsBtn = findViewById(R.id.plurals_btn);
        _saveBtn = findViewById(R.id.saveBtn);
        _deleteBtn = findViewById(R.id.delete_btn);

    }

    public static void refreshBtns(){
        _btnList = new Button[4];
        _btnList[0] = _articlesBtn;
        _btnList[1] = _pluralsBtn;
        _btnList[2] = _saveBtn;
        _btnList[3] = _deleteBtn;

        for (Button articleBtn : _btnList) {
            articleBtn.setBackground(mContext.getResources().getDrawable(R.drawable.buttonshape));
        }
    }

    @Override
    public void onClick(View v) {

        v.setBackground(getResources().getDrawable(R.drawable.buttonshapeclicked));
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

                if(hasCred()) {
                    startAc();
                } else {
                    showDialog();
                }
                break;
            //new "delete word" activity
            case R.id.delete_btn:
                intent = new Intent(this, DeleteWord.class);

                if(hasCred()) {
                    startAc();
                } else {
                    showDialog();
                }

                break;
        }
    }

    public void showDialog() {
        Intent dialogIntent = new Intent(this, Helpers.CustomDialog.class);
        this.startActivity(dialogIntent);
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
