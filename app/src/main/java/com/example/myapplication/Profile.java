package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String SHARED_PREFS= "sharedPrefs";
    public static final String Text = "Text";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.recipies:
                        startActivity(new Intent(getApplicationContext(),Recipies.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),Kcal.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });




        //////////Firestore set data/////////
        TextView txt1 = findViewById(R.id.textView);
        TextView txt2 = findViewById(R.id.textView2);
        TextView txt3 = findViewById(R.id.textView9);
        TextView txt4 = findViewById(R.id.textView12);
        TextView txt5 = findViewById(R.id.textView30);
        TextView txt6 = findViewById(R.id.textView31);
        TextView txt7 = findViewById(R.id.textView32);
        TextView txt8 = findViewById(R.id.textView33);
        TextView txt13 = findViewById(R.id.textView35);
        TextView txt14 = findViewById(R.id.textView36);
        TextView txt15 = findViewById(R.id.textView37);
        TextView txt16 = findViewById(R.id.textView38);
        TextView txt17 = findViewById(R.id.textView39);
        TextView txt18 = findViewById(R.id.textView40);
        ImageView logout = findViewById(R.id.imageView);

        SharedPreferences sharedPreferences = getSharedPreferences(Text, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();




        db.collection("UsersInfo").document(sharedPreferences.getString("ID", "")).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                txt1.setText(documentSnapshot.getString("Email"));
                txt2.setText(documentSnapshot.getString("Activity"));
                txt3.setText(documentSnapshot.getString("Age"));
                txt4.setText(documentSnapshot.getString("Weight"));
                txt5.setText(documentSnapshot.getString("Activity"));
                txt6.setText(documentSnapshot.getString("Age"));
                txt7.setText(documentSnapshot.getString("Gender"));
                txt8.setText(documentSnapshot.getString("Goal"));
                txt14.setText(documentSnapshot.getString("Height"));
                txt17.setText(documentSnapshot.getString("Kcal"));
                txt18.setText(documentSnapshot.getString("Weight"));

            }
        });


    }
    public void logOut (View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        SharedPreferences sharedPreferences = getSharedPreferences(Text, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences1 = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences sharedPreferences2 = getSharedPreferences("Meal", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        SharedPreferences.Editor editor2 = sharedPreferences2.edit();


        editor.clear();
        editor1.clear();
        editor2.clear();

    }

    public void goToStaticis(View view){
        Intent i = new Intent(this, Statistics.class);
        startActivity(i);

    }

}