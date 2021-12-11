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
import com.bryant.projectmdpai.AddQuestionForum;
import com.bryant.projectmdpai.Class.Article;
import com.bryant.projectmdpai.Class.Question;
import com.bryant.projectmdpai.Class.User;
import com.bryant.projectmdpai.R;
import com.bryant.projectmdpai.databinding.FragmentUserForumBinding;
import com.bryant.projectmdpai.databinding.FragmentUserHomeBinding;
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

public class UserForumFragment extends Fragment {

    private static final String ARG_PARAM_MENU = "param-menu";
    private static final String ARG_PARAM_UID = "param-uid";

    private FragmentUserForumBinding binding;
    private String menu;
    private String uid;
    ArrayList<Question> listQuestion = new ArrayList<>();
    ArrayList<User> listUser = new ArrayList<>();
    QuestionForumAdapter qa;

    public UserForumFragment() {
        // Required empty public constructor
    }

    public static UserForumFragment newInstance(String paramMenu, String paramUID) {
        UserForumFragment fragment = new UserForumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_MENU, paramMenu);
        args.putString(ARG_PARAM_UID, paramUID);
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
        binding = FragmentUserForumBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase
                .getInstance(getResources().getString(R.string.url_db));
        DatabaseReference reference = database.getReference("questions");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    Question q = childSnapshot.getValue(Question.class);
                        listQuestion.add(q);
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
                                if(u.getRole().equals("User")){
                                    listUser.add(u);
                                }
                            }

                            binding.rvDataUserForum.setLayoutManager(new LinearLayoutManager(getContext()));
                            qa = new QuestionForumAdapter(listQuestion, listUser);
                            binding.rvDataUserForum.setAdapter(qa);
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

        //binding.txtTitleForum.setText(menu);
        binding.btnAddQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent toForum = new Intent(getContext(), AddQuestionForum.class);
                toForum.putExtra("uid",uid);
                startActivity(toForum);
            }
        });
    }
}