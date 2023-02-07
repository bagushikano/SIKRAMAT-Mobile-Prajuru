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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.LoginActivity;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;
import com.bagushikano.sikedatmobileadmin.viewmodel.ProfileViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {

    View v;

    SharedPreferences loginPreferences;
    SharedPreferences userPreferences;

    private LinearLayout profileLoadingContainer, profileFailedContainer;
    private SwipeRefreshLayout profileContainer;
    private Button profileRefreshButton, profileDetailButton,
            profileCacahMipilDetail;
    private MaterialCardView profileLogoutButton, profileCacahMipilCard, profileCacahTamiuCard;

    private ImageView profileCacahKramaImage;
    private TextView profileCacahKramaNama, profileCacahKramaNik, profileCacahKramaNoTlp;
    private Chip profileCacahKramaJabatan;

    ProfileViewModel profileViewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = ViewModelProviders.of(getActivity()).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile, container, false);

        loginPreferences = getActivity().getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        profileLoadingContainer = v.findViewById(R.id.profile_loading_container);
        profileFailedContainer = v.findViewById(R.id.profile_failed_container);
        profileContainer = v.findViewById(R.id.profile_container);
        profileRefreshButton = v.findViewById(R.id.profile_refresh);

        profileCacahKramaImage = v.findViewById(R.id.cacah_krama_image);
        profileCacahKramaNama = v.findViewById(R.id.cacah_krama_nama_text);
        profileCacahKramaNik = v.findViewById(R.id.cacah_krama_email_text);
        profileCacahKramaNoTlp = v.findViewById(R.id.cacah_krama_notelp_text);
        profileCacahKramaJabatan = v.findViewById(R.id.jabatan_chip);

        profileLogoutButton = v.findViewById(R.id.logout_sikedat_button);
        profileLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginPreferences.getInt("login_status", 0) != 0) {
                    SharedPreferences.Editor loginPrefEditor = loginPreferences.edit();

                    loginPrefEditor.putInt("login_status", 0);
                    loginPrefEditor.putString("token", "empty");
                    loginPrefEditor.apply();
                }
                Toast.makeText(getActivity(), "Logout berhasil", Toast.LENGTH_SHORT).show();
                Intent mainActivity = new Intent(getActivity(), LoginActivity.class);
                startActivity(mainActivity);
                getActivity().finishAffinity();
            }
        });

        profileRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(loginPreferences.getString("token", "empty"), 1);
            }
        });


        profileContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
            profileViewModel.init(token);
        }
        else if (flag == 1) {
            profileViewModel.getData(token);
        }
        setLoadingContainerVisible();
        profileViewModel.getProfileData().observe(getViewLifecycleOwner(), profileGetResponse -> {
            if (profileGetResponse != null) {
                profileCacahKramaNik.setText(profileGetResponse.getKramaMipil().getCacahKramaMipil().getPenduduk().getNik());

                String jabatan = "";
                if (profileGetResponse.getJabatan().equals("kelihan_adat")) {
                    jabatan = "Kelihan Adat";
                }
                else if (profileGetResponse.getJabatan().equals("pangliman_banjar")) {
                    jabatan = "Pangliman Banjar";
                }
                else if (profileGetResponse.getJabatan().equals("penyarikan_banjar")) {
                    jabatan = "Penyarikan Banjar";
                }
                else if (profileGetResponse.getJabatan().equals("patengen_banjar")) {
                    jabatan = "Patengen Banjar";
                }
                profileCacahKramaJabatan.setText(jabatan);
                profileCacahKramaNama.setText(StringFormatter.formatNamaWithGelar(
                        profileGetResponse.getKramaMipil().getCacahKramaMipil().getPenduduk().getNama(),
                        profileGetResponse.getKramaMipil().getCacahKramaMipil().getPenduduk().getGelarDepan(),
                        profileGetResponse.getKramaMipil().getCacahKramaMipil().getPenduduk().getGelarBelakang()
                ));

                if (profileGetResponse.getKramaMipil().getCacahKramaMipil().getPenduduk().getTelepon() != null) {
                    profileCacahKramaNoTlp.setText(profileGetResponse.getKramaMipil().getCacahKramaMipil().getPenduduk().getTelepon().toString());
                }

                if (profileGetResponse.getKramaMipil().getCacahKramaMipil().getPenduduk().getFoto() != null) {
//                    holder.progressContainer.setVisibility(View.VISIBLE);
                    Picasso.get()
                            .load(getActivity().getResources().getString(R.string.image_endpoint) +
                                    profileGetResponse.getKramaMipil().getCacahKramaMipil().getPenduduk().getFoto())
                            .into(profileCacahKramaImage, new Callback() {
                                @Override
                                public void onSuccess() {
//                                    holder.progressContainer.setVisibility(View.GONE);
//                                    holder.produkImageContainer.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(R.drawable.paceholder_krama_pict).into(profileCacahKramaImage);
                                }
                            });
                }
                else {
                    Picasso.get().load(R.drawable.paceholder_krama_pict).into(profileCacahKramaImage);
                }
                setProfileContainerVisible();
            }
            else {
                setFailedContainerVisible();
            }
        });
        profileContainer.setRefreshing(false);
    }


    public void setFailedContainerVisible() {
        profileLoadingContainer.setVisibility(GONE);
        profileFailedContainer.setVisibility(View.VISIBLE);
        profileContainer.setVisibility(GONE);
    }

    public void setLoadingContainerVisible() {
        profileLoadingContainer.setVisibility(View.VISIBLE);
        profileFailedContainer.setVisibility(GONE);
        profileContainer.setVisibility(GONE);
    }

    public void setProfileContainerVisible() {
        profileLoadingContainer.setVisibility(GONE);
        profileFailedContainer.setVisibility(GONE);
        profileContainer.setVisibility(View.VISIBLE);
    }
}