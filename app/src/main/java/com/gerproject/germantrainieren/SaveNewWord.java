package com.gerproject.germantrainieren;

import android.Manifest;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.gerproject.germantrainieren.CSVFile.articles;
import static com.gerproject.germantrainieren.CSVFile.concat;
import static com.gerproject.germantrainieren.CSVFile.plural;
import static com.gerproject.germantrainieren.CSVFile.singular;
import static com.gerproject.germantrainieren.MainActivity.permissionGranted;

public class SaveNewWord extends AppCompatActivity implements View.OnClickListener {

    private EditText new_article, new_singular, new_plural;
    private String _new_article_value, _new_singular_value, _new_plural_value;
    private Boolean saveIsValid = false;
    public static ArrayList<String> cpArticles, cpSingular, cpPlural, cpConcat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_new_word_layout);

        //Get EditTexts from the layout
        new_article = findViewById(R.id.new_article);
        new_singular = findViewById(R.id.new_singular);
        new_plural = findViewById(R.id.new_plural);

        cpArticles = articles;
        cpSingular = singular;
        cpPlural = plural;
        cpConcat = concat;
    }

    @Override
    public void onClick(View v) {

        _new_article_value = new_article.getText().toString();
        _new_singular_value = new_singular.getText().toString();
        _new_plural_value = new_plural.getText().toString();

        //Check if all values have been inserted
        if(_new_article_value.isEmpty() ||
                _new_singular_value.isEmpty() ||
                _new_plural_value.isEmpty()){
            Toast.makeText(this, "You must enter all the values", Toast.LENGTH_SHORT).show();
        } else {
            saveIsValid = true;
        }

        if (saveIsValid){
            SaveToFile();
            saveIsValid = false;
        }

    }

    public void SaveToFile() {
        //Ask for WRITE_EXTERNAL_STORAGE permission
        if(permissionGranted){

            String filename = "csvfile.csv";
            File file = new File(getExternalFilesDir(null), filename);
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file, true);

                cpArticles.add(_new_article_value);
                cpSingular.add(_new_singular_value);
                cpPlural.add(_new_plural_value);
                cpConcat.add(_new_article_value + " " + _new_singular_value + " " + _new_plural_value);

                outputStream.write((_new_article_value + ",").getBytes());
                outputStream.write((_new_singular_value + ",").getBytes());
                outputStream.write((_new_plural_value+ "\n").getBytes());

                outputStream.flush();
                outputStream.close();
                Toast.makeText(this, "Word added to file", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            //Ask for permission
            ActivityCompat.requestPermissions(SaveNewWord.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }
}
