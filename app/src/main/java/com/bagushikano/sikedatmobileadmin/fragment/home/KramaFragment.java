package com.bagushikano.sikedatmobileadmin.fragment.home;

import static android.view.View.GONE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaMipilDaftarActivity;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaTamiuDaftarActivity;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahTamiuDaftarActivity;
import com.bagushikano.sikedatmobileadmin.activity.krama.KramaMipilDaftarActivity;
import com.bagushikano.sikedatmobileadmin.activity.krama.KramaTamiuDaftarActivity;
import com.bagushikano.sikedatmobileadmin.activity.krama.TamiuDaftarActivity;
import com.bagushikano.sikedatmobileadmin.viewmodel.BerandaViewModel;
import com.bagushikano.sikedatmobileadmin.viewmodel.KramaViewModel;

public class KramaFragment extends Fragment {

    View v;
    private Button kramaMipilListButton, kramaTamiuListButton,
            cacahKramaMipilListButton, cacahKramaTamiuListButton, tamiuListButton, cacahTamiuListButton, kramaRefresh;
    private TextView kramaMipilTotalText, kramaTamiuTotalText,
            tamiuTotalText, cacahMipilTotalText, cacahKramaTamiuTotalText, cacahTamiuTotalText;

    LinearLayout kramaLoadingContainer, kramaFailedContainer;
    SwipeRefreshLayout kramaContainer;
    SharedPreferences loginPreferences;
    KramaViewModel kramaViewModel;

    public KramaFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kramaViewModel = ViewModelProviders.of(getActivity()).get(KramaViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_krama, container, false);

        loginPreferences = getActivity().getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        kramaLoadingContainer = v.findViewById(R.id.krama_loading_container);
        kramaFailedContainer = v.findViewById(R.id.krama_failed_container);
        kramaContainer = v.findViewById(R.id.krama_container);
        kramaRefresh = v.findViewById(R.id.krama_refresh_button);


        kramaMipilTotalText = v.findViewById(R.id.krama_krama_mipil_total_text);
        kramaTamiuTotalText = v.findViewById(R.id.krama_krama_tamiu_total_text);
        tamiuTotalText = v.findViewById(R.id.krama_tamiu_total_text);
        cacahMipilTotalText = v.findViewById(R.id.krama_cacah_krama_mipil_total_text);
        cacahKramaTamiuTotalText = v.findViewById(R.id.krama_cacah_krama_tamiu_total_text);
        cacahTamiuTotalText = v.findViewById(R.id.krama_cacah_tamiu_total_text);

        kramaMipilListButton = v.findViewById(R.id.krama_mipil_list_button);
        kramaMipilListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kramaMipilListIntent = new Intent(getActivity(), KramaMipilDaftarActivity.class);
                startActivity(kramaMipilListIntent);
            }
        });

        cacahKramaMipilListButton = v.findViewById(R.id.krama_cacah_mipil_list_button);
        cacahKramaMipilListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kramaMipilListIntent = new Intent(getActivity(), CacahKramaMipilDaftarActivity.class);
                startActivity(kramaMipilListIntent);
            }
        });

        kramaTamiuListButton = v.findViewById(R.id.krama_tamiu_list_button);
        kramaTamiuListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kramaTamiuListIntent = new Intent(getActivity(), KramaTamiuDaftarActivity.class);
                startActivity(kramaTamiuListIntent);
            }
        });



        cacahKramaTamiuListButton = v.findViewById(R.id.krama_cacah_krama_tamiu_list_button);
        cacahKramaTamiuListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kramaTamiuListIntent = new Intent(getActivity(), CacahKramaTamiuDaftarActivity.class);
                startActivity(kramaTamiuListIntent);
            }
        });

        tamiuListButton = v.findViewById(R.id.tamiu_list_button);
        tamiuListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kramaTamiuListIntent = new Intent(getActivity(), TamiuDaftarActivity.class);
                startActivity(kramaTamiuListIntent);
            }
        });



        cacahTamiuListButton = v.findViewById(R.id.krama_cacah_tamiu_list_button);
        cacahTamiuListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kramaTamiuListIntent = new Intent(getActivity(), CacahTamiuDaftarActivity.class);
                startActivity(kramaTamiuListIntent);
            }
        });

        kramaRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(loginPreferences.getString("token", "empty"), 1);
            }
        });

        kramaContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(loginPreferences.getString("token", "empty"), 1);
            }
        });

        getData(loginPreferences.getString("token", "empty"), 0);

        return v;
    }
    public void getData(String token, int flag) {
        if (flag == 0) {
            kramaViewModel.init(token);
        } else if (flag == 1) {
            kramaViewModel.getData(token);
        }
        setLoadingContainerVisible();
        kramaViewModel.getDashboardData().observe(getViewLifecycleOwner(), dashboardDataKramaResponse -> {
            if (dashboardDataKramaResponse != null) {
                kramaMipilTotalText.setText(dashboardDataKramaResponse.getKramaMipil().toString() + " Krama");
                kramaTamiuTotalText.setText(dashboardDataKramaResponse.getKramaTamiu().toString() + " Krama");
                tamiuTotalText.setText(dashboardDataKramaResponse.getTamiu().toString() + " Tamiu");
                cacahMipilTotalText.setText(dashboardDataKramaResponse.getCacahMipil().toString() + " Cacah");
                cacahKramaTamiuTotalText.setText(dashboardDataKramaResponse.getCacahKramaTamiu().toString() + " Cacah");
                cacahTamiuTotalText.setText(dashboardDataKramaResponse.getCacahTamiu().toString() + " Cacah");
                setBerandaContainerVisible();

            } else {
                setFailedContainerVisible();
            }
        });
        kramaContainer.setRefreshing(false);
    }

    public void setFailedContainerVisible() {
        kramaLoadingContainer.setVisibility(GONE);
        kramaFailedContainer.setVisibility(View.VISIBLE);
        kramaContainer.setVisibility(GONE);
    }

    public void setLoadingContainerVisible() {
        kramaLoadingContainer.setVisibility(View.VISIBLE);
        kramaFailedContainer.setVisibility(GONE);
        kramaContainer.setVisibility(GONE);
    }

    public void setBerandaContainerVisible() {
        kramaLoadingContainer.setVisibility(GONE);
        kramaFailedContainer.setVisibility(GONE);
        kramaContainer.setVisibility(View.VISIBLE);
    }

}