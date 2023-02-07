package com.bagushikano.sikedatmobileadmin.activity.perkawinan;

import static android.view.View.GONE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.PerkawinanSelectTypeActivity;
import com.bagushikano.sikedatmobileadmin.adapter.perkawinan.PerkawinanListAdapter;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.Perkawinan;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.PerkawinanGetResponse;
import com.bagushikano.sikedatmobileadmin.util.ChangeDateTimeFormat;
import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerkawinanDaftarActivity extends AppCompatActivity {

    private Toolbar homeToolbar;

    private Boolean isFilterApplied = false, isStatusPerkawinanFilter = false,
            isJenisPerkawinanFilter = false, isRentangWaktuPerkawinanFilter = false;

    private Chip statusPerkawinanFilterChip, jenisPerkawinanFilterChip,
            rentangWaktuPerkawinanFilter, clearFilterPerkawinanChip;

    // jenis perkawinan
    View dialogFilterPerkawinanJenisView;
    RadioGroup perkawinanJenisRadioGroup;
    RadioButton perkawinanSatuBanjarAdatRadio, perkawinanBedaBanjarRadio,
            perkawinanCampuranMasukRadio, perkawinanCampuranKeluarRadio, perkawinanAllTypeRadio;
    MaterialAlertDialogBuilder dialogFilterJenisPerkawinan;

    // status perkawinan
    View dialogFilterPerkawinanStatusView;
    RadioGroup perkawinanStatusRadioGroup;
    RadioButton perkawinanSahRadio, perkawinanDraftRadio,
            perkawinanTerkonfirmasiRadio, perkawinanTidakTerkonfirmasiRadio, perkawinanAllStatusRadio;
    MaterialAlertDialogBuilder dialogFilterStatusPerkawinan;

    // rentang waktu
    View dialogFilterPerkawinanWaktuView;
    TextInputEditText perkawinanWaktuStartField, perkawinanWaktuEndField;
    TextInputLayout perkawinanWaktuStartLayout, perkawinanWaktuEndLayout;
    MaterialAlertDialogBuilder dialogFilterWaktuPerkawinan;
    CheckBox perkawinanWaktuAllCheckBox;
    Boolean isTanggalStart = false, isTanggalEnd = false;

    private String filterStatus, filterStartDate, filterEndDate, filterType;

    RecyclerView perkawinanList;
    ArrayList<Perkawinan> perkawinanArrayList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    PerkawinanListAdapter perkawinanListAdapter;

    LinearLayout loadingContainer, failedContainer, perkawinanEmptyContainer;
    SwipeRefreshLayout perkawinanContainer;
    Button refreshPerkawinan;
    SharedPreferences loginPreferences;

    LinearLayout perkawinanLoadProgressContainer;
    NestedScrollView perkawinanNestedScroll;

    TextView perkawinanAllDataLoadedTextView;
    int currentPage;
    int nextPage;
    int lastPage;

    private Button perkawinanDataBaruButton;
    ActivityResultLauncher<Intent> startActivityIntent;

    TextView perkawinanTotalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perkawinan_daftar);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        perkawinanTotalText = findViewById(R.id.kelahiran_ajuan_total_text);

        perkawinanDataBaruButton = findViewById(R.id.perkawinan_data_baru_button);
        perkawinanDataBaruButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent perkawinanBaruIntent = new Intent(getApplicationContext() , PerkawinanSelectTypeActivity.class);
                startActivityIntent.launch(perkawinanBaruIntent);
            }
        });

        statusPerkawinanFilterChip = findViewById(R.id.perkawinan_status_filter_chip);
        jenisPerkawinanFilterChip = findViewById(R.id.perkawinan_jenis_filter_chip);
        rentangWaktuPerkawinanFilter = findViewById(R.id.perkawinan_waktu_filter_chip);
        clearFilterPerkawinanChip = findViewById(R.id.perkawinan_reset_filter_chip);

        /**
         * Filter jenis perkawinan
         */
        dialogFilterPerkawinanJenisView = LayoutInflater.from(this).inflate(R.layout.dialog_filter_perkawinan_jenis,
                getWindow().getDecorView().findViewById(android.R.id.content), false);
        perkawinanJenisRadioGroup = dialogFilterPerkawinanJenisView.findViewById(R.id.perkawinan_jenis_radio_group);
        perkawinanSatuBanjarAdatRadio = dialogFilterPerkawinanJenisView.findViewById(R.id.perkawinan_satu_banjar_radio);
        perkawinanBedaBanjarRadio = dialogFilterPerkawinanJenisView.findViewById(R.id.perkawinan_beda_banjar_radio);
        perkawinanCampuranMasukRadio = dialogFilterPerkawinanJenisView.findViewById(R.id.perkawinan_campuran_masuk_radio);
        perkawinanCampuranKeluarRadio = dialogFilterPerkawinanJenisView.findViewById(R.id.perkawinan_campuran_keluar_radio);
        perkawinanAllTypeRadio = dialogFilterPerkawinanJenisView.findViewById(R.id.perkawinan_all_type_radio);


        dialogFilterJenisPerkawinan =  new MaterialAlertDialogBuilder(this)
                .setTitle("Pilih Jenis Perkawinan")
                .setView(dialogFilterPerkawinanJenisView)
                .setPositiveButton("Terapkan Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (perkawinanJenisRadioGroup.getCheckedRadioButtonId() == R.id.perkawinan_satu_banjar_radio) {
                            jenisPerkawinanFilterChip.setText("Satu Banjar Adat");
                            isJenisPerkawinanFilter = true;
                            filterType = "satu_banjar_adat";
                        } else if (perkawinanJenisRadioGroup.getCheckedRadioButtonId() == R.id.perkawinan_beda_banjar_radio) {
                            jenisPerkawinanFilterChip.setText("Beda Banjar Adat");
                            isJenisPerkawinanFilter = true;
                            filterType = "beda_banjar_adat";
                        } else if (perkawinanJenisRadioGroup.getCheckedRadioButtonId() == R.id.perkawinan_campuran_masuk_radio) {
                            jenisPerkawinanFilterChip.setText("Campuran Masuk");
                            isJenisPerkawinanFilter = true;
                            filterType = "campuran_masuk";
                        } else if (perkawinanJenisRadioGroup.getCheckedRadioButtonId() == R.id.perkawinan_campuran_keluar_radio) {
                            jenisPerkawinanFilterChip.setText("Campuran Keluar");
                            isJenisPerkawinanFilter = true;
                            filterType = "campuran_keluar";
                        } else {
                            jenisPerkawinanFilterChip.setText("Semua Jenis");
                            isJenisPerkawinanFilter = false;
                            filterType = null;
                            getData(loginPreferences.getString("token", "empty"));
                        }
                        checkForAppliedFilter();
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        jenisPerkawinanFilterChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogFilterPerkawinanJenisView.getParent() != null) {
                    ((ViewGroup) dialogFilterPerkawinanJenisView.getParent()).removeView(dialogFilterPerkawinanJenisView);
                }
                dialogFilterJenisPerkawinan.show();
            }
        });

        //status perkawinan
        dialogFilterPerkawinanStatusView = LayoutInflater.from(this).inflate(R.layout.dialog_filter_perkawinan_status,
                getWindow().getDecorView().findViewById(android.R.id.content), false);
        perkawinanStatusRadioGroup = dialogFilterPerkawinanStatusView.findViewById(R.id.perkawinan_status_radio_group);
        perkawinanSahRadio = dialogFilterPerkawinanStatusView.findViewById(R.id.perkawinan_status_sah_radio);
        perkawinanAllStatusRadio = dialogFilterPerkawinanStatusView.findViewById(R.id.perkawinan_all_status_radio);
        perkawinanDraftRadio = dialogFilterPerkawinanStatusView.findViewById(R.id.perkawinan_status_draft_radio);
        perkawinanTerkonfirmasiRadio = dialogFilterPerkawinanStatusView.findViewById(R.id.perkawinan_status_terkonfirmasi_radio);
        perkawinanTidakTerkonfirmasiRadio = dialogFilterPerkawinanStatusView.findViewById(R.id.perkawinan_status_tidak_terkonfirmasi_radio);


        dialogFilterStatusPerkawinan =  new MaterialAlertDialogBuilder(this)
                .setTitle("Pilih Status Perkawinan")
                .setView(dialogFilterPerkawinanStatusView)
                .setPositiveButton("Terapkan Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (perkawinanStatusRadioGroup.getCheckedRadioButtonId() == R.id.perkawinan_status_sah_radio) {
                            statusPerkawinanFilterChip.setText("Sah");
                            isStatusPerkawinanFilter = true;
                            filterStatus = "3";
                        } else if (perkawinanStatusRadioGroup.getCheckedRadioButtonId() == R.id.perkawinan_status_draft_radio) {
                            statusPerkawinanFilterChip.setText("Draft");
                            isStatusPerkawinanFilter = true;
                            filterStatus = "0";
                        } else if (perkawinanStatusRadioGroup.getCheckedRadioButtonId() == R.id.perkawinan_status_terkonfirmasi_radio) {
                            statusPerkawinanFilterChip.setText("Terkonfirmasi");
                            isStatusPerkawinanFilter = true;
                            filterStatus = "1";
                        } else if (perkawinanStatusRadioGroup.getCheckedRadioButtonId() == R.id.perkawinan_status_tidak_terkonfirmasi_radio) {
                            statusPerkawinanFilterChip.setText("Tidak Terkonfirmasi");
                            isStatusPerkawinanFilter = true;
                            filterStatus = "2";
                        } else {
                            statusPerkawinanFilterChip.setText("Semua Status");
                            isStatusPerkawinanFilter = false;
                            filterStatus = null;
                            getData(loginPreferences.getString("token", "empty"));
                        }
                        checkForAppliedFilter();
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        statusPerkawinanFilterChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogFilterPerkawinanStatusView.getParent() != null) {
                    ((ViewGroup) dialogFilterPerkawinanStatusView.getParent()).removeView(dialogFilterPerkawinanStatusView);
                }
                dialogFilterStatusPerkawinan.show();
            }
        });


        //rentang tanggal


        dialogFilterPerkawinanWaktuView = LayoutInflater.from(this).inflate(R.layout.dialog_filter_perkawinan_waktu,
                getWindow().getDecorView().findViewById(android.R.id.content), false);
        perkawinanWaktuStartField = dialogFilterPerkawinanWaktuView.findViewById(R.id.perkawinan_filter_tanggal_mulai_field);
        perkawinanWaktuStartLayout = dialogFilterPerkawinanWaktuView.findViewById(R.id.perkawinan_filter_tanggal_mulai_form);
        perkawinanWaktuEndField = dialogFilterPerkawinanWaktuView.findViewById(R.id.perkawinan_filter_tanggal_akhir_field);
        perkawinanWaktuEndLayout = dialogFilterPerkawinanWaktuView.findViewById(R.id.perkawinan_filter_tanggal_akhir_form);
        perkawinanWaktuAllCheckBox = dialogFilterPerkawinanWaktuView.findViewById(R.id.perkawinan_filter_tanggal_all_checkbox);

        MaterialDatePicker.Builder<Long> datePickerBuilderPerkawinanStartDate = MaterialDatePicker.Builder.datePicker();
        datePickerBuilderPerkawinanStartDate.setTitleText("Pilih tanggal");
        final MaterialDatePicker<Long> datePickerPerkawinanStartDate = datePickerBuilderPerkawinanStartDate.build();

        datePickerPerkawinanStartDate.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selectedDate) {
                // link: https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
                TimeZone timeZoneUTC = TimeZone.getDefault();
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                SimpleDateFormat simpleFormat = new SimpleDateFormat("EEE, dd-MM-yyyy", Locale.US);
                Date date = new Date(selectedDate + offsetFromUTC);
                perkawinanWaktuStartField.setText(simpleFormat.format(date));
                isTanggalStart = true;
                perkawinanWaktuAllCheckBox.setChecked(false);
            }
        });

        perkawinanWaktuStartField.setShowSoftInputOnFocus(false);
        perkawinanWaktuStartField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(datePickerPerkawinanStartDate.isVisible())) {
                    datePickerPerkawinanStartDate.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                }
            }
        });

        perkawinanWaktuStartField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    if (!(datePickerPerkawinanStartDate.isVisible())) {
                        datePickerPerkawinanStartDate.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                }
            }
        });

        MaterialDatePicker.Builder<Long> datePickerBuilderPerkawinanEndDate = MaterialDatePicker.Builder.datePicker();
        datePickerBuilderPerkawinanEndDate.setTitleText("Pilih tanggal");
        final MaterialDatePicker<Long> datePickerPerkawinanEndDate = datePickerBuilderPerkawinanEndDate.build();

        datePickerPerkawinanEndDate.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selectedDate) {
                // link: https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
                TimeZone timeZoneUTC = TimeZone.getDefault();
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                SimpleDateFormat simpleFormat = new SimpleDateFormat("EEE, dd-MM-yyyy", Locale.US);
                Date date = new Date(selectedDate + offsetFromUTC);
                perkawinanWaktuEndField.setText(simpleFormat.format(date));
                isTanggalEnd = true;
                perkawinanWaktuAllCheckBox.setChecked(false);
            }
        });

        perkawinanWaktuEndField.setShowSoftInputOnFocus(false);
        perkawinanWaktuEndField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(datePickerPerkawinanEndDate.isVisible())) {
                    datePickerPerkawinanEndDate.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                }
            }
        });

        perkawinanWaktuEndField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    if (!(datePickerPerkawinanEndDate.isVisible())) {
                        datePickerPerkawinanEndDate.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                }
            }
        });

        perkawinanWaktuAllCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    perkawinanWaktuEndField.setText(null);
                    perkawinanWaktuStartField.setText(null);
                    perkawinanWaktuStartField.clearFocus();
                    perkawinanWaktuEndField.clearFocus();
                    isTanggalEnd = false;
                    isTanggalStart = false;
                }
            }
        });


        dialogFilterWaktuPerkawinan =  new MaterialAlertDialogBuilder(this)
                .setTitle("Pilih Rentang Waktu")
                .setView(dialogFilterPerkawinanWaktuView)
                .setPositiveButton("Terapkan Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (perkawinanWaktuAllCheckBox.isChecked()) {
                            isRentangWaktuPerkawinanFilter = false;
                            rentangWaktuPerkawinanFilter.setText("Semua Waktu");
                            filterStartDate = null;
                            filterEndDate = null;
                            getData(loginPreferences.getString("token", "empty"));
                        } else {
                            if (isTanggalEnd && isTanggalStart) {
                                isRentangWaktuPerkawinanFilter = true;
                                rentangWaktuPerkawinanFilter.setText(perkawinanWaktuStartField.getText()
                                        + " - " + perkawinanWaktuEndField.getText());
                                filterStartDate = ChangeDateTimeFormat.changeDateFormatForForm(perkawinanWaktuStartField.getText().toString());
                                filterEndDate = ChangeDateTimeFormat.changeDateFormatForForm(perkawinanWaktuEndField.getText().toString());
                            } else {
                                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                        "Rentang waktu tidak valid.", Snackbar.LENGTH_LONG).show();
                                isRentangWaktuPerkawinanFilter = false;
                                filterStartDate = null;
                                filterEndDate = null;
                            }
                        }
                        checkForAppliedFilter();
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        rentangWaktuPerkawinanFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogFilterPerkawinanWaktuView.getParent() != null) {
                    ((ViewGroup) dialogFilterPerkawinanWaktuView.getParent()).removeView(dialogFilterPerkawinanWaktuView);
                }
                dialogFilterWaktuPerkawinan.show();
            }
        });

        clearFilterPerkawinanChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFilter();
            }
        });

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        loadingContainer = findViewById(R.id.perkawinan_loading_container);
        failedContainer = findViewById(R.id.perkawinan_failed_container);
        perkawinanContainer = findViewById(R.id.perkawinan_container);
        refreshPerkawinan = findViewById(R.id.perkawinan_refresh);
        perkawinanList = findViewById(R.id.perkawinan_list);
        perkawinanAllDataLoadedTextView = findViewById(R.id.all_data_loaded_perkawinan_text);
        perkawinanEmptyContainer = findViewById(R.id.perkawinan_empty_container);
        perkawinanLoadProgressContainer = findViewById(R.id.perkawinan_load_progress);
        perkawinanNestedScroll = findViewById(R.id.perkawinan_nested_scroll);

        linearLayoutManager = new LinearLayoutManager(this);
        perkawinanListAdapter = new PerkawinanListAdapter(this, perkawinanArrayList);
        perkawinanList.setLayoutManager(linearLayoutManager);
        perkawinanList.setAdapter(perkawinanListAdapter);
        perkawinanList.setNestedScrollingEnabled(true);


        refreshPerkawinan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(loginPreferences.getString("token", "empty"));
            }
        });

        perkawinanContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(loginPreferences.getString("token", "empty"));
            }
        });

        startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 1) {
                            getData(loginPreferences.getString("token", "empty"));
                        }
                    }
                });

        getData(loginPreferences.getString("token", "empty"));
    }

    public void checkForAppliedFilter() {
        if (isJenisPerkawinanFilter || isStatusPerkawinanFilter || isRentangWaktuPerkawinanFilter) {
            isFilterApplied = true;
            clearFilterPerkawinanChip.setVisibility(View.VISIBLE);
            getData(loginPreferences.getString("token", "empty"));
        } else {
            isFilterApplied = false;
            clearFilterPerkawinanChip.setVisibility(View.GONE);
        }
    }

    public void clearFilter() {
        isStatusPerkawinanFilter = false;
        isJenisPerkawinanFilter = false;
        isRentangWaktuPerkawinanFilter = false;
        isTanggalStart = false;
        isTanggalEnd = false;
        filterStatus = null;
        filterStartDate = null;
        filterEndDate = null;
        filterType = null;
        jenisPerkawinanFilterChip.setText("Semua Jenis");
        statusPerkawinanFilterChip.setText("Semua Status");
        rentangWaktuPerkawinanFilter.setText("Semua Waktu");
        getData(loginPreferences.getString("token", "empty"));
        checkForAppliedFilter();
    }

    public void getData(String token) {
        setLoadingContainerVisible();
        perkawinanContainer.setRefreshing(true);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<PerkawinanGetResponse> perkawinanGetResponseCall = getData.getPerkawinan(
                "Bearer " + token, filterStatus, filterStartDate, filterEndDate, filterType
                );
        perkawinanGetResponseCall.enqueue(new Callback<PerkawinanGetResponse>() {
            @Override
            public void onResponse(Call<PerkawinanGetResponse> call, Response<PerkawinanGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    perkawinanArrayList.clear();
                    perkawinanArrayList.addAll(response.body().getPerkawinanPaginate().getData());
//                    CacahKramaMipil cacahKramaMipil = perkawinanArrayList.get(0).getPurusa();
                    perkawinanListAdapter.notifyDataSetChanged();
                    perkawinanTotalText.setText(String.valueOf(perkawinanArrayList.size()));
                    if (perkawinanArrayList.size() == 0) {
                        perkawinanEmptyContainer.setVisibility(View.VISIBLE);
                    }
                    else {
                        perkawinanEmptyContainer.setVisibility(GONE);
                        currentPage = response.body().getPerkawinanPaginate().getCurrentPage();
                        lastPage = response.body().getPerkawinanPaginate().getLastPage();
                        if (currentPage != lastPage) {
                            nextPage = currentPage+1;
                            perkawinanNestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                                @Override
                                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                    if (!perkawinanNestedScroll.canScrollVertically(1)) {
                                        getNextData(token, nextPage);
                                    }
                                }
                            });
                        }
                        else {
                            perkawinanAllDataLoadedTextView.setVisibility(View.VISIBLE);
                        }
                    }
                    setKramaContainerVisible();
                }
                else {
                    setFailedContainerVisible();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                perkawinanContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<PerkawinanGetResponse> call, Throwable t) {
                setFailedContainerVisible();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void getNextData(String token, int page) {
        perkawinanLoadProgressContainer.setVisibility(View.VISIBLE);
        perkawinanContainer.setRefreshing(true);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<PerkawinanGetResponse> perkawinanGetResponseCall = getData.getPerkawinanNextPage(
                "Bearer " + token, filterStatus, filterStartDate, filterEndDate, filterType, page
        );
        perkawinanGetResponseCall.enqueue(new Callback<PerkawinanGetResponse>() {
            @Override
            public void onResponse(Call<PerkawinanGetResponse> call, Response<PerkawinanGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    perkawinanArrayList.addAll(response.body().getPerkawinanPaginate().getData());
                    currentPage = response.body().getPerkawinanPaginate().getCurrentPage();
                    perkawinanListAdapter.notifyItemInserted(perkawinanArrayList.size()-1);
                    perkawinanLoadProgressContainer.setVisibility(GONE);
                    if (currentPage != lastPage) {
                        nextPage++;
                    }
                    else {
                        perkawinanAllDataLoadedTextView.setVisibility(View.VISIBLE);
                        perkawinanNestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                            @Override
                            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                            }
                        });
                    }
                    perkawinanContainer.setRefreshing(false);
                }
                else {
                    perkawinanLoadProgressContainer.setVisibility(GONE);
                    perkawinanContainer.setRefreshing(false);
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PerkawinanGetResponse> call, Throwable t) {
                setFailedContainerVisible();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void setFailedContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(View.VISIBLE);
        perkawinanContainer.setVisibility(GONE);
    }

    public void setLoadingContainerVisible() {
        loadingContainer.setVisibility(View.VISIBLE);
        failedContainer.setVisibility(GONE);
        perkawinanContainer.setVisibility(GONE);
    }

    public void setKramaContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(GONE);
        perkawinanContainer.setVisibility(View.VISIBLE);
    }
}