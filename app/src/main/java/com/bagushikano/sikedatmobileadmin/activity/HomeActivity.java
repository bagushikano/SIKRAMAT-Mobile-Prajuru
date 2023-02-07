package com.bagushikano.sikedatmobileadmin.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.viewmodel.BerandaViewModel;
import com.bagushikano.sikedatmobileadmin.viewmodel.KramaViewModel;
import com.bagushikano.sikedatmobileadmin.viewmodel.ProfileViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView homeBottomNav;
    BerandaViewModel berandaViewModel;
    KramaViewModel kramaViewModel;
    ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeBottomNav = findViewById(R.id.home_bottom_nav);
        NavController navController = Navigation.findNavController(this, R.id.nav_home_fragment);
        NavigationUI.setupWithNavController(homeBottomNav, navController);

        // handler for snackbar
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar snackbar = Snackbar.make(
                        getWindow().getDecorView().findViewById(android.R.id.content),
                        "Selamat datang " + "di SIKRAMAT" + "!",Snackbar.LENGTH_SHORT);
                snackbar.setAnchorView(homeBottomNav);
                snackbar.show();
            }
        }, 1000);

        berandaViewModel = ViewModelProviders.of(this).get(BerandaViewModel.class);
        kramaViewModel = ViewModelProviders.of(this).get(KramaViewModel.class);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        berandaViewModel.getNotifikasiData().observe(this, notifikasiGetResponse -> {

            if (notifikasiGetResponse != null) {
                if (notifikasiGetResponse.getNotifikasiList().size() != 0) {
                    homeBottomNav.getOrCreateBadge(R.id.navigation_beranda).setNumber(notifikasiGetResponse.getNotifikasiList().size());
                } else {
                    homeBottomNav.getOrCreateBadge(R.id.navigation_beranda).setVisible(false);
                }
            }
            else {
                homeBottomNav.getOrCreateBadge(R.id.navigation_beranda).setVisible(false);
            }
        });
    }
}