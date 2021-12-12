package com.bryant.projectmdpai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bryant.projectmdpai.Class.Answer;
import com.bryant.projectmdpai.Class.Question;
import com.bryant.projectmdpai.Class.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoctorQuestionDetail extends AppCompatActivity {
    Intent secondIntent;
    ArrayList<Answer> answers = new ArrayList<>();
    Question question;
    TextView txtTitle, txtQuestion, txtAuthor, txtDate;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_question_detail);
        secondIntent = getIntent();
        if(secondIntent.hasExtra("question")){
            question = secondIntent.getParcelableExtra("question");
        }
        if(secondIntent.hasExtra("uid")){
            uid = secondIntent.getStringExtra("uid");
        }
        txtTitle = findViewById(R.id.txt_question_detail_title2);
        txtQuestion = findViewById(R.id.txt_question_detail_question2);
        txtAuthor = findViewById(R.id.txt_question_detail_author2);
        txtDate = findViewById(R.id.txt_question_detail_date2);
        txtTitle.setText(question.getTitle());
        txtQuestion.setText(question.getQuestion());
        txtDate.setText(question.getTime());
        FirebaseDatabase database = FirebaseDatabase
                .getInstance(getResources().getString(R.string.url_db));
        DatabaseReference ref = database.getReference("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    User u = childSnapshot.getValue(User.class);
                    if(u.getId().equals(uid)){
                        txtAuthor.setText(u.getFull_name());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Fail to read");
            }
        });
    }

    public void backToHome(View v){
        Intent i = new Intent(DoctorQuestionDetail.this, DoctorHome.class);
        i.putExtra("uid",uid);
        startActivity(i);
        finish();
    }
}