package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by tutlane on 08-01-2018.
 */

public class RegistrationActivity extends AppCompatActivity {
    EditText editText1,editText2, editText3;
    TextView textView1, textView2, textView3;
    private FirebaseAuth mAuth;
    public static final String SHARED_PREFS= "sharedPrefs";
    public static final String Text = "Text";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        editText1 = findViewById(R.id.username);
        editText2 = findViewById(R.id.Email);
        editText3 = findViewById(R.id.password);
        textView1 = findViewById(R.id.registerTxt);
        textView2 = findViewById(R.id.textview4);
        textView3 = findViewById(R.id.login);
        mAuth = FirebaseAuth.getInstance();


    }
    public void goLogin(View view){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
    public void goInformation(View view){

        try {
            mAuth.createUserWithEmailAndPassword(editText2.getText().toString(), editText3.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){


                        SharedPreferences sharedPreferences = getSharedPreferences(Text, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        String ID = mAuth.getUid();
                        editor.putString("Email", editText2.getText().toString());
                        editor.putString("ID",ID);
                        editor.apply();
                        changePage(ID);


                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Error! Try again", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error! Please fill the gaps", Toast.LENGTH_SHORT).show();

        }
    }
    public void changePage(String id){
        Intent i = new Intent(getApplicationContext(), Information.class);
        i.putExtra("ID", id);
        startActivity(i);


    }


}