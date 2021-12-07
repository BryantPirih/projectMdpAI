package com.bryant.projectmdpai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bryant.projectmdpai.databinding.ActivityProfileBinding;

public class profile extends AppCompatActivity {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBackUserHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.option_home:
                Intent toHome = new Intent(getApplicationContext(), UserHome.class);
                startActivity(toHome);
                finish();
                break;
            case R.id.option_profile:

                break;
            default:
                Intent toLogin = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(toLogin);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}