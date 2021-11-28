package com.bryant.projectmdpai.DoctorFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bryant.projectmdpai.R;
import com.bryant.projectmdpai.UserFragment.UserHomeFragment;
import com.bryant.projectmdpai.databinding.FragmentDoctorHomeBinding;

public class DoctorHomeFragment extends Fragment {

    private FragmentDoctorHomeBinding binding;
    private String menu;
    private Fragment fragment;

    public DoctorHomeFragment() {
        // Required empty public constructor
    }

    public static DoctorHomeFragment newInstance() {
        DoctorHomeFragment fragment = new DoctorHomeFragment();
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
        binding = FragmentDoctorHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnAddArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = DoctorWriteFragment.newInstance();
                getParentFragmentManager().beginTransaction().replace(R.id.frameLayoutDoctor, fragment).commit();
            }
        });
    }
}