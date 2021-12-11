package com.bryant.projectmdpai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bryant.projectmdpai.Class.Article;
import com.bryant.projectmdpai.Class.Question;
import com.bryant.projectmdpai.Class.User;
import com.bryant.projectmdpai.databinding.ActivityAddQuestionForumBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddQuestionForum extends AppCompatActivity {

    private ActivityAddQuestionForumBinding binding;
    Intent secondIntent;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddQuestionForumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        secondIntent = getIntent();
        if(secondIntent.hasExtra("uid")){
            uid = secondIntent.getStringExtra("uid");
            System.out.println(uid);
        }

        binding.btnPostQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question = binding.edtQuestionForum.getText().toString();
                String title = binding.edtTitleForum.getText().toString();
                if(question.equals("")||title.equals("")){
                    Toast.makeText(AddQuestionForum.this, "Isi dahulu semua field yang tersedia", Toast.LENGTH_SHORT).show();
                }
                else{
                    binding.progressBarQuestionForum.setVisibility(View.VISIBLE);
                    DatabaseReference reference = FirebaseDatabase
                            .getInstance(getResources().getString(R.string.url_db))
                            .getReference("questions");

                    Date date = Calendar. getInstance(). getTime();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd, hh:mm");
                    String strDate = dateFormat. format(date);

                    DatabaseReference pushedRef = reference.push();
                    String key = pushedRef.getKey();
                    Question q = new Question(key,uid,title,question, strDate);
                    reference.child(key).setValue(q).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            binding.progressBarQuestionForum.setVisibility(View.GONE);
                            Intent toHome = new Intent(getApplicationContext(), UserHome.class);
                            toHome.putExtra("uid",uid);
                            startActivity(toHome);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddQuestionForum.this, "Question gagal di post", Toast.LENGTH_SHORT).show();
                            binding.progressBarQuestionForum.setVisibility(View.GONE);
                        }
                    });



                }

            }
        });
    }
}