package com.gerproject.germantrainieren;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.gerproject.germantrainieren.CSVFile.singular;
import static com.gerproject.germantrainieren.CSVFile.plural;

public class Plurals extends AppCompatActivity implements View.OnClickListener {

    private TextView _question_txt;
    private String _next_plural;
    private int _random_index;
    private EditText _answer_txt;
    private Toast toastMsg;
    public static ArrayList<String> cpSingular, cpPlurals;
    private ArrayList<String> _allPluralsArray;
    private boolean _isCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plurals_layout);

        cpSingular = new ArrayList<String>(singular);
        cpPlurals = new ArrayList<String>(plural);

        _question_txt = findViewById(R.id.question_txt);
        _answer_txt = findViewById(R.id.answer_txt);
        _random_index = getRandomNumberInRange(0, cpSingular.size() - 1);
        _next_plural = "The plural of " + cpSingular.get(_random_index) + " is : ";
        _question_txt.setText(_next_plural);
    }

    @Override
    public void onClick(View v) {

        _isCorrect = false;
        _allPluralsArray = new ArrayList<String>();
        if(!cpSingular.isEmpty()){
            if (_answer_txt.getText().toString().isEmpty()){
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
            } else {

                //Check if there are more plurals
                if(cpPlurals.get(_random_index).contains("/") ){
                    String[] allPlurals = cpPlurals.get(_random_index).split("/");

                    Collections.addAll(_allPluralsArray, allPlurals);
                } else {
                    _allPluralsArray.add(cpPlurals.get(_random_index));
                }


                for (int i=0; i<_allPluralsArray.size(); i++){
                    if(_allPluralsArray.get(i).equalsIgnoreCase(_answer_txt.getText().toString())){
                        _isCorrect = true;
                    }
                }
                if(_isCorrect){
                    Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Wrong! The correct plural is " + cpPlurals.get(_random_index), Toast.LENGTH_SHORT).show();
                }

                //Remove word from the lists
                cpSingular.remove(_random_index);
                cpPlurals.remove(_random_index);

                //Update view
                if(!cpSingular.isEmpty()){
                    //Generate new random index
                    _random_index = getRandomNumberInRange(0, cpSingular.size() - 1);
                    //Update view
                    _next_plural = "The plural of " + cpSingular.get(_random_index) + " is : ";
                    _question_txt.setText(_next_plural);
                    _answer_txt.setText("");
                } else {
                    //Close activity and show MainActivity
                    toastMsg = Toast.makeText(this, "The quiz is over!", Toast.LENGTH_SHORT);
                    toastMsg.setGravity(Gravity.TOP| Gravity.CENTER, 0, 0);
                    toastMsg.show();

                    finish();
                }

            }
        }
    }

    private static int getRandomNumberInRange(int min, int max) {

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
