package com.sesi.miplata.view.main;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.sesi.miplata.R;
import com.sesi.miplata.databinding.ActivityMenuBinding;

public class MenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuBinding binding;
    private boolean isFABOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        loadAds();
        setSupportActionBar(binding.appBarMenu.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.FirstFragment)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_current_balance);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        binding.appBarMenu.fab.setOnClickListener(view -> {
            if (!isFABOpen){
                showFabMenu();
            } else {
                hideFabMenu();
            }
        });

        binding.appBarMenu.fabAddOpDay.setOnClickListener(v ->{
            Intent intent = new Intent(this, RegistroOperacionesMensualesActivity.class);
            startActivity(intent);
        });

        binding.appBarMenu.fabAddOpRec.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistroGastoIngresoActivity.class);
            startActivity(intent);
        });
    }

    private void loadAds(){
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.appBarMenu.content.adViewBanner.loadAd(adRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_current_balance);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void showFabMenu(){
        isFABOpen = true;
        binding.appBarMenu.fabLayoutOpDay.setVisibility(View.VISIBLE);
        binding.appBarMenu.fabLayoutOpRec.setVisibility(View.VISIBLE);

        binding.appBarMenu.fab.animate().rotationBy(-45);
        binding.appBarMenu.fabLayoutOpDay.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        binding.appBarMenu.fabLayoutOpRec.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
    }

    private void hideFabMenu(){
        isFABOpen= false;
        binding.appBarMenu.fab.animate().rotationBy(45);
        binding.appBarMenu.fabLayoutOpDay.animate().translationY(0);
        binding.appBarMenu.fabLayoutOpRec.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!isFABOpen){
                    binding.appBarMenu.fabLayoutOpDay.setVisibility(View.GONE);
                    binding.appBarMenu.fabLayoutOpRec.setVisibility(View.GONE);
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