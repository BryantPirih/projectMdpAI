package com.bryant.projectmdpai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bryant.projectmdpai.databinding.ActivityProfileBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class profile extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().hasExtra("uid")){
            uid=getIntent().getStringExtra("uid");
        }
        loadProfile();
    }

    void makeToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void loadProfile(){
        DatabaseReference dbRef = FirebaseDatabase
                .getInstance(getResources().getString(R.string.url_db))
                .getReference().child("users");
        StorageReference storageRef = FirebaseStorage
                .getInstance(getResources().getString(R.string.url_storage))
                .getReference().child("images");

        //get user datas
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.hasChild(uid)){
                        DataSnapshot userSnapshot = snapshot.child(uid);
                        binding.tvUserID.setText(userSnapshot.child("full_name").getValue().toString());
                        binding.tvUsernameP.setText(userSnapshot.child("username").getValue().toString());
                        binding.tvFullName.setText(userSnapshot.child("full_name").getValue().toString());
                        binding.tvAddress.setText(userSnapshot.child("address").getValue().toString());
                        binding.tvEmail.setText(userSnapshot.child("email").getValue().toString());
//                        binding.txtTimeDocProfile.setText(userSnapshot.child("time").getValue().toString());
                        binding.txtDescUserProfile.setText(userSnapshot.child("desc").getValue().toString());
                    }
                }catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                makeToast("Failed to get data");
            }
        });

        //get images
        storageRef.child("profile_pictures").child(uid+".png")
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                Glide.with(getApplicationContext()).load(imageURL).into(binding.imgUsProfile);
                binding.pgBarUserProfile.setVisibility(View.INVISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
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
        Intent intent;
        switch (item.getItemId()){
            case R.id.option_home:
                intent = new Intent(getApplicationContext(), UserHome.class);
                break;
            case R.id.option_profile:
                intent = new Intent(getApplicationContext(), profile.class);
                break;
            default:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                break;
        }

        intent.putExtra("uid", uid);
        startActivity(intent);
        finish();
        return super.onOptionsItemSelected(item);
    }
}