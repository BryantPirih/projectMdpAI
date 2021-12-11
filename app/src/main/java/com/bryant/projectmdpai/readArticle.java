package com.bryant.projectmdpai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bryant.projectmdpai.Adapter.readAdapter;
import com.bryant.projectmdpai.Class.Article;
import com.bryant.projectmdpai.databinding.ActivityMainBinding;
import com.bryant.projectmdpai.databinding.ActivityReadArticleBinding;

import java.util.ArrayList;

public class readArticle extends AppCompatActivity {

    ArrayList<Article> articles = new ArrayList<>();
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
}