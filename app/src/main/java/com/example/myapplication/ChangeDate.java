package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChangeDate extends AppCompatActivity {

    DatePicker picker;
    Button displayDate;
    TextView textview1;
    public static final String SHARED_PREFS= "sharedPrefs";
    public static final String Text = "Text";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_date);
        textview1=(TextView)findViewById(R.id.textView1);
        picker=(DatePicker)findViewById(R.id.datePicker);
        displayDate=(Button)findViewById(R.id.button1);

        ///DATE/////

        //////////////////////


        textview1.setText("Current Date: "+getCurrentDate());

        displayDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences1 = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                editor1.putString("Day", String.valueOf(picker.getDayOfMonth()));
                editor1.putString("Month", String.valueOf(picker.getMonth() + 1)); //month is 0 based
                editor1.putString("Year", String.valueOf(picker.getYear()));
                editor1.commit();
                textview1.setText("Change Date: " + getCurrentDate());
                String day = sharedPreferences1.getString("Day", "");
                String month = sharedPreferences1.getString("Month", "");
                String year = sharedPreferences1.getString("Year", "");


                //New Data on Firebase when we change the date//
                SharedPreferences sharedPreferences = getSharedPreferences(Text, MODE_PRIVATE);
                String ID = sharedPreferences.getString("ID", "");
                Map<String, Object> days= new HashMap<>();
                days.put("Day", day);
                days.put("Month", month);
                days.put("Year", year);


                Map<String, Object> mapDays = new HashMap<>();
                mapDays.putAll(days);

                Map<String,Object>  ArrayDays = new HashMap<>();
                ArrayDays.put("DayList", Arrays.asList(mapDays));
                //Nutrition//
                Map<String, Object> nutr = new HashMap<>();
                nutr.put("Food", " ");
                nutr.put("Carbs", "0");
                nutr.put("Protein", "0");
                nutr.put("Kcal", "0");
                nutr.put("Fats", "0");
                nutr.put("Quantity", String.valueOf(0));

                Map<String, Object> mapForFirestore = new HashMap<>();
                mapForFirestore.putAll(nutr);


                Map<String, Object> arrayForFirestore = new HashMap<>();
                arrayForFirestore.put("FoodList", Arrays.asList(mapForFirestore));

                //Overall///
                Map<String, Object> overAll = new HashMap<>();
                overAll.put("Protein", "0");
                overAll.put("Carbs", "0");
                overAll.put("Fats", "0");
                overAll.put("Kcal", "0");


                db.collection("Users").document(ID).collection(day + " " + month + " " + year).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if(task.getResult().size() > 0) {
                                        for (DocumentSnapshot document : task.getResult()) {
                                          goBack();

                                        }


                                    } else {
                                        db.collection("Users").document(ID).collection(day + " " + month + " " + year).document("Breakfast").collection("List").document("Food").set(arrayForFirestore);
                                        db.collection("Users").document(ID).collection(day + " " + month + " " + year).document("Lunch").collection("List").document("Food").set(arrayForFirestore);
                                        db.collection("Users").document(ID).collection(day + " " + month + " " + year).document("Dinner").collection("List").document("Food").set(arrayForFirestore);
                                        db.collection("Users").document(ID).collection(day + " " + month + " " + year).document("Snack").collection("List").document("Food").set(arrayForFirestore);
                                        db.collection("ListOfDays").document(ID).update("DayList", FieldValue.arrayUnion(days));
                                        db.collection("Users").document(ID).collection(day + " " + month + " " + year).document("Breakfast").set(nutr);
                                        db.collection("Users").document(ID).collection(day + " " + month + " " + year).document("Lunch").set(nutr);
                                        db.collection("Users").document(ID).collection(day + " " + month + " " + year).document("Dinner").set(nutr);
                                        db.collection("Users").document(ID).collection(day + " " + month + " " + year).document("Snack").set(nutr);
                                        db.collection("Users").document(ID).collection(day + " " + month + " " + year).document("OverAll").set(overAll);
                                        goBack();
                                    }


                                }

                            }
                        });


            }

        });
        }



    public String getCurrentDate(){
        StringBuilder builder=new StringBuilder();;
        builder.append((picker.getMonth() + 1)+"/");//month is 0 based
        builder.append(picker.getDayOfMonth()+"/");
        builder.append(picker.getYear());
        return builder.toString();
    }
    public void goBack(){
        Intent i = new Intent(this, Kcal.class);
        startActivity(i);
    }
}