package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Information extends AppCompatActivity {


    EditText weight;
    EditText height;
    EditText age;
    String goal, activity, gender;
    TextView buttonTxt;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String SHARED_PREFS= "sharedPrefs";
    public static final String Text = "Text";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String[] users = { "Απώλεια βάρους", "Αύξηση μυικής μάζας", "Διατήρηση βάρους"};
        String[] users1 = { "Καθόλου", "Μέτρια – μέχρι 3 φορές ", "Έντονη – 3-5 φορές","Καθημερινή"};
        String[] users2 = {"Άνδρας", "Γυναίκα"};
        Intent intent = getIntent();
        String ID = intent.getExtras().getString("ID");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        Spinner spin = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        Spinner spin1 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(adapter1);
        Spinner spin2 = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users2);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(adapter2);





        ///DATE/////
        SharedPreferences sharedPreferences1 = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String day = sharedPreferences1.getString("Day", "");
        String month = sharedPreferences1.getString("Month", "");
        String year = sharedPreferences1.getString("Year", "");

        //////////////////////







        buttonTxt = findViewById(R.id.buttontxt);
        buttonTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(Text, MODE_PRIVATE);

                String email = sharedPreferences.getString("Email", "");

                weight = findViewById(R.id.editTextNumber);
                height = findViewById(R.id.editTextNumber3);
                age = findViewById(R.id.editTextNumber2);
                goal = spin.getSelectedItem().toString();
                activity = spin1.getSelectedItem().toString();
                gender = spin2.getSelectedItem().toString();


                try
                {
                    int valueOfheight = Integer.valueOf(String.valueOf(height.getText()));
                    int valueOfage = Integer.valueOf(String.valueOf(age.getText()));
                    int valueOfweight= Integer.valueOf(String.valueOf(age.getText()));
                    int kcal = 0;
                    int bmr;
                    String final_kcal="";


                    if (gender == "Γυναίκα") {


                        bmr = (int) (655.1 + (9.563 * valueOfweight) + (1.85 * valueOfheight) - (4.676 * valueOfage));

                        if (activity == "Καθόλου") {
                            kcal = (int) (bmr * 1.2);
                            final_kcal = String.valueOf(kcal);
                        }
                        if (activity == "Μέτρια – μέχρι 3 φορές") {
                            kcal = (int) (bmr * 1.375);
                            final_kcal = String.valueOf(kcal);
                        }
                        if (activity == "Έντονη – 3-5 φορές") {
                            kcal = (int) (bmr * 1.55);
                            final_kcal = String.valueOf(kcal);
                        } else {
                            kcal = (int) (bmr * 1.725);
                            final_kcal = String.valueOf(kcal);
                        }
                        final_kcal = String.valueOf(kcal);
                        Intent i = new Intent(getApplicationContext(), Kcal.class);
                        i.putExtra("ID", ID);
                        startActivity(i);


                    }
                    if (gender == "Άνδρας") {


                        bmr = (int) (66.47 + 13.75 * valueOfweight + 5.003 * valueOfheight - 6.755 * valueOfage);
                        if (activity == "Καθόλου") {
                            kcal = (int) (bmr * 1.2);
                            final_kcal = String.valueOf(kcal);
                        }
                        if (activity == "Μέτρια – μέχρι 3 φορές") {
                            kcal = (int) (bmr * 1.375);
                            final_kcal = String.valueOf(kcal);
                        }
                        if (activity == "Έντονη – 3-5 φορές") {
                            kcal = (int) (bmr * 1.55);
                            final_kcal = String.valueOf(kcal);
                        } else {
                            kcal = (int) (bmr * 1.725);
                            final_kcal = String.valueOf(kcal);
                        }

                        final_kcal = String.valueOf(kcal);
                        //Toast.makeText(getApplicationContext(), ID.toString(), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), Kcal.class);
                        i.putExtra("ID", ID);
                        startActivity(i);

                    }
                    Map<String, Object> days= new HashMap<>();
                    days.put("Day", day);
                    days.put("Month", month);
                    days.put("Year", year);


                    Map<String, Object> mapDays = new HashMap<>();
                    mapDays.putAll(days);

                    Map<String,Object>  ArrayDays = new HashMap<>();
                    ArrayDays.put("DayList", Arrays.asList(mapDays));
                    ///////calculate protein//////////////



                    int protein = valueOfweight;
                    int fats = ((Integer.parseInt(final_kcal) * 30) / 100) / 9;
                    int carbs = ((Integer.parseInt(final_kcal) * 45) / 100) / 4;







                    Map<String, Object> info = new HashMap<>();
                    info.put("ID", ID);
                    info.put("Email", email);
                    info.put("Weight", weight.getText().toString());
                    info.put("Height", height.getText().toString());
                    info.put("Age", age.getText().toString());
                    info.put("Goal", goal);
                    info.put("Activity", activity);
                    info.put("Gender", gender);
                    info.put("Kcal", final_kcal);
                    info.put("Protein", String.valueOf(protein));
                    info.put("Fats", String.valueOf(fats));
                    info.put("Carbs", String.valueOf(carbs));





                    //Nutrition//
                    Map <String, Object> nutr = new HashMap<>();
                    nutr.put("Food", " ");
                    nutr.put("Carbs", "0");
                    nutr.put("Protein", "0");
                    nutr.put("Kcal", "0");
                    nutr.put("Fats", "0");


                    //OverAll//

                    Map <String, Object> overAll = new HashMap<>();
                    overAll.put("Protein", "0");
                    overAll.put("Carbs", "0");
                    overAll.put("Fats", "0");
                    overAll.put("Kcal", "0");


                    // For List of Food//
                    Map <String , Object> listFood = new HashMap<>();
                    listFood.put("Food", " ");
                    listFood.put("Kcal", "0");
                    listFood.put("Protein", "0");
                    listFood.put("Fats", "0");
                    listFood.put("Carbs","0");
                    listFood.put("Quantity", String.valueOf(0));


                    Map <String , Object> mapForFirestore = new HashMap<>();
                    mapForFirestore.putAll(listFood);


                    Map <String, Object> ArrayForFirestore = new HashMap<>();
                    ArrayForFirestore.put("FoodList", Arrays.asList(mapForFirestore));








                    db.collection("UsersInfo").document(ID).set(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                    db.collection("Users").document(ID).collection(day + " " + month + " "+ year).document("Breakfast").collection("List").document("Food").set(ArrayForFirestore);
                    db.collection("Users").document(ID).collection(day + " " + month + " "+ year).document("Lunch").collection("List").document("Food").set(ArrayForFirestore);
                    db.collection("Users").document(ID).collection(day + " " + month + " "+ year).document("Dinner").collection("List").document("Food").set(ArrayForFirestore);
                    db.collection("Users").document(ID).collection(day + " " + month + " "+ year).document("Snack").collection("List").document("Food").set(ArrayForFirestore);

                    db.collection("ListOfDays").document(ID).set(ArrayDays);

                    db.collection("Users").document(ID).collection(day + " " + month + " "+ year).document("Breakfast").set(nutr);
                    db.collection("Users").document(ID).collection(day + " " + month + " "+ year).document("Lunch").set(nutr);
                    db.collection("Users").document(ID).collection(day + " " + month + " "+ year).document("Dinner").set(nutr);
                    db.collection("Users").document(ID).collection(day + " " + month + " "+ year).document("Snack").set(nutr);
                    db.collection("Users").document(ID).collection(day + " " + month + " "+ year).document("OverAll").set(overAll);




                }
                catch (NumberFormatException e)
                {
                    Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_SHORT).show();
                }

            }
        });
        }
}