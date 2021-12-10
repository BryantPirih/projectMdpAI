package com.bryant.projectmdpai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bryant.projectmdpai.Class.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AdminVerification extends AppCompatActivity {
    Intent secondIntent;
    User user;
    TextView txtFullName, txtEmail, txtAddress, txtAlert;
    ImageView imgProfile, imgVerif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_verification);
        secondIntent = getIntent();
        if(secondIntent.hasExtra("user")){
            user = secondIntent.getParcelableExtra("user");
        }

        txtFullName = findViewById(R.id.txt_admin_detail_fullname);
        txtEmail = findViewById(R.id.txt_admin_detail_email);
        txtAddress = findViewById(R.id.txt_admin_detail_address);
        txtAlert = findViewById(R.id.txt_admin_detail_alert);
        imgProfile = findViewById(R.id.img_admin_detail_profile);
        imgVerif = findViewById(R.id.img_admin_detail_verif);

        txtFullName.setText(user.getFull_name());
        txtEmail.setText(user.getEmail());
        txtAddress.setText(user.getAddress());

        StorageReference storageRef = FirebaseStorage
                .getInstance("gs://mdp-project-9db6f.appspot.com/")
                .getReference().child("images");
        storageRef.child("profile_pictures").child(user.getId())
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                Glide.with(getApplicationContext())
                        .load(imageURL)
                        .placeholder(R.drawable.ic_user_icon)
                        .into(imgProfile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        storageRef = FirebaseStorage
                .getInstance("gs://mdp-project-9db6f.appspot.com/")
                .getReference().child("images");
        storageRef.child("license").child(user.getId())
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                Glide.with(getApplicationContext())
                        .load(imageURL)
                        .into(imgVerif);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                txtAlert.setText(txtAlert.getText().toString()+"\nNo License Found");
            }
        });
    }

    public void approve(View v){
        try{
            FirebaseDatabase root = FirebaseDatabase.getInstance(getResources().getString(R.string.url_db));
            root.getReference("users/"+user.getId()+"/status").setValue(1);
            Intent i = new Intent(AdminVerification.this, AdminHome.class);
            startActivity(i);
            finish();
        }
        catch (Exception x){
            System.out.println(x.getMessage());
        }
    }
}