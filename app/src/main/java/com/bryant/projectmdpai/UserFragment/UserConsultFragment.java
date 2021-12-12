package com.bryant.projectmdpai.UserFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.bryant.projectmdpai.Adapter.DoctorListAdapter;
import com.bryant.projectmdpai.Adapter.UserListAdapter;
import com.bryant.projectmdpai.Class.User;
import com.bryant.projectmdpai.R;
import com.bryant.projectmdpai.databinding.FragmentUserConsultBinding;
import com.bryant.projectmdpai.databinding.FragmentUserForumBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserConsultFragment extends Fragment {

    private static final String ARG_PARAM_MENU = "param-menu";

    private FragmentUserConsultBinding binding;
    private String menu;
    DoctorListAdapter adapter;
    ArrayList<User> listUser;


    public UserConsultFragment() {
        // Required empty public constructor
    }

    public static UserConsultFragment newInstance(String paramMenu) {
        UserConsultFragment fragment = new UserConsultFragment();
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
        binding = FragmentUserConsultBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSearchAskDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.searchAskDoctor.setIconifiedByDefault(false);
                binding.searchAskDoctor.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        //Toast.makeText(getContext(), "keyword : " + s, Toast.LENGTH_SHORT).show();
                        adapter.search(s);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        // dipanggil ketika terjadi perubahan text keyword di SearchView
                        if (TextUtils.isEmpty(s)){
                            adapter.showAll();
                        }
                        return true;
                    }
                });
            }
        });


        listUser = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase
                .getInstance(getResources().getString(R.string.url_db));
        DatabaseReference ref = database.getReference("users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    User u = (User)childSnapshot.getValue(User.class);
                    if(u.getRole().equals("Dokter")){
                        System.out.println(u.getFull_name());
                        listUser.add(u);
                        System.out.println("Size of list : "+listUser.size());
                    }

                }
                binding.rvDataAskDoctor.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new DoctorListAdapter(listUser);
                binding.rvDataAskDoctor.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Fail to read");
            }
        });
    }
}