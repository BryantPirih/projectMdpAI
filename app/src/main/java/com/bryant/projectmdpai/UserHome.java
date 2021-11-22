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
                switch (item.getItemId()){
                    case R.id.itm_user_forum:
                        menu = "Forum";
                        break;
                    case R.id.itm_user_ask:
                        menu = "Ask Doctor";
                        break;
                    case R.id.itm_user_es:
                        menu = "Expert System";
                        break;
                    default:
                        menu = "Articles";
                        break;
                }

                try {
                    Fragment fragment = UserHomeFragment.newInstance(menu);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayoutUser, fragment)
                            .commit();
                }catch (Exception e){
                    Log.e("MainActivity", e.getMessage());
                }
                return true;
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

                break;
            default:
                Intent toLogin = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(toLogin);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}