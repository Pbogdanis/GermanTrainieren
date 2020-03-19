package com.gerproject.germantrainieren;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;


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

    }
}
