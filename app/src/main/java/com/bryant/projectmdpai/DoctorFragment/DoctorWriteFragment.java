package com.bryant.projectmdpai.DoctorFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bryant.projectmdpai.R;
import com.bryant.projectmdpai.databinding.FragmentDoctorWriteBinding;

public class DoctorWriteFragment extends Fragment {

    private FragmentDoctorWriteBinding binding;

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
    }
}