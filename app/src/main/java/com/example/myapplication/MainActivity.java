package com.example.myapplication;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText username, password;
    TextView textView1, textView2, textView3;
    private FirebaseAuth mAuth;
    DatePicker picker;
    public static final String SHARED_PREFS= "sharedPrefs";
    public static final String Text = "Text";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        textView1 = findViewById(R.id.buttontxt);
        textView2 = findViewById(R.id.textview1);
        textView3 = findViewById(R.id.register);
        mAuth = FirebaseAuth.getInstance();
        picker=(DatePicker)findViewById(R.id.datePicker);


    }
    public void GoRegister(View view){
        SharedPreferences sharedPreferences1 = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putString("Day", String.valueOf(picker.getDayOfMonth() ));
        editor1.putString("Month", String.valueOf(picker.getMonth() + 1)); //month is 0 based
        editor1.putString("Year", String.valueOf(picker.getYear()));
        editor1.commit();
        Intent i=new Intent(getApplicationContext(),RegistrationActivity.class);
        startActivity(i);

    }
    public void GoKcal(View view){
        try {
            mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                            //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences = getSharedPreferences(Text, MODE_PRIVATE);
                            SharedPreferences sharedPreferences1 = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                            String ID = mAuth.getUid();
                            String email = username.getText().toString();
                            editor.putString("ID",ID);
                            editor.putString("Email", email);
                            editor.apply();
                            editor1.putString("Day", String.valueOf(picker.getDayOfMonth() ));
                            editor1.putString("Month", String.valueOf(picker.getMonth() + 1)); //month is 0 based
                            editor1.putString("Year", String.valueOf(picker.getYear()));
                            //Toast.makeText(getApplicationContext(), String.valueOf(picker.getDayOfMonth()), Toast.LENGTH_SHORT).show();
                            editor1.commit();
                        Map<String, Object> days = new HashMap<>();
                        days.put("Day", String.valueOf(picker.getDayOfMonth()));
                        days.put("Month", String.valueOf(picker.getMonth() +1 ));
                        days.put("Year", String.valueOf(picker.getYear()));


                        Map<String, Object> mapDays = new HashMap<>();
                        mapDays.putAll(days);

                        Map<String,Object>  ArrayDays = new HashMap<>();
                        ArrayDays.put("DayList", Arrays.asList(mapDays));

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
                            db.collection("Users").document(ID).collection(String.valueOf(picker.getDayOfMonth()) + " " + String.valueOf(picker.getMonth() + 1) + " " + String.valueOf(picker.getYear())).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if(task.getResult().size() > 0) {
                                                    for (DocumentSnapshot document : task.getResult()) {
                                                        changePage();

                                                    }
                                                }
                                                    else {
                                                    db.collection("Users").document(ID).collection(String.valueOf(picker.getDayOfMonth()) + " " + String.valueOf(picker.getMonth() + 1) + " " + String.valueOf(picker.getYear())).document("Breakfast").collection("List").document("Food").set(arrayForFirestore);
                                                    db.collection("Users").document(ID).collection(String.valueOf(picker.getDayOfMonth()) + " " + String.valueOf(picker.getMonth() + 1) + " " + String.valueOf(picker.getYear())).document("Lunch").collection("List").document("Food").set(arrayForFirestore);
                                                    db.collection("Users").document(ID).collection(String.valueOf(picker.getDayOfMonth()) + " " + String.valueOf(picker.getMonth() + 1) + " " + String.valueOf(picker.getYear())).document("Dinner").collection("List").document("Food").set(arrayForFirestore);
                                                    db.collection("Users").document(ID).collection(String.valueOf(picker.getDayOfMonth()) + " " + String.valueOf(picker.getMonth() + 1) + " " + String.valueOf(picker.getYear())).document("Snack").collection("List").document("Food").set(arrayForFirestore);
                                                    db.collection("ListOfDays").document(ID).update("DayList", FieldValue.arrayUnion(days));
                                                    db.collection("Users").document(ID).collection(String.valueOf(picker.getDayOfMonth()) + " " + String.valueOf(picker.getMonth() + 1) + " " + String.valueOf(picker.getYear())).document("Breakfast").set(nutr);
                                                    db.collection("Users").document(ID).collection(String.valueOf(picker.getDayOfMonth()) + " " + String.valueOf(picker.getMonth() + 1) + " " + String.valueOf(picker.getYear())).document("Lunch").set(nutr);
                                                    db.collection("Users").document(ID).collection(String.valueOf(picker.getDayOfMonth()) + " " + String.valueOf(picker.getMonth() + 1) + " " + String.valueOf(picker.getYear())).document("Dinner").set(nutr);
                                                    db.collection("Users").document(ID).collection(String.valueOf(picker.getDayOfMonth()) + " " + String.valueOf(picker.getMonth() + 1) + " " + String.valueOf(picker.getYear())).document("Snack").set(nutr);
                                                    db.collection("Users").document(ID).collection(String.valueOf(picker.getDayOfMonth()) + " " + String.valueOf(picker.getMonth() + 1) + " " + String.valueOf(picker.getYear())).document("OverAll").set(overAll);
                                                    changePage();

                                                }


                                            }

                                        }
                                    });

                    } else {
                        showMessage("Error", task.getException().getLocalizedMessage());

                    }


                }

            });
        }  catch (NumberFormatException e){
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

        }


    }
    void showMessage (String title, String message){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }


    public void changePage(){
        Intent i = new Intent(this, Kcal.class);
        startActivity(i);
    }
    public String getCurrentDate(){
        StringBuilder builder=new StringBuilder();;
        builder.append((picker.getMonth() + 1)+"/");//month is 0 based
        builder.append(picker.getDayOfMonth()+"/");
        builder.append(picker.getYear());
        return builder.toString();
    }


}