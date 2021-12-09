package com.bryant.projectmdpai.AdminFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bryant.projectmdpai.Adapter.DoctorVerifAdapter;
import com.bryant.projectmdpai.Adapter.UserListAdapter;
import com.bryant.projectmdpai.AdminHome;
import com.bryant.projectmdpai.Class.User;
import com.bryant.projectmdpai.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminRequestFragment extends Fragment {


    ArrayList<User> unverifiedDoctors;
    RecyclerView rv;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AdminRequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminRequestFragment newInstance() {
        AdminRequestFragment fragment = new AdminRequestFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.rv_list_req_doctor);
        unverifiedDoctors = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase
                .getInstance(getResources().getString(R.string.url_db));
        DatabaseReference ref = database.getReference("users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    User u = childSnapshot.getValue(User.class);
                    if(u.getRole().equals("Dokter")&&u.getStatus()==0){
                        System.out.println(u.getFull_name());
                        unverifiedDoctors.add(u);
                        System.out.println("Size of list : "+unverifiedDoctors.size());
                    }
                }
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                DoctorVerifAdapter adapter = new DoctorVerifAdapter(unverifiedDoctors);
                rv.setAdapter(adapter);
                adapter.setOnItemClickCallback(new DoctorVerifAdapter.OnItemClickCallback() {
                    @Override
                    public void onItemClicked(User user) {
                        Toast.makeText(getContext(), user.getFull_name(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Fail to read");
            }
        });



    }
}