package com.bryant.projectmdpai.DoctorFragment;

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
import com.bryant.projectmdpai.Class.comment;
import com.bryant.projectmdpai.Class.like;
import com.bryant.projectmdpai.Class.photo;
import com.bryant.projectmdpai.R;
import com.bryant.projectmdpai.databinding.FragmentDoctorHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoctorHomeFragment extends Fragment {

    private static final String ARG_PARAM_UID = "param-uid";
    private FragmentDoctorHomeBinding binding;
    private String uid;
    private Fragment fragment;

    ArrayList<Article> articles = new ArrayList<>();
    ArrayList<like> likes = new ArrayList<>();
    ArrayList<photo> photos = new ArrayList<>();
    articleAdapter aa;

    public DoctorHomeFragment() {
        // Required empty public constructor
    }

    public static DoctorHomeFragment newInstance(String paramUid) {
        DoctorHomeFragment fragment = new DoctorHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_UID, paramUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uid = getArguments().getString(ARG_PARAM_UID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDoctorHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnAddArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = DoctorWriteFragment.newInstance(uid);
                getParentFragmentManager().beginTransaction().replace(R.id.frameLayoutDoctor, fragment).commit();
            }
        });
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
                binding.rvDataDocArticle.setLayoutManager(new LinearLayoutManager(getContext()));
                aa = new articleAdapter(articles);
                binding.rvDataDocArticle.setAdapter(aa);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("failed to read article");
            }
        });
    }
}