package com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.bedabanjar;

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

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.documentfile.provider.DocumentFile;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.krama.KramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.satubanjar.MaperasSatuBanjarSummaryActivity;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipil;
import com.bagushikano.sikedatmobileadmin.model.maperas.Maperas;
import com.bagushikano.sikedatmobileadmin.model.maperas.MaperasDetailResponse;
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

public class MaperasBedaBanjarSummaryActivity extends AppCompatActivity {


    Gson gson = new Gson();
    private final String ANAK_KEY = "ANAK_KEY";
    private final String KRAMA_LAMA_KEY = "KRAMA_LAMA_KEY";
    private final String KRAMA_BARU_KEY = "KRAMA_BARU_KEY";
    private final String AYAH_KEY = "AYAH_KEY";
    private final String IBU_KEY = "IBU_KEY";
    private final String MAPERAS_KEY = "MAPERAS_KEY";

    ActivityResultLauncher<Intent> startActivityIntent;
    SharedPreferences loginPreferences;

    private Uri uriAktaMaperas, uriBuktiSerahTerimaMaperas;
    private final String AKTA_FILE_KEY = "BERKAS_AKTA_MAPERAS_KEY",
            SERAH_TERIMA_FILE_KEY = "BERKAS_SERAH_TERIMA_MAPERAS_KEY";

    private Toolbar homeToolbar;

    TextView tanggalMaperasText, namaPemuputText, noBuktiMaperas, noAktaText,
            keteranganText;
    Button aktaFileButton, buktiMaperasFileButton, saveDraftButton, saveSahButton;

    private MaterialCardView kramaMipilCard;
    private TextView kramaMipilName, kramaMipilNo, kramaMipilBanjarAdat;
    private Button kramaMipilDetailButton;

    private MaterialCardView kramaMipilBaruCard;
    private TextView kramaMipilBaruName, kramaMipilBaruNo, kramaMipilBaruBanjarAdat;
    private Button kramaMipilBaruDetailButton;

    private TextView kramaAnakName, kramaAnakNik, noKramaMipilAnak;
    private Button kramaMipilAnakDetailButton;
    private LinearLayout kramaAnakImageLoadingContainer;
    private MaterialCardView kramaAnakCard;
    private ImageView kramaAnakImage;

    private TextView ayahLamaText, ayahBaruText, ibuLamaText, ibuBaruText;

    private Maperas maperas;
    private CacahKramaMipil anakMaperas, ayahBaru, ibuBaru;

    private KramaMipil kramaMipilLama, kramaMipilBaru;

    LinearLayout maperasProgressLayout;

    ApiRoute retro;

    private final String KRAMA_MIPIL_DETAIL_KEY = "KRAMA_MIPIL_DETAIL_KEY";
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maperas_beda_banjar_summary);

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        maperas = gson.fromJson(getIntent().getStringExtra(MAPERAS_KEY), Maperas.class);
        anakMaperas = gson.fromJson(getIntent().getStringExtra(ANAK_KEY), CacahKramaMipil.class);
        ayahBaru = gson.fromJson(getIntent().getStringExtra(AYAH_KEY), CacahKramaMipil.class);
        ibuBaru = gson.fromJson(getIntent().getStringExtra(IBU_KEY), CacahKramaMipil.class);
        kramaMipilLama =  gson.fromJson(getIntent().getStringExtra(KRAMA_LAMA_KEY), KramaMipil.class);
        kramaMipilBaru =  gson.fromJson(getIntent().getStringExtra(KRAMA_BARU_KEY), KramaMipil.class);

        kramaMipilName = findViewById(R.id.krama_lama_nama_text);
        kramaMipilNo = findViewById(R.id.krama_lama_no_mipil_text);
        kramaMipilBanjarAdat = findViewById(R.id.krama_lama_banjar_adat_text);
        kramaMipilDetailButton = findViewById(R.id.krama_lama_detail_button);

        kramaMipilBaruName = findViewById(R.id.krama_baru_nama_text);
        kramaMipilBaruNo = findViewById(R.id.krama_baru_no_mipil_text);
        kramaMipilBaruBanjarAdat = findViewById(R.id.krama_baru_banjar_adat_text);
        kramaMipilBaruDetailButton = findViewById(R.id.krama_baru_detail_button);

        kramaAnakName = findViewById(R.id.anak_maperas_nama_text);
        kramaAnakNik = findViewById(R.id.anak_maperas_nik_text);
        noKramaMipilAnak = findViewById(R.id.anak_maperas_no_mipil_text);
        kramaMipilAnakDetailButton = findViewById(R.id.anak_maperas_detail_button);
        kramaAnakImageLoadingContainer = findViewById(R.id.anak_maperas_image_loading_container);
        kramaAnakImage = findViewById(R.id.anak_maperas_image);
        kramaAnakCard = findViewById(R.id.anak_maperas_card);

        ayahLamaText = findViewById(R.id.anak_ayah_lama_nama_text);
        ibuLamaText = findViewById(R.id.anak_ibu_lama_nama_text);
        ayahBaruText = findViewById(R.id.anak_ayah_baru_nama_text);
        ibuBaruText = findViewById(R.id.anak_ibu_baru_nama_text);

        tanggalMaperasText = findViewById(R.id.maperas_tanggal_text);
        namaPemuputText = findViewById(R.id.maperas_nama_pemuput_text);
        noBuktiMaperas = findViewById(R.id.maperas_no_text);
        noAktaText = findViewById(R.id.maperas_no_akta_text);
        keteranganText = findViewById(R.id.maperas_keterangan_text);
        aktaFileButton = findViewById(R.id.maperas_berkas_akta_show);
        buktiMaperasFileButton = findViewById(R.id.maperas_berkas_show);
        maperasProgressLayout = findViewById(R.id.maperas_pendataan_progress_layout);
        saveSahButton = findViewById(R.id.maperas_store_sah_button);
        saveDraftButton = findViewById(R.id.maperas_store_draft_button);

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


        if (getIntent().hasExtra(AKTA_FILE_KEY)) {
            uriAktaMaperas = Uri.parse(getIntent().getStringExtra(AKTA_FILE_KEY));
            noAktaText.setText(maperas.getNomorAktaPengangkatanAnak());
            aktaFileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FileOpenUtil fileOpenUtil = new FileOpenUtil(MaperasBedaBanjarSummaryActivity.this);
                    fileOpenUtil.openFile(uriAktaMaperas);
                }
            });
        } else {
            aktaFileButton.setVisibility(View.GONE);
            noAktaText.setText("-");
        }
        if (getIntent().hasExtra(SERAH_TERIMA_FILE_KEY)) {
            uriBuktiSerahTerimaMaperas = Uri.parse(getIntent().getStringExtra(SERAH_TERIMA_FILE_KEY));
            noBuktiMaperas.setText(maperas.getNomorMaperas());
            buktiMaperasFileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FileOpenUtil fileOpenUtil = new FileOpenUtil(MaperasBedaBanjarSummaryActivity.this);
                    fileOpenUtil.openFile(uriBuktiSerahTerimaMaperas);
                }
            });
        }
        tanggalMaperasText.setText(ChangeDateTimeFormat.changeDateFormat(maperas.getTanggalMaperas()));
        namaPemuputText.setText(maperas.getNamaPemuput());
        if (maperas.getKeterangan() != null) {
            if (maperas.getKeterangan().length() != 0) {
                keteranganText.setText(maperas.getKeterangan());
            } else {
                keteranganText.setText("-");
            }
        }

        if (anakMaperas.getPenduduk().getAyah() != null) {
            ayahLamaText.setText(anakMaperas.getPenduduk().getAyah().getNama());
        }
        if (anakMaperas.getPenduduk().getIbu() != null) {
            ibuLamaText.setText(anakMaperas.getPenduduk().getIbu().getNama());
        }

        ayahBaruText.setText(ayahBaru.getPenduduk().getNama());
        ibuBaruText.setText(ibuBaru.getPenduduk().getNama());

        // krama baru
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
        kramaMipilBaruName.setText(namaFormated);
        kramaMipilBaruNo.setText(kramaMipilBaru.getNomorKramaMipil());
        kramaMipilBaruBanjarAdat.setText(kramaMipilBaru.getBanjarAdat().getNamaBanjarAdat());
        kramaMipilBaruDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kramaDetail = new Intent(getApplicationContext(), KramaMipilDetailActivity.class);
                kramaDetail.putExtra(KRAMA_MIPIL_DETAIL_KEY, kramaMipilBaru.getId());
                startActivity(kramaDetail);
            }
        });

        //krama lama
        String namaFormatedLama = kramaMipilLama.getCacahKramaMipil().getPenduduk().getNama();
        if (kramaMipilLama.getCacahKramaMipil().getPenduduk().getGelarDepan() != null) {
            namaFormatedLama = String.format("%s %s",
                    kramaMipilLama.getCacahKramaMipil().getPenduduk().getGelarDepan(),
                    kramaMipilLama.getCacahKramaMipil().getPenduduk().getNama()
            );
        }
        if (kramaMipilLama.getCacahKramaMipil().getPenduduk().getGelarBelakang() != null) {
            namaFormatedLama = String.format("%s %s",
                    namaFormatedLama,
                    kramaMipilLama.getCacahKramaMipil().getPenduduk().getGelarBelakang()
            );
        }
        kramaMipilName.setText(namaFormatedLama);
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

        // anak
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
    }

    public void submitData() {
        RequestBody maperasJsonData, fileBuktiSerahTerimaData, fileAktaData;
        Call<MaperasDetailResponse> maperasDetailResponseCall = null;

        maperas.setKramaMipilLamaId(kramaMipilLama.getId());
        maperas.setKramaMipilBaruId(kramaMipilBaru.getId());
        maperas.setCacahKramaMipilLamaId(anakMaperas.getId());
        maperas.setCacahKramaMipilBaruId(anakMaperas.getId());
        maperas.setAyahBaruId(ayahBaru.getId());
        maperas.setIbuBaruId(ibuBaru.getId());

        maperasJsonData = RequestBody.create(MediaType.parse("text/plain"), gson.toJson(maperas));

        MultipartBody.Part buktiSerahTerimaFile = null;
        MultipartBody.Part aktaFile = null;

        /**
         * multi part akta
         */

        if (uriAktaMaperas != null) {
            File filesDir = getApplicationContext().getCacheDir();
            File file = new File(filesDir, DocumentFile.fromSingleUri(this, uriAktaMaperas).getName());
            OutputStream os;
            ContentResolver cr = getApplicationContext().getContentResolver();
            InputStream inputStream = null;
            try {
                inputStream = cr.openInputStream(uriAktaMaperas);
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
            aktaFile = MultipartBody.Part.createFormData("file_akta_pengangkatan_anak", file.getName(), fileAktaData);
        }

        /**
         * multi part serah terima
         */

        if (uriBuktiSerahTerimaMaperas != null) {
            File filesDir = getApplicationContext().getCacheDir();
            File file = new File(filesDir, DocumentFile.fromSingleUri(this, uriBuktiSerahTerimaMaperas).getName());
            OutputStream os;
            ContentResolver cr = getApplicationContext().getContentResolver();
            InputStream inputStream = null;
            try {
                inputStream = cr.openInputStream(uriBuktiSerahTerimaMaperas);
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
            buktiSerahTerimaFile = MultipartBody.Part.createFormData("file_bukti_maperas", file.getName(), fileBuktiSerahTerimaData);
        }

        if (getIntent().hasExtra("EDIT_MAPERAS")) {
            maperasDetailResponseCall = retro.editMaperasBedaBanjar(
                    "Bearer " + loginPreferences.getString("token", "empty"),
                    buktiSerahTerimaFile,
                    aktaFile,
                    maperasJsonData
            );
        } else {
            maperasDetailResponseCall = retro.storeMaperasBedaBanjar(
                    "Bearer " + loginPreferences.getString("token", "empty"),
                    buktiSerahTerimaFile,
                    aktaFile,
                    maperasJsonData
            );
        }

        saveSahButton.setVisibility(View.GONE);
        saveDraftButton.setVisibility(View.GONE);
        maperasProgressLayout.setVisibility(View.VISIBLE);

        maperasDetailResponseCall.enqueue(new Callback<MaperasDetailResponse>() {
            @Override
            public void onResponse(Call<MaperasDetailResponse> call, Response<MaperasDetailResponse> response) {
                saveSahButton.setVisibility(View.VISIBLE);
                saveDraftButton.setVisibility(View.VISIBLE);
                maperasProgressLayout.setVisibility(View.GONE);
                if (response.code() == 200 && response.body().getStatusCode() == 200 && response.body().getMessage().equals("data maperas sukses")) {
                    Intent perkawinanPendataanCompleteIntent = new Intent(getApplicationContext(), MaperasBedaBanjarCompleteActivity.class);
                    Gson gson = new Gson();
                    String perkawinanJson = gson.toJson(response.body().getMaperas());
                    perkawinanPendataanCompleteIntent.putExtra("MAPERAS_KEY", perkawinanJson);
                    startActivity(perkawinanPendataanCompleteIntent);
                    setResult(1);
                    finish();
                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MaperasDetailResponse> call, Throwable t) {
                saveSahButton.setVisibility(View.VISIBLE);
                saveDraftButton.setVisibility(View.VISIBLE);
                maperasProgressLayout.setVisibility(View.GONE);
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}