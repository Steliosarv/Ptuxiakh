package com.example.myapplication;

import static com.google.firebase.firestore.FieldValue.arrayRemove;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListofFood extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String Text = "Text";
    private List<String> myList;
    private  List <String> group;
    private ArrayList<String> lngList;
    ArrayList<String> names = new ArrayList<>();
    Map<String , Object> finalList = new HashMap<>();
     

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listof_food);
        ListView listView = findViewById(R.id.list);
        SharedPreferences sharedPreferences = getSharedPreferences("Meal", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences1 = getSharedPreferences(Text, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences2 = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String day = sharedPreferences2.getString("Day", "");
        String month = sharedPreferences2.getString("Month", "");
        String year = sharedPreferences2.getString("Year", "");
        lngList = new ArrayList<>();
        Map<String , Object> updatedList = new HashMap<>();


        lngList.add("");
        lngList.add("");


        String meal = sharedPreferences.getString("TimeOfDay", "");

        db.collection("Users").document(sharedPreferences1.getString("ID", "")).collection(day + " " + month + " " + year).document(meal).collection("List")
                .document("Food").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        group = (List<String>) documentSnapshot.get("FoodList");
                        List<Map<String, Object>> groups = (List<Map<String, Object>>) documentSnapshot.get("FoodList");

                        for (Map<String, Object> group : groups) {
                            String name = (String) group.get("Food");
                            names.add(name);
                        }
                        setList(lngList, listView, names);


                    }
                });




        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                new AlertDialog.Builder(ListofFood.this)
                        .setTitle("Do you want to delete this food:  " + names.get(position))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String item = names.get(position);
                                db.collection("Users").document(sharedPreferences1.getString("ID", "")).collection(day + " " + month + " " + year).document(meal).collection("List")
                                        .document("Food").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                DocumentSnapshot documentSnapshot = task.getResult();
                                                List<Map<String, Object>> groups = (List<Map<String, Object>>) documentSnapshot.get("FoodList");
                                                Map<String, Object> group1 = new HashMap<>();
                                                String food = (String) group1.get("Food");


                                                for (Map<String, Object> group : groups) {

                                                    if(item.equals(group.get("Food"))){

                                                        db.collection("Users").document(sharedPreferences1.getString("ID", "")).collection(day + " " + month + " " + year)
                                                                .document("OverAll").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                        finalList.put("Kcal", String.valueOf(Integer.parseInt(documentSnapshot.getString("Kcal")) - Integer.parseInt(String.valueOf(group.get("Kcal")))));
                                                                        finalList.put("Carbs", String.valueOf(Integer.parseInt(documentSnapshot.getString("Carbs")) - Integer.parseInt(String.valueOf(group.get("Carbs")))));
                                                                        finalList.put("Fats", String.valueOf(Integer.parseInt(documentSnapshot.getString("Fats")) - Integer.parseInt(String.valueOf(group.get("Fats")))));
                                                                        finalList.put("Protein", String.valueOf(Integer.parseInt(documentSnapshot.getString("Protein")) - Integer.parseInt(String.valueOf(group.get("Protein")))));

                                                                        db.collection("Users").document(sharedPreferences1.getString("ID", "")).collection(day + " " + month + " " + year)
                                                                                .document("OverAll").set(finalList);
                                                                        groups.remove(position + 1);

                                                                        db.collection("Users").document(sharedPreferences1.getString("ID", "")).collection(day + " " + month + " " + year).document(meal).collection("List")
                                                                                .document("Food").update("FoodList", groups);

                                                                        names.remove(item);
                                                                        setAdapter(names, listView);
                                                                    }
                                                                });
                                                        break;
                                                    }else{

                                                    } }
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                return false;
            }
        });

    }

    public void setList (List x, ListView y, List x1) {
        x1.remove("");
        x1.remove(" ");
        x1.remove(null);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, x1);
        y.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

    }
    public void setAdapter(List x, ListView y){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, x);
        y.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

    }
}