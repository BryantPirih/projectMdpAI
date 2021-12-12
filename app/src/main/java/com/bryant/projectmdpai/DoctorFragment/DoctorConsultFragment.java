package com.bryant.projectmdpai.DoctorFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bryant.projectmdpai.Adapter.DoctorQuestionAdapter;
import com.bryant.projectmdpai.Adapter.QuestionForumAdapter;
import com.bryant.projectmdpai.Class.Question;
import com.bryant.projectmdpai.Class.User;
import com.bryant.projectmdpai.DoctorQuestionDetail;
import com.bryant.projectmdpai.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorConsultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorConsultFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM_UID = "param-uid";

    // TODO: Rename and change types of parameters
    private String uid;
    private String mParam2;
    ArrayList<Question> listQuestion = new ArrayList<>();
    ArrayList<User> listUser = new ArrayList<>();
    RecyclerView rv;
    DoctorQuestionAdapter dqa;

    public DoctorConsultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DoctorConsultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DoctorConsultFragment newInstance(String uid) {
        DoctorConsultFragment fragment = new DoctorConsultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_UID, uid);
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_consult, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.rvQuestionForum2);

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

                            rv.setLayoutManager(new LinearLayoutManager(getContext()));
                            dqa = new DoctorQuestionAdapter(listQuestion, listUser);
                            rv.setAdapter(dqa);
                            dqa.setOnItemClickCallback(new QuestionForumAdapter.OnItemClickCallback() {
                                @Override
                                public void onItemClicked(Question question) {
                                    System.out.println(question.getId());
                                    Intent i = new Intent(getContext(), DoctorQuestionDetail.class);
                                    i.putExtra("question",question);
                                    i.putExtra("uid",uid);
                                    startActivity(i);
                                    getActivity().finish();
                                }
                            });

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
}