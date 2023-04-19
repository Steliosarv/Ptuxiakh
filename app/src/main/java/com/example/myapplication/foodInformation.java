package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class foodInformation extends AppCompatActivity {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String Text = "Text";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private List<String> myList;
    private List<String> newList;
    Map <String, Object> ArrayForFirestore = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_information);
        Intent intent = getIntent();
        String food = intent.getExtras().getString("Food");
        int grams = intent.getExtras().getInt("Grams");
        int calories = intent.getExtras().getInt("Calories");
        int protein = intent.getExtras().getInt("Protein");
        int fat = intent.getExtras().getInt("Fat");
        int carbs = intent.getExtras().getInt("Carbs");
        firebaseDatabase = FirebaseDatabase.getInstance();



        TextView foodName = findViewById(R.id.foodName);
        TextView buttonAdd = findViewById(R.id.buttontxt);
        TextView foodCalories = findViewById(R.id.foodCalories);
        TextView foodCarbs = findViewById(R.id.foodCarbs);
        TextView foodProtein = findViewById(R.id.foodProtein);
        TextView foodFats = findViewById(R.id.foodFats);
        EditText editTextGrams = findViewById(R.id.editTextGrams);

        ///////Date/// /////
        SharedPreferences sharedPreferences1 = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String day = sharedPreferences1.getString("Day", "");
        String month = sharedPreferences1.getString("Month", "");
        String year = sharedPreferences1.getString("Year", "");
        ////////////


        foodName.setText(food);
        foodCalories.setText(Integer.toString(calories) + " Kcal");
        foodCarbs.setText(Integer.toString(carbs) + " gr.");
        foodProtein.setText(Integer.toString(protein) + "gr");
        foodFats.setText(Integer.toString(fat) + " gr.");
        editTextGrams.setText(Integer.toString(grams));




        editTextGrams.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ////////For kcal////////
                if(editTextGrams.getText().toString().matches(" ")|| editTextGrams.getText().toString().matches("")) {
                    foodCalories.setText(Integer.toString(calories) + " Kcal");
                    foodCarbs.setText(Integer.toString(carbs) + " gr.");
                    foodProtein.setText(Integer.toString(protein) + "gr");
                    foodFats.setText(Integer.toString(fat) + " gr.");

                }
                else{
                    Toast.makeText(getApplicationContext(), s.toString(), Toast.LENGTH_SHORT).show();
                    foodCalories.setText(String.valueOf(calories * Integer.parseInt(s.toString()) / grams));
                    foodCarbs.setText(String.valueOf(carbs*Integer.parseInt(s.toString())/ grams));
                    foodProtein.setText(String.valueOf(protein*Integer.parseInt(s.toString())/grams));
                    foodFats.setText(String.valueOf(fat*Integer.parseInt(s.toString())/grams));



                    buttonAdd.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("RestrictedApi")
                        @Override
                        public void onClick(View v) {

                            SharedPreferences sharedPreferences = getSharedPreferences(Text, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //////Food Info /////
                            Map<String, Object> foodInfo = new HashMap<>();
                            Map<String, Object> overAll = new HashMap<>();
                            Map< String, Object> info = new HashMap<>();
                            foodInfo.put("Food", food);
                            foodInfo.put("Kcal", String.valueOf(foodCalories.getText()));
                            foodInfo.put("Protein", String.valueOf(foodProtein.getText()));
                            foodInfo.put("Fats",String.valueOf(foodFats.getText()));
                            foodInfo.put("Carbs", String.valueOf(foodCarbs.getText()));
                            foodInfo.put("Quantity", String.valueOf(0));
                            editor.commit();
                            info.put("Food", foodInfo.get("Food"));
                            info.put("Kcal", foodInfo.get("Kcal"));
                            //insertToSP(foodInfo);
                            Map<String, Object> mapForFirestore = new HashMap<>();
                            Map<String , Object> newArray = new HashMap<>();

                            mapForFirestore.put("FoodList", Arrays.asList(food, foodCalories.getText().toString()));

                            ArrayForFirestore.put(food, Arrays.asList(foodInfo));
                            newArray.put(food, foodInfo);

                            db.collection("Users").document(sharedPreferences.getString("ID", "")).collection(day + " " + month + " " + year).
                                    document(sharedPreferences.getString("TimeOfDay", "")).collection("List")
                                    .document("Food").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            List<Map<String, Object>> groups = (List<Map<String, Object>>) documentSnapshot.get("FoodList");
                                            ArrayList<String> names = new ArrayList<>();
                                            ArrayList<String> quantity = new ArrayList<>();
                                            for (Map<String, Object> group : groups) {
                                                String name = (String) group.get("Food");
                                                names.add(name);
                                                String quantity1 = (String) group.get("Quantity");

                                                if(names.contains(food)){

                                                    foodInfo.remove("Quantity");
                                                    foodInfo.put("Quantity", String.valueOf(Integer.parseInt(quantity1) + 1));

                                                }else{

                                                }

                                            }
                                            db.collection("Users").document(sharedPreferences.getString("ID", ""))
                                                    .collection(day + " " + month + " "+ year).document(sharedPreferences.getString("TimeOfDay", ""))
                                                    .collection("List")
                                                    .document("Food").update("FoodList", FieldValue.arrayUnion(foodInfo)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {

                                                        }
                                                    });
                                        }
                                    });

                            db.collection("Users").document(sharedPreferences.getString("ID", "")).collection(day + " " + month + " " + year).document(sharedPreferences.getString("TimeOfDay", "")).set(foodInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });

                            db.collection("Users").document(sharedPreferences.getString("ID", "")).collection(day + " " + month + " " + year).document("OverAll").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {


                                    overAll.put("Protein", documentSnapshot.getString("Protein"));
                                    overAll.put("Fats", documentSnapshot.getString("Fats"));
                                    overAll.put("Kcal", documentSnapshot.getString("Kcal"));
                                    overAll.put("Carbs", documentSnapshot.getString("Carbs"));


                                    int xP = Integer.parseInt((String) overAll.get("Protein"));
                                    int xF = Integer.parseInt((String) overAll.get("Fats"));
                                    int xK = Integer.parseInt((String) overAll.get("Kcal"));
                                    int xC = Integer.parseInt((String) overAll.get("Carbs"));


                                    String p = String.valueOf(xP + Integer.parseInt(foodProtein.getText().toString()));
                                    String k = String.valueOf(xK + Integer.parseInt(foodCalories.getText().toString()));
                                    String f = String.valueOf(xF + Integer.parseInt(foodFats.getText().toString()));
                                    String c = String.valueOf(xC + Integer.parseInt(foodCarbs.getText().toString()));


                                    overAll.put("Protein", p);
                                    overAll.put("Kcal", k);
                                    overAll.put("Fats", f);
                                    overAll.put("Carbs", c);





                                    db.collection("Users").document(sharedPreferences.getString("ID", "")).collection(day + " " + month + " " + year).document("OverAll").set(overAll).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {


                                        }
                                    });
                                }
                            });
                            addNutrition();
                        }
                    });
                }



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences(Text, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                //////Food Info /////
                Map<String, Object> foodInfo = new HashMap<>();
                Map<String, Object> overAll = new HashMap<>();
                Map< String, Object> info = new HashMap<>();
                foodInfo.put("Food", food);
                foodInfo.put("Kcal", String.valueOf(calories));
                foodInfo.put("Protein", String.valueOf(protein));
                foodInfo.put("Fats",String.valueOf(fat));
                foodInfo.put("Carbs", String.valueOf(carbs));
                foodInfo.put("Quantity", String.valueOf(0));

                editor.commit();
                info.put("Food", foodInfo.get("Food"));
                info.put("Kcal", foodInfo.get("Kcal"));
                //insertToSP(foodInfo);
                Map<String, Object> mapForFirestore = new HashMap<>();
                Map<String , Object> newArray = new HashMap<>();

                mapForFirestore.put("FoodList", Arrays.asList(food, foodCalories.getText().toString()));




                ArrayForFirestore.put(food, Arrays.asList(foodInfo));
                newArray.put(food, foodInfo);



                db.collection("Users").document(sharedPreferences.getString("ID", "")).collection(day + " " + month + " " + year).
                        document(sharedPreferences.getString("TimeOfDay", "")).collection("List")
                        .document("Food").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                          @Override
                                                                          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                              DocumentSnapshot documentSnapshot = task.getResult();
                                                                              List<Map<String, Object>> groups = (List<Map<String, Object>>) documentSnapshot.get("FoodList");
                                                                              ArrayList<String> names = new ArrayList<>();
                                                                              ArrayList<String> quantity = new ArrayList<>();
                                                                              for (Map<String, Object> group : groups) {
                                                                                  String name = (String) group.get("Food");
                                                                                  names.add(name);
                                                                                  String quantity1 = (String) group.get("Quantity");

                                                                                  if(names.contains(food)){

                                                                                      foodInfo.remove("Quantity");
                                                                                      foodInfo.put("Quantity", String.valueOf(Integer.parseInt(quantity1) + 1));


                                                                                  }else{


                                                                                  }


                                                                              }
                                                                              db.collection("Users").document(sharedPreferences.getString("ID", ""))
                                                                                      .collection(day + " " + month + " "+ year).document(sharedPreferences.getString("TimeOfDay", ""))
                                                                                      .collection("List")
                                                                                      .document("Food").update("FoodList", FieldValue.arrayUnion(foodInfo)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                          @Override
                                                                                          public void onSuccess(Void unused) {

                                                                                          }
                                                                                      });









                                                                          }
                                                                      });








                    db.collection("Users").document(sharedPreferences.getString("ID", "")).collection(day + " " + month + " " + year).document(sharedPreferences.getString("TimeOfDay", "")).set(foodInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });

                    db.collection("Users").document(sharedPreferences.getString("ID", "")).collection(day + " " + month + " " + year).document("OverAll").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {


                            overAll.put("Protein", documentSnapshot.getString("Protein"));
                            overAll.put("Fats", documentSnapshot.getString("Fats"));
                            overAll.put("Kcal", documentSnapshot.getString("Kcal"));
                            overAll.put("Carbs", documentSnapshot.getString("Carbs"));


                            int xP = Integer.parseInt((String) overAll.get("Protein"));
                            int xF = Integer.parseInt((String) overAll.get("Fats"));
                            int xK = Integer.parseInt((String) overAll.get("Kcal"));
                            int xC = Integer.parseInt((String) overAll.get("Carbs"));


                            String p = String.valueOf(xP + protein);
                            String k = String.valueOf(xK + calories);
                            String f = String.valueOf(xF + fat);
                            String c = String.valueOf(xC + carbs);


                            overAll.put("Protein", p);
                            overAll.put("Kcal", k);
                            overAll.put("Fats", f);
                            overAll.put("Carbs", c);





                            db.collection("Users").document(sharedPreferences.getString("ID", "")).collection(day + " " + month + " " + year).document("OverAll").set(overAll).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {


                                }
                            });
                        }
                    });
                addNutrition();
            }
        });
    }


    public void addNutrition() {
        Intent i = new Intent(foodInformation.this, Kcal.class);

        startActivity(i);
    }

}


