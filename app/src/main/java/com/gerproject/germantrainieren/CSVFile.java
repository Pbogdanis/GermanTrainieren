package com.gerproject.germantrainieren;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVFile {
    InputStream inputStream;
    public static ArrayList<String> articles, singular, plural, concat;

    public CSVFile(InputStream inputStream){
        this.inputStream = inputStream;
        articles = new ArrayList<String>();
        singular = new ArrayList<String>();
        plural = new ArrayList<String>();
        concat = new ArrayList<String>();
    }

    public List read(){
        List resultList = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                resultList.add(row);
                articles.add(row[0]);
                singular.add(row[1]);
                plural.add(row[2]);
                concat.add(row[0] + " " + row[1] + " " + row[2]);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
        return resultList;
    }
}
