package com.bryant.projectmdpai.UserFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bryant.projectmdpai.R;
import com.bryant.projectmdpai.databinding.FragmentUserConsultBinding;
import com.bryant.projectmdpai.databinding.FragmentUserForumBinding;

public class UserConsultFragment extends Fragment {

    private static final String ARG_PARAM_MENU = "param-menu";

    private FragmentUserConsultBinding binding;
    private String menu;


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
}