package com.sesi.miplata.view;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.sesi.miplata.R;
import com.sesi.miplata.databinding.ActivityCurrentBalanceBinding;

public class CurrentBalanceActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityCurrentBalanceBinding binding;
    private boolean isFABOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCurrentBalanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_current_balance);
        //appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.FirstFragment,R.id.SecondFragment).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen){
                    showFabMenu();
                } else {
                    hideFabMenu();
                }
            }
        });
        binding.fabAddOpDay.setOnClickListener(v ->{
            Intent intent = new Intent(this,RegistroGastoIngresoActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_current_balance);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void showFabMenu(){
        isFABOpen = true;
        binding.fabLayoutOpDay.setVisibility(View.VISIBLE);
        binding.fabLayoutOpRec.setVisibility(View.VISIBLE);

        binding.fab.animate().rotationBy(-45);
        binding.fabLayoutOpDay.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        binding.fabLayoutOpRec.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
    }

    private void hideFabMenu(){
        isFABOpen= false;
        binding.fab.animate().rotationBy(45);
        binding.fabLayoutOpDay.animate().translationY(0);
        binding.fabLayoutOpRec.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!isFABOpen){
                    binding.fabLayoutOpDay.setVisibility(View.GONE);
                    binding.fabLayoutOpRec.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
}