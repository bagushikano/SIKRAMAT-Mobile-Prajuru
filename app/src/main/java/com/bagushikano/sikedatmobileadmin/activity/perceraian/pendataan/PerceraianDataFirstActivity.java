package com.bagushikano.sikedatmobileadmin.activity.perceraian.pendataan;

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
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.krama.KramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.bedabanjar.MaperasBedaBanjarKramaMipilLamaSelectActivity;
import com.bagushikano.sikedatmobileadmin.activity.perceraian.pendataan.kramapradana.PerceraianKramaPradanaTetapActivity;
import com.bagushikano.sikedatmobileadmin.activity.perceraian.pendataan.kramapurusa.PerceraianKramaPurusaTetapActivity;
import com.bagushikano.sikedatmobileadmin.activity.perceraian.pendataan.kramaselect.PerceraianSelectKramaMipilActivity;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipilDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipil;
import com.bagushikano.sikedatmobileadmin.model.perceraian.Perceraian;
import com.bagushikano.sikedatmobileadmin.model.perceraian.PerceraianGetKramaCerai;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerceraianDataFirstActivity extends AppCompatActivity {

    TextView kramaNamaText, pasanganNamaText, kramaKedudukanText, pasanganKedudukanText;
    Button kramaSelectButton, peceraianNextButton;

    ActivityResultLauncher<Intent> startActivityIntent;
    SharedPreferences loginPreferences;

    //di pake untuk krama mipilnya
    private final String KRAMA_MIPIL_SELECT_KEY = "KRAMA_MIPIL_SELECT_KEY";
    private final String KRAMA_MIPIL_DETAIL_KEY = "KRAMA_MIPIL_DETAIL_KEY";
    Gson gson = new Gson();

    Perceraian perceraian = new Perceraian();
    KramaMipil kramaMipilSelected;
    AnggotaKramaMipil pasanganKrama;
    ArrayList<AnggotaKramaMipil> anggotaKramaMipilArrayList = new ArrayList<>();

    private final String KRAMA_CERAI_KEY = "KRAMA_CERAI_KEY";
    private final String PASANGAN_CERAI_KEY = "PASANGAN_CERAI_KEY";
    private final String ANGGOTA_CERAI_KEY = "ANGGOTA_CERAI_KEY";
    private final String PERCERAIAN_KEY = "PERCERAIAN_KEY";

    private Boolean isKramaSelected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perceraian_data_first);

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        kramaNamaText = findViewById(R.id.perceraian_krama_mipil_nama_text);
        pasanganNamaText = findViewById(R.id.perceraian_pasangan_nama_text);
        kramaKedudukanText = findViewById(R.id.perceraian_krama_mipil_kedudukan_text);
        pasanganKedudukanText = findViewById(R.id.perceraian_pasangan_kedudukan_text);
        peceraianNextButton = findViewById(R.id.perceraian_next_button);
        kramaSelectButton = findViewById(R.id.perceraian_krama_mipil_button);

        kramaSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent maperasSelectKramaIntent = new Intent(getApplicationContext() , PerceraianSelectKramaMipilActivity.class);
                startActivityIntent.launch(maperasSelectKramaIntent);
            }
        });

        peceraianNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                        "Pilih Krama Mipil Lama terebih dahulu", Snackbar.LENGTH_SHORT).show();
            }
        });

        startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 200) {
                            kramaMipilSelected = gson.fromJson(result.getData().getStringExtra(KRAMA_MIPIL_SELECT_KEY), KramaMipil.class);
                            getDataKramaSelected(kramaMipilSelected.getId(), loginPreferences.getString("token", "empty"));
                        } else if (result.getResultCode() == 1) {
                            setResult(1);
                            finish();
                        }
                    }
                });
    }


    public void getDataKramaSelected(int idKrama, String token) {
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<PerceraianGetKramaCerai> kramaCeraiCall = getData.perceraianGetKramaCerai(
                "Bearer " + token, idKrama);
        kramaCeraiCall.enqueue(new Callback<PerceraianGetKramaCerai>() {
            @Override
            public void onResponse(Call<PerceraianGetKramaCerai> call, Response<PerceraianGetKramaCerai> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    kramaMipilSelected = response.body().getKramaMipil();
                    pasanganKrama = response.body().getPasangan().get(0);
                    isKramaSelected = true;
                   if ( response.body().getAnggotaKramaMipil() != null) {
                       anggotaKramaMipilArrayList.clear();
                       anggotaKramaMipilArrayList.addAll(response.body().getAnggotaKramaMipil());
                   }

                    kramaNamaText.setText(StringFormatter.formatNamaWithGelar(
                            kramaMipilSelected.getCacahKramaMipil().getPenduduk().getNama(),
                            kramaMipilSelected.getCacahKramaMipil().getPenduduk().getGelarDepan(),
                            kramaMipilSelected.getCacahKramaMipil().getPenduduk().getGelarBelakang()
                            ));
                    pasanganNamaText.setText(StringFormatter.formatNamaWithGelar(
                            pasanganKrama.getCacahKramaMipil().getPenduduk().getNama(),
                            pasanganKrama.getCacahKramaMipil().getPenduduk().getGelarDepan(),
                            pasanganKrama.getCacahKramaMipil().getPenduduk().getGelarBelakang()
                    ));

                    if (kramaMipilSelected.getKedudukanKramaMipil().equals("Pradana")) {
                        // masuk ke krama pradana
                        kramaKedudukanText.setText("Pradana");
                        pasanganKedudukanText.setText("Purusa");

                        perceraian.setPurusaId(pasanganKrama.getCacahKramaMipil().getId());
                        perceraian.setPradanaId(kramaMipilSelected.getCacahKramaMipil().getId());
                        perceraian.setKramaMipilId(kramaMipilSelected.getId());
                        peceraianNextButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent kramaPradana = new Intent(getApplicationContext(), PerceraianKramaPradanaTetapActivity.class);
                                kramaPradana.putExtra(PERCERAIAN_KEY, gson.toJson(perceraian));
                                kramaPradana.putExtra(KRAMA_CERAI_KEY, gson.toJson(kramaMipilSelected));
                                kramaPradana.putExtra(PASANGAN_CERAI_KEY, gson.toJson(pasanganKrama));
                                if (anggotaKramaMipilArrayList.size() != 0) {
                                    kramaPradana.putExtra(ANGGOTA_CERAI_KEY, gson.toJson(anggotaKramaMipilArrayList));
                                }
                                startActivityIntent.launch(kramaPradana);
                            }
                        });


                    } else {
                        //masuk ke krama purusa
                        kramaKedudukanText.setText("Purusa");
                        pasanganKedudukanText.setText("Pradana");
                        perceraian.setPurusaId(kramaMipilSelected.getCacahKramaMipil().getId());
                        perceraian.setPradanaId(pasanganKrama.getCacahKramaMipil().getId());
                        perceraian.setKramaMipilId(kramaMipilSelected.getId());
                        peceraianNextButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent kramaPradana = new Intent(getApplicationContext(), PerceraianKramaPurusaTetapActivity.class);
                                kramaPradana.putExtra(PERCERAIAN_KEY, gson.toJson(perceraian));
                                kramaPradana.putExtra(KRAMA_CERAI_KEY, gson.toJson(kramaMipilSelected));
                                kramaPradana.putExtra(PASANGAN_CERAI_KEY, gson.toJson(pasanganKrama));
                                if (anggotaKramaMipilArrayList.size() != 0) {
                                    kramaPradana.putExtra(ANGGOTA_CERAI_KEY, gson.toJson(anggotaKramaMipilArrayList));
                                }
                                startActivityIntent.launch(kramaPradana);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<PerceraianGetKramaCerai> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}