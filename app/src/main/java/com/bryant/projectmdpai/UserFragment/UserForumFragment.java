package com.bryant.projectmdpai.UserFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bryant.projectmdpai.AddQuestionForum;
import com.bryant.projectmdpai.R;
import com.bryant.projectmdpai.databinding.FragmentUserForumBinding;
import com.bryant.projectmdpai.databinding.FragmentUserHomeBinding;

public class UserForumFragment extends Fragment {

    private static final String ARG_PARAM_MENU = "param-menu";

    private FragmentUserForumBinding binding;
    private String menu;

    public UserForumFragment() {
        // Required empty public constructor
    }

    public static UserForumFragment newInstance(String paramMenu) {
        UserForumFragment fragment = new UserForumFragment();
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
        binding = FragmentUserForumBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //binding.txtTitleForum.setText(menu);
        binding.btnAddQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toForum = new Intent(getContext(), AddQuestionForum.class);
                startActivity(toForum);
            }
        });
    }
}