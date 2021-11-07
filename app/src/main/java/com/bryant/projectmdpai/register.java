package com.bryant.projectmdpai;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    EditText username,password,confirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.edtUsernameRegister);
        password = findViewById(R.id.edtPasswordRegister);
        confirm = findViewById(R.id.edtConfirmRegister);
    }

    public void btnRegister_Clicked(View view) {
        if (TextUtils.isEmpty(username.getText().toString()) || TextUtils.isEmpty(password.getText().toString())
        || TextUtils.isEmpty(confirm.getText().toString())){
            Toast.makeText(this, "harap isi semua data terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
        else{
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
                params.put("username",username.getText().toString());
                params.put("password",password.getText().toString());
                params.put("confirm",confirm.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void btnToLogin_Clicked(View view) {
        Intent i = new Intent(register.this,MainActivity.class);
        startActivity(i);
        finish();
    }
}