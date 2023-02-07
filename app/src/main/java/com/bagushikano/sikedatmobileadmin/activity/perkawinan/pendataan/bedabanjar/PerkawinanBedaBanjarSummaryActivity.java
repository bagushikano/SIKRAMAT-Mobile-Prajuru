package com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.bedabanjar;

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
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.satubanjar.PerkawinanSatuBanjarCompleteActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.satubanjar.PerkawinanSatuBanjarSummaryActivity;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
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

public class PerkawinanBedaBanjarSummaryActivity extends AppCompatActivity {


    Gson gson = new Gson();
    private final String PURUSA_KEY = "PURUSA_SELECT_KEY";
    private final String PRADANA_KEY = "PRADANA_SELECT_KEY";
    private final String PERKAWINAN_KEY = "PERKAWINAN_KEY";
    private CacahKramaMipil purusaKrama, pradanaKrama;
    Perkawinan perkawinan;

    private Uri uriAktaPerkawinan, uriBuktiSerahTerimaPerkawinan;
    private final String AKTA_FILE_KEY = "BERKAS_AKTA_PERKAWINAN_KEY",
            SERAH_TERIMA_FILE_KEY = "BERKAS_SERAH_TERIMA_PERKAWINAN_KEY";

    private Toolbar homeToolbar;

    ApiRoute retro;
    SharedPreferences loginPreferences;

    TextView tanggalPerkawinanText, namaPemuputText, noBuktiSerahTerimaText, noAktaText,
            keteranganText, statusKekeluargaanText, calonKramaText;
    Button aktaFileButton, serahTerimaFileButton, saveDraftButton, saveSahButton;
    LinearLayout perkawinanProgressLayout, perkawinanCalonKramaLayout;
    private TextView kramaPurusaName, kramaPurusaNik, noKramaMipilPurusa;
    private Button kramaMipilPurusaDetailButton;
    private LinearLayout kramaPurusaImageLoadingContainer;
    private MaterialCardView kramaPurusaCard;
    private ImageView kramaPurusaImage;

    private TextView kramaPradanaName, kramaPradanaNik, noKramaMipilPradana;
    private Button kramaMipilPradanaDetailButton;
    private LinearLayout kramaPradanaImageLoadingContainer;
    private MaterialCardView kramaPradanaCard;
    private ImageView kramaPradanaImage;

    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    private int idBanjarPradana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perkawinan_beda_banjar_summary);

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        kramaPurusaName = findViewById(R.id.krama_nama_text);
        kramaPurusaNik = findViewById(R.id.krama_nik_text);
        noKramaMipilPurusa = findViewById(R.id.krama_no_mipil_text);
        kramaMipilPurusaDetailButton = findViewById(R.id.krama_purusa_detail_button);
        kramaPurusaImageLoadingContainer = findViewById(R.id.krama_image_loading_container);
        kramaPurusaImage = findViewById(R.id.krama_image);
        kramaPurusaCard = findViewById(R.id.krama_purusa_card);

        kramaPradanaName = findViewById(R.id.krama_pradana_nama_text);
        kramaPradanaNik = findViewById(R.id.krama_pradana_nik_text);
        noKramaMipilPradana = findViewById(R.id.krama_pradana_no_mipil_text);
        kramaMipilPradanaDetailButton = findViewById(R.id.krama_pradana_detail_button);
        kramaPradanaImageLoadingContainer = findViewById(R.id.krama_pradana_image_loading_container);
        kramaPradanaImage = findViewById(R.id.krama_pradana_image);
        kramaPradanaCard = findViewById(R.id.krama_pradana_card);

        tanggalPerkawinanText = findViewById(R.id.perkawinan_tanggal_text);
        namaPemuputText = findViewById(R.id.perkawinan_nama_pemuput_text);
        noBuktiSerahTerimaText = findViewById(R.id.perkawinan_no_serah_terima_text);
        noAktaText = findViewById(R.id.perkawinan_no_akta_text);
        keteranganText = findViewById(R.id.perkawinan_keterangan_text);
        aktaFileButton = findViewById(R.id.perkawinan_berkas_akta_show);
        serahTerimaFileButton = findViewById(R.id.perkawinan_berkas_serah_terima_show);
        perkawinanProgressLayout = findViewById(R.id.perkawinan_pendataan_progress_layout);
        saveSahButton = findViewById(R.id.perkawinan_store_sah_button);
        saveDraftButton = findViewById(R.id.perkawinan_store_draft_button);
        calonKramaText = findViewById(R.id.perkawinan_calon_krama_text);
        perkawinanCalonKramaLayout = findViewById(R.id.perkawinan_calon_krama_layout);
        statusKekeluargaanText = findViewById(R.id.perkawinan_status_kekeluargaan_text);

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
        purusaKrama = gson.fromJson(getIntent().getStringExtra(PURUSA_KEY), CacahKramaMipil.class);
        idBanjarPradana = getIntent().getIntExtra("BANJAR_KEY", -1);

        if (getIntent().hasExtra(AKTA_FILE_KEY)) {
            uriAktaPerkawinan = Uri.parse(getIntent().getStringExtra(AKTA_FILE_KEY));
            noAktaText.setText(perkawinan.getNomorAktaPerkawinan());
            aktaFileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FileOpenUtil fileOpenUtil = new FileOpenUtil(PerkawinanBedaBanjarSummaryActivity.this);
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
                    FileOpenUtil fileOpenUtil = new FileOpenUtil(PerkawinanBedaBanjarSummaryActivity.this);
                    fileOpenUtil.openFile(uriBuktiSerahTerimaPerkawinan);
                }
            });
        }
        tanggalPerkawinanText.setText(ChangeDateTimeFormat.changeDateFormat(perkawinan.getTanggalPerkawinan()));
        namaPemuputText.setText(perkawinan.getNamaPemuput());
        if (perkawinan.getKeterangan() != null) {
            if (perkawinan.getKeterangan().length() != 0) {
                keteranganText.setText(perkawinan.getKeterangan());
            } else {
                keteranganText.setText("-");
            }
        }
        if (perkawinan.getStatusKekeluargaan().equals("baru")) {
            statusKekeluargaanText.setText("Pembentukan Krama Mipil (Kepala Keluarga) Baru");
            perkawinanCalonKramaLayout.setVisibility(View.VISIBLE);
            if (perkawinan.getCalonKramaId().equals(pradanaKrama.getId())) {
                calonKramaText.setText(pradanaKrama.getPenduduk().getNama());
            } else {
                calonKramaText.setText(purusaKrama.getPenduduk().getNama());
            }
        } else {
            statusKekeluargaanText.setText("Tetap di Krama Mipil (Kepala Keluarga) Lama");
        }

        // purusa
        String namaFormated = purusaKrama.getPenduduk().getNama();
        if (purusaKrama.getPenduduk().getGelarDepan() != null) {
            namaFormated = String.format("%s %s",
                    purusaKrama.getPenduduk().getGelarDepan(),
                    purusaKrama.getPenduduk().getNama()
            );
        }


        if (purusaKrama.getPenduduk().getGelarBelakang() != null) {
            namaFormated = String.format("%s %s",
                    namaFormated,
                    purusaKrama.getPenduduk().getGelarBelakang()
            );
        }
        kramaPurusaName.setText(namaFormated);
        kramaPurusaNik.setText(purusaKrama.getPenduduk().getNik());
        noKramaMipilPurusa.setText(purusaKrama.getNomorCacahKramaMipil());
        kramaMipilPurusaDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                kramaDetail.putExtra(KRAMA_DETAIL_KEY, purusaKrama.getId());
                startActivity(kramaDetail);
            }
        });
        if (purusaKrama.getPenduduk().getFoto() != null) {
            kramaPurusaImageLoadingContainer.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(getResources().getString(R.string.image_endpoint) +
                            purusaKrama.getPenduduk().getFoto())
                    .fit().centerCrop()
                    .into(kramaPurusaImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            kramaPurusaImageLoadingContainer.setVisibility(View.GONE);
                            kramaPurusaImage.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                            kramaPurusaImageLoadingContainer.setVisibility(View.GONE);
                            Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaPurusaImage);
                        }
                    });
        }
        else {
            Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaPurusaImage);
            kramaPurusaImage.setVisibility(View.VISIBLE);
        }
        kramaPurusaCard.setVisibility(View.VISIBLE);

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
        RequestBody purusaData, pradanaData, banjarPradana, noBuktiSerahTerimaData, noAktaData, tanggalPerkawinanData,
                keteranganData, statusKekeluargaanData, calonKepalaKeluargaData, namaPemuputData, fileBuktiSerahTerimaData, fileAktaData;
        Call<PerkawinanDetailResponse> perkawinanStoreResponseCall = null;

        purusaData = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(purusaKrama.getId()));
        pradanaData = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(pradanaKrama.getId()));
        banjarPradana = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(idBanjarPradana));
        noBuktiSerahTerimaData = RequestBody.create(MediaType.parse("text/plain"), perkawinan.getNomorPerkawinan());
        noAktaData = RequestBody.create(MediaType.parse("text/plain"), perkawinan.getNomorAktaPerkawinan());
        tanggalPerkawinanData = RequestBody.create(MediaType.parse("text/plain"), perkawinan.getTanggalPerkawinan());
        keteranganData = RequestBody.create(MediaType.parse("text/plain"), perkawinan.getKeterangan());
        namaPemuputData = RequestBody.create(MediaType.parse("text/plain"), perkawinan.getNamaPemuput());
        statusKekeluargaanData = RequestBody.create(MediaType.parse("text/plain"), perkawinan.getStatusKekeluargaan());
        if (perkawinan.getStatusKekeluargaan().equals("baru")) {
            calonKepalaKeluargaData = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(perkawinan.getCalonKramaId()));
        } else {
            calonKepalaKeluargaData = null;
        }

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

        if (getIntent().hasExtra("EDIT_PERKAWINAN")) {
            RequestBody idPerkawinanData;
            idPerkawinanData = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(perkawinan.getId()));
            perkawinanStoreResponseCall = retro.perkawinanEditBedaBanjarAdat(
                    "Bearer " + loginPreferences.getString("token", "empty"),
                    purusaData, pradanaData, banjarPradana, noBuktiSerahTerimaData, noAktaData, tanggalPerkawinanData,
                    keteranganData, statusKekeluargaanData, calonKepalaKeluargaData,
                    namaPemuputData, buktiSerahTerimaFile, aktaFile, idPerkawinanData
            );
        } else {
            perkawinanStoreResponseCall = retro.perkawinanStoreBedaBanjarAdat(
                    "Bearer " + loginPreferences.getString("token", "empty"),
                    purusaData, pradanaData, banjarPradana, noBuktiSerahTerimaData, noAktaData, tanggalPerkawinanData,
                    keteranganData, statusKekeluargaanData, calonKepalaKeluargaData,
                    namaPemuputData, buktiSerahTerimaFile, aktaFile
            );
        }

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
                    Intent perkawinanPendataanCompleteIntent = new Intent(getApplicationContext(), PerkawinanBedaBanjarCompleteActivity.class);
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