package com.sesi.miplata.view.main;

import android.Manifest;
import android.animation.Animator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigation.NavigationView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.sesi.miplata.R;
import com.sesi.miplata.data.preference.StoragePreference;
import com.sesi.miplata.databinding.ActivityMenuBinding;
import com.sesi.miplata.schedule.JobManager;
import com.sesi.miplata.view.BaseActivity;
import com.sesi.miplata.view.dialog.DialogNotification;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MenuActivity extends BaseActivity implements DialogNotification.OnAction {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuBinding binding;
    private boolean isFABOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadAds();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission();
        } else {
            if (StoragePreference.INSTANCE.isFirstTime(getApplicationContext())) {
                createNotificationJobs();
                StoragePreference.INSTANCE.setData(StoragePreference.FIRST_TIME, false, getApplicationContext());
            }
        }
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
            Intent intent = new Intent(this, RegistroOperacionesDayliActivity.class);
            startActivity(intent);
        });

        binding.appBarMenu.fabAddOpRec.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistroGastoIngresoActivity.class);
            startActivity(intent);
        });
    }



    private void loadAds(){
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.appBarMenu.adViewBanner.loadAd(adRequest);
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

    private final ActivityResultLauncher<String> requestNotification = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    createNotificationJobs();
                }
            });

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void requestNotificationPermission(){
        int[] permissions = {ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS)};
        if (permissions[0] != PackageManager.PERMISSION_GRANTED) {
            DialogNotification.INSTANCE.createDialog(getLayoutInflater(), this);
        }
    }

    @Override
    public void onAccept() {
        requestNotification.launch(Manifest.permission.POST_NOTIFICATIONS);
    }

    public void createNotificationJobs() {
        JobManager jobManager = new JobManager();
        jobManager.createNotificationJob(getApplicationContext());
        jobManager.createDailyNotificationJob(getApplicationContext());
    }

}