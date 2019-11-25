package com.gerproject.germantrainieren;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import static com.gerproject.germantrainieren.CSVFile.articles;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static SQLiteDatabase trainingDB;
    public static List dataFromCSV;
    public static String pathFromFile;
    public static boolean permissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ask for permission
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        String[] mimetypes = new String[]{"text/csv", "text/comma-separated-values", "application/csv"};
        switch (v.getId()) {

            //new play with articles activity
            case R.id.article_btn:

                //Check if user has provided a .csv first!
                if(checkForCSV()){
                    intent = new Intent(this, Articles.class);
                    this.startActivity(intent);
                    break;
                } else {
                    Toast.makeText(this, "Please provide a .csv first to load the data", Toast.LENGTH_SHORT).show();
                    break;
                }



            //new play with plurals activity
            case R.id.plurals_btn:

                //Check if user has provided a .csv first!
                if(checkForCSV()){
                    intent = new Intent(this, Plurals.class);
                    this.startActivity(intent);
                    break;
                } else {
                    Toast.makeText(this, "Please provide a .csv first to load the data", Toast.LENGTH_SHORT).show();
                    break;
                }

            //New LoadList function
            case R.id.load_list:

                //Read data from .csv before editing the file
                intent = new Intent().setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);

                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a file"), 1);
                }
                catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "No app found for importing the file.", Toast.LENGTH_LONG).show();
                }
                break;

            //new EditList activity
            case R.id.edit_btn:

                //Read data from .csv before editing the file
                intent = new Intent().setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);

                if(dataFromCSV != null){
                    intent = new Intent(this, EditList.class);
                    this.startActivity(intent);
                } else {
                    Toast.makeText(this, "Please load your list first!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        InputStream inputStream = null;
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK && data != null){
                try {
                    inputStream = getContentResolver().openInputStream(Objects.requireNonNull(data.getData()));
                    pathFromFile = data.getData().getPath();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                String extension = "";

                int i = pathFromFile.lastIndexOf('.');
                if (i > 0) {
                    extension = pathFromFile.substring(i+1);
                }
                if(!extension.equals("csv")){
                    Toast.makeText(this, "Wrong filetype!", Toast.LENGTH_SHORT).show();
                } else {
                    CSVFile csvFile = new CSVFile(inputStream);
                    dataFromCSV = csvFile.read();
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
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

                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                    permissionGranted = false;

                }
            }
        }
    }

    private boolean checkForCSV() {
        if(articles == null){
            return false;
        }
        return true;
    }
}
