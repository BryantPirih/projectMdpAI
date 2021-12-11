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

import com.bryant.projectmdpai.Adapter.QuestionForumAdapter;
import com.bryant.projectmdpai.Adapter.articleAdapter;
import com.bryant.projectmdpai.Class.Article;
import com.bryant.projectmdpai.Class.User;
import com.bryant.projectmdpai.R;
import com.bryant.projectmdpai.databinding.FragmentUserHomeBinding;
import com.bryant.projectmdpai.readArticle;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserHomeFragment extends Fragment {

    private static final String ARG_PARAM_MENU = "param-menu";
    private static final String ARG_PARAM_UID = "param-uid";
    private FragmentUserHomeBinding binding;
    private String menu;
    private String uid;

    ArrayList<Article> articles = new ArrayList<>();
    ArrayList<User> listUser = new ArrayList<>();
    articleAdapter aa;

    public UserHomeFragment() {
        // Required empty public constructor
    }

    public static UserHomeFragment newInstance(String paramMenu,String paramUid) {
        UserHomeFragment fragment = new UserHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_MENU, paramMenu);
        args.putString(ARG_PARAM_UID, paramUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            menu = getArguments().getString(ARG_PARAM_MENU);
            uid = getArguments().getString(ARG_PARAM_UID);
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

        getData(new FirebaseCallback() {
            @Override
            public void onCallbackArticles(ArrayList<Article> a) {
                articles = a;
                setUpRecyclerView();
            }
        });
    }
    private void setUpRecyclerView(){
        binding.rvDataUserHome.setLayoutManager(new LinearLayoutManager(getContext()));
        aa = new articleAdapter(articles/*,listUser*/);
        aa.setOnItemClickCallback(new articleAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Article article) {
                Intent i = new Intent(getContext(), readArticle.class);
                i.putExtra("pass",article);
                i.putExtra("uid",uid);
                System.out.println(article.getContent());
                startActivity(i);
            }
        });
        binding.rvDataUserHome.setAdapter(aa);
    }
    private void getData(FirebaseCallback f){
        articles = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase
                .getInstance(getActivity().getResources().getString(R.string.url_db));
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
                        String id_author = ds.child("id_author").getValue().toString();
                        String timeString = ds.child("timeString").getValue().toString();
                        int jl = Integer.parseInt(ds.child("jumlahLike").getValue().toString()) ;
                        int jc = Integer.parseInt(ds.child("jumlahComment").getValue().toString());
                        articles.add( new Article(id,id_author,author,title,content,timeString,jl,jc,null));
                        System.out.println("Size of article : "+articles.size());
                        f.onCallbackArticles(articles);
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                try {
                    FirebaseDatabase database = FirebaseDatabase
                            .getInstance(getActivity().getResources().getString(R.string.url_db));
                    DatabaseReference ref = database.getReference("users");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot childSnapshot : snapshot.getChildren()){
                                User u = (User)childSnapshot.getValue(User.class);
                                listUser.add(u);
                            }
                            binding.rvDataUserHome.setLayoutManager(new LinearLayoutManager(getContext()));
                            aa = new articleAdapter(articles/*,listUser*/);
                            binding.rvDataUserHome.setAdapter(aa);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            System.out.println("Fail to read");
                        }
                    });
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("failed to read article");
            }
        });
    }
    private interface FirebaseCallback{
        void onCallbackArticles(ArrayList<Article> a);
    }
}