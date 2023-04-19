package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kcal extends AppCompatActivity {
    String kg;
    int kcal;
    DatePicker picker;
    int xProtein, xFats, xCarbs;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String Text = "Text";
    public static final String Text1 = "Values";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressBar kcalProgress, proteinProgress, fatsProgress, carbsProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kcal);

        TextView textView2 = findViewById(R.id.textView2);
        TextView textView5 = findViewById(R.id.textView5);
        TextView textView7 = findViewById(R.id.textView7);
        TextView textView13 = findViewById(R.id.textView13);
        TextView calories = findViewById(R.id.textView);
        TextView prot = findViewById(R.id.textView4);
        TextView carbs = findViewById(R.id.textView8);
        TextView fats = findViewById(R.id.textView6);
        TextView date = findViewById(R.id.monthYearTV);

        kcalProgress = findViewById(R.id.progressBar2);
        proteinProgress = findViewById(R.id.progressBar);
        fatsProgress = findViewById(R.id.progressBar3);
        carbsProgress = findViewById(R.id.progressBar4);
        picker=(DatePicker)findViewById(R.id.datePicker);

        ///DATE/////
        SharedPreferences sharedPreferences1 = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String day = sharedPreferences1.getString("Day", "");
        String month = sharedPreferences1.getString("Month", "");
        String year = sharedPreferences1.getString("Year", "");

        date.setText(day + " " + month + " " + year);
        //////////////////////

        SharedPreferences sharedPreferences = getSharedPreferences(Text, Context.MODE_PRIVATE);
        String ID = sharedPreferences.getString("ID", "");
        //////////////////////////////////////////////////////////



        db.collection("UsersInfo").document(ID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //Kcal

                textView2.setText(documentSnapshot.getString("Kcal") + "  kcal");
                kg = documentSnapshot.getString("Weight");
                //Calculate protein
                xProtein = Integer.parseInt(kg);
                int protein = xProtein * 1;
                textView5.setText("of " + protein);
                proteinProgress.setMax(protein);
                //Calculate fats
                String kcal = documentSnapshot.getString("Kcal");
                kcalProgress.setMax(Integer.parseInt(kcal));
                xFats = Integer.parseInt(kcal);
                int fats = ((xFats * 30) / 100) / 9;
                textView7.setText("of " + fats);
                fatsProgress.setMax(fats);
                //Calculate Carbs
                xCarbs = Integer.parseInt(kcal);
                int carbs = ((xCarbs * 45) / 100) / 4;
                textView13.setText("of " + carbs);
                carbsProgress.setMax(carbs);


            }

        });


        readFromFirestore(ID, day, month, year, prot, carbs, fats, calories);
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.home);
        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.recipies:
                        startActivity(new Intent(getApplicationContext(), Recipies.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });


    }









    public void addBreakfast(View view) {
        String x = "Breakfast";
        Intent i = new Intent(this, Add.class);
        i.putExtra("x", x);
        SharedPreferences sharedPreferences = getSharedPreferences(Text, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TimeOfDay", x);
        editor.commit();
        startActivity(i);
    }

    public void addLunch(View view) {
        String x = "Lunch";
        Intent i = new Intent(this, Add.class);
        i.putExtra("x", x);
        SharedPreferences sharedPreferences = getSharedPreferences(Text, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TimeOfDay", x);
        editor.commit();
        startActivity(i);
    }

    public void addSnack(View view) {
        String x = "Snack";
        Intent i = new Intent(this, Add.class);
        i.putExtra("x", x);
        SharedPreferences sharedPreferences = getSharedPreferences(Text, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TimeOfDay", x);
        editor.commit();
        startActivity(i);

    }

    public void addDinner(View view) {
        String x = "Dinner";
        Intent i = new Intent(this, Add.class);
        i.putExtra("x", x);
        SharedPreferences sharedPreferences = getSharedPreferences(Text, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TimeOfDay", x);
        editor.commit();
        startActivity(i);

    }

    public void readFromFirestore(String ID, String day, String monthString, String year, TextView prot, TextView carbs, TextView fats, TextView calories) {
        SharedPreferences sharedPreferences = getSharedPreferences(Text1, Context.MODE_PRIVATE);
        Map<String, Object> values = new HashMap<>();


        db.collection("Users").document(ID).collection(day + " " + monthString + " " + year).document("OverAll").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                prot.setText(value.getString("Protein"));
                carbs.setText(value.getString("Carbs"));
                fats.setText(value.getString("Fats"));
                calories.setText(value.getString("Kcal"));


                proteinProgress.setProgress(Integer.parseInt(prot.getText().toString()));
                fatsProgress.setProgress(Integer.parseInt(fats.getText().toString()));
                carbsProgress.setProgress(Integer.parseInt(carbs.getText().toString()));
                kcalProgress.setProgress(Integer.parseInt(value.getString("Kcal")));



            }
        });


    }


    public void breakfastList(View view) {
        String x = "Breakfast";
        Intent i = new Intent(this, ListofFood.class);

        SharedPreferences sharedPreferences = getSharedPreferences("Meal", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TimeOfDay", x);
        editor.commit();
        startActivity(i);


    }

    public void lunchList(View view) {
        String x = "Lunch";
        Intent i = new Intent(this, ListofFood.class);

        SharedPreferences sharedPreferences = getSharedPreferences("Meal", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TimeOfDay", x);
        editor.commit();
        startActivity(i);
    }

    public void snackList(View view) {
        String x = "Snack";
        Intent i = new Intent(this, ListofFood.class);

        SharedPreferences sharedPreferences = getSharedPreferences("Meal", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TimeOfDay", x);
        editor.commit();
        startActivity(i);
    }

    public void dinnerList(View view) {
        String x = "Dinner";
        Intent i = new Intent(this, ListofFood.class);
        SharedPreferences sharedPreferences = getSharedPreferences("Meal", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TimeOfDay", x);
        editor.commit();
        startActivity(i);
    }

    public void changeDate(View view) {

        Intent i = new Intent(this, ChangeDate.class);
        startActivity(i);

    }

    public void goToNextDay(TextView d) {
        int day1= picker.getDayOfMonth();
        int day = day1 + 1;
        int month = picker.getMonth() + 1;
        int year = picker.getYear();

    }

}