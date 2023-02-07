package com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.campuranmasuk;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.documentfile.provider.DocumentFile;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.krama.KramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.satubanjar.MaperasSatuBanjarCompleteActivity;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaperasCampuranMasukSummaryActivity extends AppCompatActivity {

    Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    private final String ANAK_KEY = "ANAK_KEY";
    private final String KRAMA_LAMA_KEY = "KRAMA_LAMA_KEY";
    private final String KRAMA_BARU_KEY = "KRAMA_BARU_KEY";
    private final String AYAH_KEY = "AYAH_KEY";
    private final String IBU_KEY = "IBU_KEY";
    private final String MAPERAS_KEY = "MAPERAS_KEY";

    ActivityResultLauncher<Intent> startActivityIntent;
    SharedPreferences loginPreferences;

    private Uri uriAktaMaperas, uriBuktiSerahTerimaMaperas, anakFotoUri;
    private final String AKTA_FILE_KEY = "BERKAS_AKTA_MAPERAS_KEY",
            SERAH_TERIMA_FILE_KEY = "BERKAS_SERAH_TERIMA_MAPERAS_KEY";

    private Toolbar homeToolbar;

    TextView tanggalMaperasText, namaPemuputText, noBuktiMaperas, noAktaText,
            keteranganText;
    Button aktaFileButton, buktiMaperasFileButton, saveDraftButton, saveSahButton;

    private MaterialCardView kramaMipilBaruCard;
    private TextView kramaMipilBaruName, kramaMipilBaruNo, kramaMipilBaruBanjarAdat;
    private Button kramaMipilBaruDetailButton;

    private TextView kramaAnakName, kramaAnakNik, noKramaMipilAnak;
    private Button kramaMipilAnakDetailButton;
    private LinearLayout kramaAnakImageLoadingContainer;
    private MaterialCardView kramaAnakCard;
    private ImageView kramaAnakImage;

    private TextView ayahBaruText, ibuBaruText;
    private TextInputEditText ayahLamaField, ibuLamaField, ayahNikLamaField, ibuNikLamaField;

    private Maperas maperas;
    private CacahKramaMipil anakMaperas, ayahBaru, ibuBaru;

    private KramaMipil kramaMipilBaru;

    LinearLayout maperasProgressLayout;

    ApiRoute retro;

    private final String KRAMA_MIPIL_DETAIL_KEY = "KRAMA_MIPIL_DETAIL_KEY";
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maperas_campuran_masuk_summary);

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
        kramaMipilBaru =  gson.fromJson(getIntent().getStringExtra(KRAMA_BARU_KEY), KramaMipil.class);

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

        ayahBaruText = findViewById(R.id.anak_ayah_baru_nama_text);
        ibuBaruText = findViewById(R.id.anak_ibu_baru_nama_text);

        ayahLamaField = findViewById(R.id.maperas_ayah_lama_field);
        ibuLamaField = findViewById(R.id.maperas_ibu_lama_field);
        ayahNikLamaField = findViewById(R.id.maperas_ayah_nik_lama_field);
        ibuNikLamaField = findViewById(R.id.maperas_ibu_nik_lama_field);

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
                    FileOpenUtil fileOpenUtil = new FileOpenUtil(MaperasCampuranMasukSummaryActivity.this);
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
                    FileOpenUtil fileOpenUtil = new FileOpenUtil(MaperasCampuranMasukSummaryActivity.this);
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

        if (maperas.getNamaAyahLama() != null) {
            ayahLamaField.setText(maperas.getNamaAyahLama().toString());
            ayahNikLamaField.setText(maperas.getNikAyahLama().toString());
        }
        if (maperas.getNamaIbuLama() != null ) {
            ibuLamaField.setText(maperas.getNamaIbuLama().toString());
            ibuNikLamaField.setText(maperas.getNikIbuLama().toString());
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
        noKramaMipilAnak.setText("-");
        kramaMipilAnakDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
//                kramaDetail.putExtra(KRAMA_DETAIL_KEY, anakMaperas.getId());
//                startActivity(kramaDetail);
            }
        });
        if (anakMaperas.getPenduduk().getFoto() != null) {
            Bitmap bitmap = null;
            try {
                anakFotoUri = Uri.parse(anakMaperas.getPenduduk().getFoto());
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), anakFotoUri);
                kramaAnakImage.setImageBitmap(bitmap);
                kramaAnakImageLoadingContainer.setVisibility(View.GONE);
                kramaAnakImage.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
                Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaAnakImage);
                kramaAnakImage.setVisibility(View.VISIBLE);
            }
        }
        else {
            Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaAnakImage);
            kramaAnakImage.setVisibility(View.VISIBLE);
        }
    }

    public void submitData() {
        RequestBody maperasJsonData, fileBuktiSerahTerimaData, fileAktaData, anakjsonData,
                fileFotoAnak, fileSudhiWadhani;
        Call<MaperasDetailResponse> maperasDetailResponseCall = null;

        maperas.setKramaMipilBaruId(kramaMipilBaru.getId());
        maperas.setCacahKramaMipilBaruId(anakMaperas.getId());
        maperas.setAyahBaruId(ayahBaru.getId());
        maperas.setIbuBaruId(ibuBaru.getId());

        maperasJsonData = RequestBody.create(MediaType.parse("text/plain"), gson.toJson(maperas));
        anakjsonData = RequestBody.create(MediaType.parse("text/plain"), gson.toJson(anakMaperas));

        MultipartBody.Part buktiSerahTerimaFile = null;
        MultipartBody.Part aktaFile = null;
        MultipartBody.Part sudhiWadhaniFile = null;
        MultipartBody.Part fotoAnakFile = null;

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

        if (maperas.getFileSudhiWadhani() != null) {
            File sudhiWadhani = new File(getApplicationContext().getCacheDir(),
                    maperas.getFileSudhiWadhani().toString());
            fileSudhiWadhani = RequestBody.create(MediaType.parse("multipart/form-data"), sudhiWadhani);
            sudhiWadhaniFile = MultipartBody.Part.createFormData("file_sudhi_wadhani", sudhiWadhani.getName(), fileSudhiWadhani);
        }

        if (anakMaperas.penduduk.getFoto() != null ) {
            File imageFile = new File(getApplicationContext().getCacheDir(), "profile_anak.jpg");
            fileFotoAnak = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            fotoAnakFile = MultipartBody.Part.createFormData("foto", imageFile.getName(), fileFotoAnak);
        }

        maperasDetailResponseCall = retro.storeMaperasCampuranMasuk(
                "Bearer " + loginPreferences.getString("token", "empty"),
                buktiSerahTerimaFile,
                aktaFile,
                maperasJsonData,
                anakjsonData,
                fotoAnakFile,
                sudhiWadhaniFile
        );

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
                    Intent perkawinanPendataanCompleteIntent = new Intent(getApplicationContext(), MaperasCampuranMasukCompleteActivity.class);
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