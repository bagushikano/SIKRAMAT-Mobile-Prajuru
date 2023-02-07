package com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.campuranmasuk;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.krama.KramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.campuranmasuk.inputdataanak.MaperasCampuranMasukIdentitasAnakActivity;
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.satubanjar.MaperasSatuBanjarAnakSelectActivity;
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.satubanjar.MaperasSatuBanjarDataKramaBaruActivity;
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.satubanjar.MaperasSatuBanjarKramaMipilLamaSelectActivity;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipil;
import com.bagushikano.sikedatmobileadmin.model.maperas.Maperas;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.Perkawinan;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class MaperasCampuranMasukDataAnakOrtuLamaActivity extends AppCompatActivity {


    private Button maperasNextButton;

    private Button maperasSelectKramaButton, maperasSelectAnakButton;
    private Boolean isAnakSelected = false;

    private CacahKramaMipil anakMaperas;

    ActivityResultLauncher<Intent> startActivityIntent;

    private TextView kramaAnakName, kramaAnakNik, noKramaMipilAnak;
    private Button kramaMipilAnakDetailButton;
    private LinearLayout kramaAnakImageLoadingContainer;
    private MaterialCardView kramaAnakCard;
    private ImageView kramaAnakImage;

    private TextInputEditText ayahLamaField, ibuLamaField, ayahNikLamaField, ibuNikLamaField;

    // di pake untuk anak
    private final String KRAMA_SELECT_KEY = "KRAMA_SELECT_KEY";
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    //di pake untuk krama mipilnya
    private final String KRAMA_MIPIL_SELECT_KEY = "KRAMA_MIPIL_SELECT_KEY";
    private final String KRAMA_MIPIL_DETAIL_KEY = "KRAMA_MIPIL_DETAIL_KEY";

    // di pake tukar data antar activity nya
    private final String ANAK_KEY = "ANAK_KEY";
    private final String KRAMA_LAMA_KEY = "KRAMA_LAMA_KEY";

    private final String ANAK_CACAH_KEY = "ANAK_CACAH_KEY";
    private final String ANAK_CACAH_FOTO_KEY = "ANAK_CACAH_FOTO_KEY";
    private final String CAMPURAN_KEY = "CAMPURAN_KEY";
    private final String SUDHI_WADHANI_KEY = "SUDHI_WADHANI_KEY";

    private Uri sudhiWadhaniUri, pradanaCampuranFotoUri;
    private Maperas maperasCampuran = new Maperas();

    Gson gson = new Gson();

    SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maperas_campuran_masuk_data_anak_ortu_lama);

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        ayahLamaField = findViewById(R.id.maperas_ayah_lama_field);
        ibuLamaField = findViewById(R.id.maperas_ibu_lama_field);
        ayahNikLamaField = findViewById(R.id.maperas_ayah_nik_lama_field);
        ibuNikLamaField = findViewById(R.id.maperas_ibu_nik_lama_field);

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
                if (isAnakSelected) {
                    Intent maperasNextIntent = new Intent(getApplicationContext(), MaperasCampuranMasukDataKramaBaruActivity.class);
                    maperasNextIntent.putExtra(ANAK_KEY, gson.toJson(anakMaperas));
                    maperasNextIntent.putExtra(CAMPURAN_KEY, gson.toJson(maperasCampuran));
                    startActivityIntent.launch(maperasNextIntent);
                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Periksa data kembali.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        maperasSelectAnakButton = findViewById(R.id.maperas_anak_button);
        maperasSelectAnakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent maperasAnakIntent = new Intent(getApplicationContext() , MaperasCampuranMasukIdentitasAnakActivity.class);
                startActivityIntent.launch(maperasAnakIntent);
            }
        });

        startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 700) {
                            // anak
                            maperasCampuran = gson.fromJson(result.getData().getStringExtra(CAMPURAN_KEY), Maperas.class);
                            anakMaperas = gson.fromJson(result.getData().getStringExtra(ANAK_CACAH_KEY), CacahKramaMipil.class);

                            if (maperasCampuran.getNamaAyahLama() != null) {
                                ayahLamaField.setText(maperasCampuran.getNamaAyahLama().toString());
                                ayahNikLamaField.setText(maperasCampuran.getNikAyahLama().toString());
                            }
                            if (maperasCampuran.getNamaIbuLama() != null ) {
                                ibuLamaField.setText(maperasCampuran.getNamaIbuLama().toString());
                                ibuNikLamaField.setText(maperasCampuran.getNikIbuLama().toString());
                            }

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
                            noKramaMipilAnak.setText("-");
                            kramaMipilAnakDetailButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                                    kramaDetail.putExtra(KRAMA_DETAIL_KEY, anakMaperas.getId());
                                    startActivity(kramaDetail);
                                }
                            });
                            if (result.getData().hasExtra(ANAK_CACAH_FOTO_KEY)) {
                                pradanaCampuranFotoUri = Uri.parse(result.getData().getStringExtra(ANAK_CACAH_FOTO_KEY));
                                anakMaperas.penduduk.setFoto(pradanaCampuranFotoUri.toString());
                                Bitmap bitmap = null;
                                try {
                                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), pradanaCampuranFotoUri);
                                    kramaAnakImage.setImageBitmap(bitmap);
                                    kramaAnakImageLoadingContainer.setVisibility(View.GONE);
                                    kramaAnakImage.setVisibility(View.VISIBLE);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaAnakImage);
                                    kramaAnakImage.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaAnakImage);
                                kramaAnakImage.setVisibility(View.VISIBLE);
                            }
                            kramaAnakCard.setVisibility(View.VISIBLE);
                            isAnakSelected = true;
                            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                    "Data Anak berhasil diinput.", Snackbar.LENGTH_SHORT).show();

                        } else if (result.getResultCode() == 1) {
                            setResult(1);
                            finish();
                        }
                    }
                });
    }
}