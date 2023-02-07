package com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.bedabanjar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.krama.KramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.adapter.maperas.MaperasOrtuSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipil;
import com.bagushikano.sikedatmobileadmin.model.maperas.Maperas;
import com.bagushikano.sikedatmobileadmin.model.maperas.MaperasOrtuBaruGetResponse;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaperasBedaBanjarDataKramaBaruActivity extends AppCompatActivity {

    private Button maperasNextButton;

    private Button maperasSelectKramaButton;
    private Boolean isKramaSelected = false, isAyahSelected = false, isIbuSelected;

    private CacahKramaMipil anakMaperas;
    private KramaMipil kramaMipilLama;
    private CacahKramaMipil ayahAnggotaKeluarga;
    private CacahKramaMipil ibuAnggotaKeluarga;
    private Maperas maperas;
    private KramaMipil kramaMipilBaru;

    ActivityResultLauncher<Intent> startActivityIntent;

    private MaterialCardView kramaMipilCard;
    private TextView kramaMipilName, kramaMipilNo, kramaMipilBanjarAdat;
    private Button kramaMipilDetailButton;

    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    //di pake untuk krama mipilnya
    private final String KRAMA_MIPIL_SELECT_KEY = "KRAMA_MIPIL_SELECT_KEY";
    private final String KRAMA_MIPIL_DETAIL_KEY = "KRAMA_MIPIL_DETAIL_KEY";

    // di pake tukar data antar activity nya
    private final String ANAK_KEY = "ANAK_KEY";
    private final String KRAMA_LAMA_KEY = "KRAMA_LAMA_KEY";
    private final String KRAMA_BARU_KEY = "KRAMA_BARU_KEY";
    private final String AYAH_KEY = "AYAH_KEY";
    private final String IBU_KEY = "IBU_KEY";
    private final String MAPERAS_KEY = "MAPERAS_KEY";


    Gson gson = new Gson();

    private AutoCompleteTextView maperasAyahAutoComplete;
    private TextInputLayout maperasAyahLayout;

    private AutoCompleteTextView maperasIbuAutoComplete;
    private TextInputLayout maperasIbuLayout;

    private ArrayList<CacahKramaMipil> ayahAnggotaList = new ArrayList<>();
    private ArrayList<CacahKramaMipil> ibuAnggotaList = new ArrayList<>();
    private MaperasOrtuSelectionAdapter ayahSelectionAdapter;
    private MaperasOrtuSelectionAdapter ibuSelectionAdapter;

    SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maperas_beda_banjar_data_krama_baru);

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        kramaMipilName = findViewById(R.id.krama_nama_text);
        kramaMipilNo = findViewById(R.id.krama_no_mipil_text);
        kramaMipilBanjarAdat = findViewById(R.id.krama_banjar_adat_text);
        kramaMipilDetailButton = findViewById(R.id.krama_detail_button);
        kramaMipilCard = findViewById(R.id.krama_mipil_baru_card);

        anakMaperas = gson.fromJson(getIntent().getStringExtra(ANAK_KEY), CacahKramaMipil.class);
        kramaMipilLama = gson.fromJson(getIntent().getStringExtra(KRAMA_LAMA_KEY), KramaMipil.class);
        maperas = new Maperas();
        kramaMipilBaru = new KramaMipil();

        maperasAyahAutoComplete = findViewById(R.id.maperas_ayah_baru_field);
        maperasAyahLayout = findViewById(R.id.maperas_ayah_baru_layout);

        maperasIbuAutoComplete = findViewById(R.id.maperas_ibu_baru_field);
        maperasIbuLayout = findViewById(R.id.maperas_ibu_baru_layout);

        ayahSelectionAdapter = new MaperasOrtuSelectionAdapter(this, ayahAnggotaList);
        maperasAyahAutoComplete.setAdapter(ayahSelectionAdapter);
        maperasAyahAutoComplete.setThreshold(100);
        maperasAyahAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ayahAnggotaKeluarga = (CacahKramaMipil) adapterView.getItemAtPosition(i);
                maperasAyahAutoComplete.setText(ayahAnggotaKeluarga.getPenduduk().getNama());
                maperas.setAyahBaruId(ayahAnggotaKeluarga.getId());
            }
        });

        ibuSelectionAdapter = new MaperasOrtuSelectionAdapter(this, ibuAnggotaList);
        maperasIbuAutoComplete.setAdapter(ibuSelectionAdapter);
        maperasIbuAutoComplete.setThreshold(100);
        maperasIbuAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ibuAnggotaKeluarga = (CacahKramaMipil) adapterView.getItemAtPosition(i);
                maperasIbuAutoComplete.setText(ibuAnggotaKeluarga.getPenduduk().getNama());
                maperas.setAyahBaruId(ibuAnggotaKeluarga.getId());
            }
        });

        maperasSelectKramaButton = findViewById(R.id.maperas_krama_mipil_baru_button);
        maperasSelectKramaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent maperasSelectKramaIntent = new Intent(getApplicationContext() , MaperasBedaBanjarKramaMipilBaruSelectActivity.class);
                maperasSelectKramaIntent.putExtra("KRAMA_LAMA_ID", kramaMipilLama.getId());
                startActivityIntent.launch(maperasSelectKramaIntent);
            }
        });

        startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 1) {
                            setResult(1);
                            finish();
                        } else if (result.getResultCode() == 200) {
                            // krama mipil
                            kramaMipilBaru = gson.fromJson(result.getData().getStringExtra(KRAMA_MIPIL_SELECT_KEY), KramaMipil.class);
                            getOrtuBaru(kramaMipilBaru.getId(), loginPreferences.getString("token", "empty"), 0);
                            String namaFormated = kramaMipilBaru.getCacahKramaMipil().getPenduduk().getNama();
                            if (kramaMipilBaru.getCacahKramaMipil().getPenduduk().getGelarDepan() != null) {
                                namaFormated = String.format("%s %s",
                                        kramaMipilBaru.getCacahKramaMipil().getPenduduk().getGelarDepan(),
                                        kramaMipilBaru.getCacahKramaMipil().getPenduduk().getNama()
                                );
                            }
                            if (kramaMipilBaru.getCacahKramaMipil().getPenduduk().getGelarBelakang() != null) {
                                namaFormated = String.format("%s %s",
                                        namaFormated,
                                        kramaMipilBaru.getCacahKramaMipil().getPenduduk().getGelarBelakang()
                                );
                            }
                            kramaMipilName.setText(namaFormated);
                            kramaMipilNo.setText(kramaMipilBaru.getNomorKramaMipil());
                            kramaMipilBanjarAdat.setText(kramaMipilBaru.getBanjarAdat().getNamaBanjarAdat());
                            kramaMipilDetailButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent kramaDetail = new Intent(getApplicationContext(), KramaMipilDetailActivity.class);
                                    kramaDetail.putExtra(KRAMA_MIPIL_DETAIL_KEY, kramaMipilBaru.getId());
                                    startActivity(kramaDetail);
                                }
                            });
                            kramaMipilCard.setVisibility(View.VISIBLE);
                            isKramaSelected = true;
                            maperasSelectKramaButton.setText("Ganti Krama Mipil");
                            if (isKramaSelected) {
                                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                        "Krama Mipil telah diganti. Silahkan pilih Orang Tua.", Snackbar.LENGTH_SHORT).show();
                                isAyahSelected = false;
                                isIbuSelected = false;
                            } else {
                                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                        "Krama Mipil berhasil dipilih. Silahkan pilih Orang Tua.", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        maperasNextButton = findViewById(R.id.maperas_krama_baru_next_button);
        maperasNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maperas.setAyahBaruId(ayahAnggotaKeluarga.getId());
                maperas.setIbuBaruId(ibuAnggotaKeluarga.getId());
                maperas.setKramaMipilBaruId(kramaMipilBaru.getId());
                maperas.setCacahKramaMipilLamaId(anakMaperas.getId());
                maperas.setCacahKramaMipilBaruId(anakMaperas.getId());
                maperas.setKramaMipilLamaId(kramaMipilLama.getId());
                Intent maperasIntent = new Intent(getApplicationContext(), MaperasBedaBanjarDataMaperasActivity.class);
                maperasIntent.putExtra(ANAK_KEY, gson.toJson(anakMaperas));
                maperasIntent.putExtra(KRAMA_LAMA_KEY, gson.toJson(kramaMipilLama));
                maperasIntent.putExtra(KRAMA_BARU_KEY, gson.toJson(kramaMipilBaru));
                maperasIntent.putExtra(AYAH_KEY, gson.toJson(ayahAnggotaKeluarga));
                maperasIntent.putExtra(IBU_KEY, gson.toJson(ibuAnggotaKeluarga));
                maperasIntent.putExtra(MAPERAS_KEY, gson.toJson(maperas));
                if (getIntent().hasExtra("EDIT_MAPERAS")) {
                    maperasIntent.putExtra("EDIT_MAPERAS", "duar");
                }
                startActivityIntent.launch(maperasIntent);
            }
        });
        if (getIntent().hasExtra("EDIT_MAPERAS")) {
            maperas = gson.fromJson(getIntent().getStringExtra("EDIT_MAPERAS"), Maperas.class);
            setEdit();
        }
    }

    public void getOrtuBaru(Integer idKrama, String token, int flag) {
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<MaperasOrtuBaruGetResponse> maperasOrtuBaruGetResponseCall = getData.getOrtuBatuMaperas("Bearer " + token, idKrama);
        maperasOrtuBaruGetResponseCall.enqueue(new Callback<MaperasOrtuBaruGetResponse>() {
            @Override
            public void onResponse(Call<MaperasOrtuBaruGetResponse> call, Response<MaperasOrtuBaruGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    ayahAnggotaList.clear();
                    ibuAnggotaList.clear();

                    ayahAnggotaList.addAll(response.body().getAyahList());
                    ibuAnggotaList.addAll(response.body().getIbuList());

                    ibuSelectionAdapter.notifyDataSetChanged();
                    ayahSelectionAdapter.notifyDataSetChanged();

                    maperasIbuLayout.setEnabled(true);
                    maperasAyahLayout.setEnabled(true);
                    if (flag == 0) {
                        maperasIbuAutoComplete.setText(null);
                        maperasAyahAutoComplete.setText(null);
                    }
                }
                else {
                    maperasIbuLayout.setEnabled(false);
                    maperasAyahLayout.setEnabled(false);
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MaperasOrtuBaruGetResponse> call, Throwable t) {
                maperasIbuLayout.setEnabled(false);
                maperasAyahLayout.setEnabled(false);
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void setEdit() {
        // krama mipil
        kramaMipilBaru = maperas.getKramaMipilBaru();
        getOrtuBaru(kramaMipilBaru.getId(), loginPreferences.getString("token", "empty"), 1);
        String namaFormated = kramaMipilBaru.getCacahKramaMipil().getPenduduk().getNama();
        if (kramaMipilBaru.getCacahKramaMipil().getPenduduk().getGelarDepan() != null) {
            namaFormated = String.format("%s %s",
                    kramaMipilBaru.getCacahKramaMipil().getPenduduk().getGelarDepan(),
                    kramaMipilBaru.getCacahKramaMipil().getPenduduk().getNama()
            );
        }
        if (kramaMipilBaru.getCacahKramaMipil().getPenduduk().getGelarBelakang() != null) {
            namaFormated = String.format("%s %s",
                    namaFormated,
                    kramaMipilBaru.getCacahKramaMipil().getPenduduk().getGelarBelakang()
            );
        }
        kramaMipilName.setText(namaFormated);
        kramaMipilNo.setText(kramaMipilBaru.getNomorKramaMipil());
        kramaMipilBanjarAdat.setText(kramaMipilBaru.getBanjarAdat().getNamaBanjarAdat());
        kramaMipilDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kramaDetail = new Intent(getApplicationContext(), KramaMipilDetailActivity.class);
                kramaDetail.putExtra(KRAMA_MIPIL_DETAIL_KEY, kramaMipilBaru.getId());
                startActivity(kramaDetail);
            }
        });
        kramaMipilCard.setVisibility(View.VISIBLE);
        isKramaSelected = true;
        maperasSelectKramaButton.setText("Ganti Krama Mipil");
        ayahAnggotaKeluarga = maperas.getAyahBaru();
        ibuAnggotaKeluarga = maperas.getIbuBaru();
        maperasAyahAutoComplete.setThreshold(100);
        maperasIbuAutoComplete.setThreshold(100);
        maperasAyahAutoComplete.setText(StringFormatter.formatNamaWithGelar(
                maperas.getAyahBaru().getPenduduk().getNama(),
                maperas.getAyahBaru().getPenduduk().getGelarDepan(),
                maperas.getAyahBaru().getPenduduk().getGelarBelakang()
                )
        );
        maperasIbuAutoComplete.setText(StringFormatter.formatNamaWithGelar(
                maperas.getIbuBaru().getPenduduk().getNama(),
                maperas.getIbuBaru().getPenduduk().getGelarDepan(),
                maperas.getIbuBaru().getPenduduk().getGelarBelakang()
                )
        );
    }
}