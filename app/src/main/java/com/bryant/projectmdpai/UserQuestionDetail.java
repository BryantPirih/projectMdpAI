package com.bryant.projectmdpai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bryant.projectmdpai.Adapter.AnswerAdapter;
import com.bryant.projectmdpai.Adapter.DoctorVerifAdapter;
import com.bryant.projectmdpai.Class.Answer;
import com.bryant.projectmdpai.Class.Question;
import com.bryant.projectmdpai.Class.User;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserQuestionDetail extends AppCompatActivity {
    Intent secondIntent;
    ArrayList<Answer> answers = new ArrayList<>();
    Question question;
    ArrayList<User> listUser = new ArrayList<>();
    TextView txtTitle, txtQuestion, txtAuthor, txtDate;
    String uid;
    RecyclerView rv;
    AnswerAdapter adapter;
    User userNow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_question_detail);
        secondIntent = getIntent();
        if(secondIntent.hasExtra("question")){
            question = secondIntent.getParcelableExtra("question");
        }
        if(secondIntent.hasExtra("uid")){
            uid = secondIntent.getStringExtra("uid");
        }
        rv = findViewById(R.id.rv_doctor_answers);
        txtTitle = findViewById(R.id.txt_question_detail_title);
        txtQuestion = findViewById(R.id.txt_question_detail_question);
        txtAuthor = findViewById(R.id.txt_question_detail_author);
        txtDate = findViewById(R.id.txt_question_detail_date);
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
                    if(u.getRole().equals("Dokter")){
                        listUser.add(u);
                    }
                    if(u.getId().equals(uid)){
                        txtAuthor.setText(u.getFull_name());
                        userNow = u;
                    }
                }
                loadAnswers();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Fail to read");
            }
        });
    }

    public void backToHome(View v){
        Intent i = new Intent(UserQuestionDetail.this, UserHome.class);
        i.putExtra("uid",uid);
        startActivity(i);
        finish();
    }

    public void loadAnswers(){
        answers = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase
                .getInstance(getResources().getString(R.string.url_db));
        DatabaseReference ref = database.getReference("answers");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    Answer a = childSnapshot.getValue(Answer.class);
                    if(a.getQuestion_id().equals(question.getId())){
                        answers.add(a);
                    }
                }
                rv.setLayoutManager(new LinearLayoutManager(UserQuestionDetail.this));
                adapter = new AnswerAdapter(answers, listUser);
                rv.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Fail to read");
            }
        });
    }
}