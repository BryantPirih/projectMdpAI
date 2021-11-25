package com.bryant.projectmdpai;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.bryant.projectmdpai.databinding.ActivityRegisterBinding;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnRegister_Clicked();
            }
        });

        binding.btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnToLogin_Clicked();
            }
        });
    }

    public void btnRegister_Clicked() {
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