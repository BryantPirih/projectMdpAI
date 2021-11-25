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
        String address = binding.edtAddressRegister.getText().toString().trim();
        String password = binding.edtPasswordRegister.getText().toString().trim();
        String password_confirmation = binding.edtConfirmRegister.getText().toString().trim();
        String role;

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
        }else{
            role = binding.rbDokter.getText().toString();
        }

        binding.progressBarRegis.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(username, email, address, password, role, 0);

                    FirebaseDatabase.getInstance(getString(R.string.url_db)).getReference("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                makeToast("User has been registered successfully!");
                                binding.progressBarRegis.setVisibility(View.VISIBLE);
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

    void btnRegister_Clicked(){
        //ga tak pake
        if (TextUtils.isEmpty(binding.edtUsernameRegister.getText().toString())
                || TextUtils.isEmpty(binding.edtPasswordRegister.getText().toString())
                || TextUtils.isEmpty(binding.edtEmailRegister.getText().toString())
                || TextUtils.isEmpty(binding.edtAddressRegister.getText().toString())){
            makeToast("Harap isi semua data terlebih dahulu!");
        }else if(!binding.edtPasswordRegister.getText().toString().equals(binding.edtConfirmRegister.getText().toString())){
            makeToast("Password Tidak Cocok!");
        }else{
            register();
        }
    }

    public void register(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.urlB),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("function","register");
                params.put("username", binding.edtUsernameRegister.getText().toString());
                params.put("password", binding.edtPasswordRegister.getText().toString());
                params.put("confirm", binding.edtConfirmRegister.getText().toString());
                if(binding.rbUser.isChecked()){
                    params.put("role", binding.rbUser.getText().toString());
                }
                else{
                    params.put("role", binding.rbDokter.getText().toString());
                }
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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