package com.bryant.projectmdpai;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bryant.projectmdpai.databinding.ActivityProfileBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class profile extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private String uid, oldPw;
    private String Document_img1 = "";
    private Uri selectedProfilePicture;
    private Boolean toggle, valid;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toggle = false;
        toggleVisibility(toggle);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (getIntent().hasExtra("uid")){
            uid = getIntent().getStringExtra("uid");
        }

        //button change picture
        binding.btnChangePic.setOnClickListener(view -> selectImage("profile_picture"));

        //button change password
        binding.btnChangePass.setOnClickListener(view -> {
            toggle = true;
            toggleVisibility(toggle);
            getPwNowUser();

            binding.btnNextChangePw.setOnClickListener(view1 -> changePw());

            binding.btnBackToUsProfile.setOnClickListener(view12 -> {
                toggle = false;
                toggleVisibility(toggle);
                binding.pgBarUserProfile.setVisibility(View.INVISIBLE);
            });
        });

        //button save
        binding.btnChangeProf.setOnClickListener(view -> {
            if (selectedProfilePicture!=null){
                uploadImage(selectedProfilePicture,"images/profile_pictures/",uid);
            }
            //save other user profiles
            try {
                FirebaseDatabase root = FirebaseDatabase.getInstance(getResources().getString(R.string.url_db));
                root.getReference("users/"+uid+"/desc").setValue(binding.txtDescUserProfile.getText().toString());
                root.getReference("users/"+uid+"/address").setValue(binding.txtAddressUserProfile.getText().toString());
                makeToast("Berhasil mengubah profile");
            }catch (Exception exception){
                System.out.println(exception);
            }
        });
        loadProfile();
    }

    private void getPwNowUser(){
        DatabaseReference dbRef = FirebaseDatabase
                .getInstance(getResources().getString(R.string.url_db))
                .getReference().child("users");

        //get user password
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.hasChild(uid)){
                        DataSnapshot userSnapshot = snapshot.child(uid);
                        oldPw = userSnapshot.child("password").getValue().toString();
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
    }

    private void changePw(){
        if(TextUtils.isEmpty(binding.edtNowPw.getText().toString())){
            binding.edtNowPw.setError("Current Password is required!");
            binding.edtNowPw.requestFocus();
            valid = false;
            return;
        }else if(!binding.edtNowPw.getText().toString().equals(oldPw)){
            binding.edtNowPw.setError("Current Password invalid!");
            binding.edtNowPw.requestFocus();
            valid = false;
            return;
        }else{
            valid = true;
            if(valid){
                binding.textView42.setVisibility(View.INVISIBLE);
                binding.edtNowPw.setVisibility(View.INVISIBLE);
                binding.btnNextChangePw.setVisibility(View.INVISIBLE);
                binding.btnBackToUsProfile.setVisibility(View.INVISIBLE);
                binding.textView44.setVisibility(View.VISIBLE);
                binding.edtNewPw.setVisibility(View.VISIBLE);
                binding.textView45.setVisibility(View.VISIBLE);
                binding.edtConPw.setVisibility(View.VISIBLE);
                binding.btnUpdateNewPw.setVisibility(View.VISIBLE);

                binding.btnUpdateNewPw.setOnClickListener(view -> {
                    if(TextUtils.isEmpty(binding.edtNewPw.getText().toString())){
                        binding.edtNewPw.setError("New Password is required!");
                        binding.edtNewPw.requestFocus();
                        return;
                    }else if(!binding.edtConPw.getText().toString().equals(binding.edtNewPw.getText().toString())){
                        binding.edtConPw.setError("Confirmation Password not matched!");
                        binding.edtConPw.requestFocus();
                        return;
                    }else{
                        String newPassword = binding.edtNewPw.getText().toString();

                        try {
                            user.updatePassword(newPassword).addOnSuccessListener(unused -> {
                                FirebaseDatabase root = FirebaseDatabase.getInstance(getResources().getString(R.string.url_db));
                                root.getReference("users/"+uid+"/password").setValue(newPassword);
                                makeToast("Password Reset Successfully!");
                            }).addOnFailureListener(e -> makeToast("Password Reset Failed. Password Should be at least 6 characters!"));
                        }catch (Exception exception){
                            System.out.println(exception);
                        }

                        toggle = false;
                        toggleVisibility(toggle);
                        binding.pgBarUserProfile.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }
    }

    private void toggleVisibility(boolean yes){
        if (yes){
            binding.linearLayoutUsProfile.setVisibility(View.VISIBLE);
            binding.tvUserID.setVisibility(View.VISIBLE);
            binding.btnChangePic.setVisibility(View.INVISIBLE);
            binding.pgBarUserProfile.setVisibility(View.INVISIBLE);
            binding.scrollViewDataUser.setVisibility(View.INVISIBLE);
            binding.btnChangePass.setVisibility(View.INVISIBLE);
            binding.btnChangeProf.setVisibility(View.INVISIBLE);
            binding.ChangePwLayout.setVisibility(View.VISIBLE);
        }else{
            binding.linearLayoutUsProfile.setVisibility(View.VISIBLE);
            binding.tvUserID.setVisibility(View.VISIBLE);
            binding.btnChangePic.setVisibility(View.VISIBLE);
            binding.pgBarUserProfile.setVisibility(View.VISIBLE);
            binding.scrollViewDataUser.setVisibility(View.VISIBLE);
            binding.btnChangePass.setVisibility(View.VISIBLE);
            binding.btnChangeProf.setVisibility(View.VISIBLE);
            binding.ChangePwLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void selectImage(String mode){
        if (mode.equals("profile_picture")){
            Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 1){ //profile_picture
                selectedProfilePicture = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedProfilePicture,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                thumbnail = getResizedBitmap(thumbnail, 400);
                //Log.w("path of image from gallery......******************.........", picturePath+"");
                binding.imgUsProfile.setImageBitmap(thumbnail);
                BitMapToString(thumbnail);
            }
        }
    }

    public String BitMapToString(Bitmap thumbnail) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    }

    private Bitmap getResizedBitmap(Bitmap thumbnail, int maxSize) {
        int width = thumbnail.getWidth();
        int height = thumbnail.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(thumbnail, width, height, true);
    }

    // UploadImage method
    private void uploadImage(Uri filePath , String storagePath, String fileName) {
        if (filePath != null) {
//            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Saving Changes...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = FirebaseStorage
                    .getInstance(getResources().getString(R.string.url_storage))
                    .getReference()
                    .child(
                            storagePath + fileName);
            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(profile.this,
                                                    "Image Uploaded!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(profile.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
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
                        binding.txtAddressUserProfile.setText(userSnapshot.child("address").getValue().toString());
                        binding.tvEmail.setText(userSnapshot.child("email").getValue().toString());
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
        storageRef.child("profile_pictures").child(uid)
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                Glide.with(getApplicationContext()).load(imageURL).into(binding.imgUsProfile);
                binding.pgBarUserProfile.setVisibility(View.INVISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                binding.imgUsProfile.setBackground(getResources().getDrawable(R.drawable.ic_user_icon));
                binding.pgBarUserProfile.setVisibility(View.INVISIBLE);
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

    void makeToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}