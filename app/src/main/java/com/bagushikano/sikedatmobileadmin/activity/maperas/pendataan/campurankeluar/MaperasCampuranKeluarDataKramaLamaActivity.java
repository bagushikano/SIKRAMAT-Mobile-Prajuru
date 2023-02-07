package com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.campurankeluar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.krama.KramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.satubanjar.MaperasSatuBanjarAnakSelectActivity;
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.satubanjar.MaperasSatuBanjarDataKramaBaruActivity;
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.satubanjar.MaperasSatuBanjarKramaMipilLamaSelectActivity;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipilDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipil;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaperasCampuranKeluarDataKramaLamaActivity extends AppCompatActivity {


    private Button maperasNextButton;

    private Button maperasSelectKramaButton, maperasSelectAnakButton;
    private Boolean isKramaSelected = false, isAnakSelected = false;

    private CacahKramaMipil anakMaperas;
    private KramaMipil kramaMipilLama;

    ActivityResultLauncher<Intent> startActivityIntent;

    private MaterialCardView kramaMipilCard;
    private TextView kramaMipilName, kramaMipilNo, kramaMipilBanjarAdat;
    private Button kramaMipilDetailButton;

    private TextView kramaAnakName, kramaAnakNik, noKramaMipilAnak;
    private Button kramaMipilAnakDetailButton;
    private LinearLayout kramaAnakImageLoadingContainer;
    private MaterialCardView kramaAnakCard;
    private ImageView kramaAnakImage;

    private TextInputEditText ayahLamaField, ibuLamaField;

    // di pake untuk anak
    private final String KRAMA_SELECT_KEY = "KRAMA_SELECT_KEY";
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    //di pake untuk krama mipilnya
    private final String KRAMA_MIPIL_SELECT_KEY = "KRAMA_MIPIL_SELECT_KEY";
    private final String KRAMA_MIPIL_DETAIL_KEY = "KRAMA_MIPIL_DETAIL_KEY";

    // di pake tukar data antar activity nya
    private final String ANAK_KEY = "ANAK_KEY";
    private final String KRAMA_LAMA_KEY = "KRAMA_LAMA_KEY";

    Gson gson = new Gson();

    SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maperas_campuran_keluar_data_krama_lama);

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        kramaMipilName = findViewById(R.id.krama_nama_text);
        kramaMipilNo = findViewById(R.id.krama_no_mipil_text);
        kramaMipilBanjarAdat = findViewById(R.id.krama_banjar_adat_text);
        kramaMipilDetailButton = findViewById(R.id.krama_detail_button);
        kramaMipilCard = findViewById(R.id.krama_mipil_lama_card);

        ayahLamaField = findViewById(R.id.maperas_ayah_lama_field);
        ibuLamaField = findViewById(R.id.maperas_ibu_lama_field);

        kramaAnakName = findViewById(R.id.anak_maperas_nama_text);
        kramaAnakNik = findViewById(R.id.anak_maperas_nik_text);
        noKramaMipilAnak = findViewById(R.id.anak_maperas_no_mipil_text);
        kramaMipilAnakDetailButton = findViewById(R.id.anak_maperas_detail_button);
        kramaAnakImageLoadingContainer = findViewById(R.id.anak_maperas_image_loading_container);
        kramaAnakImage = findViewById(R.id.anak_maperas_image);
        kramaAnakCard = findViewById(R.id.anak_maperas_card);

        maperasNextButton = findViewById(R.id.maperas_krama_lama_next_button);
        maperasNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAnakSelected && isKramaSelected) {
                    Intent maperasNextIntent = new Intent(getApplicationContext(), MaperasCampuranKeluarDataOrtuBaruActivity.class);
                    maperasNextIntent.putExtra(ANAK_KEY, gson.toJson(anakMaperas));
                    maperasNextIntent.putExtra(KRAMA_LAMA_KEY, gson.toJson(kramaMipilLama));
                    startActivityIntent.launch(maperasNextIntent);
                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Periksa data kembali.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        maperasSelectKramaButton = findViewById(R.id.maperas_krama_mipil_lama_button);
        maperasSelectAnakButton = findViewById(R.id.maperas_anak_button);
        maperasSelectAnakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isKramaSelected) {
                    Intent maperasAnakIntent = new Intent(getApplicationContext() , MaperasSatuBanjarAnakSelectActivity.class);
                    maperasAnakIntent.putExtra("KRAMA_ID", kramaMipilLama.getId());
                    startActivityIntent.launch(maperasAnakIntent);
                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Pilih Krama Mipil Lama terebih dahulu", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        maperasSelectKramaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent maperasSelectKramaIntent = new Intent(getApplicationContext() , MaperasSatuBanjarKramaMipilLamaSelectActivity.class);
                startActivityIntent.launch(maperasSelectKramaIntent);
            }
        });

        startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 200) {
                            // krama mipil
                            kramaMipilLama = gson.fromJson(result.getData().getStringExtra(KRAMA_MIPIL_SELECT_KEY), KramaMipil.class);
                            String namaFormated = kramaMipilLama.getCacahKramaMipil().getPenduduk().getNama();
                            if (kramaMipilLama.getCacahKramaMipil().getPenduduk().getGelarDepan() != null) {
                                namaFormated = String.format("%s %s",
                                        kramaMipilLama.getCacahKramaMipil().getPenduduk().getGelarDepan(),
                                        kramaMipilLama.getCacahKramaMipil().getPenduduk().getNama()
                                );
                            }
                            if (kramaMipilLama.getCacahKramaMipil().getPenduduk().getGelarBelakang() != null) {
                                namaFormated = String.format("%s %s",
                                        namaFormated,
                                        kramaMipilLama.getCacahKramaMipil().getPenduduk().getGelarBelakang()
                                );
                            }
                            kramaMipilName.setText(namaFormated);
                            kramaMipilNo.setText(kramaMipilLama.getNomorKramaMipil());
                            kramaMipilBanjarAdat.setText(kramaMipilLama.getBanjarAdat().getNamaBanjarAdat());
                            kramaMipilDetailButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent kramaDetail = new Intent(getApplicationContext(), KramaMipilDetailActivity.class);
                                    kramaDetail.putExtra(KRAMA_MIPIL_DETAIL_KEY, kramaMipilLama.getId());
                                    startActivity(kramaDetail);
                                }
                            });
                            kramaMipilCard.setVisibility(View.VISIBLE);
                            isKramaSelected = true;
                            maperasSelectKramaButton.setText("Ganti Krama Mipil");
                            maperasSelectAnakButton.setText("Pilih Anak");

                            if (isAnakSelected) {
                                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                        "Krama Mipil telah diganti. Silahkan pilih Anak.", Snackbar.LENGTH_SHORT).show();
                                isAnakSelected = false;
                                kramaAnakCard.setVisibility(View.GONE);
                                anakMaperas = null;
                            } else {
                                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                        "Krama Mipil berhasil dipilih. Silahkan pilih Anak.", Snackbar.LENGTH_SHORT).show();
                            }
                        } else if (result.getResultCode() == 100) {
                            // anak
                            anakMaperas = gson.fromJson(result.getData().getStringExtra(KRAMA_SELECT_KEY), CacahKramaMipil.class);
                            String namaFormated = anakMaperas.getPenduduk().getNama();
                            if (anakMaperas.getPenduduk().getGelarDepan() != null) {
                                namaFormated = String.format("%s %s",
                                        anakMaperas.getPenduduk().getGelarDepan(),
                                        anakMaperas.getPenduduk().getNama()
                                );
                            }

                            if (anakMaperas.getPenduduk().getGelarBelakang() != null) {
                                namaFormated = String.format("%s %s",
                                        namaFormated,
                                        anakMaperas.getPenduduk().getGelarBelakang()
                                );
                            }
                            kramaAnakName.setText(namaFormated);
                            kramaAnakNik.setText(anakMaperas.getPenduduk().getNik());
                            noKramaMipilAnak.setText(anakMaperas.getNomorCacahKramaMipil());
                            kramaMipilAnakDetailButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                                    kramaDetail.putExtra(KRAMA_DETAIL_KEY, anakMaperas.getId());
                                    startActivity(kramaDetail);
                                }
                            });
                            if (anakMaperas.getPenduduk().getFoto() != null) {
                                kramaAnakImageLoadingContainer.setVisibility(View.VISIBLE);
                                Picasso.get()
                                        .load(getResources().getString(R.string.image_endpoint) +
                                                anakMaperas.getPenduduk().getFoto())
                                        .fit().centerCrop()
                                        .into(kramaAnakImage, new com.squareup.picasso.Callback() {
                                            @Override
                                            public void onSuccess() {
                                                kramaAnakImageLoadingContainer.setVisibility(View.GONE);
                                                kramaAnakImage.setVisibility(View.VISIBLE);
                                            }
                                            @Override
                                            public void onError(Exception e) {
                                                kramaAnakImageLoadingContainer.setVisibility(View.GONE);
                                                Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaAnakImage);
                                            }
                                        });
                            }
                            else {
                                Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaAnakImage);
                                kramaAnakImage.setVisibility(View.VISIBLE);
                            }
                            getOrtuLama(loginPreferences.getString("token", "empty"), anakMaperas.getId());
                            kramaAnakCard.setVisibility(View.VISIBLE);
                            isAnakSelected = true;
                            maperasSelectAnakButton.setText("Ganti Anak");
                            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                    "Anak berhasil dipilih.", Snackbar.LENGTH_SHORT).show();

                        } else if (result.getResultCode() == 1) {
                            setResult(1);
                            finish();
                        }
                    }
                });
    }


    public void getOrtuLama(String token, Integer idCacahAnak) {
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<CacahKramaMipilDetailResponse> kramaMipilGetResponseCall = getData.getOrtuLamaMaperas(
                "Bearer " + token, idCacahAnak);
        kramaMipilGetResponseCall.enqueue(new Callback<CacahKramaMipilDetailResponse>() {
            @Override
            public void onResponse(Call<CacahKramaMipilDetailResponse> call, Response<CacahKramaMipilDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    if (response.body().getCacahKramaMipil().getPenduduk().getIbu() != null) {
                        ibuLamaField.setText(response.body().getCacahKramaMipil().getPenduduk().getIbu().getNama());
                        anakMaperas.getPenduduk().setIbu(response.body().getCacahKramaMipil().getPenduduk().getIbu());
                    }
                    if (response.body().getCacahKramaMipil().getPenduduk().getAyah() != null) {
                        ayahLamaField.setText(response.body().getCacahKramaMipil().getPenduduk().getAyah().getNama());
                        anakMaperas.getPenduduk().setAyah(response.body().getCacahKramaMipil().getPenduduk().getAyah());
                    }
                }
            }

            @Override
            public void onFailure(Call<CacahKramaMipilDetailResponse> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}