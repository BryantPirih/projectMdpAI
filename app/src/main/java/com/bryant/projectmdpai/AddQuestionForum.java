package com.bryant.projectmdpai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bryant.projectmdpai.Class.User;
import com.bryant.projectmdpai.databinding.ActivityAddQuestionForumBinding;

public class AddQuestionForum extends AppCompatActivity {

    private ActivityAddQuestionForumBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddQuestionForumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnPostQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHome = new Intent(getApplicationContext(), UserHome.class);
                startActivity(toHome);
            }
        });
    }
}