package com.bryant.projectmdpai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bryant.projectmdpai.DoctorFragment.DoctorConsultFragment;
import com.bryant.projectmdpai.DoctorFragment.DoctorHomeFragment;
import com.bryant.projectmdpai.UserFragment.UserConsultFragment;
import com.bryant.projectmdpai.UserFragment.UserExpertFragment;
import com.bryant.projectmdpai.UserFragment.UserForumFragment;
import com.bryant.projectmdpai.UserFragment.UserHomeFragment;
import com.bryant.projectmdpai.databinding.ActivityDoctorHomeBinding;
import com.google.android.material.navigation.NavigationBarView;

public class DoctorHome extends AppCompatActivity {

    private ActivityDoctorHomeBinding binding;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent().hasExtra("uid")){
            uid=getIntent().getStringExtra("uid");
        }
        Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();

        binding.bottNavDoctor.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                if (item.getItemId() == R.id.itm_doc_article){
                    fragment = DoctorHomeFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutDoctor, fragment).commit();
                    return true;
                }else if (item.getItemId() == R.id.itm_doc_forum){
                    fragment = DoctorConsultFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutDoctor, fragment).commit();
                    return true;
                }else{
                    fragment = DoctorHomeFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutDoctor, fragment).commit();
                    return true;
                }
            }
        });

        if (savedInstanceState == null){
            binding.bottNavDoctor.setSelectedItemId(R.id.itm_doc_article);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.option_home:
                intent = new Intent(getApplicationContext(), DoctorHome.class);
                break;
            case R.id.option_profile:
                intent = new Intent(getApplicationContext(), DoctorProfile.class);
                break;
            default:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                break;
        }
        intent.putExtra("uid",uid);
        startActivity(intent);
        finish();
        return super.onOptionsItemSelected(item);
    }
}