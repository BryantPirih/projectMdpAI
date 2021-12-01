package com.bryant.projectmdpai.DoctorFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bryant.projectmdpai.Class.Article;
import com.bryant.projectmdpai.Class.ExpertSystem.QuestionES;
import com.bryant.projectmdpai.Class.ExpertSystem.Rules;
import com.bryant.projectmdpai.Class.ExpertSystem.Solution;
import com.bryant.projectmdpai.R;
import com.bryant.projectmdpai.UserFragment.UserExpertFragment;
import com.bryant.projectmdpai.databinding.FragmentDoctorWriteBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoctorWriteFragment extends Fragment {

    private FragmentDoctorWriteBinding binding;
    private ArrayList<Article> articles = new ArrayList<>();

    public DoctorWriteFragment() {
        // Required empty public constructor
    }

    public static DoctorWriteFragment newInstance() {
        DoctorWriteFragment fragment = new DoctorWriteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDoctorWriteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        readData(new FirebaseCallback() {
            @Override
            public void onCallbackArticles(ArrayList<Article> list) {
                articles = list;
                isDataReady();
            }
        });
    }

    private boolean isDataReady(){
        if (articles.isEmpty()){
            return false;
        }
        return true;
    }


    // GET DATAS FROM DB
    private void readData(FirebaseCallback firebaseCallback){
        //articles
        DatabaseReference root = FirebaseDatabase
                .getInstance(getActivity().getResources().getString(R.string.url_db))
                .getReference();
        DatabaseReference questionsRef = root.child("articles");
        questionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Loading articles...");
                for (DataSnapshot ds: dataSnapshot.getChildren() ) {
                    try {
                        int id = (int) ds.child("id").getValue();
                        int author = (int) ds.child("user_id").getValue();
                        String desc = ds.child("desc").getValue().toString();
                        String title = ds.child("title").getValue().toString();
                        articles.add(new Article(id, author, desc, title));
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                firebaseCallback.onCallbackArticles(articles);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private interface FirebaseCallback{
        void onCallbackArticles(ArrayList<Article> list);
    }
}