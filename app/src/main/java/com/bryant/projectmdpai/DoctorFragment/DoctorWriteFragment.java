package com.bryant.projectmdpai.DoctorFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bryant.projectmdpai.Class.Article;
import com.bryant.projectmdpai.R;
import com.bryant.projectmdpai.databinding.FragmentDoctorWriteBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DoctorWriteFragment extends Fragment {


    private static final String ARG_PARAM_UID = "param-uid";
    private String uid;

    private FragmentDoctorWriteBinding binding;
    private ArrayList<Article> articles = new ArrayList<>();
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Uri imageUri;
    private String tempFullName;
    private String Key;

    public DoctorWriteFragment() {
        // Required empty public constructor
    }

    public static DoctorWriteFragment newInstance(String paramUid) {
        DoctorWriteFragment fragment = new DoctorWriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_UID, paramUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDoctorWriteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            uid = getArguments().getString(ARG_PARAM_UID);
        }

        firebaseStorage = FirebaseStorage.getInstance(getActivity().getResources().getString(R.string.url_storage));
        storageReference = firebaseStorage.getReference();
        imageUri=null;

        getData(new FirebaseCallback() {
            @Override
            public void onCallbackArticles(String fullname) {
                tempFullName = fullname;
            }
        });
        ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent i = result.getData();
                        if (i!=null){
                            try {
                                Bitmap b = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),i.getData());
                                binding.imgArticle.setImageBitmap(b);
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                b.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                                String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),b,"val",null);
                                imageUri = Uri.parse(path);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
        );

        binding.btnAddImgArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                resultLauncher.launch(intent);
            }
        });

        binding.btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.edtDocContentArticle.setText("");
                binding.edtDocTitleArticle.setText("");
                binding.imgArticle.setImageBitmap(null);
            }
        });

        binding.btnPostArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(binding.edtDocContentArticle.getText().toString())){
                    binding.edtDocContentArticle.setError("Content is required!");
                    binding.edtDocContentArticle.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(binding.edtDocTitleArticle.getText().toString())){
                    binding.edtDocTitleArticle.setError("Title is required!");
                    binding.edtDocTitleArticle.requestFocus();
                    return;
                }
                postArticle(
                        tempFullName,
                        binding.edtDocTitleArticle.getText().toString(),
                        binding.edtDocContentArticle.getText().toString()
                );
                binding.imgArticle.setImageBitmap(null);
                binding.edtDocTitleArticle.setText("");
                binding.edtDocContentArticle.setText("");
            }
        });
    }
    // GET DATAS FROM DB
    private interface FirebaseCallback{
        void onCallbackArticles(String fullname);
    }
    private void getData(FirebaseCallback f){
        DatabaseReference reference = FirebaseDatabase
                .getInstance(getResources().getString(R.string.url_db))
                .getReference().child("users");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.hasChild(uid)){
                        DataSnapshot userSnapshot = snapshot.child(uid);
                        tempFullName = userSnapshot.child("full_name").getValue().toString();
                        f.onCallbackArticles(tempFullName);
                    }
                }catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    // Insert data to firebase/storage
    private void uploadPicture(String articlekey){
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("image uploading");
        pd.show();

        StorageReference sr = storageReference.child("images/article_pictures/"+articlekey);
        sr.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "upload successfull", Toast.LENGTH_SHORT).show();
                System.out.println("berhasil");
                pd.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "upload failed", Toast.LENGTH_SHORT).show();
                System.out.println("gagal");
                pd.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("percentage: " + (int) progressPercent + "%");
            }
        });
    }
    private void postArticle(String author,String title, String content){
        binding.progressBarArticle.setVisibility(View.VISIBLE);
        DatabaseReference reference = FirebaseDatabase
                .getInstance(getResources().getString(R.string.url_db))
                .getReference("articles");

        Date date = Calendar. getInstance(). getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd, hh:mm");
        String strDate = dateFormat. format(date);

        DatabaseReference pushedRef = reference.push();
        String key = pushedRef.getKey();
        Article a = new Article(key,uid,author,title,content,strDate,0,0,null);
        reference.child(key).setValue(a).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getActivity(), "article berhasil di post", Toast.LENGTH_SHORT).show();
                binding.progressBarArticle.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "article gagal di post", Toast.LENGTH_SHORT).show();
                binding.progressBarArticle.setVisibility(View.GONE);
            }
        });

        if (imageUri!=null){
            uploadPicture(key);
        }
        imageUri=null;
        binding.progressBarArticle.setVisibility(View.GONE);
    }

}