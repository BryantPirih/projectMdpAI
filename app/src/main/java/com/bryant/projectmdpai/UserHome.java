package com.bryant.projectmdpai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bryant.projectmdpai.Class.User;
import com.bryant.projectmdpai.UserFragment.UserConsultFragment;
import com.bryant.projectmdpai.UserFragment.UserExpertFragment;
import com.bryant.projectmdpai.UserFragment.UserForumFragment;
import com.bryant.projectmdpai.UserFragment.UserHomeFragment;
import com.bryant.projectmdpai.databinding.ActivityUserHomeBinding;
import com.google.android.material.navigation.NavigationBarView;

public class UserHome extends AppCompatActivity {

    private ActivityUserHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottNavUser.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String menu;
                Fragment fragment;
                if (item.getItemId() == R.id.itm_user_article){
                    menu = "Articles";
                    fragment = UserHomeFragment.newInstance(menu);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutUser, fragment).commit();
                    return true;
                }else if (item.getItemId() == R.id.itm_user_forum){
                    menu = "Forum";
                    fragment = UserForumFragment.newInstance(menu);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutUser, fragment).commit();
                    return true;
                }else if (item.getItemId() == R.id.itm_user_ask){
                    menu = "Ask Doctor";
                    fragment = UserConsultFragment.newInstance(menu);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutUser, fragment).commit();
                    return true;
                }else{
                    menu = "Expert System";
                    fragment = UserExpertFragment.newInstance(menu);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutUser, fragment).commit();
                    return true;
                }
            }
        });

        if (savedInstanceState == null){
            binding.bottNavUser.setSelectedItemId(R.id.itm_user_article);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.option_home:

                break;
            case R.id.option_profile:
                    Intent i = new Intent(getApplicationContext(),profile.class);
                    startActivity(i);
                    finish();
                break;
            default:
                Intent toLogin = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(toLogin);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}