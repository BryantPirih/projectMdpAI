package com.bryant.projectmdpai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.bryant.projectmdpai.Class.ExpertSystem.QuestionES;
import com.bryant.projectmdpai.Class.ExpertSystem.Rules;
import com.bryant.projectmdpai.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase root = FirebaseDatabase
            .getInstance("https://mdp-project-9db6f-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference reference;
    private FirebaseAuth mAuth;
    private ActivityMainBinding binding;
    private String role="User";
    private ObjectAnimator bgcenter;
    private ObjectAnimator layer1;
    private ObjectAnimator layer2;
    private ObjectAnimator layer3;
    private ObjectAnimator mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bgcenter.ofFloat(binding.bgCenter, "translationY", -50f).setDuration(1500).start();
        layer1.ofFloat(binding.bglayer1, "translationY", 80f).setDuration(1500).start();
        layer2.ofFloat(binding.bglayer2, "translationY", 50f).setDuration(1500).start();
        layer3.ofFloat(binding.bglayer3, "translationY", 50f).setDuration(1500).start();
        mainLayout.ofFloat(binding.MainLayout, "alpha", 1).setDuration(3000).start();;

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
                    String uid = mAuth.getCurrentUser().getUid();
                    getUserType(new FirebaseCallback() {
                        @Override
                        public void onCallbackType(String type) {
                            role=type;
                            Intent i;
                            if (role.equalsIgnoreCase("Dokter")){
                                i = new Intent(MainActivity.this,DoctorHome.class);
                            }else if(role.equalsIgnoreCase("User")){
                                i = new Intent(MainActivity.this,UserHome.class);
                            }else{
                                i = new Intent(MainActivity.this, AdminHome.class);
                            }
                            i.putExtra("uid",uid);
                            startActivity(i);
                            finish();
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    },uid);
                }else{
                    Toast.makeText(getApplicationContext(), "Login Failed! Email blom terdaftar", Toast.LENGTH_SHORT).show();
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getUserType(FirebaseCallback firebaseCallback , String uid){
        String type="User";
        DatabaseReference usersRef = root.getReference().child("users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren() ) {
                    String type="User";
                    try {
                        usersRef.child(ds.getKey()).child("id").setValue(ds.getKey());
                        if (ds.getKey().equals(uid)){
                            type=ds.child("role").getValue().toString();
                            firebaseCallback.onCallbackType(type);
                        }
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
    private interface FirebaseCallback{
        void onCallbackType(String type);
    }
}