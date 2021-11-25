package com.bryant.projectmdpai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase root;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnToRegister_Clicked(View view) {
        Intent i = new Intent(MainActivity.this,register.class);
        startActivity(i);
        finish();
    }

    public void btnLoginClicked(View v){
        root = FirebaseDatabase.getInstance();
        reference = root.getReference("users");
        reference.setValue("user");
//        reference.child().child();
    }
}