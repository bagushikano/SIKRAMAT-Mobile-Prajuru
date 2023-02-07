package com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.campurankeluar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.documentfile.provider.DocumentFile;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.campuranmasuk.PerkawinanCampuranMasukCompleteActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.satubanjar.PerkawinanSatuBanjarCompleteActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.satubanjar.PerkawinanSatuBanjarSummaryActivity;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.master.DesaDinas;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.Perkawinan;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.PerkawinanDetailResponse;
import com.bagushikano.sikedatmobileadmin.util.ChangeDateTimeFormat;
import com.bagushikano.sikedatmobileadmin.util.FileOpenUtil;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerkawinanCampuranKeluarSummaryActivity extends AppCompatActivity {


    Gson gson = new Gson();

    private final String PRADANA_KEY = "PRADANA_SELECT_KEY";
    private final String PERKAWINAN_KEY = "PERKAWINAN_KEY";
    private final String DESA_DINAS_KEY = "DESA_DINAS_KEY";

    private CacahKramaMipil pradanaKrama;
    Perkawinan perkawinan;
    DesaDinas desaDinas;

    private Uri uriAktaPerkawinan, uriBuktiSerahTerimaPerkawinan;
    private final String AKTA_FILE_KEY = "BERKAS_AKTA_PERKAWINAN_KEY",
            SERAH_TERIMA_FILE_KEY = "BERKAS_SERAH_TERIMA_PERKAWINAN_KEY";

    private Toolbar homeToolbar;

    ApiRoute retro;
    SharedPreferences loginPreferences;

    TextView tanggalPerkawinanText, noBuktiSerahTerimaText, noAktaText,
            keteranganText;
    Button aktaFileButton, serahTerimaFileButton, saveDraftButton, saveSahButton;
    LinearLayout perkawinanProgressLayout, perkawinanCalonKramaLayout;


    private TextView kramaPradanaName, kramaPradanaNik, noKramaMipilPradana;
    private Button kramaMipilPradanaDetailButton;
    private LinearLayout kramaPradanaImageLoadingContainer;
    private MaterialCardView kramaPradanaCard;
    private ImageView kramaPradanaImage;

    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    private TextView nikPasangan, namaPasangan, agamaPasangan, alamatPasangan, desaPasangan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perkawinan_campuran_keluar_summary);

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nikPasangan = findViewById(R.id.nik_pasangan_text);
        namaPasangan = findViewById(R.id.nama_pasangan_text);
        agamaPasangan = findViewById(R.id.agama_pasangan_text);
        alamatPasangan = findViewById(R.id.alamat_asal_pasangan_text);
        desaPasangan = findViewById(R.id.desdinas_asal_pradana_text);

        kramaPradanaName = findViewById(R.id.krama_pradana_nama_text);
        kramaPradanaNik = findViewById(R.id.krama_pradana_nik_text);
        noKramaMipilPradana = findViewById(R.id.krama_pradana_no_mipil_text);
        kramaMipilPradanaDetailButton = findViewById(R.id.krama_pradana_detail_button);
        kramaPradanaImageLoadingContainer = findViewById(R.id.krama_pradana_image_loading_container);
        kramaPradanaImage = findViewById(R.id.krama_pradana_image);
        kramaPradanaCard = findViewById(R.id.krama_pradana_card);

        tanggalPerkawinanText = findViewById(R.id.perkawinan_tanggal_text);

        noBuktiSerahTerimaText = findViewById(R.id.perkawinan_no_serah_terima_text);
        noAktaText = findViewById(R.id.perkawinan_no_akta_text);
        keteranganText = findViewById(R.id.perkawinan_keterangan_text);
        aktaFileButton = findViewById(R.id.perkawinan_berkas_akta_show);
        serahTerimaFileButton = findViewById(R.id.perkawinan_berkas_serah_terima_show);
        perkawinanProgressLayout = findViewById(R.id.perkawinan_pendataan_progress_layout);
        saveSahButton = findViewById(R.id.perkawinan_store_sah_button);
        saveDraftButton = findViewById(R.id.perkawinan_store_draft_button);
        perkawinanCalonKramaLayout = findViewById(R.id.perkawinan_calon_krama_layout);


        saveDraftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });

        saveSahButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        retro = RetrofitClient.buildRetrofit().create(ApiRoute.class);

        perkawinan = gson.fromJson(getIntent().getStringExtra(PERKAWINAN_KEY), Perkawinan.class);
        pradanaKrama = gson.fromJson(getIntent().getStringExtra(PRADANA_KEY), CacahKramaMipil.class);
        desaDinas = gson.fromJson(getIntent().getStringExtra(DESA_DINAS_KEY), DesaDinas.class);

        nikPasangan.setText(perkawinan.getNikPasangan().toString());
        namaPasangan.setText(perkawinan.getNamaPasangan().toString());
        agamaPasangan.setText(perkawinan.getAgamaPasangan().toString());
        alamatPasangan.setText(perkawinan.getAlamatAsalPasangan().toString());
        desaPasangan.setText(desaDinas.getName());

        if (getIntent().hasExtra(AKTA_FILE_KEY)) {
            uriAktaPerkawinan = Uri.parse(getIntent().getStringExtra(AKTA_FILE_KEY));
            noAktaText.setText(perkawinan.getNomorAktaPerkawinan());
            aktaFileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FileOpenUtil fileOpenUtil = new FileOpenUtil(PerkawinanCampuranKeluarSummaryActivity.this);
                    fileOpenUtil.openFile(uriAktaPerkawinan);
                }
            });
        } else {
            aktaFileButton.setVisibility(View.GONE);
            noAktaText.setText("-");
        }
        if (getIntent().hasExtra(SERAH_TERIMA_FILE_KEY)) {
            uriBuktiSerahTerimaPerkawinan = Uri.parse(getIntent().getStringExtra(SERAH_TERIMA_FILE_KEY));
            noBuktiSerahTerimaText.setText(perkawinan.getNomorPerkawinan());
            serahTerimaFileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FileOpenUtil fileOpenUtil = new FileOpenUtil(PerkawinanCampuranKeluarSummaryActivity.this);
                    fileOpenUtil.openFile(uriBuktiSerahTerimaPerkawinan);
                }
            });
        }
        tanggalPerkawinanText.setText(ChangeDateTimeFormat.changeDateFormat(perkawinan.getTanggalPerkawinan()));

        if (perkawinan.getKeterangan() != null) {
            if (perkawinan.getKeterangan().length() != 0) {
                keteranganText.setText(perkawinan.getKeterangan());
            } else {
                keteranganText.setText("-");
            }
        }

        // purusa
        String namaFormated;
        namaFormated = pradanaKrama.getPenduduk().getNama();
        if (pradanaKrama.getPenduduk().getGelarDepan() != null) {
            namaFormated = String.format("%s %s",
                    pradanaKrama.getPenduduk().getGelarDepan(),
                    pradanaKrama.getPenduduk().getNama()
            );
        }


        if (pradanaKrama.getPenduduk().getGelarBelakang() != null) {
            namaFormated = String.format("%s %s",
                    namaFormated,
                    pradanaKrama.getPenduduk().getGelarBelakang()
            );
        }
        kramaPradanaName.setText(namaFormated);
        kramaPradanaNik.setText(pradanaKrama.getPenduduk().getNik());
        noKramaMipilPradana.setText(pradanaKrama.getNomorCacahKramaMipil());
        kramaMipilPradanaDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                kramaDetail.putExtra(KRAMA_DETAIL_KEY, pradanaKrama.getId());
                startActivity(kramaDetail);
            }
        });
        if (pradanaKrama.getPenduduk().getFoto() != null) {
            kramaPradanaImageLoadingContainer.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(getResources().getString(R.string.image_endpoint) +
                            pradanaKrama.getPenduduk().getFoto())
                    .fit().centerCrop()
                    .into(kramaPradanaImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            kramaPradanaImageLoadingContainer.setVisibility(View.GONE);
                            kramaPradanaImage.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                            kramaPradanaImageLoadingContainer.setVisibility(View.GONE);
                            Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaPradanaImage);
                        }
                    });
        }
        else {
            Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaPradanaImage);
            kramaPradanaImage.setVisibility(View.VISIBLE);
        }
        kramaPradanaCard.setVisibility(View.VISIBLE);
    }


    public void submitData() {
        RequestBody perkawinanData, fileBuktiSerahTerimaData, fileAktaData;
        Call<PerkawinanDetailResponse> perkawinanStoreResponseCall = null;

        perkawinanData = RequestBody.create(MediaType.parse("text/plain"), gson.toJson(perkawinan));

        MultipartBody.Part buktiSerahTerimaFile = null;
        MultipartBody.Part aktaFile = null;

        /**
         * multi part akta
         */

        if (uriAktaPerkawinan != null) {
            File filesDir = getApplicationContext().getCacheDir();
            File file = new File(filesDir, DocumentFile.fromSingleUri(this, uriAktaPerkawinan).getName());
            OutputStream os;
            ContentResolver cr = getApplicationContext().getContentResolver();
            InputStream inputStream = null;
            try {
                inputStream = cr.openInputStream(uriAktaPerkawinan);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                os = new FileOutputStream(file);
                int length;
                byte[] bytes = new byte[1024];
                while ((length = inputStream.read(bytes)) != -1) {
                    os.write(bytes, 0, length);
                }
                os.flush();
                os.close();
                inputStream.close();
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Error writing temp file", e);
            }
            fileAktaData = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            aktaFile = MultipartBody.Part.createFormData("file_akta_perkawinan", file.getName(), fileAktaData);
        }

        /**
         * multi part serah terima
         */

        if (uriBuktiSerahTerimaPerkawinan != null) {
            File filesDir = getApplicationContext().getCacheDir();
            File file = new File(filesDir, DocumentFile.fromSingleUri(this, uriBuktiSerahTerimaPerkawinan).getName());
            OutputStream os;
            ContentResolver cr = getApplicationContext().getContentResolver();
            InputStream inputStream = null;
            try {
                inputStream = cr.openInputStream(uriBuktiSerahTerimaPerkawinan);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                os = new FileOutputStream(file);
                int length;
                byte[] bytes = new byte[1024];
                while ((length = inputStream.read(bytes)) != -1) {
                    os.write(bytes, 0, length);
                }
                os.flush();
                os.close();
                inputStream.close();
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Error writing temp file", e);
            }
            fileBuktiSerahTerimaData = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            buktiSerahTerimaFile = MultipartBody.Part.createFormData("file_bukti_serah_terima_perkawinan", file.getName(), fileBuktiSerahTerimaData);
        }

        perkawinanStoreResponseCall = retro.perkawinanStoreCampuranKeluar(
                "Bearer " + loginPreferences.getString("token", "empty"), buktiSerahTerimaFile, aktaFile, perkawinanData
        );

        saveSahButton.setVisibility(View.GONE);
        saveDraftButton.setVisibility(View.GONE);
        perkawinanProgressLayout.setVisibility(View.VISIBLE);

        perkawinanStoreResponseCall.enqueue(new Callback<PerkawinanDetailResponse>() {
            @Override
            public void onResponse(Call<PerkawinanDetailResponse> call, Response<PerkawinanDetailResponse> response) {
                saveSahButton.setVisibility(View.VISIBLE);
                saveDraftButton.setVisibility(View.VISIBLE);
                perkawinanProgressLayout.setVisibility(View.GONE);
                if (response.code() == 200 && response.body().getStatusCode() == 200 && response.body().getMessage().equals("data perkawinan sukses")) {
                    Intent perkawinanPendataanCompleteIntent = new Intent(getApplicationContext(), PerkawinanCampuranKeluarCompleteActivity.class);
                    Gson gson = new Gson();
                    String perkawinanJson = gson.toJson(response.body().getPerkawinan());
                    perkawinanPendataanCompleteIntent.putExtra("PERKAWINAN_KEY", perkawinanJson);
                    startActivity(perkawinanPendataanCompleteIntent);
                    setResult(1);
                    finish();
                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PerkawinanDetailResponse> call, Throwable t) {
                saveSahButton.setVisibility(View.VISIBLE);
                saveDraftButton.setVisibility(View.VISIBLE);
                perkawinanProgressLayout.setVisibility(View.GONE);
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}