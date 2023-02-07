package com.bagushikano.sikedatmobileadmin.activity.perceraian.pendataan;

import static android.view.View.GONE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.adapter.master.DesaDinasSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.KabupatenSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.KecamatanSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.ProvinsiSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipil;
import com.bagushikano.sikedatmobileadmin.model.master.DesaDinas;
import com.bagushikano.sikedatmobileadmin.model.master.DesaDinasGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.Kabupaten;
import com.bagushikano.sikedatmobileadmin.model.master.KabupatenGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.Kecamatan;
import com.bagushikano.sikedatmobileadmin.model.master.KecamatanGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.Provinsi;
import com.bagushikano.sikedatmobileadmin.model.master.ProvinsiGetResponse;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerceraianDataKramaMipilActivity extends AppCompatActivity {

    private Button perceraianNextButton;

    ActivityResultLauncher<Intent> startActivityIntent;

    Gson gson = new Gson();

    SharedPreferences loginPreferences;


    private TextView perceraianKramaMipilNamaText, perceraianKramaMipilKedudukanText;
    private Button perceraianSelectKramaMipilButton;

    // if purusa
    private LinearLayout perceraianKramaMipilPurusaContainer;
    private RadioGroup perceraianKramaMipilPurusaStatusKeluargaRadioGroup;
    private RadioButton perceraianKramaMipilPurusaStayRadio, perceraiankramaMipilPurusaPindahKramaRadio;
    private Button perceraiankramaMipilPurusaSelectKramaTujuanButton;
    private TextInputLayout perceraianKramaMipilPindahKramaHubunganLayout;
    private AutoCompleteTextView perceraianKramaMipilPindahKramaHubunganField;
    private TextView perceraianKramaMipilTujuanKramaText;
    private MaterialCardView perceraianKramaMipilTujuanCard;


    //if pradana
    private LinearLayout perceraianKramaMipilPradanaContainer;
    private RadioGroup perceraianKramaMipilPradanaStatusKeluargaRadioGroup;
    private RadioButton perceraianKramaMipilPradanaStay,
            perceraianKramaMipilPradanaKeluarBanjar, perceraianKramaMipilPradanaKeluarBali;
    private MaterialCardView perceraianKramaMipilPradanaKramaTujuanCard;
    private LinearLayout perceraianKramaMipilBanjarTujuanLayout;
    private LinearLayout perceraianKramaMipilKeluarBaliLayout;
    private Button perceraiankramaMipilPradanaSelectKramaTujuanButton;
    private TextInputLayout perceraianKramaMipilPradanaPindahKramaHubunganLayout;
    private AutoCompleteTextView perceraianKramaMipilPradanaPindahKramaHubunganField;

    private LinearLayout perkawinanPasanganLoadingLayout;

    private AutoCompleteTextView perkawinanKabupatenAutoComplete, perkawinanKecamatanAutoComplete,
            perkawinanDesaDinasAutoComplete, perkawinanProvinsiAutoComplete;

    private KabupatenSelectionAdapter kabupatenSelectionAdapter;
    private KecamatanSelectionAdapter kecamatanSelectionAdapter;
    private DesaDinasSelectionAdapter desaDinasSelectionAdapter;
    private ProvinsiSelectionAdapter provinsiSelectionAdapter;

    private Integer idKabupaten = null, idKecamatan = null, idProvinsi = null;
    private String idDesaDinas = null;
    private Boolean isDesaDinasSelected = false;
    private Kabupaten kabupaten = null;
    private Kecamatan kecamatan = null;
    private DesaDinas desaDinas = null;
    private Provinsi provinsi = null;

    private ArrayList<Kabupaten> kabupatens = new ArrayList<>();
    private ArrayList<Kecamatan> kecamatans = new ArrayList<>();
    private ArrayList<DesaDinas> desaDinasArrayList = new ArrayList<>();
    private ArrayList<Provinsi> provinsis = new ArrayList<>();

    private TextInputLayout perkawinanKabupatenLayout, perkawinanKecamatanLayout,
            perkawinanDesaDinasLayout, perkawinanProvinsiLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perceraian_data_krama_mipil);

        perceraianKramaMipilNamaText = findViewById(R.id.perceraian_krama_mipil_nama_text);
        perceraianKramaMipilKedudukanText = findViewById(R.id.perceraian_krama_mipil_kedudukan_text);
        perceraianSelectKramaMipilButton = findViewById(R.id.perceraian_krama_mipil_button);

        perceraianKramaMipilPurusaContainer = findViewById(R.id.perceraian_if_krama_mipil_purusa_container);
        perceraianKramaMipilPurusaStatusKeluargaRadioGroup = findViewById(R.id.perceraian_krama_mipil_status_kekeluargaan);
        perceraianKramaMipilPurusaStayRadio = findViewById(R.id.perceraian_krama_mipil_stay);
        perceraiankramaMipilPurusaPindahKramaRadio = findViewById(R.id.perceraian_krama_mipil_pindah_keluarga);
        perceraianKramaMipilTujuanCard = findViewById(R.id.perceraian_krama_mipil_krama_tujuan_card);
        perceraiankramaMipilPurusaSelectKramaTujuanButton = findViewById(R.id.perceraian_krama_mipil_select_new_krama);
        perceraianKramaMipilPindahKramaHubunganLayout = findViewById(R.id.perceraian_krama_mipil_new_krama_status_hubungan_layout);
        perceraianKramaMipilPindahKramaHubunganField = findViewById(R.id.perceraian_krama_mipil_new_krama_status_hubungan_field);
        perceraianKramaMipilTujuanKramaText = findViewById(R.id.krama_tujuan_nama_text);

        perceraianKramaMipilPradanaContainer = findViewById(R.id.perceraian_if_krama_mipil_pradana_container);
        perceraianKramaMipilPradanaStatusKeluargaRadioGroup = findViewById(R.id.perceraian_krama_mipil_pradana_status_keluarga);
        perceraianKramaMipilPradanaStay = findViewById(R.id.perceraian_krama_mipil_pradana_stay_pindah_krama);
        perceraianKramaMipilPradanaKeluarBanjar = findViewById(R.id.perceraian_krama_mipil_pradana_keluar_banjar);
        perceraianKramaMipilPradanaKeluarBali = findViewById(R.id.perceraian_krama_mipil_pradana_keluar_bali);
        perceraianKramaMipilPradanaKramaTujuanCard = findViewById(R.id.perceraian_krama_mipil_pradana_pindah_krama_container);
        perceraianKramaMipilBanjarTujuanLayout = findViewById(R.id.perceraian_pasangan_keluar_banjar_tujuan_layout);
        perceraiankramaMipilPradanaSelectKramaTujuanButton = findViewById(R.id.perceraian_krama_mipil_pradana_new_krama_button);
        perceraianKramaMipilPradanaPindahKramaHubunganLayout = findViewById(R.id.perceraian_krama_mipil_pradana_status_hubungan_new_krama_layout);
        perceraianKramaMipilPradanaPindahKramaHubunganField = findViewById(R.id.perceraian_krama_mipil_pradana_status_hubungan_new_krama_field);


        perceraianKramaMipilKeluarBaliLayout = findViewById(R.id.perceraian_keluar_bali_layout);

        String[] hubunganArr = new String[] {"Anak", "Cucu", "Ayah", "Ibu", "Famili Lain"};
        ArrayAdapter statusHubunganKramaPurusaKramaBaru = new ArrayAdapter<>(this, R.layout.dropdown_menu_item, hubunganArr);

        perceraianKramaMipilPindahKramaHubunganField.setAdapter(statusHubunganKramaPurusaKramaBaru);
        perceraianKramaMipilPindahKramaHubunganField.setInputType(0);
        perceraianKramaMipilPindahKramaHubunganField.setKeyListener(null);

        perceraianKramaMipilPindahKramaHubunganField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Anak")) {

                } else if (adapterView.getItemAtPosition(i).equals("Cucu")) {

                } else if (adapterView.getItemAtPosition(i).equals("Ayah")) {

                } else if (adapterView.getItemAtPosition(i).equals("Ibu")) {

                } else if (adapterView.getItemAtPosition(i).equals("Famili Lain")) {

                }
            }
        });

        perceraianKramaMipilPurusaStatusKeluargaRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.perceraian_krama_mipil_stay) {

                } else if (i == R.id.perceraian_krama_mipil_pindah_keluarga) {

                }
            }
        });

        perkawinanProvinsiAutoComplete = findViewById(R.id.perceraian_provinsi_field);
        perkawinanKabupatenAutoComplete = findViewById(R.id.perceraian_keluar_bali_kabupaten_field);
        perkawinanKecamatanAutoComplete = findViewById(R.id.perceraian_keluar_bali_kecamatan_field);
        perkawinanDesaDinasAutoComplete = findViewById(R.id.perceraian_keluar_bali_desa_kelurahan_field);

        perkawinanKabupatenLayout = findViewById(R.id.perceraian_keluar_bali_kabupaten_form);
        perkawinanKecamatanLayout = findViewById(R.id.perceraian_keluar_bali_kecamatan_form);
        perkawinanProvinsiLayout = findViewById(R.id.perceraian_provinsi_form);
        perkawinanDesaDinasLayout = findViewById(R.id.perceraian_keluar_bali_desa_kelurahan_form);

        provinsiSelectionAdapter = new ProvinsiSelectionAdapter(this, provinsis);
        perkawinanProvinsiAutoComplete.setAdapter(provinsiSelectionAdapter);
        perkawinanProvinsiAutoComplete.setInputType(0);
        perkawinanProvinsiAutoComplete.setKeyListener(null);

        perkawinanProvinsiAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                provinsi = (Provinsi) adapterView.getItemAtPosition(i);
                perkawinanProvinsiAutoComplete.setText(provinsi.getName());
                idProvinsi = provinsi.getId();

                kabupaten = null;
                kecamatan = null;
                desaDinas = null;

                idKabupaten = null;
                idKecamatan = null;
                idDesaDinas = null;

                isDesaDinasSelected = false;

                perkawinanKabupatenLayout.setVisibility(View.VISIBLE);
                perkawinanKecamatanLayout.setVisibility(View.GONE);
                perkawinanDesaDinasLayout.setVisibility(View.GONE);

                perkawinanKabupatenAutoComplete.setText(null);
                perkawinanKecamatanAutoComplete.setText(null);
                perkawinanDesaDinasAutoComplete.setText(null);

                getKabupaten();
            }
        });

        kabupatenSelectionAdapter = new KabupatenSelectionAdapter(this, kabupatens);
        perkawinanKabupatenAutoComplete.setAdapter(kabupatenSelectionAdapter);
        perkawinanKabupatenAutoComplete.setInputType(0);
        perkawinanKabupatenAutoComplete.setKeyListener(null);

        perkawinanKabupatenAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                kabupaten = (Kabupaten) adapterView.getItemAtPosition(i);
                perkawinanKabupatenAutoComplete.setText(kabupaten.getName());
                idKabupaten = kabupaten.getId();

                kecamatan = null;
                desaDinas = null;

                idKecamatan = null;
                idDesaDinas = null;

                isDesaDinasSelected = false;

                perkawinanKecamatanLayout.setVisibility(View.VISIBLE);
                perkawinanDesaDinasLayout.setVisibility(View.GONE);

                perkawinanKecamatanAutoComplete.setText(null);
                perkawinanDesaDinasAutoComplete.setText(null);

                getKecamatan();
            }
        });

        kecamatanSelectionAdapter = new KecamatanSelectionAdapter(this, kecamatans);
        perkawinanKecamatanAutoComplete.setAdapter(kecamatanSelectionAdapter);
        perkawinanKecamatanAutoComplete.setThreshold(100);
        perkawinanKecamatanAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                kecamatan = (Kecamatan) adapterView.getItemAtPosition(i);
                perkawinanKecamatanAutoComplete.setText(kecamatan.getName());
                idKecamatan = kecamatan.getId();

                desaDinas = null;
                idDesaDinas = null;
                isDesaDinasSelected = false;
                perkawinanDesaDinasLayout.setVisibility(View.VISIBLE);
                perkawinanDesaDinasAutoComplete.setText(null);
                getDesaDinas();
            }
        });

        desaDinasSelectionAdapter = new DesaDinasSelectionAdapter(this, desaDinasArrayList);
        perkawinanDesaDinasAutoComplete.setAdapter(desaDinasSelectionAdapter);
        perkawinanDesaDinasAutoComplete.setThreshold(100);
        perkawinanDesaDinasAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                desaDinas = (DesaDinas) adapterView.getItemAtPosition(i);
                perkawinanDesaDinasAutoComplete.setText(desaDinas.getName());
                idDesaDinas = desaDinas.getId();
                isDesaDinasSelected = true;
            }
        });

        getProvinsi();
    }


    public void getProvinsi() {
        disableForm();
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<ProvinsiGetResponse> provinsiGetResponseCall = getData.getMasterProvinsi();
        provinsiGetResponseCall.enqueue(new Callback<ProvinsiGetResponse>() {
            @Override
            public void onResponse(Call<ProvinsiGetResponse> call, Response<ProvinsiGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berhasil memuat data Provinsi.", Snackbar.LENGTH_LONG).show();
                    provinsis.clear();
                    provinsis.addAll(response.body().getData());
                    provinsiSelectionAdapter.notifyDataSetChanged();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Gagal memuat data Provinsi. Silahkan coba lagi nanti.", Snackbar.LENGTH_LONG).show();
                }
                enableForm();
            }

            @Override
            public void onFailure(Call<ProvinsiGetResponse> call, Throwable t) {
                enableForm();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                        "Gagal memuat data Provinsi. Silahkan coba lagi nanti.", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void getKabupaten() {
        disableForm();
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KabupatenGetResponse> kabupatenGetResponseCall = getData.getMasterKabupatenProv(String.valueOf(idProvinsi));
        kabupatenGetResponseCall.enqueue(new Callback<KabupatenGetResponse>() {
            @Override
            public void onResponse(Call<KabupatenGetResponse> call, Response<KabupatenGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berhasil memuat data Kabupaten.", Snackbar.LENGTH_LONG).show();
                    kabupatens.clear();
                    kabupatens.addAll(response.body().getData());
                    kabupatenSelectionAdapter.notifyDataSetChanged();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Gagal memuat data Kabupaten. Silahkan coba lagi nanti.", Snackbar.LENGTH_LONG).show();
                }
                enableForm();
            }

            @Override
            public void onFailure(Call<KabupatenGetResponse> call, Throwable t) {
                enableForm();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                        "Gagal memuat data Kabupaten. Silahkan coba lagi nanti.", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void getKecamatan() {
        disableForm();
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KecamatanGetResponse> kecamatanGetResponseCall = getData.getMasterKecamatan(String.valueOf(idKabupaten));
        kecamatanGetResponseCall.enqueue(new Callback<KecamatanGetResponse>() {
            @Override
            public void onResponse(Call<KecamatanGetResponse> call, Response<KecamatanGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Berhasil memuat data Kecamatan. Silahkan pilih Kecamatan.", Snackbar.LENGTH_LONG).show();
                    kecamatans.clear();
                    kecamatans.addAll(response.body().getData());
                    kecamatanSelectionAdapter.notifyDataSetChanged();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Gagal memuat data Kecamatan. Silahkan coba lagi nanti.", Snackbar.LENGTH_LONG).show();
                }
                enableForm();
            }

            @Override
            public void onFailure(Call<KecamatanGetResponse> call, Throwable t) {
                enableForm();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                        "Gagal memuat data Kecamatan. Silahkan coba lagi nanti.", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void getDesaDinas() {
        disableForm();
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<DesaDinasGetResponse> desaDinasGetResponseCall = getData.getMasterDesaDinas(String.valueOf(idKecamatan));
        desaDinasGetResponseCall.enqueue(new Callback<DesaDinasGetResponse>() {
            @Override
            public void onResponse(Call<DesaDinasGetResponse> call, Response<DesaDinasGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Berhasil memuat data Desa/Kelurahan. Silahkan pilih Desa/Kelurahan.", Snackbar.LENGTH_LONG).show();
                    desaDinasArrayList.clear();
                    desaDinasArrayList.addAll(response.body().getData());
                    desaDinasSelectionAdapter.notifyDataSetChanged();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Gagal memuat data Desa/Kelurahan. Silahkan coba lagi nanti.", Snackbar.LENGTH_LONG).show();
                }
                enableForm();
            }

            @Override
            public void onFailure(Call<DesaDinasGetResponse> call, Throwable t) {
                enableForm();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                        "Gagal memuat data Desa/Kelurahan. Silahkan coba lagi nanti.", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void disableForm() {
        perkawinanPasanganLoadingLayout.setVisibility(View.VISIBLE);
        perkawinanKabupatenLayout.setEnabled(false);
        perkawinanKecamatanLayout.setEnabled(false);
        perkawinanDesaDinasLayout.setEnabled(false);
        perkawinanProvinsiLayout.setEnabled(false);
    }

    public void enableForm() {
        perkawinanPasanganLoadingLayout.setVisibility(View.GONE);
        perkawinanKabupatenLayout.setEnabled(true);
        perkawinanKecamatanLayout.setEnabled(true);
        perkawinanDesaDinasLayout.setEnabled(true);
        perkawinanProvinsiLayout.setEnabled(true);
    }
}