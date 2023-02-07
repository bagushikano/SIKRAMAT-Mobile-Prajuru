package com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.satubanjar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class PerkawinanSatuBanjarPasanganActivity extends AppCompatActivity {

    private Button perkawinanNextButton;

    private Button perkawinanSelectPurusaButton, perkawinanSelectPradanaButton;
    private Boolean isPradanaSelected = false, isPurusaSelected = false;

    private CacahKramaMipil purusaKrama, pradanaKrama;

    ActivityResultLauncher<Intent> startActivityIntent;

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

    private final String KRAMA_SELECT_KEY = "KRAMA_SELECT_KEY";
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    private final String PURUSA_KEY = "PURUSA_SELECT_KEY";
    private final String PRADANA_KEY = "PRADANA_SELECT_KEY";

    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perkawinan_satu_banjar_pasangan);

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

        perkawinanNextButton = findViewById(R.id.perkawinan_pasangan_next_button);
        perkawinanNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPradanaSelected && isPurusaSelected) {
                    Intent perkawinanNextIntent = new Intent(getApplicationContext(), PerkawinanSatuBanjarStatusKekeluargaanActivity.class);
                    perkawinanNextIntent.putExtra(PURUSA_KEY, gson.toJson(purusaKrama));
                    perkawinanNextIntent.putExtra(PRADANA_KEY, gson.toJson(pradanaKrama));
                    startActivityIntent.launch(perkawinanNextIntent);
                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Periksa data pasangan kembali.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        perkawinanSelectPradanaButton = findViewById(R.id.perkawinan_pasangan_pradana_button);
        perkawinanSelectPurusaButton = findViewById(R.id.perkawinan_pasangan_purusa_button);

        perkawinanSelectPradanaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPurusaSelected) {
                    Intent perkawinanPradanaIntent = new Intent(getApplicationContext() , PerkawinanSatuBanjarPradanaSelectActivity.class);
                    perkawinanPradanaIntent.putExtra("PURUSA_ID", purusaKrama.getId());
                    startActivityIntent.launch(perkawinanPradanaIntent);
                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Pilih Purusa terebih dahulu", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        perkawinanSelectPurusaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent perkawinanPurusaIntent = new Intent(getApplicationContext() , PerkawinanSatuBanjarPurusaSelectActivity.class);
                startActivityIntent.launch(perkawinanPurusaIntent);
            }
        });

        startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 200) {
                            // purusa
                            purusaKrama = gson.fromJson(result.getData().getStringExtra(KRAMA_SELECT_KEY), CacahKramaMipil.class);
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
                            isPurusaSelected = true;
                            perkawinanSelectPurusaButton.setText("Ganti Purusa");
                            perkawinanSelectPradanaButton.setText("Pilih Pradana");

                            if (isPradanaSelected) {
                                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                        "Purusa telah diganti. Silahkan pilih Pradana.", Snackbar.LENGTH_SHORT).show();
                                isPradanaSelected = false;
                                kramaPradanaCard.setVisibility(View.GONE);
                                pradanaKrama = null;
                            } else {
                                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                        "Purusa berhasil dipilih. Silahkan pilih Pradana.", Snackbar.LENGTH_SHORT).show();
                            }
                        } else if (result.getResultCode() == 100) {
                            // pradana
                            pradanaKrama = gson.fromJson(result.getData().getStringExtra(KRAMA_SELECT_KEY), CacahKramaMipil.class);
                            String namaFormated = pradanaKrama.getPenduduk().getNama();
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
                            isPradanaSelected = true;
                            perkawinanSelectPradanaButton.setText("Ganti Pradana");
                            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                    "Pradana berhasil dipilih.", Snackbar.LENGTH_SHORT).show();

                        } else if (result.getResultCode() == 1) {
                            setResult(1);
                            finish();
                        }
                    }
                });
    }
}