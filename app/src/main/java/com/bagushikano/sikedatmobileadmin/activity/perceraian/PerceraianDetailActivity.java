package com.bagushikano.sikedatmobileadmin.activity.perceraian;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
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
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.bedabanjar.PerkawinanBedaBanjarDetailActivity;
import com.bagushikano.sikedatmobileadmin.adapter.krama.AnggotaKramaListAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.perceraian.PerceraianAnggotaListAdapter;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.perceraian.PerceraianDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.PerkawinanDetailResponse;
import com.bagushikano.sikedatmobileadmin.util.ChangeDateTimeFormat;
import com.bagushikano.sikedatmobileadmin.util.DownloadUtil;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerceraianDetailActivity extends AppCompatActivity {

    private Toolbar homeToolbar;
    SharedPreferences loginPreferences;

    LinearLayout loadingContainer, failedContainer;
    SwipeRefreshLayout perceraianContainer;
    Button refreshPerceraian;

    TextView tanggalPerceraianText, namaPemuputText, noBuktiSerahTerimaText, noAktaText,
            keteranganText, statusKekeluargaanText, calonKramaText, perceraianStatusText;
    Button aktaFileButton, serahTerimaFileButton, saveSahButton;
    LinearLayout perceraianProgressLayout, perceraianCalonKramaLayout;
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
    private final String PERCERAIAN_DETAIL_KEY = "PERCERAIAN_DETAIL_KEY";

    RecyclerView kramaMipilAnggotaList;
    ArrayList<AnggotaKramaMipil> anggotaKramaMipilArrayList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    PerceraianAnggotaListAdapter anggotaKramaListAdapter;

    MaterialCardView tolakAlasanCard;
    TextInputEditText alasanTolakField;
    Button showTolakPerkawinanButton, tolakCancelButton, submitTolakButton, konfirmasiButton;

    MaterialCardView penolakanCard;
    TextView tolakAlasanText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perceraian_detail);

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        kramaMipilAnggotaList = findViewById(R.id.krama_mipil_anggota_list);
        linearLayoutManager = new LinearLayoutManager(this);
        anggotaKramaListAdapter = new PerceraianAnggotaListAdapter(this, anggotaKramaMipilArrayList);
        kramaMipilAnggotaList.setLayoutManager(linearLayoutManager);
        kramaMipilAnggotaList.setAdapter(anggotaKramaListAdapter);


        loadingContainer = findViewById(R.id.perceraian_detail_loading_container);
        failedContainer = findViewById(R.id.perceraian_detail_failed_container);
        perceraianContainer = findViewById(R.id.perceraian_detail_container);
        refreshPerceraian = findViewById(R.id.perceraian_detail_refresh);

        tolakAlasanCard = findViewById(R.id.perkawinan_tolak_card);
        alasanTolakField = findViewById(R.id.perkawinan_tolak_field);
        showTolakPerkawinanButton = findViewById(R.id.perkawinan_tolak_show_button);
        tolakCancelButton = findViewById(R.id.perkawinan_tolak_cancel_button);
        submitTolakButton = findViewById(R.id.perkawinan_tolak_submit_button);
        konfirmasiButton = findViewById(R.id.perkawinan_konfirmasi_button);


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

        tanggalPerceraianText = findViewById(R.id.perceraian_tanggal_text);
        namaPemuputText = findViewById(R.id.perceraian_nama_pemuput_text);
        noBuktiSerahTerimaText = findViewById(R.id.perceraian_no_serah_terima_text);
        noAktaText = findViewById(R.id.perceraian_no_akta_text);
        keteranganText = findViewById(R.id.perceraian_keterangan_text);
        aktaFileButton = findViewById(R.id.perceraian_berkas_akta_show);
        serahTerimaFileButton = findViewById(R.id.perceraian_berkas_serah_terima_show);
        perceraianProgressLayout = findViewById(R.id.perceraian_pendataan_progress_layout);
        saveSahButton = findViewById(R.id.perceraian_store_sah_button);

        penolakanCard = findViewById(R.id.perkawinan_tolak_alasan_tolak_card);
        tolakAlasanText = findViewById(R.id.perkawinan_tolak_text);

        perceraianStatusText = findViewById(R.id.perceraian_detail_status_text);

        saveSahButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sahPerceraian(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(PERCERAIAN_DETAIL_KEY, -1));
            }
        });

        showTolakPerkawinanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tolakAlasanCard.setVisibility(View.VISIBLE);
                saveSahButton.setVisibility(GONE);
                showTolakPerkawinanButton.setVisibility(GONE);
                konfirmasiButton.setVisibility(GONE);
            }
        });

        tolakCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tolakAlasanCard.setVisibility(GONE);
                saveSahButton.setVisibility(GONE);
                showTolakPerkawinanButton.setVisibility(View.VISIBLE);
                konfirmasiButton.setVisibility(View.VISIBLE);
            }
        });

        konfirmasiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(PerceraianDetailActivity.this)
                        .setTitle("Konfirmasi Perceraian")
                        .setMessage("Yakin ingin mengkonfirmasi Perceraian?")
                        .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                konfirmasiPerkawinan(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(PERCERAIAN_DETAIL_KEY, -1));
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });

        submitTolakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alasanTolakField.getText().toString().length() == 0) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Alasan tidak boleh kosong.", Snackbar.LENGTH_LONG).show();
                }
                else {
                    new MaterialAlertDialogBuilder(PerceraianDetailActivity.this)
                            .setTitle("Perceraian Belum Dapat Dikonfirmasi")
                            .setMessage("Yakin ingin tidak mengkonfirmasi Perceraian?")
                            .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    tolakPerkawinan(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(PERCERAIAN_DETAIL_KEY, -1));
                                }
                            })
                            .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                }
            }
        });

        perceraianContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(PERCERAIAN_DETAIL_KEY, -1));
            }
        });

        getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(PERCERAIAN_DETAIL_KEY, -1));
    }

    public void getDetail(String token, Integer id) {
        setLoadingContainerVisible();
        perceraianContainer.setRefreshing(false);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<PerceraianDetailResponse> perceraianDetailResponseCall = getData.getDetailPerceraian("Bearer " + token, id);
        perceraianDetailResponseCall.enqueue(new Callback<PerceraianDetailResponse>() {
            @Override
            public void onResponse(Call<PerceraianDetailResponse> call, Response<PerceraianDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    if (response.body().getPerceraian().getStatusPerceraian() == 0) {
                        perceraianStatusText.setText("Draft");
                        if (response.body().getPerceraian().getStatusPradana().equals("keluar_banjar")) {
                            if (response.body().getSisiPradana()) {
                                saveSahButton.setVisibility(GONE);
                                showTolakPerkawinanButton.setVisibility(GONE);
                                konfirmasiButton.setVisibility(GONE);
                            } else {
                                saveSahButton.setVisibility(View.VISIBLE);
                                showTolakPerkawinanButton.setVisibility(GONE);
                                konfirmasiButton.setVisibility(GONE);
                            }
                        } else {
                            saveSahButton.setVisibility(View.VISIBLE);
                            showTolakPerkawinanButton.setVisibility(GONE);
                            konfirmasiButton.setVisibility(GONE);
                        }
                        perceraianStatusText.setTextColor(getApplicationContext().getColor(R.color.yellow));
                    } else if (response.body().getPerceraian().getStatusPerceraian() == 3) {
                        perceraianStatusText.setText("Sah");
                        perceraianStatusText.setTextColor(getApplicationContext().getColor(R.color.green));
                        saveSahButton.setVisibility(GONE);
                        showTolakPerkawinanButton.setVisibility(GONE);
                        konfirmasiButton.setVisibility(GONE);
                    } else if (response.body().getPerceraian().getStatusPerceraian() == 1) {
                        // perceraian udh di sahkan di banjar asal, menunggu konfirmasi/tolak,
                        // klo konfirmasi data pradana masuk ke banjar nya
                        perceraianStatusText.setText("Menunggu Konfirmasi");
                        perceraianStatusText.setTextColor(getApplicationContext().getColor(R.color.yellow));
                        if (response.body().getSisiPradana()) {
                            saveSahButton.setVisibility(GONE);
                            showTolakPerkawinanButton.setVisibility(View.VISIBLE);
                            konfirmasiButton.setVisibility(View.VISIBLE);
                        } else {
                            saveSahButton.setVisibility(GONE);
                            showTolakPerkawinanButton.setVisibility(GONE);
                            konfirmasiButton.setVisibility(GONE);
                        }
                    } else if (response.body().getPerceraian().getStatusPerceraian() == 2) {
                        if (response.body().getSisiPradana()) {
                            saveSahButton.setVisibility(GONE);
                        } else {
                            saveSahButton.setVisibility(GONE);
                        }
                        penolakanCard.setVisibility(View.VISIBLE);
                        tolakAlasanText.setText(response.body().getPerceraian().getAlasanPenolakan().toString());
                        showTolakPerkawinanButton.setVisibility(GONE);
                        konfirmasiButton.setVisibility(GONE);
                    }

                    anggotaKramaMipilArrayList.clear();
                    anggotaKramaMipilArrayList.addAll(response.body().getAnggotaKramaMipil());
                    anggotaKramaListAdapter.notifyDataSetChanged();

                    if (response.body().getPerceraian().getNomorAktaPerceraian() != null) {
                        noAktaText.setText(response.body().getPerceraian().getNomorAktaPerceraian().toString());
                        aktaFileButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DownloadUtil downloadUtil = new DownloadUtil(getApplicationContext());
                                Long download =   downloadUtil.downloadFile(
                                        getResources().getString(R.string.api_endpoint) + response.body().getPerceraian().getFileAktaPerceraian());
                                if (download != 0) {
                                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                            "Berkas sedang diunduh", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                            "Berkas gagal diunduh", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        aktaFileButton.setVisibility(View.GONE);
                    }
                    if (response.body().getPerceraian().getNomorPerceraian() != null) {
                        noBuktiSerahTerimaText.setText(response.body().getPerceraian().getNomorPerceraian());
                        serahTerimaFileButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DownloadUtil downloadUtil = new DownloadUtil(getApplicationContext());
                                Long download =   downloadUtil.downloadFile(
                                        getResources().getString(R.string.api_endpoint) + response.body().getPerceraian().getFileBuktiPerceraian());
                                if (download != 0) {
                                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                            "Berkas sedang diunduh", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                            "Berkas gagal diunduh", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        serahTerimaFileButton.setVisibility(GONE);
                    }
                    tanggalPerceraianText.setText(ChangeDateTimeFormat.changeDateFormat(response.body().getPerceraian().getTanggalPerceraian()));
                    namaPemuputText.setText(response.body().getPerceraian().getNamaPemuput());

                    if (response.body().getPerceraian().getKeterangan() != null) {
                        keteranganText.setText(response.body().getPerceraian().getKeterangan());
                    }

                    // purusa
                    String namaFormated = response.body().getPerceraian().getPurusa().getPenduduk().getNama();
                    if (response.body().getPerceraian().getPurusa().getPenduduk().getGelarDepan() != null) {
                        namaFormated = String.format("%s %s",
                                response.body().getPerceraian().getPurusa().getPenduduk().getGelarDepan(),
                                response.body().getPerceraian().getPurusa().getPenduduk().getNama()
                        );
                    }


                    if (response.body().getPerceraian().getPurusa().getPenduduk().getGelarBelakang() != null) {
                        namaFormated = String.format("%s %s",
                                namaFormated,
                                response.body().getPerceraian().getPurusa().getPenduduk().getGelarBelakang()
                        );
                    }
                    kramaPurusaName.setText(namaFormated);
                    kramaPurusaNik.setText(response.body().getPerceraian().getPurusa().getPenduduk().getNik());
                    noKramaMipilPurusa.setText(response.body().getPerceraian().getPurusa().getNomorCacahKramaMipil());
                    kramaMipilPurusaDetailButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                            kramaDetail.putExtra(KRAMA_DETAIL_KEY, response.body().getPerceraian().getPurusa().getId());
                            startActivity(kramaDetail);
                        }
                    });
                    if (response.body().getPerceraian().getPurusa().getPenduduk().getFoto() != null) {
                        kramaPurusaImageLoadingContainer.setVisibility(View.VISIBLE);
                        Picasso.get()
                                .load(getResources().getString(R.string.image_endpoint) +
                                        response.body().getPerceraian().getPurusa().getPenduduk().getFoto())
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

                    //pradana
                    namaFormated = response.body().getPerceraian().getPradana().getPenduduk().getNama();
                    if (response.body().getPerceraian().getPradana().getPenduduk().getGelarDepan() != null) {
                        namaFormated = String.format("%s %s",
                                response.body().getPerceraian().getPradana().getPenduduk().getGelarDepan(),
                                response.body().getPerceraian().getPradana().getPenduduk().getNama()
                        );
                    }


                    if (response.body().getPerceraian().getPradana().getPenduduk().getGelarBelakang() != null) {
                        namaFormated = String.format("%s %s",
                                namaFormated,
                                response.body().getPerceraian().getPradana().getPenduduk().getGelarBelakang()
                        );
                    }
                    kramaPradanaName.setText(namaFormated);
                    kramaPradanaNik.setText(response.body().getPerceraian().getPradana().getPenduduk().getNik());
                    noKramaMipilPradana.setText(response.body().getPerceraian().getPradana().getNomorCacahKramaMipil());
                    kramaMipilPradanaDetailButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                            kramaDetail.putExtra(KRAMA_DETAIL_KEY, response.body().getPerceraian().getPradana().getId());
                            startActivity(kramaDetail);
                        }
                    });
                    if (response.body().getPerceraian().getPradana().getPenduduk().getFoto() != null) {
                        kramaPradanaImageLoadingContainer.setVisibility(View.VISIBLE);
                        Picasso.get()
                                .load(getResources().getString(R.string.image_endpoint) +
                                        response.body().getPerceraian().getPradana().getPenduduk().getFoto())
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
                    setKramaContainerVisible();
                }
                else {
                    setFailedContainerVisible();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                perceraianContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<PerceraianDetailResponse> call, Throwable t) {
                setFailedContainerVisible();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void konfirmasiPerkawinan(String token, int id) {
        perceraianProgressLayout.setVisibility(View.VISIBLE);
        tolakAlasanCard.setVisibility(GONE);
        saveSahButton.setVisibility(GONE);
        showTolakPerkawinanButton.setVisibility(GONE);
        konfirmasiButton.setVisibility(GONE);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<PerceraianDetailResponse> perkawinanDetailResponseCall = getData.perceraianKonfirmasi("Bearer " + token, id);
        perkawinanDetailResponseCall.enqueue(new Callback<PerceraianDetailResponse>() {
            @Override
            public void onResponse(Call<PerceraianDetailResponse> call, Response<PerceraianDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Perceraian berhasil dikonfirmasi.", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                perceraianProgressLayout.setVisibility(View.GONE);
                getDetail(loginPreferences.getString("token", "empty"), id);
            }

            @Override
            public void onFailure(Call<PerceraianDetailResponse> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void tolakPerkawinan(String token, int id) {
        perceraianProgressLayout.setVisibility(View.VISIBLE);
        tolakAlasanCard.setVisibility(GONE);
        saveSahButton.setVisibility(GONE);
        showTolakPerkawinanButton.setVisibility(GONE);
        konfirmasiButton.setVisibility(GONE);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<PerceraianDetailResponse> perkawinanDetailResponseCall = getData.perceraianTolak(
                "Bearer " + token,
                id,
                alasanTolakField.getText().toString()
        );
        perkawinanDetailResponseCall.enqueue(new Callback<PerceraianDetailResponse>() {
            @Override
            public void onResponse(Call<PerceraianDetailResponse> call, Response<PerceraianDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Perceraian telak tidak dikonfirmasi.", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                perceraianProgressLayout.setVisibility(View.GONE);
                getDetail(loginPreferences.getString("token", "empty"), id);
            }

            @Override
            public void onFailure(Call<PerceraianDetailResponse> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void sahPerceraian(String token, int id) {
        perceraianProgressLayout.setVisibility(View.VISIBLE);
        tolakAlasanCard.setVisibility(GONE);
        saveSahButton.setVisibility(GONE);
        showTolakPerkawinanButton.setVisibility(GONE);
        konfirmasiButton.setVisibility(GONE);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<PerceraianDetailResponse> perkawinanDetailResponseCall = getData.perceraianSah("Bearer " + token, id);
        perkawinanDetailResponseCall.enqueue(new Callback<PerceraianDetailResponse>() {
            @Override
            public void onResponse(Call<PerceraianDetailResponse> call, Response<PerceraianDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Data Perceraian berhasil disahkan.", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                perceraianProgressLayout.setVisibility(View.GONE);
                getDetail(loginPreferences.getString("token", "empty"), id);
            }

            @Override
            public void onFailure(Call<PerceraianDetailResponse> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void setFailedContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(View.VISIBLE);
        perceraianContainer.setVisibility(GONE);
    }

    public void setLoadingContainerVisible() {
        loadingContainer.setVisibility(View.VISIBLE);
        failedContainer.setVisibility(GONE);
        perceraianContainer.setVisibility(GONE);
    }

    public void setKramaContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(GONE);
        perceraianContainer.setVisibility(View.VISIBLE);
    }
}