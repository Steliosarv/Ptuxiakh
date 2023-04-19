package com.example.myapplication;

import static java.util.jar.Pack200.Unpacker.TRUE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Add extends AppCompatActivity {
    private MyDB mydb;
    private SQLiteDatabase db;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String Text = "Text";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        mydb = new MyDB(this);
        AutoCompleteTextView foodAutoComplete= findViewById(R.id.FoodAutoComplete);
        ListView listView = findViewById(R.id.list);
        TextView list= findViewById(R.id.textView49);

        SharedPreferences sharedPreferences = getSharedPreferences(Text, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //Toast.makeText(getApplicationContext(), sharedPreferences.getString("HeHasEaten", ""), Toast.LENGTH_SHORT).show();





        db = mydb.getWritableDatabase();//connect to MyDB
        final String [] mydata;

        ArrayList<String> array = new ArrayList<>();
        String sql="SELECT * FROM Food_Info";
        Cursor cr = db.rawQuery(sql, null);
        cr.moveToFirst();//cursor pointing to first row
        mydata = new String[cr.getCount()];//create array string based on numbers of row
        int i=0;
        do  {
            mydata[i] = cr.getString(0) ;//insert new stations to the array list
            Log.i("ArrayList",mydata[i]);


           i++;
        }while(cr.moveToNext());
        //Toast.makeText(getApplicationContext(), mydata[1], Toast.LENGTH_SHORT).show();
        //Finally Set the adapter to AutoCompleteTextView like this,
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, mydata);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mydata);
        //populate the list to the AutoCompleteTextView controls
        foodAutoComplete.setAdapter(adapter);
        listView.setAdapter(adapter);



        foodAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position).toString() , Toast.LENGTH_SHORT).show();

                //go(cr.getString(0), cr.getInt(2),cr.getInt(3),cr.getInt(4),cr.getInt(5),cr.getInt(6));

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //go(cr.getString(0), cr.getInt(2), cr.getInt(3), cr.getInt(4), cr.getInt(5), cr.getInt(6));
                //Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();


               try {
                   String sql="SELECT * FROM Food_Info WHERE Food='" + parent.getItemAtPosition(position) + "'";
                   Cursor cr = db.rawQuery(sql, null);
                    if (cr.getCount() > 0) {
                        cr.moveToFirst();
                        go(cr.getString(0), cr.getInt(2), cr.getInt(3), cr.getInt(4), cr.getInt(5), cr.getInt(6));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private void go(String food,int grams,int calories,int protein, int fat,int carbs  ) {
        Intent i = new Intent(this, foodInformation.class);
        i.putExtra("Food",food );
        i.putExtra("Grams",grams );
        i.putExtra("Calories",calories );
        i.putExtra("Protein",protein );
        i.putExtra("Fat",fat );
        i.putExtra("Carbs",carbs );
        startActivity(i);
    }
    private void goToList(){

    }


}