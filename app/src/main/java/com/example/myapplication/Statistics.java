package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String Text = "Text";
    public static final String TAG = "Text1";
    private static ArrayList<String> mArrayList = new ArrayList<>();
    private List<String> days;
    ArrayList<String> date = new ArrayList<>();
    Map<String, Integer> sum = new HashMap<>();
    int i =0;
    int averageForKcal = 0;
    long finalSumK =0;
    long finalSumP =0;
    long finalSumF =0;
    long finalSumC =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        String[] statistics = { "","Users stats for losing weight", "Users stats for muscle mass", "Users stats for maintenance"};
        TextView textView2 = findViewById(R.id.textView2);
        TextView textView5 = findViewById(R.id.textView5);
        TextView textView7 = findViewById(R.id.textView7);
        TextView textView13 = findViewById(R.id.textView13);
        TextView textView51 = findViewById(R.id.textView51);
        TextView textView52 = findViewById(R.id.textView52);
        TextView textView53 = findViewById(R.id.textView53);
        TextView calories = findViewById(R.id.textView);
        TextView prot = findViewById(R.id.textView4);
        TextView carbs = findViewById(R.id.textView8);
        TextView fats = findViewById(R.id.textView6);
        TextView textEmail = findViewById(R.id.textView47);
        ProgressBar kcalProgress = findViewById(R.id.progressBar2);
        ProgressBar proteinProgress = findViewById(R.id.progressBar);
        ProgressBar fatsProgress = findViewById(R.id.progressBar3);
        ProgressBar carbsProgress = findViewById(R.id.progressBar4);
        SharedPreferences sharedPreferences = getSharedPreferences("Meal", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences1 = getSharedPreferences(Text, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences2 = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statistics);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        textView51.setVisibility(View.INVISIBLE);
        textView52.setVisibility(View.INVISIBLE);
        textView53.setVisibility(View.INVISIBLE);


        int sumKcal = 0;
        int sumProtein = 0;
        int sumFats = 0;
        int sumCarbs = 0;


        sum.put("Kcal", sumKcal);
        sum.put("Protein", sumProtein);
        sum.put("Fats", sumFats);
        sum.put("Carbs", sumCarbs);


        db.collection("UsersInfo").document(sharedPreferences1.getString("ID", "")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                textView2.setText(documentSnapshot.getString("Kcal"));

            }
        });



        loadData(sumKcal, sumProtein, sumFats, sumCarbs, kcalProgress, proteinProgress, fatsProgress, carbsProgress, prot, fats, carbs, calories);

        db.collection("UsersInfo").document(sharedPreferences1.getString("ID", "")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                String email = documentSnapshot.getString("Email");
                textEmail.setText(email);

            }
        });




        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = spin.getSelectedItem().toString();
                ///////////////////////// ΑΠΩΛΕΙΑ ΒΑΡΟΥΣ////////////////////////////
                if(selectedItem=="Users stats for losing weight") {

                    db.collection("UsersInfo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<String> list = new ArrayList<>();
                                List<Integer> kcalList = new ArrayList<>();
                                List<Integer> proteinList= new ArrayList<>();
                                List<Integer> fatsList= new ArrayList<>();
                                List<Integer> carbsList= new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    list.add(document.getId());
                                }
                                for(String element : list){

                                    db.collection("UsersInfo").document(element).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            String goal = documentSnapshot.getString("Goal");
                                            String kcal = documentSnapshot.getString("Kcal");
                                            String protein= documentSnapshot.getString("Protein");
                                            String fat= documentSnapshot.getString("Fats");
                                            String carb= documentSnapshot.getString("Carbs");

                                            if (goal.matches("Απώλεια βάρους")){
                                                int intKcal = Integer.parseInt(kcal);
                                                int intProtein= Integer.parseInt(protein);
                                                int intFats = Integer.parseInt(fat);
                                                int intCarbs= Integer.parseInt(carb);
                                                kcalList.add(intKcal);
                                                proteinList.add(intProtein);
                                                fatsList.add(intFats);
                                                carbsList.add(intCarbs);

                                                i++;

                                                long intSumK = kcalList.stream()
                                                        .mapToLong(Integer::longValue)
                                                        .sum();

                                                long intSumP =  proteinList.stream()
                                                        .mapToLong(Integer::longValue)
                                                        .sum();
                                                long intSumF =  fatsList.stream()
                                                        .mapToLong(Integer::longValue)
                                                        .sum();
                                                long intSumC =  carbsList.stream()
                                                        .mapToLong(Integer::longValue)
                                                        .sum();


                                                finalSumK= intSumK/i;
                                                finalSumP= intSumP/i;
                                                finalSumC= intSumC/i;
                                                finalSumF= intSumF/i;
                                                calories.setText(String.valueOf(finalSumK));
                                                prot.setVisibility(View.INVISIBLE);
                                                carbs.setVisibility(View.INVISIBLE);
                                                fats.setVisibility(View.INVISIBLE);
                                                kcalProgress.setVisibility(View.INVISIBLE);
                                                proteinProgress.setVisibility(View.INVISIBLE);
                                                fatsProgress.setVisibility(View.INVISIBLE);
                                                carbsProgress.setVisibility(View.INVISIBLE);
                                                textView2.setVisibility(View.INVISIBLE);
                                                textView5.setVisibility(View.INVISIBLE);
                                                textView7.setVisibility(View.INVISIBLE);
                                                textView13.setVisibility(View.INVISIBLE);
                                                textView51.setText(String.valueOf(finalSumP));
                                                textView52.setText(String.valueOf(finalSumC));
                                                textView53.setText(String.valueOf(finalSumF));
                                                textView51.setVisibility(View.VISIBLE);
                                                textView52.setVisibility(View.VISIBLE);
                                                textView53.setVisibility(View.VISIBLE);



                                            }
                                            else{
                                                calories.setText("0");
                                                textView51.setText("0");
                                                textView52.setText("0");
                                                textView53.setText("0");
                                                kcalProgress.setVisibility(View.INVISIBLE);
                                                proteinProgress.setVisibility(View.INVISIBLE);
                                                fatsProgress.setVisibility(View.INVISIBLE);
                                                carbsProgress.setVisibility(View.INVISIBLE);
                                                prot.setVisibility(View.INVISIBLE);
                                                carbs.setVisibility(View.INVISIBLE);
                                                fats.setVisibility(View.INVISIBLE);
                                                textView2.setVisibility(View.INVISIBLE);
                                                textView5.setVisibility(View.INVISIBLE);
                                                textView7.setVisibility(View.INVISIBLE);
                                                textView13.setVisibility(View.INVISIBLE);
                                                textView51.setVisibility(View.VISIBLE);
                                                textView52.setVisibility(View.VISIBLE);
                                                textView53.setVisibility(View.VISIBLE);





                                            }

                                        }
                                    });

                                }
                                kcalList.removeAll(kcalList);
                                proteinList.removeAll(proteinList);
                                fatsList.removeAll(fatsList);
                                carbsList.removeAll(carbsList);
                                list.removeAll(list);



                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
                }
                if(selectedItem=="Users stats for muscle mass") {

                    db.collection("UsersInfo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<String> list = new ArrayList<>();
                                List<Integer> kcalList = new ArrayList<>();
                                List<Integer> proteinList= new ArrayList<>();
                                List<Integer> fatsList= new ArrayList<>();
                                List<Integer> carbsList= new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    list.add(document.getId());
                                }
                                for(String element : list){

                                    db.collection("UsersInfo").document(element).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            String goal = documentSnapshot.getString("Goal");
                                            String kcal = documentSnapshot.getString("Kcal");
                                            String protein= documentSnapshot.getString("Protein");
                                            String fat= documentSnapshot.getString("Fats");
                                            String carb= documentSnapshot.getString("Carbs");

                                            if (goal.matches("Αύξηση μυικής μάζας")){
                                                int intKcal = Integer.parseInt(kcal);
                                                int intProtein= Integer.parseInt(protein);
                                                int intFats = Integer.parseInt(fat);
                                                int intCarbs= Integer.parseInt(carb);
                                                kcalList.add(intKcal);
                                                proteinList.add(intProtein);
                                                fatsList.add(intFats);
                                                carbsList.add(intCarbs);
                                                i++;
                                                long intSumK = kcalList.stream()
                                                        .mapToLong(Integer::longValue)
                                                        .sum();

                                                long intSumP =  proteinList.stream()
                                                        .mapToLong(Integer::longValue)
                                                        .sum();
                                                long intSumF =  fatsList.stream()
                                                        .mapToLong(Integer::longValue)
                                                        .sum();
                                                long intSumC =  carbsList.stream()
                                                        .mapToLong(Integer::longValue)
                                                        .sum();

                                                finalSumK= intSumK/i;
                                                finalSumP= intSumP/i;
                                                finalSumC= intSumC/i;
                                                finalSumF= intSumF/i;
                                                calories.setText(String.valueOf(finalSumK));
                                                prot.setVisibility(View.INVISIBLE);
                                                carbs.setVisibility(View.INVISIBLE);
                                                fats.setVisibility(View.INVISIBLE);
                                                kcalProgress.setVisibility(View.INVISIBLE);
                                                proteinProgress.setVisibility(View.INVISIBLE);
                                                fatsProgress.setVisibility(View.INVISIBLE);
                                                carbsProgress.setVisibility(View.INVISIBLE);
                                                textView2.setVisibility(View.INVISIBLE);
                                                textView5.setVisibility(View.INVISIBLE);
                                                textView7.setVisibility(View.INVISIBLE);
                                                textView13.setVisibility(View.INVISIBLE);
                                                textView51.setText(String.valueOf(finalSumP));
                                                textView52.setText(String.valueOf(finalSumC));
                                                textView53.setText(String.valueOf(finalSumF));
                                                textView51.setVisibility(View.VISIBLE);
                                                textView52.setVisibility(View.VISIBLE);
                                                textView53.setVisibility(View.VISIBLE);

                                            }
                                            else{
                                                calories.setText("0");
                                                textView51.setText("0");
                                                textView52.setText("0");
                                                textView53.setText("0");
                                                kcalProgress.setVisibility(View.INVISIBLE);
                                                proteinProgress.setVisibility(View.INVISIBLE);
                                                fatsProgress.setVisibility(View.INVISIBLE);
                                                carbsProgress.setVisibility(View.INVISIBLE);
                                                prot.setVisibility(View.INVISIBLE);
                                                carbs.setVisibility(View.INVISIBLE);
                                                fats.setVisibility(View.INVISIBLE);
                                                textView2.setVisibility(View.INVISIBLE);
                                                textView5.setVisibility(View.INVISIBLE);
                                                textView7.setVisibility(View.INVISIBLE);
                                                textView13.setVisibility(View.INVISIBLE);
                                                textView51.setVisibility(View.VISIBLE);
                                                textView52.setVisibility(View.VISIBLE);
                                                textView53.setVisibility(View.VISIBLE);

                                            }
                                        }
                                    });

                                }
                                kcalList.removeAll(kcalList);
                                proteinList.removeAll(proteinList);
                                fatsList.removeAll(fatsList);
                                carbsList.removeAll(carbsList);
                                list.removeAll(list);
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
                }

                if(selectedItem=="Users stats for maintenance") {

                    db.collection("UsersInfo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<String> list = new ArrayList<>();
                                List<Integer> kcalList = new ArrayList<>();
                                List<Integer> proteinList= new ArrayList<>();
                                List<Integer> fatsList= new ArrayList<>();
                                List<Integer> carbsList= new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    list.add(document.getId());
                                }
                                for(String element : list){
                                    db.collection("UsersInfo").document(element).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            String goal = documentSnapshot.getString("Goal");
                                            String kcal = documentSnapshot.getString("Kcal");
                                            String protein= documentSnapshot.getString("Protein");
                                            String fat= documentSnapshot.getString("Fats");
                                            String carb= documentSnapshot.getString("Carbs");

                                            if (goal.matches("Διατήρηση βάρους")){
                                                int intKcal = Integer.parseInt(kcal);
                                                int intProtein= Integer.parseInt(protein);
                                                int intFats = Integer.parseInt(fat);
                                                int intCarbs= Integer.parseInt(carb);
                                                kcalList.add(intKcal);
                                                proteinList.add(intProtein);
                                                fatsList.add(intFats);
                                                carbsList.add(intCarbs);
                                                i++;
                                                long intSumK = kcalList.stream()
                                                        .mapToLong(Integer::longValue)
                                                        .sum();

                                                long intSumP =  proteinList.stream()
                                                        .mapToLong(Integer::longValue)
                                                        .sum();
                                                long intSumF =  fatsList.stream()
                                                        .mapToLong(Integer::longValue)
                                                        .sum();
                                                long intSumC =  carbsList.stream()
                                                        .mapToLong(Integer::longValue)
                                                        .sum();


                                                finalSumK= intSumK/i;
                                                finalSumP= intSumP/i;
                                                finalSumC= intSumC/i;
                                                finalSumF= intSumF/i;
                                                calories.setText(String.valueOf(finalSumK));
                                                prot.setVisibility(View.INVISIBLE);
                                                carbs.setVisibility(View.INVISIBLE);
                                                fats.setVisibility(View.INVISIBLE);
                                                kcalProgress.setVisibility(View.INVISIBLE);
                                                proteinProgress.setVisibility(View.INVISIBLE);
                                                fatsProgress.setVisibility(View.INVISIBLE);
                                                carbsProgress.setVisibility(View.INVISIBLE);
                                                textView2.setVisibility(View.INVISIBLE);
                                                textView5.setVisibility(View.INVISIBLE);
                                                textView7.setVisibility(View.INVISIBLE);
                                                textView13.setVisibility(View.INVISIBLE);
                                                textView51.setText(String.valueOf(finalSumP));
                                                textView52.setText(String.valueOf(finalSumC));
                                                textView53.setText(String.valueOf(finalSumF));
                                                textView51.setVisibility(View.VISIBLE);
                                                textView52.setVisibility(View.VISIBLE);
                                                textView53.setVisibility(View.VISIBLE);
                                            }
                                            else{
                                                calories.setText("0");
                                                textView51.setText("0");
                                                textView52.setText("0");
                                                textView53.setText("0");
                                                kcalProgress.setVisibility(View.INVISIBLE);
                                                proteinProgress.setVisibility(View.INVISIBLE);
                                                fatsProgress.setVisibility(View.INVISIBLE);
                                                carbsProgress.setVisibility(View.INVISIBLE);
                                                prot.setVisibility(View.INVISIBLE);
                                                carbs.setVisibility(View.INVISIBLE);
                                                fats.setVisibility(View.INVISIBLE);
                                                textView2.setVisibility(View.INVISIBLE);
                                                textView5.setVisibility(View.INVISIBLE);
                                                textView7.setVisibility(View.INVISIBLE);
                                                textView13.setVisibility(View.INVISIBLE);
                                                textView51.setVisibility(View.VISIBLE);
                                                textView52.setVisibility(View.VISIBLE);
                                                textView53.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });
                                }
                                kcalList.removeAll(kcalList);
                                proteinList.removeAll(proteinList);
                                fatsList.removeAll(fatsList);
                                carbsList.removeAll(carbsList);
                                list.removeAll(list);
                                i=0;
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
                }
                if (selectedItem=="Personαl Stats"){
                    loadData(sumKcal, sumProtein, sumFats, sumCarbs, kcalProgress, proteinProgress, fatsProgress, carbsProgress, prot, fats, carbs, calories);
                }
            }
                    @Override
                    public void onNothingSelected (AdapterView < ? > parent){

                    }

        });
    }

    public void loadData(int sKcal, int sProtein, int sFats, int sCarbs, ProgressBar kcalProgress,ProgressBar proteinProgress, ProgressBar fatsProgress, ProgressBar carbsProgress,TextView prot, TextView fats, TextView carbs, TextView calories  ){

        sum.put("Kcal", sKcal);
        sum.put("Protein", sProtein);
        sum.put("Fats", sFats);
        sum.put("Carbs", sCarbs);
        SharedPreferences sharedPreferences1 = getSharedPreferences(Text, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences2 = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        db.collection("ListOfDays").document(sharedPreferences1.getString("ID", "")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                final List<Map<String, Object>>[] days = new List[]{(List<Map<String, Object>>) documentSnapshot.get("DayList")};
                for (Map<String, Object> group : days[0]) {
                    String d = String.valueOf(group.get("Day"));
                    String m = String.valueOf(group.get("Month"));
                    String x = String.valueOf(group.get("Year"));
                    date.add(d + " " + m + " " + x);

                }


                for (int i = 0; i < date.size(); i++) {

                    db.collection("Users").document(sharedPreferences1.getString("ID", "")).collection(date.get(i)).document("OverAll").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentSnapshot = task.getResult();

                            int kcal = Integer.parseInt(documentSnapshot.getString("Kcal"));
                            int protein = Integer.parseInt(documentSnapshot.getString("Protein"));
                            int fat = Integer.parseInt(documentSnapshot.getString("Fats"));
                            int carb = Integer.parseInt(documentSnapshot.getString("Carbs"));



                            sum.put("Kcal", kcal + sum.get("Kcal"));
                            sum.put("Protein", protein + sum.get("Protein"));
                            sum.put("Fats", fat + sum.get("Fats"));
                            sum.put("Carbs", carb + sum.get("Carbs"));
                            averageForKcal = averageForKcal + 1;



                            editor.putString("SumKcal", String.valueOf(sum.get("Kcal")));
                            editor.putString("SumProtein", String.valueOf(sum.get("Protein")));
                            editor.putString("SumFats", String.valueOf(sum.get("Fats")));
                            editor.putString("SumCarbs", String.valueOf(sum.get("Carbs")));
                            editor.putString("averageForKcal", String.valueOf(averageForKcal));
                            editor.commit();



                            int averageKcal = Integer.parseInt(sharedPreferences2.getString("SumKcal", "")) / Integer.parseInt(sharedPreferences2.getString("averageForKcal", ""));
                            int averageProt = Integer.parseInt(sharedPreferences2.getString("SumProtein", "")) / Integer.parseInt(sharedPreferences2.getString("averageForKcal", ""));
                            int averageFats = Integer.parseInt(sharedPreferences2.getString("SumFats", "")) / Integer.parseInt(sharedPreferences2.getString("averageForKcal", ""));
                            int averageCarbs = Integer.parseInt(sharedPreferences2.getString("SumCarbs", "")) / Integer.parseInt(sharedPreferences2.getString("averageForKcal", ""));


                            kcalProgress.setProgress(averageKcal);
                            proteinProgress.setProgress(averageProt);
                            fatsProgress.setProgress(averageFats);
                            carbsProgress.setProgress(averageCarbs);


                            calories.setText(String.valueOf(averageKcal));
                            prot.setText(String.valueOf(averageProt));
                            carbs.setText(String.valueOf(averageCarbs));
                            fats.setText(String.valueOf(averageFats));
                            kcalProgress.setVisibility(View.VISIBLE);
                            proteinProgress.setVisibility(View.VISIBLE);
                            fatsProgress.setVisibility(View.VISIBLE);
                            carbsProgress.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
    }
}








