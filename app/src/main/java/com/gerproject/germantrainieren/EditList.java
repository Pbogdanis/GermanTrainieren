package com.gerproject.germantrainieren;

import android.content.Intent;
import android.content.res.Resources;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class EditList extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editlist_main_layout);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            //Save a new word activity
            case R.id.enter_new_btn:
                intent = new Intent(this, SaveNewWord.class);
                this.startActivity(intent);
                break;

            //delete a word ( DB record )
            case R.id.delete_btn:
                intent = new Intent(this, DeleteWord.class);
                this.startActivity(intent);
                break;
        }
    }

}
