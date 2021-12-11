package com.bryant.projectmdpai.UserFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bryant.projectmdpai.Adapter.articleAdapter;
import com.bryant.projectmdpai.Class.Article;
import com.bryant.projectmdpai.R;
import com.bryant.projectmdpai.databinding.FragmentUserHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserHomeFragment extends Fragment {

    private static final String ARG_PARAM_MENU = "param-menu";
    private FragmentUserHomeBinding binding;
    private String menu;

    ArrayList<Article> articles = new ArrayList<>();
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
    }

    private void setUpRecyclerView(){
        articles = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase
                .getInstance(getResources().getString(R.string.url_db));
        DatabaseReference reference = database.getReference("articles");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren() ) {
                    try {
                        String author = ds.child("author").getValue().toString();
                        String title = ds.child("title").getValue().toString();
                        String content = ds.child("content").getValue().toString();
                        String id = ds.child("id").getValue().toString();
                        String timeString = ds.child("timeString").getValue().toString();
                        articles.add( new Article(id,author,title,content,timeString));
                        System.out.println("Size of article : "+articles.size());
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                binding.rvDataUserHome.setLayoutManager(new LinearLayoutManager(getContext()));
                aa = new articleAdapter(articles);
                binding.rvDataUserHome.setAdapter(aa);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("failed to read article");
            }
        });
    }
}