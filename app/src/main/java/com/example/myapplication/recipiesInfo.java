package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class recipiesInfo extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipies_info);
        Intent intent = getIntent();
        String Name = intent.getExtras().getString("Name");
        String Meal = intent.getExtras().getString("Meal");
        TextView recipieName =findViewById(R.id.textView43);
        ImageView imageview = findViewById(R.id.imageView9);
        TextView directions = findViewById(R.id.textView46);
         TextView ingredients = findViewById(R.id.textView48);


        recipieName.setText(Name);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Meal).child(Name);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String img = snapshot.child("Image").getValue(String.class);
                Glide.with(getApplicationContext()).load(img).into(imageview);
                directions.setText(snapshot.child("Directions").getValue(String.class));
                ingredients.setText(snapshot.child("Ingredients").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
}