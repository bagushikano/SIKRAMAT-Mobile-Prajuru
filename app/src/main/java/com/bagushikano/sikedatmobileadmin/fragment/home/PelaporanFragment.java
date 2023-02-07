package com.bagushikano.sikedatmobileadmin.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaMipilDaftarActivity;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaTamiuDaftarActivity;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahTamiuDaftarActivity;
import com.bagushikano.sikedatmobileadmin.activity.krama.KramaMipilDaftarActivity;
import com.bagushikano.sikedatmobileadmin.activity.krama.KramaTamiuDaftarActivity;
import com.bagushikano.sikedatmobileadmin.activity.krama.TamiuDaftarActivity;
import com.bagushikano.sikedatmobileadmin.activity.report.ReportActivity;
import com.bagushikano.sikedatmobileadmin.viewmodel.BerandaViewModel;


public class PelaporanFragment extends Fragment {

    View v;
    private Button kramaMipilListButton, kramaTamiuListButton,
            cacahKramaMipilListButton, cacahKramaTamiuListButton, tamiuListButton, cacahTamiuListButton, kelahiranButton, kematianButton,
            perkawinanButton, perceraianButton, maperasButton, mutasiKramaButton;

    private TextView kramaMipilTotalText, kramaTamiuTotalText,
            tamiuTotalText, cacahMipilTotalText, cacahKramaTamiuTotalText, cacahTamiuTotalText,
            kelahiranTotalText, kematianTotalText, perkawinanTotalText, perceraianTotalText, maperasTotalText;
    BerandaViewModel berandaViewModel;
    SharedPreferences loginPreferences;

    public PelaporanFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        berandaViewModel = ViewModelProviders.of(getActivity()).get(BerandaViewModel.class);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_pelaporan, container, false);
        loginPreferences = getActivity().getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        kramaMipilTotalText = v.findViewById(R.id.krama_krama_mipil_total_text);
        kramaTamiuTotalText = v.findViewById(R.id.krama_krama_tamiu_total_text);
        tamiuTotalText = v.findViewById(R.id.krama_tamiu_total_text);
        cacahMipilTotalText = v.findViewById(R.id.krama_cacah_krama_mipil_total_text);
        cacahKramaTamiuTotalText = v.findViewById(R.id.krama_cacah_krama_tamiu_total_text);
        cacahTamiuTotalText = v.findViewById(R.id.krama_cacah_tamiu_total_text);

        kelahiranTotalText = v.findViewById(R.id.kelahiran_report_text);
        kematianTotalText = v.findViewById(R.id.kematian_report_text);
        perkawinanTotalText = v.findViewById(R.id.perkawinan_report_text);
        perceraianTotalText = v.findViewById(R.id.perceraian_report_text);
        maperasTotalText = v.findViewById(R.id.maperas_report_text);

        kramaMipilListButton = v.findViewById(R.id.krama_mipil_list_button);

        cacahKramaMipilListButton = v.findViewById(R.id.krama_cacah_mipil_list_button);

        mutasiKramaButton = v.findViewById(R.id.mutasi_krama_report_button);

        getData(loginPreferences.getString("token", "empty"));

        return v;
    }

    public void getData(String token) {
        berandaViewModel.getData(token);
        berandaViewModel.getDashboardData().observe(getViewLifecycleOwner(), dashboardDataResponse -> {
            if (dashboardDataResponse != null) {
                kramaMipilTotalText.setText(dashboardDataResponse.getKramaMipil().toString() + " Krama");
                kramaTamiuTotalText.setText(dashboardDataResponse.getKramaTamiu().toString() + " Krama");
                tamiuTotalText.setText(dashboardDataResponse.getTamiu().toString() + " Tamiu");

                cacahMipilTotalText.setText(dashboardDataResponse.getCacahMipil().toString() + " Cacah");
                cacahKramaTamiuTotalText.setText(dashboardDataResponse.getCacahTamiu().toString() + " Cacah");
                cacahTamiuTotalText.setText(dashboardDataResponse.getCacahTamiu2().toString() + " Cacah");

                kelahiranTotalText.setText(dashboardDataResponse.getKelahiran().toString());
                kematianTotalText.setText(dashboardDataResponse.getKematian().toString());
                perkawinanTotalText.setText(dashboardDataResponse.getPerkawinan().toString());
                perceraianTotalText.setText(dashboardDataResponse.getPerceraian().toString());
                maperasTotalText.setText(dashboardDataResponse.getMaperas().toString());

                cacahKramaMipilListButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        builder.setToolbarColor(getActivity().getResources().getColor(R.color.primaryColor));
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.launchUrl(getActivity(), Uri.parse(
                                getActivity().getResources().getString(R.string.api_endpoint) +
                                        "/api/report/cacah-krama?banjar_adat_id=" + dashboardDataResponse.getBanjarAdat().getId() +
                                        "&desa_adat_id=" + dashboardDataResponse.getDesaAdat().getId())
                        );
                    }
                });

                mutasiKramaButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        builder.setToolbarColor(getActivity().getResources().getColor(R.color.primaryColor));
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.launchUrl(getActivity(), Uri.parse(
                                getActivity().getResources().getString(R.string.api_endpoint) +
                                        "/api/report/mutasi?banjar_adat_id=" + dashboardDataResponse.getBanjarAdat().getId() +
                                        "&desa_adat_id=" + dashboardDataResponse.getDesaAdat().getId())
                        );
                    }
                });

                kramaMipilListButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        builder.setToolbarColor(getActivity().getResources().getColor(R.color.primaryColor));
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.launchUrl(getActivity(), Uri.parse(
                                getActivity().getResources().getString(R.string.api_endpoint) +
                                        "/api/report/krama?banjar_adat_id=" + dashboardDataResponse.getBanjarAdat().getId() +
                                        "&desa_adat_id=" + dashboardDataResponse.getDesaAdat().getId())
                        );
//
//                        Intent kramaMipilListIntent = new Intent(getActivity(), ReportActivity.class);
//                        kramaMipilListIntent.putExtra("URL",
//                                "/api/report/krama?banjar_adat_id=" + dashboardDataResponse.getBanjarAdat().getId() + "&desa_adat_id=" + dashboardDataResponse.getDesaAdat().getId());
//                        startActivity(kramaMipilListIntent);
                    }
                });

            }
        });
    }
}