package com.gerproject.germantrainieren;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {

            //new play with articles activity
            case R.id.article_btn:
                intent = new Intent(this, Articles.class);
                this.startActivity(intent);
                break;
            //new play with plurals activity
            case R.id.plurals_btn:
                intent = new Intent(this, Plurals.class);
                this.startActivity(intent);
                break;
            case R.id.saveBtn:
                intent = new Intent(this, SaveNewWord.class);
                this.startActivity(intent);
                break;
        }
    }


}
