package com.bryant.projectmdpai;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bryant.projectmdpai.Class.User;
import com.bryant.projectmdpai.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class register extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        binding.progressBarRegis.setVisibility(View.GONE);

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerWithFirebase();
            }
        });

        binding.btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnToLogin_Clicked();
            }
        });
    }

    void registerWithFirebase() {
        String username = binding.edtUsernameRegister.getText().toString().trim();
        String email = binding.edtEmailRegister.getText().toString().trim();
        String full = binding.edtFullName.getText().toString().trim();
        String address = binding.edtAddressRegister.getText().toString().trim();
        String password = binding.edtPasswordRegister.getText().toString().trim();
        String password_confirmation = binding.edtConfirmRegister.getText().toString().trim();
        String role;
        int status;

        if(username.isEmpty()){
            binding.edtUsernameRegister.setError("Username is required!");
            binding.edtUsernameRegister.requestFocus();
            return;
        }
        if(email.isEmpty()){
            binding.edtEmailRegister.setError("Email is required!");
            binding.edtEmailRegister.requestFocus();
            return;
        }
        if (full.isEmpty()){
            binding.edtFullName.setError("Full name is required!");
            binding.edtFullName.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.edtEmailRegister.setError("Please input valid email!");
            binding.edtEmailRegister.requestFocus();
            return;
        }
        if(address.isEmpty()){
            binding.edtAddressRegister.setError("Address is required!");
            binding.edtAddressRegister.requestFocus();
            return;
        }
        if(password.isEmpty()){
            binding.edtPasswordRegister.setError("Password is required!");
            binding.edtPasswordRegister.requestFocus();
            return;
        }
        if(!password.equals(password_confirmation)){
            binding.edtConfirmRegister.setError("Password Unmatched!");
            binding.edtConfirmRegister.requestFocus();
            return;
        }
        if(binding.rbUser.isChecked()){
            role = binding.rbUser.getText().toString();
            status=1;
        }else{
            role = binding.rbDokter.getText().toString();
            status=0;
        }

        binding.progressBarRegis.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(username, email, full, address, password, role, 0,status);
                    //Toast.makeText(getApplicationContext(), , Toast.LENGTH_SHORT).show();
                    String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseDatabase.getInstance(getResources().getString(R.string.url_db))
                            .getReference("users")
                            .child(userKey)
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                makeToast("User has been registered successfully!");
                                //set id
                                try {
                                    FirebaseDatabase.getInstance(getResources().getString(R.string.url_db))
                                            .getReference("users/"+userKey).child("id").setValue(userKey);
                                }catch (Exception exception){
                                    makeToast("users/id nya belom masuk");
                                }
                                binding.progressBarRegis.setVisibility(View.GONE);
                            }else{
                                makeToast("Failed to register! Try again!");
                                binding.progressBarRegis.setVisibility(View.GONE);
                            }
                        }
                    });

                }else{
                    makeToast("Failed to register! Try again!");
                    binding.progressBarRegis.setVisibility(View.GONE);
                }
            }
        });
    }
    public void btnToLogin_Clicked() {
        Intent i = new Intent(register.this,MainActivity.class);
        startActivity(i);
        finish();
    }

    void makeToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}