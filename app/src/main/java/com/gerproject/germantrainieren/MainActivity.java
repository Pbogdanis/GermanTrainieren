package com.gerproject.germantrainieren;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;

import Helpers.DialogBuilder;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static Intent intent;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
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
                //TODO show dialog only if there are no sharedPreferences credentials
                showDialog();
                break;
            //new "delete word" activity
            case R.id.delete_btn:
                intent = new Intent(this, DeleteWord.class);
                startAc();
                break;
        }
    }

    public void showDialog() {
        DialogFragment newDialog = new DialogBuilder();
        newDialog.show(getSupportFragmentManager(),"Dialog");
    }

    public static void startAc(){
        mContext.startActivity(intent);
    }
}
