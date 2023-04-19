package com.example.myapplication;

import static android.R.layout.simple_dropdown_item_1line;
import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Recipies extends AppCompatActivity {


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ArrayList<InfoFromDatabase> infoList;
    private CustomAdapter customAdapter;
    private Context iContext;
    public static final String SHARED_PREFS= "sharedPrefs";
    public static final String Text = "Text";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipies);

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
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),Kcal.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.recipies:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        overridePendingTransition(0,0);
                }
                return false;
            }
        });
        ImageButton  imageBreakfast= findViewById(R.id.imageButton);
        ImageButton imageLunch = findViewById(R.id.imageButton4);
        ImageButton imageSnack = findViewById(R.id.imageButton2);
        ImageButton imageDinner = findViewById(R.id.imageButton3);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        infoList = new ArrayList<>();
        /// Button for showing breakfast recipies

        imageBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = firebaseDatabase.getReference("Breakfast");
                SharedPreferences sharedPreferences = getSharedPreferences(Text, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Meal", "Breakfast");
                editor.apply();


                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        clearAll();


                        for(DataSnapshot ds : snapshot.getChildren()) {
                            InfoFromDatabase info = new InfoFromDatabase();
                            info.setUrl(ds.child("Image").getValue(String.class));
                            info.setName(ds.child("Name").getValue(String.class));
                            infoList.add(info);


                        }
                        customAdapter = new CustomAdapter(getApplicationContext(), infoList);
                        recyclerView.setAdapter(customAdapter);
                        customAdapter.notifyDataSetChanged();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });





        /// Button for showing Lunch recipies

        imageLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = firebaseDatabase.getReference("Lunch");
                SharedPreferences sharedPreferences = getSharedPreferences(Text, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Meal", "Lunch");
                editor.apply();
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        clearAll();
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            InfoFromDatabase info = new InfoFromDatabase();
                            info.setUrl(ds.child("Image").getValue(String.class));
                            info.setName(ds.child("Name").getValue(String.class));
                            infoList.add(info);

                        }
                        customAdapter = new CustomAdapter(getApplicationContext(), infoList);
                        recyclerView.setAdapter(customAdapter);
                        customAdapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });


        ///  Button for showing Snack recipies

        imageSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = firebaseDatabase.getReference("Snack");
                SharedPreferences sharedPreferences = getSharedPreferences(Text, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Meal", "Snack");
                editor.apply();
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        clearAll();
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            InfoFromDatabase info = new InfoFromDatabase();
                            info.setUrl(ds.child("Image").getValue(String.class));
                            info.setName(ds.child("Name").getValue(String.class));
                            infoList.add(info);

                        }
                        customAdapter = new CustomAdapter(getApplicationContext(), infoList);
                        recyclerView.setAdapter(customAdapter);
                        customAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        /// Button for Showing Dinner Recipies
        imageDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = firebaseDatabase.getReference("Dinner");
                SharedPreferences sharedPreferences = getSharedPreferences(Text, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Meal", "Dinner");
                editor.apply();
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        clearAll();
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            InfoFromDatabase info = new InfoFromDatabase();
                            info.setUrl(ds.child("Image").getValue(String.class));
                            info.setName(ds.child("Name").getValue(String.class));
                            infoList.add(info);
                        }
                        customAdapter = new CustomAdapter(getApplicationContext(), infoList);
                        recyclerView.setAdapter(customAdapter);
                        customAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });





    }
    private void clearAll(){

        if(infoList!=null){
            infoList.clear();
            if (customAdapter != null){
                customAdapter.notifyDataSetChanged();
            }
        }
        infoList = new ArrayList<>();


    }
}