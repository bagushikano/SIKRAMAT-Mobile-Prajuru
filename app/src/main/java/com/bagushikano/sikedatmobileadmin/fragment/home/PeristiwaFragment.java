package com.bagushikano.sikedatmobileadmin.fragment.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.kelahiran.KelahiranAjuanActivity;
import com.bagushikano.sikedatmobileadmin.activity.kematian.KematianAjuanActivity;
import com.bagushikano.sikedatmobileadmin.activity.maperas.MaperasDaftarActivity;
import com.bagushikano.sikedatmobileadmin.activity.perceraian.PerceraianDaftarActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.PerkawinanDaftarActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.PerkawinanSelectTypeActivity;
import com.google.android.material.card.MaterialCardView;

public class PeristiwaFragment extends Fragment {


    View v;
    MaterialCardView mutasiKelahiranButton, mutasiKematianButton,
            mutasiPerkawianButton, mutasiMaperasButton, mutasiPerceraian;


    public PeristiwaFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_peristiwa, container, false);

        mutasiKelahiranButton = v.findViewById(R.id.mutasi_kelahiran_button);
        mutasiKelahiranButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kelahiranIntent = new Intent(getActivity(), KelahiranAjuanActivity.class);
                startActivity(kelahiranIntent);
            }
        });

        mutasiKematianButton = v.findViewById(R.id.mutasi_kematian_button);
        mutasiKematianButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kematianIntent = new Intent(getActivity(), KematianAjuanActivity.class);
                startActivity(kematianIntent);
            }
        });

        mutasiPerkawianButton = v.findViewById(R.id.mutasi_perkawinan_button);
        mutasiPerkawianButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent perkawinanIntent = new Intent(getActivity(), PerkawinanDaftarActivity.class);
                startActivity(perkawinanIntent);
            }
        });

        mutasiMaperasButton = v.findViewById(R.id.mutasi_maperas_button);
        mutasiMaperasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent maperasIntent = new Intent(getActivity(), MaperasDaftarActivity.class);
                startActivity(maperasIntent);
            }
        });

        mutasiPerceraian = v.findViewById(R.id.mutasi_perceraian_button);
        mutasiPerceraian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent perceraianIntent = new Intent(getActivity(), PerceraianDaftarActivity.class);
                startActivity(perceraianIntent);
            }
        });


        return v;
    }
}