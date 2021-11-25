package com.bryant.projectmdpai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bryant.projectmdpai.databinding.ActivityDoctorProfileBinding;

public class DoctorProfile extends AppCompatActivity {

    private ActivityDoctorProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //button change picture
        binding.btnChangePicDocProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //button Add License Image
        binding.txtAddLicenseDocProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeToast("Add License Image!");
            }
        });

        //button change password
        binding.btnChangePwDocProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //button change profile
        binding.btnChangeProfileDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    void makeToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}