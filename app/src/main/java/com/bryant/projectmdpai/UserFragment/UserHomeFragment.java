package com.bryant.projectmdpai.UserFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bryant.projectmdpai.Adapter.articleAdapter;
import com.bryant.projectmdpai.Class.Article;
import com.bryant.projectmdpai.Class.ExpertSystem.QuestionES;
import com.bryant.projectmdpai.Class.ExpertSystem.Rules;
import com.bryant.projectmdpai.Class.ExpertSystem.Solution;
import com.bryant.projectmdpai.Class.likeComments;
import com.bryant.projectmdpai.R;
import com.bryant.projectmdpai.databinding.FragmentUserHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class UserHomeFragment extends Fragment {
    private static final String ARG_PARAM_MENU = "param-menu";

    private FragmentUserHomeBinding binding;
    private String menu;
    ArrayList<Article> articles = new ArrayList<>();
    ArrayList<likeComments> LikeComments = new ArrayList<>();

    DatabaseReference database;
    articleAdapter aa;

    public UserHomeFragment() {
        // Required empty public constructor
    }

    public static UserHomeFragment newInstance(String paramMenu) {
        UserHomeFragment fragment = new UserHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_MENU, paramMenu);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            menu = getArguments().getString(ARG_PARAM_MENU);
        }

        database = FirebaseDatabase.getInstance().getReference("articles");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapShot: snapshot.getChildren()) {
                    Article a = dataSnapShot.getValue(Article.class);
                    articles.add(a);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database = FirebaseDatabase.getInstance().getReference("likeComments");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapShot: snapshot.getChildren()) {
                    likeComments l = dataSnapShot.getValue(likeComments.class);
                    LikeComments.add(l);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
        //binding.txtTitle.setText(menu);
    }

    void setUpRecyclerView(){
        binding.rvDataUserHome.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvDataUserHome.setHasFixedSize(true);

        aa = new articleAdapter(articles,LikeComments);

        binding.rvDataUserHome.setAdapter(aa);
    }

    private void getData(FirebaseCallback firebaseCallback){
        DatabaseReference root = FirebaseDatabase
                .getInstance(getResources().getString(R.string.url_db))
                .getReference();
        DatabaseReference articleRef = root.child("");
    }

    private interface FirebaseCallback{
        void onCallbackRules(ArrayList<Rules> list);
        void onCallbackQuestion(ArrayList<QuestionES> list);
        void onCallbackSolution(ArrayList<Solution> list);
    }
}