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

import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bryant.projectmdpai.Class.Article;
import com.bryant.projectmdpai.Class.ExpertSystem.QuestionES;
import com.bryant.projectmdpai.Class.ExpertSystem.Rules;
import com.bryant.projectmdpai.Class.ExpertSystem.Solution;
import com.bryant.projectmdpai.R;
import com.bryant.projectmdpai.UserFragment.UserExpertFragment;
import com.bryant.projectmdpai.databinding.FragmentDoctorWriteBinding;
import com.google.android.gms.auth.api.signin.internal.Storage;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class DoctorWriteFragment extends Fragment {


    private static final String ARG_PARAM_UID = "param-uid";
    private String uid;

    private FragmentDoctorWriteBinding binding;
    private ArrayList<Article> articles = new ArrayList<>();
    private FirebaseStorage storage;
    private StorageReference reference;
    private Uri imageUri;
    private String tempFullName;
    private FirebaseDatabase database;
    private DatabaseReference dbReference;

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
        if (getArguments() != null) {
            uid = getArguments().getString(ARG_PARAM_UID);
        }
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        imageUri=null;

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

//        readData(new FirebaseCallback() {
//            @Override
//            public void onCallbackArticles(ArrayList<Article> list) {
//                articles = list;
//                isDataReady();
//            }
//        });


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

                binding.progressBarArticle.setVisibility(View.VISIBLE);
                database = FirebaseDatabase.getInstance();
                dbReference = database.getReference("article");
                tempFullName="";
                getData(new FirebaseCallback() {
                    @Override
                    public void onCallbackArticles(String fullname) {
                        tempFullName = fullname;
                        Toast.makeText(getActivity(), "masuk", Toast.LENGTH_SHORT).show();
                        postArticle(
                                fullname,
                                binding.edtDocTitleArticle.getText().toString(),
                                binding.edtDocContentArticle.getText().toString()
                        );
                    }
                });
                dbReference.setValue("");
                if (imageUri!=null){
                    uploadPicture();
                }
                imageUri=null;
                System.out.println("berhasil");
                binding.progressBarArticle.setVisibility(View.GONE);
            }
        });
    }
    private boolean isDataReady(){
        if (articles.isEmpty()){
            return false;
        }
        return true;
    }
    // GET DATAS FROM DB

    private void readData(FirebaseCallback firebaseCallback){
        //articles
        DatabaseReference root = FirebaseDatabase
                .getInstance(getActivity().getResources().getString(R.string.url_db))
                .getReference();
        DatabaseReference questionsRef = root.child("articles");
        questionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Loading articles...");
                for (DataSnapshot ds: dataSnapshot.getChildren() ) {
                    try {
                        int id = (int) ds.child("id").getValue();
                        int author = (int) ds.child("user_id").getValue();
                        String desc = ds.child("desc").getValue().toString();
                        String title = ds.child("title").getValue().toString();
                        //articles.add(new Article(id, author, desc, title));
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                //firebaseCallback.onCallbackArticles(articles);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
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
    private void uploadPicture(){

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("image uploading");
        pd.show();

        StorageReference sr = reference.child("images/article_pictures/"+2);
        sr.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "upload successfull", Toast.LENGTH_SHORT).show();
                System.out.println("berhasil");
                pd.dismiss();
                //Toast.makeText(getActivity(), "masuk1", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "upload failed", Toast.LENGTH_SHORT).show();
                System.out.println("gagal");
                pd.dismiss();
                //Toast.makeText(getActivity(), "masuk1", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("percentage: " + (int) progressPercent + "%");
                //Toast.makeText(getActivity(), "masuk1", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void postArticle(String author,String title, String content){
        DatabaseReference reference = FirebaseDatabase
                .getInstance(getResources().getString(R.string.url_db))
                .getReference().child("article");

        String  currentDateTimeString = DateFormat.getDateTimeInstance()
                .format(new Date());
        Article a = new Article(1+"",author,title,content,currentDateTimeString);
        reference.push().setValue(a);
        Toast.makeText(getActivity(), "masuk", Toast.LENGTH_SHORT).show();
    }

}