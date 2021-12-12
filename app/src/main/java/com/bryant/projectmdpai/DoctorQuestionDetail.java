package com.bryant.projectmdpai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bryant.projectmdpai.Adapter.AnswerAdapter;
import com.bryant.projectmdpai.Class.Answer;
import com.bryant.projectmdpai.Class.Question;
import com.bryant.projectmdpai.Class.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DoctorQuestionDetail extends AppCompatActivity {
    Intent secondIntent;
    ArrayList<Answer> answers = new ArrayList<>();
    ArrayList<User> listUser = new ArrayList<>();
    Question question;
    TextView txtTitle, txtQuestion, txtAuthor, txtDate;
    EditText edtAnswer;
    String uid;
    RecyclerView rv;
    AnswerAdapter adapter;
    User userNow;

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
        edtAnswer = findViewById(R.id.edtAnswer);
        rv = findViewById(R.id.rv_doctor_answers2);
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
        Intent i = new Intent(DoctorQuestionDetail.this, DoctorHome.class);
        i.putExtra("uid",uid);
        startActivity(i);
        finish();
    }

    public void postAnswer(View v){
        if(edtAnswer.getText().toString().equals("")){
            Toast.makeText(DoctorQuestionDetail.this,
                    "Answer is still empty!",
                    Toast.LENGTH_SHORT);
        }
        else if(userNow.getStatus()==0){
            Toast.makeText(DoctorQuestionDetail.this,
                    "Akun dokter belum terverifikasi",
                    Toast.LENGTH_SHORT);
        }
        else{
            DatabaseReference reference = FirebaseDatabase
                    .getInstance(getResources().getString(R.string.url_db))
                    .getReference("answers");

            Date date = Calendar. getInstance(). getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd, hh:mm");
            String strDate = dateFormat. format(date);
            DatabaseReference pushedRef = reference.push();
            String key = pushedRef.getKey();
            Answer a = new Answer(key, question.getId(), uid,
                    edtAnswer.getText().toString(),strDate);
            reference.child(key).setValue(a).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    loadAnswers();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DoctorQuestionDetail.this,
                            "Answer gagal di post",
                            Toast.LENGTH_SHORT);
                }
            });
        }

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
                rv.setLayoutManager(new LinearLayoutManager(DoctorQuestionDetail.this));
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