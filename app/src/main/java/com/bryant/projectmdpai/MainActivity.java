package com.bryant.projectmdpai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.bryant.projectmdpai.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase root;
    DatabaseReference reference;
    private FirebaseAuth mAuth;
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
        mAuth = FirebaseAuth.getInstance();
    }

    public void btnToRegister_Clicked() {
        Intent i = new Intent(MainActivity.this,register.class);
        startActivity(i);
        finish();
    }

    public void btnLoginClicked(){
        if (TextUtils.isEmpty(binding.edtPasswordLogin.getText().toString())){
            binding.edtPasswordLogin.setError("Password is required!");
            binding.edtPasswordLogin.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(binding.edtEmailLogin.getText().toString())){
            binding.edtEmailLogin.setError("Email is required!");
            binding.edtEmailLogin.requestFocus();
            return;
        }
        binding.progressBar.setVisibility(View.VISIBLE);
        String email = binding.edtEmailLogin.getText().toString().trim();
        String password = binding.edtPasswordLogin.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent i = new Intent(MainActivity.this,UserHome.class);
                    startActivity(i);
                    finish();
                    binding.progressBar.setVisibility(View.GONE);
                }else{
                    Toast.makeText(getApplicationContext(), "Login Failed! Email blom terdaftar", Toast.LENGTH_SHORT).show();
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}