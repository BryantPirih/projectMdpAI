package com.bryant.projectmdpai.DoctorFragment;

import android.annotation.SuppressLint;
import android.net.Uri;
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
import com.bryant.projectmdpai.Class.User;
import com.bryant.projectmdpai.Class.comment;
import com.bryant.projectmdpai.Class.like;
import com.bryant.projectmdpai.Class.photo;
import com.bryant.projectmdpai.R;
import com.bryant.projectmdpai.databinding.FragmentDoctorHomeBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DoctorHomeFragment extends Fragment {

    private static final String ARG_PARAM_UID = "param-uid";
    private FragmentDoctorHomeBinding binding;
    private String uid;
    private Fragment fragment;

    ArrayList<Article> articles = new ArrayList<>();
    ArrayList<User> listUser = new ArrayList<>();
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
                        String id_author = ds.child("id_author").getValue().toString();
                        String timeString = ds.child("timeString").getValue().toString();
                        int jl = Integer.parseInt(ds.child("jumlahLike").getValue().toString()) ;
                        int jc = Integer.parseInt(ds.child("jumlahComment").getValue().toString());
                        articles.add( new Article(id,id_author,author,title,content,timeString,jl,jc,null));
                        System.out.println("Size of article : "+articles.size());
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                try {
                    FirebaseDatabase database = FirebaseDatabase
                            .getInstance(getResources().getString(R.string.url_db));
                    DatabaseReference ref = database.getReference("users");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot childSnapshot : snapshot.getChildren()){
                                User u = (User)childSnapshot.getValue(User.class);
                                listUser.add(u);
                            }
                            binding.rvDataDocArticle.setLayoutManager(new LinearLayoutManager(getContext()));
                            aa = new articleAdapter(articles,listUser);
                            binding.rvDataDocArticle.setAdapter(aa);
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
    private void getData(){

    }
    private void getProfilePicture(){
//        StorageReference storageRef = FirebaseStorage
//                .getInstance(getResources().getString(R.string.url_storage))
//                .getReference().child("images");
    }
    private interface FirebaseCallback{
        void onCallbackArticles(ArrayList<Article> a);
        void onCallbackProfilePicture(String fullname);
    }





}