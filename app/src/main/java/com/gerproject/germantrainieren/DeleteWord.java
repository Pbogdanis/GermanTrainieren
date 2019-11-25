package com.gerproject.germantrainieren;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;

import static com.gerproject.germantrainieren.CSVFile.articles;
import static com.gerproject.germantrainieren.CSVFile.concat;
import static com.gerproject.germantrainieren.CSVFile.plural;
import static com.gerproject.germantrainieren.CSVFile.singular;
import static com.gerproject.germantrainieren.MainActivity.permissionGranted;

public class DeleteWord extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private String wordForDeletion;
    private EditText filter;
    private ArrayAdapter<String> listAdapter;
    private int _idForDeletion;
    public static ArrayList<String> cpArticles, cpSingular, cpPlural, cpConcat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_word_layout);
        listView = (ListView) findViewById(R.id.listview);

        cpArticles = articles;
        cpSingular = singular;
        cpPlural = plural;
        cpConcat = concat;

        listAdapter = new ArrayAdapter<String>(this, R.layout.listview_row, cpConcat);


        filter = findViewById(R.id.filter);

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                listView.getSelector().setAlpha(255);
                wordForDeletion = listView.getItemAtPosition(position).toString();
                _idForDeletion = position;
            }
        });

        filter.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                listAdapter.getFilter().filter(cs);
                listView.clearFocus();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                listView.getSelector().setAlpha(0);
            }
        });

    }

    @Override
    public void onClick(View v) {
        //Save changes to file
        WriteToFile();
    }

    public void WriteToFile() {
        //Ask for WRITE_EXTERNAL_STORAGE permission
        if(permissionGranted){

            FileWriter writer = null;

            String filename = "csvfile.csv";

            File file = new File(getExternalFilesDir(null), filename);

            FileOutputStream outputStream = null;
            try {
                cpArticles.remove(_idForDeletion);
                cpSingular.remove(_idForDeletion);
                cpPlural.remove(_idForDeletion);
                cpConcat.remove(_idForDeletion);

                //Refresh listadapter
                listAdapter = new ArrayAdapter<String>(this, R.layout.listview_row, cpConcat);
                listView.setAdapter(listAdapter);
                outputStream = new FileOutputStream(file, false);
                //outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                for (int i = 0; i < cpArticles.size(); i++) {
                    outputStream.write((cpArticles.get(i) + ",").getBytes());
                    outputStream.write((cpSingular.get(i) + ",").getBytes());
                    outputStream.write((cpPlural.get(i) + "\n").getBytes());
                }
                outputStream.close();
                Toast.makeText(this, "Word deleted and new file generated", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            //Ask for permission
            ActivityCompat.requestPermissions(DeleteWord.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
     }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    permissionGranted = true;

                } else {

                    Toast.makeText(DeleteWord.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                    permissionGranted = false;

                }
            }
        }
    }
}
