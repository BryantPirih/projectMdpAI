package com.bryant.projectmdpai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bryant.projectmdpai.Adapter.articleAdapter;
import com.bryant.projectmdpai.Adapter.readAdapter;
import com.bryant.projectmdpai.Class.Article;
import com.bryant.projectmdpai.Class.User;
import com.bryant.projectmdpai.Class.like;
import com.bryant.projectmdpai.databinding.ActivityMainBinding;
import com.bryant.projectmdpai.databinding.ActivityReadArticleBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class readArticle extends AppCompatActivity {

    ArrayList<Article> articles = new ArrayList<>();
    ArrayList<like> likes = new ArrayList<>();
    private String uid;

    private ActivityReadArticleBinding binding;
    readAdapter ra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReadArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent.hasExtra("pass")){
            articles.add(intent.getParcelableExtra("pass"));
            for (int j = 0; j < articles.size(); j++) {
                Toast.makeText(this, articles.get(j).getContent(), Toast.LENGTH_SHORT).show();
            }
        }
        if (intent.hasExtra("uid")){
            uid = intent.getStringExtra("uid");
            Toast.makeText(this, uid+"", Toast.LENGTH_SHORT).show();
        }

        binding.rvArticleRead.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ra = new readAdapter(articles);
        binding.rvArticleRead.setAdapter(ra);


    }

    public void btnLike_Clicked(View view) {

    }
    public void btnComment_Clicked(View view) {

    }
    private void likeArticle(){

    }
    private void getLike(){

    }
    private interface FirebaseCallback{
        void onCallbackArticles(ArrayList<Article> a);
        void onCallbackLike(ArrayList<like> l);
    }
    private void getLike(FirebaseCallback f){
        articles = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase
                .getInstance(getResources().getString(R.string.url_db));
        DatabaseReference reference = database.getReference("like");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren() ) {
                    try {
                        String id_article = ds.child("id_article").getValue().toString();
                        String id_user = ds.child("id_user").getValue().toString();
                        likes.add( new like(id_article,id_user));
                        System.out.println("Size of likes : "+likes.size());
                        f.onCallbackLike(likes);
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("failed to read article");
            }
        });
    }
}