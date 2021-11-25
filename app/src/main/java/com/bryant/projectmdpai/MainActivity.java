package com.bryant.projectmdpai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bryant.projectmdpai.databinding.ActivityMainBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase root;
    DatabaseReference reference;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLoginClicked();
            }
        });

        binding.btnToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnToRegister_Clicked();
            }
        });
    }

    public void btnToRegister_Clicked() {
        Intent i = new Intent(MainActivity.this,register.class);
        startActivity(i);
        finish();
    }

    public void btnLoginClicked(){
        root = FirebaseDatabase.getInstance(getString(R.string.url_db));
        reference = root.getReference("users");
        System.out.println(reference);
        reference.setValue("user");
//        reference.child().child();
    }
}