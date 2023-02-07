package com.bagushikano.sikedatmobileadmin.fragment.home;

import static android.view.View.GONE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.DesaAdatSummaryActivity;
import com.bagushikano.sikedatmobileadmin.adapter.NotifikasiListAdapter;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.notifikasi.Notifikasi;
import com.bagushikano.sikedatmobileadmin.model.notifikasi.NotifikasiGetResponse;
import com.bagushikano.sikedatmobileadmin.viewmodel.BerandaViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BerandaFragment extends Fragment {

    View v;
    LinearLayout berandaLoadingContainer, berandaFailedContainer;
    SwipeRefreshLayout berandaContainer;
    private TextView banjarAdatText, kramaMipilText,
            cacahKramaMipilText, cacahKramaTamiuText, kramaTamiuText, desaAdatText;
    SharedPreferences loginPreferences;
    Button berandaRefresh, berandaSelengkapnyaButton;
    BerandaViewModel berandaViewModel;
    private ImageView greetingImage, greetingBerandaIcon;
    private TextView greetingText, greetingTextSub;

    LinearLayout notificationEmptyContainer;
    RecyclerView notifikasiList;
    ArrayList<Notifikasi> notifikasiArrayList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    NotifikasiListAdapter notifikasiListAdapter;
    TextView notifikasiTotalText;
    private Button markAllAsReadNotif;

    View berandaView;
    BottomNavigationView homeBottomNav;

    public BerandaFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        berandaViewModel = ViewModelProviders.of(getActivity()).get(BerandaViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_beranda, container, false);
//
//        berandaView = inflater.inflate(R.layout.activity_home, container, false);
//        homeBottomNav = berandaView.findViewById(R.id.home_bottom_nav);

        loginPreferences = getActivity().getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        berandaLoadingContainer = v.findViewById(R.id.beranda_loading_container);
        berandaFailedContainer = v.findViewById(R.id.beranda_failed_container);
        berandaContainer = v.findViewById(R.id.beranda_container);
        berandaRefresh = v.findViewById(R.id.beranda_refresh_button);

        desaAdatText = v.findViewById(R.id.beranda_desa_adat_text);
        banjarAdatText = v.findViewById(R.id.beranda_banjar_adat_text);

        cacahKramaTamiuText = v.findViewById(R.id.beranda_cacah_krama_tamiu_text);
        cacahKramaMipilText = v.findViewById(R.id.beranda_cacah_krama_mipil_text);
        kramaMipilText = v.findViewById(R.id.beranda_krama_mipil_text);
        kramaTamiuText = v.findViewById(R.id.beranda_krama_tamiu_text);

        greetingText = v.findViewById(R.id.greeting_name_beranda_text);
        greetingTextSub = v.findViewById(R.id.greeting_beranda_text);
        greetingImage = v.findViewById(R.id.greeting_beranda_image);
        greetingBerandaIcon = v.findViewById(R.id.greeting_beranda_icon);

        berandaRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(loginPreferences.getString("token", "empty"), 1);
            }
        });

        notifikasiList = v.findViewById(R.id.beranda_notifikasi_list);
        notifikasiTotalText = v.findViewById(R.id.beranda_notif_count_text);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        notifikasiListAdapter = new NotifikasiListAdapter(getActivity(), notifikasiArrayList);
        notifikasiList.setLayoutManager(linearLayoutManager);
        notifikasiList.setAdapter(notifikasiListAdapter);

        notificationEmptyContainer = v.findViewById(R.id.notifikasi_empty_container);
        markAllAsReadNotif = v.findViewById(R.id.read_all_notif_button);

        markAllAsReadNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readAllNotif(loginPreferences.getString("token", "empty"));
            }
        });

        berandaContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(loginPreferences.getString("token", "empty"), 1);
            }
        });

        //get local time for appbar title
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        String greeting;

        if (hour>= 12 && hour < 17) {
            greeting = "Selamat siang dan semangat dalam menjalankan aktifitas";
            greetingImage.setImageResource(R.drawable.pagi_replace);
            greetingBerandaIcon.setImageResource(R.drawable.ic_outline_wb_sunny_24);
        } else if (hour >= 17 && hour < 19) {
            greeting = "Selamat sore, dan semoga sisa hari anda menyenangkan";
            greetingImage.setImageResource(R.drawable.sore_replace);
            greetingBerandaIcon.setImageResource(R.drawable.ic_outline_wb_twilight_24);
        } else if (hour >= 19 && hour < 24) {
            greeting = "Selamat malam dan selamat beristirahat";
            greetingImage.setImageResource(R.drawable.sore_replace);
            greetingBerandaIcon.setImageResource(R.drawable.ic_outline_nights_stay_24);
        } else {
            greeting = "Selamat pagi dan selamat beraktifitas! ";
            greetingImage.setImageResource(R.drawable.pagi_replace);
            greetingBerandaIcon.setImageResource(R.drawable.ic_outline_wb_sunny_24);
        }
        greetingTextSub.setText(greeting);

        getData(loginPreferences.getString("token", "empty"), 0);

        return v;
    }

    public void getData(String token, int flag) {
        if (flag == 0) {
            berandaViewModel.init(token);
        } else if (flag == 1) {
            berandaViewModel.getData(token);
            berandaViewModel.getNotifikasi(token);
        }
        setLoadingContainerVisible();
        berandaViewModel.getDashboardData().observe(getViewLifecycleOwner(), dashboardDataResponse -> {
            if (dashboardDataResponse != null) {
                banjarAdatText.setText(dashboardDataResponse.getBanjarAdat().getNamaBanjarAdat());
                desaAdatText.setText(dashboardDataResponse.getDesaAdat().getDesadatNama());
                kramaMipilText.setText(String.valueOf(dashboardDataResponse.getKramaMipil()) + " Krama");
                kramaTamiuText.setText(String.valueOf(dashboardDataResponse.getKramaTamiu()) + " Krama");
                cacahKramaMipilText.setText(String.valueOf(dashboardDataResponse.getCacahMipil()) + " Cacah Krama");
                cacahKramaTamiuText.setText(String.valueOf(dashboardDataResponse.getCacahTamiu()) + " Cacah Krama");
                setBerandaContainerVisible();
                berandaViewModel.getNotifikasiData().observe(getViewLifecycleOwner(), notifikasiGetResponse -> {
                    if (notifikasiGetResponse != null) {
                        notifikasiArrayList.clear();
                        notifikasiArrayList.addAll(notifikasiGetResponse.getNotifikasiList());
                        if (notifikasiGetResponse.getNotifikasiList().size() == 0) {
                            notificationEmptyContainer.setVisibility(View.VISIBLE);
                            notifikasiList.setVisibility(View.GONE);
                            homeBottomNav.getOrCreateBadge(R.id.navigation_beranda).setVisible(false);
                        } else {
                            notificationEmptyContainer.setVisibility(View.GONE);
                            notifikasiList.setVisibility(View.VISIBLE);
                            notifikasiListAdapter.notifyDataSetChanged();
                            notifikasiTotalText.setText(String.valueOf(notifikasiArrayList.size()));
//                            homeBottomNav.getOrCreateBadge(R.id.navigation_beranda).setNumber(notifikasiGetResponse.getNotifikasiList().size());
                        }
                    }
                    else {
                        notifikasiArrayList.clear();
                        notificationEmptyContainer.setVisibility(View.VISIBLE);
                        notifikasiTotalText.setText(String.valueOf(0));
//                        homeBottomNav.getOrCreateBadge(R.id.navigation_beranda).setVisible(false);
                    }
                });

            } else {
                setFailedContainerVisible();
            }
        });
        notifikasiListAdapter.notifyDataSetChanged();
        notifikasiArrayList.clear();
        berandaContainer.setRefreshing(false);
    }

    public void readAllNotif(String token) {
        ApiRoute submitData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<NotifikasiGetResponse> authResponseCall = submitData.readAllNotifikasi("Bearer " + token);
        authResponseCall.enqueue(new Callback<NotifikasiGetResponse>() {
            @Override
            public void onResponse(Call<NotifikasiGetResponse> call, Response<NotifikasiGetResponse> response) {
                if (response.code() == 200 && response.body().getStatusCode() == 200
                        && response.body().getMessage().equals("read notifikasi sukses")) {
                    getData(loginPreferences.getString("token", "empty"), 1);

                    Snackbar snackbar = Snackbar.make(
                            getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                            "Sukses",Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(
                            getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                            "Gagal",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<NotifikasiGetResponse> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(
                        getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                        "Gagal",Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    public void setFailedContainerVisible() {
        berandaLoadingContainer.setVisibility(GONE);
        berandaFailedContainer.setVisibility(View.VISIBLE);
        berandaContainer.setVisibility(GONE);
    }

    public void setLoadingContainerVisible() {
        berandaLoadingContainer.setVisibility(View.VISIBLE);
        berandaFailedContainer.setVisibility(GONE);
        berandaContainer.setVisibility(GONE);
    }

    public void setBerandaContainerVisible() {
        berandaLoadingContainer.setVisibility(GONE);
        berandaFailedContainer.setVisibility(GONE);
        berandaContainer.setVisibility(View.VISIBLE);
    }
}