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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bryant.projectmdpai.Class.ExpertSystem.Rules;
import com.bryant.projectmdpai.databinding.ActivityDoctorProfileBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class DoctorProfile extends AppCompatActivity {

    private ActivityDoctorProfileBinding binding;
    private String Document_img1 = "";
    private String uid, oldPw;
    private Uri selectedProfilePicture;
    private Uri selectedLicense;
    private Boolean toggle, valid;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toggle = false;
        toggleVisibility(toggle);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (getIntent().hasExtra("uid")){
            uid = getIntent().getStringExtra("uid");
        }
        loadProfile();

        //button change picture
        binding.btnChangePicDocProfile.setOnClickListener(view -> selectImage("profile_picture"));

        //button Add License Image
        binding.txtAddLicenseDocProfile.setOnClickListener(view -> {
            makeToast("Add License Image!");
            selectImage("license");
        });

        //button change password
        binding.btnChangePwDocProfile.setOnClickListener(view -> {
            toggle = true;
            toggleVisibility(toggle);
            getPwNowUser();

            binding.btnNextChangeDocPw.setOnClickListener(view12 -> changePw());

            binding.btnBackToDocProfile.setOnClickListener(view1 -> {
                toggle = false;
                toggleVisibility(toggle);
                binding.progressBarProfileImage.setVisibility(View.INVISIBLE);
            });
        });

        //button change profile
        binding.btnChangeProfileDoc.setOnClickListener(view -> {
            //save profile picture name by uid
            if (selectedProfilePicture!=null){
                uploadImage(selectedProfilePicture,"images/profile_pictures/",uid);
            }
            if (selectedLicense!=null){
                uploadImage(selectedLicense,"images/licenses/",uid);
            }
            //save other user profiles
            try {
                FirebaseDatabase root = FirebaseDatabase.getInstance(getResources().getString(R.string.url_db));
                root.getReference("users/"+uid+"/desc").setValue(binding.txtDescDocProfile.getText().toString());
                root.getReference("users/"+uid+"/address").setValue(binding.txtAddressDocProfile.getText().toString());
                makeToast("Berhasil mengubah profile");
            }catch (Exception exception){
                System.out.println(exception);
            }
        });

        //button back home
        binding.btnBackDoctorHome.setOnClickListener(view -> {
            Intent toDocHome = new Intent(getApplicationContext(), DoctorHome.class);
            toDocHome.putExtra("uid", uid);
            startActivity(toDocHome);
        });
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
        if(TextUtils.isEmpty(binding.edtNowDocPw.getText().toString())){
            binding.edtNowDocPw.setError("Current Password is required!");
            binding.edtNowDocPw.requestFocus();
            valid = false;
            return;
        }else if(!binding.edtNowDocPw.getText().toString().equals(oldPw)){
            binding.edtNowDocPw.setError("Current Password invalid!");
            binding.edtNowDocPw.requestFocus();
            valid = false;
            return;
        }else{
            valid = true;
            if(valid){
                binding.textView42.setVisibility(View.INVISIBLE);
                binding.edtNowDocPw.setVisibility(View.INVISIBLE);
                binding.btnNextChangeDocPw.setVisibility(View.INVISIBLE);
                binding.btnBackToDocProfile.setVisibility(View.INVISIBLE);
                binding.textView44.setVisibility(View.VISIBLE);
                binding.edtNewDocPw.setVisibility(View.VISIBLE);
                binding.textView45.setVisibility(View.VISIBLE);
                binding.edtConDocPw.setVisibility(View.VISIBLE);
                binding.btnUpdateNewDocPw.setVisibility(View.VISIBLE);

                binding.btnUpdateNewDocPw.setOnClickListener(view -> {
                    if(TextUtils.isEmpty(binding.edtNewDocPw.getText().toString())){
                        binding.edtNewDocPw.setError("New Password is required!");
                        binding.edtNewDocPw.requestFocus();
                        return;
                    }else if(!binding.edtConDocPw.getText().toString().equals(binding.edtNewDocPw.getText().toString())){
                        binding.edtConDocPw.setError("Confirmation Password not matched!");
                        binding.edtConDocPw.requestFocus();
                        return;
                    }else{
                        String newPassword = binding.edtNewDocPw.getText().toString();

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
                        binding.progressBarProfileImage.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }
    }

    private void toggleVisibility(boolean yes){
        if (yes){
            binding.btnChangePicDocProfile.setVisibility(View.INVISIBLE);
            binding.progressBarProfileImage.setVisibility(View.INVISIBLE);
            binding.scView.setVisibility(View.INVISIBLE);
            binding.btnChangePwDocProfile.setVisibility(View.INVISIBLE);
            binding.btnChangeProfileDoc.setVisibility(View.INVISIBLE);
            binding.ChangePwDocLayout.setVisibility(View.VISIBLE);
            binding.btnBackDoctorHome.setVisibility(View.INVISIBLE);
        }else{
            binding.btnChangePicDocProfile.setVisibility(View.VISIBLE);
            binding.progressBarProfileImage.setVisibility(View.VISIBLE);
            binding.scView.setVisibility(View.VISIBLE);
            binding.btnChangePwDocProfile.setVisibility(View.VISIBLE);
            binding.btnChangeProfileDoc.setVisibility(View.VISIBLE);
            binding.ChangePwDocLayout.setVisibility(View.INVISIBLE);
            binding.btnBackDoctorHome.setVisibility(View.VISIBLE);
        }
    }

    void makeToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void selectImage(String mode){
        if (mode.equals("profile_picture")){
            Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        }else if (mode.equals("license")){
            Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
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
                binding.imgDocProfile.setImageBitmap(thumbnail);
                BitMapToString(thumbnail);
            }
            if(requestCode == 2){ //license
                selectedLicense = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedLicense,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                thumbnail = getResizedBitmap(thumbnail, 400);
                //Log.w("path of image from gallery......******************.........", picturePath+"");
                binding.imgDocProfileLicense.setImageBitmap(thumbnail);
                binding.imgDocProfileLicense.setVisibility(View.VISIBLE);
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
    private void uploadImage(Uri filePath , String storagePath, String fileName)
    {
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
                                            .makeText(DoctorProfile.this,
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
                                    .makeText(DoctorProfile.this,
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
                        binding.txtUsnameDocProfile.setText(userSnapshot.child("username").getValue().toString());
                        binding.txtNameDocProfile.setText(userSnapshot.child("full_name").getValue().toString());
                        binding.txtAddressDocProfile.setText(userSnapshot.child("address").getValue().toString());
                        binding.txtEmailDocProfile.setText(userSnapshot.child("email").getValue().toString());
//                        binding.txtTimeDocProfile.setText(userSnapshot.child("time").getValue().toString());
                        binding.txtDescDocProfile.setText(userSnapshot.child("desc").getValue().toString());
                        if(userSnapshot.child("status").getValue().toString().equals("1")){
                            binding.txtAddLicenseDocProfile.setText("Verified");
                        }
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
                Glide.with(getApplicationContext()).load(imageURL).into(binding.imgDocProfile);
                binding.progressBarProfileImage.setVisibility(View.INVISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                binding.imgDocProfile.setBackground(getResources().getDrawable(R.drawable.ic_user_icon));
                binding.progressBarProfileImage.setVisibility(View.INVISIBLE);
            }
        });
        storageRef.child("licenses").child(uid)
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                Glide.with(getApplicationContext()).load(imageURL).into(binding.imgDocProfileLicense);
                binding.imgDocProfileLicense.setVisibility(View.VISIBLE);
                if(binding.txtAddLicenseDocProfile.getText().equals("Add License Image [Click Me]")){
                    binding.txtAddLicenseDocProfile.setText("Change License Image [Click Me]");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

}