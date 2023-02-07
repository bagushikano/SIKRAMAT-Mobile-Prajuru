package com.bagushikano.sikedatmobileadmin.activity.maperas;

import static android.view.View.GONE;

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

import com.bagushikano.sikedatmobileadmin.R;

import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.MaperasSelectTypeActivity;
import com.bagushikano.sikedatmobileadmin.adapter.maperas.MaperasListAdapter;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;

import com.bagushikano.sikedatmobileadmin.model.maperas.Maperas;
import com.bagushikano.sikedatmobileadmin.model.maperas.MaperasGetResponse;
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

public class MaperasDaftarActivity extends AppCompatActivity {


    private Toolbar homeToolbar;

    private Boolean isFilterApplied = false, isStatusMaperasFilter = false,
            isJenisMaperasFilter = false, isRentangWaktuMaperasFilter = false;

    private Chip statusMaperasFilterChip, jenisMaperasFilterChip,
            rentangWaktuMaperasFilter, clearFilterMaperasChip;

    // jenis maperas
    View dialogFilterMaperasJenisView;
    RadioGroup maperasJenisRadioGroup;
    RadioButton maperasSatuBanjarAdatRadio, maperasBedaBanjarRadio,
            maperasCampuranMasukRadio, maperasCampuranKeluarRadio, maperasAllTypeRadio;
    MaterialAlertDialogBuilder dialogFilterJenisMaperas;

    // status maperas
    View dialogFilterMaperasStatusView;
    RadioGroup maperasStatusRadioGroup;
    RadioButton maperasSahRadio, maperasDraftRadio,
            maperasTerkonfirmasiRadio, maperasTidakTerkonfirmasiRadio, maperasAllStatusRadio;
    MaterialAlertDialogBuilder dialogFilterStatusMaperas;

    // rentang waktu
    View dialogFilterMaperasWaktuView;
    TextInputEditText maperasWaktuStartField, maperasWaktuEndField;
    TextInputLayout maperasWaktuStartLayout, maperasWaktuEndLayout;
    MaterialAlertDialogBuilder dialogFilterWaktuMaperas;
    CheckBox maperasWaktuAllCheckBox;
    Boolean isTanggalStart = false, isTanggalEnd = false;

    private String filterStatus, filterStartDate, filterEndDate, filterType;

    RecyclerView maperasList;
    ArrayList<Maperas> maperasArrayList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    MaperasListAdapter maperasListAdapter;

    LinearLayout loadingContainer, failedContainer, maperasEmptyContainer;
    SwipeRefreshLayout maperasContainer;
    Button refreshMaperas;
    SharedPreferences loginPreferences;

    LinearLayout maperasLoadProgressContainer;
    NestedScrollView maperasNestedScroll;

    TextView maperasAllDataLoadedTextView;
    int currentPage;
    int nextPage;
    int lastPage;

    private Button maperasDataBaruButton;
    ActivityResultLauncher<Intent> startActivityIntent;


    TextView perkawinanTotalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maperas_daftar);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        perkawinanTotalText = findViewById(R.id.kelahiran_ajuan_total_text);

        maperasDataBaruButton = findViewById(R.id.maperas_data_baru_button);
        maperasDataBaruButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent maperasNewIntent = new Intent(getApplicationContext(), MaperasSelectTypeActivity.class);
                startActivityIntent.launch(maperasNewIntent);
            }
        });

        statusMaperasFilterChip = findViewById(R.id.maperas_status_filter_chip);
        jenisMaperasFilterChip = findViewById(R.id.maperas_jenis_filter_chip);
        rentangWaktuMaperasFilter = findViewById(R.id.maperas_waktu_filter_chip);
        clearFilterMaperasChip = findViewById(R.id.maperas_reset_filter_chip);

        /**
         * Filter jenis maperas
         */
        dialogFilterMaperasJenisView = LayoutInflater.from(this).inflate(R.layout.dialog_filter_maperas_jenis,
                getWindow().getDecorView().findViewById(android.R.id.content), false);
        maperasJenisRadioGroup = dialogFilterMaperasJenisView.findViewById(R.id.maperas_jenis_radio_group);
        maperasSatuBanjarAdatRadio = dialogFilterMaperasJenisView.findViewById(R.id.maperas_satu_banjar_radio);
        maperasBedaBanjarRadio = dialogFilterMaperasJenisView.findViewById(R.id.maperas_beda_banjar_radio);
        maperasCampuranMasukRadio = dialogFilterMaperasJenisView.findViewById(R.id.maperas_campuran_masuk_radio);
        maperasCampuranKeluarRadio = dialogFilterMaperasJenisView.findViewById(R.id.maperas_campuran_keluar_radio);
        maperasAllTypeRadio = dialogFilterMaperasJenisView.findViewById(R.id.maperas_all_type_radio);


        dialogFilterJenisMaperas =  new MaterialAlertDialogBuilder(this)
                .setTitle("Pilih Jenis Maperas")
                .setView(dialogFilterMaperasJenisView)
                .setPositiveButton("Terapkan Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (maperasJenisRadioGroup.getCheckedRadioButtonId() == R.id.maperas_satu_banjar_radio) {
                            jenisMaperasFilterChip.setText("Satu Banjar Adat");
                            isJenisMaperasFilter = true;
                            filterType = "satu_banjar_adat";
                        } else if (maperasJenisRadioGroup.getCheckedRadioButtonId() == R.id.maperas_beda_banjar_radio) {
                            jenisMaperasFilterChip.setText("Beda Banjar Adat");
                            isJenisMaperasFilter = true;
                            filterType = "beda_banjar_adat";
                        } else if (maperasJenisRadioGroup.getCheckedRadioButtonId() == R.id.maperas_campuran_masuk_radio) {
                            jenisMaperasFilterChip.setText("Campuran Masuk");
                            isJenisMaperasFilter = true;
                            filterType = "campuran_masuk";
                        } else if (maperasJenisRadioGroup.getCheckedRadioButtonId() == R.id.maperas_campuran_keluar_radio) {
                            jenisMaperasFilterChip.setText("Campuran Keluar");
                            isJenisMaperasFilter = true;
                            filterType = "campuran_keluar";
                        } else {
                            jenisMaperasFilterChip.setText("Semua Jenis");
                            isJenisMaperasFilter = false;
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

        jenisMaperasFilterChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogFilterMaperasJenisView.getParent() != null) {
                    ((ViewGroup) dialogFilterMaperasJenisView.getParent()).removeView(dialogFilterMaperasJenisView);
                }
                dialogFilterJenisMaperas.show();
            }
        });

        //status maperas
        dialogFilterMaperasStatusView = LayoutInflater.from(this).inflate(R.layout.dialog_filter_maperas_status,
                getWindow().getDecorView().findViewById(android.R.id.content), false);
        maperasStatusRadioGroup = dialogFilterMaperasStatusView.findViewById(R.id.maperas_status_radio_group);
        maperasSahRadio = dialogFilterMaperasStatusView.findViewById(R.id.maperas_status_sah_radio);
        maperasAllStatusRadio = dialogFilterMaperasStatusView.findViewById(R.id.maperas_all_status_radio);
        maperasDraftRadio = dialogFilterMaperasStatusView.findViewById(R.id.maperas_status_draft_radio);
        maperasTerkonfirmasiRadio = dialogFilterMaperasStatusView.findViewById(R.id.maperas_status_terkonfirmasi_radio);
        maperasTidakTerkonfirmasiRadio = dialogFilterMaperasStatusView.findViewById(R.id.maperas_status_tidak_terkonfirmasi_radio);


        dialogFilterStatusMaperas =  new MaterialAlertDialogBuilder(this)
                .setTitle("Pilih Status Maperas")
                .setView(dialogFilterMaperasStatusView)
                .setPositiveButton("Terapkan Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (maperasStatusRadioGroup.getCheckedRadioButtonId() == R.id.maperas_status_sah_radio) {
                            statusMaperasFilterChip.setText("Sah");
                            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                    "duar.", Snackbar.LENGTH_LONG).show();
                            isStatusMaperasFilter = true;
                            filterStatus = "3";
                        } else if (maperasStatusRadioGroup.getCheckedRadioButtonId() == R.id.maperas_status_draft_radio) {
                            statusMaperasFilterChip.setText("Draft");
                            isStatusMaperasFilter = true;
                            filterStatus = "0";
                        } else if (maperasStatusRadioGroup.getCheckedRadioButtonId() == R.id.maperas_status_terkonfirmasi_radio) {
                            statusMaperasFilterChip.setText("Terkonfirmasi");
                            isStatusMaperasFilter = true;
                            filterStatus = "1";
                        } else if (maperasStatusRadioGroup.getCheckedRadioButtonId() == R.id.maperas_status_tidak_terkonfirmasi_radio) {
                            statusMaperasFilterChip.setText("Tidak Terkonfirmasi");
                            isStatusMaperasFilter = true;
                            filterStatus = "2";
                        } else {
                            statusMaperasFilterChip.setText("Semua Status");
                            isStatusMaperasFilter = false;
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

        statusMaperasFilterChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogFilterMaperasStatusView.getParent() != null) {
                    ((ViewGroup) dialogFilterMaperasStatusView.getParent()).removeView(dialogFilterMaperasStatusView);
                }
                dialogFilterStatusMaperas.show();
            }
        });


        //rentang tanggal


        dialogFilterMaperasWaktuView = LayoutInflater.from(this).inflate(R.layout.dialog_filter_maperas_waktu,
                getWindow().getDecorView().findViewById(android.R.id.content), false);
        maperasWaktuStartField = dialogFilterMaperasWaktuView.findViewById(R.id.maperas_filter_tanggal_mulai_field);
        maperasWaktuStartLayout = dialogFilterMaperasWaktuView.findViewById(R.id.maperas_filter_tanggal_mulai_form);
        maperasWaktuEndField = dialogFilterMaperasWaktuView.findViewById(R.id.maperas_filter_tanggal_akhir_field);
        maperasWaktuEndLayout = dialogFilterMaperasWaktuView.findViewById(R.id.maperas_filter_tanggal_akhir_form);
        maperasWaktuAllCheckBox = dialogFilterMaperasWaktuView.findViewById(R.id.maperas_filter_tanggal_all_checkbox);

        MaterialDatePicker.Builder<Long> datePickerBuilderMaperasStartDate = MaterialDatePicker.Builder.datePicker();
        datePickerBuilderMaperasStartDate.setTitleText("Pilih tanggal");
        final MaterialDatePicker<Long> datePickerMaperasStartDate = datePickerBuilderMaperasStartDate.build();

        datePickerMaperasStartDate.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selectedDate) {
                // link: https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
                TimeZone timeZoneUTC = TimeZone.getDefault();
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                SimpleDateFormat simpleFormat = new SimpleDateFormat("EEE, dd-MM-yyyy", Locale.US);
                Date date = new Date(selectedDate + offsetFromUTC);
                maperasWaktuStartField.setText(simpleFormat.format(date));
                isTanggalStart = true;
                maperasWaktuAllCheckBox.setChecked(false);
            }
        });

        maperasWaktuStartField.setShowSoftInputOnFocus(false);
        maperasWaktuStartField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(datePickerMaperasStartDate.isVisible())) {
                    datePickerMaperasStartDate.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                }
            }
        });

        maperasWaktuStartField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    if (!(datePickerMaperasStartDate.isVisible())) {
                        datePickerMaperasStartDate.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                }
            }
        });

        MaterialDatePicker.Builder<Long> datePickerBuilderMaperasEndDate = MaterialDatePicker.Builder.datePicker();
        datePickerBuilderMaperasEndDate.setTitleText("Pilih tanggal");
        final MaterialDatePicker<Long> datePickerMaperasEndDate = datePickerBuilderMaperasEndDate.build();

        datePickerMaperasEndDate.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selectedDate) {
                // link: https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
                TimeZone timeZoneUTC = TimeZone.getDefault();
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                SimpleDateFormat simpleFormat = new SimpleDateFormat("EEE, dd-MM-yyyy", Locale.US);
                Date date = new Date(selectedDate + offsetFromUTC);
                maperasWaktuEndField.setText(simpleFormat.format(date));
                isTanggalEnd = true;
                maperasWaktuAllCheckBox.setChecked(false);
            }
        });

        maperasWaktuEndField.setShowSoftInputOnFocus(false);
        maperasWaktuEndField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(datePickerMaperasEndDate.isVisible())) {
                    datePickerMaperasEndDate.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                }
            }
        });

        maperasWaktuEndField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    if (!(datePickerMaperasEndDate.isVisible())) {
                        datePickerMaperasEndDate.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                }
            }
        });

        maperasWaktuAllCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    maperasWaktuEndField.setText(null);
                    maperasWaktuStartField.setText(null);
                    maperasWaktuStartField.clearFocus();
                    maperasWaktuEndField.clearFocus();
                    isTanggalEnd = false;
                    isTanggalStart = false;
                }
            }
        });


        dialogFilterWaktuMaperas =  new MaterialAlertDialogBuilder(this)
                .setTitle("Pilih Rentang Waktu")
                .setView(dialogFilterMaperasWaktuView)
                .setPositiveButton("Terapkan Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (maperasWaktuAllCheckBox.isChecked()) {
                            isRentangWaktuMaperasFilter = false;
                            rentangWaktuMaperasFilter.setText("Semua Waktu");
                            filterStartDate = null;
                            filterEndDate = null;
                            getData(loginPreferences.getString("token", "empty"));
                        } else {
                            if (isTanggalEnd && isTanggalStart) {
                                isRentangWaktuMaperasFilter = true;
                                rentangWaktuMaperasFilter.setText(maperasWaktuStartField.getText()
                                        + " - " + maperasWaktuEndField.getText());
                                filterStartDate = ChangeDateTimeFormat.changeDateFormatForForm(maperasWaktuStartField.getText().toString());
                                filterEndDate = ChangeDateTimeFormat.changeDateFormatForForm(maperasWaktuEndField.getText().toString());
                            } else {
                                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                        "Rentang waktu tidak valid.", Snackbar.LENGTH_LONG).show();
                                isRentangWaktuMaperasFilter = false;
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

        rentangWaktuMaperasFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogFilterMaperasWaktuView.getParent() != null) {
                    ((ViewGroup) dialogFilterMaperasWaktuView.getParent()).removeView(dialogFilterMaperasWaktuView);
                }
                dialogFilterWaktuMaperas.show();
            }
        });

        clearFilterMaperasChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFilter();
            }
        });

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        loadingContainer = findViewById(R.id.maperas_loading_container);
        failedContainer = findViewById(R.id.maperas_failed_container);
        maperasContainer = findViewById(R.id.maperas_container);
        refreshMaperas = findViewById(R.id.maperas_refresh);
        maperasList = findViewById(R.id.maperas_list);
        maperasAllDataLoadedTextView = findViewById(R.id.all_data_loaded_maperas_text);
        maperasEmptyContainer = findViewById(R.id.maperas_empty_container);
        maperasLoadProgressContainer = findViewById(R.id.maperas_load_progress);
        maperasNestedScroll = findViewById(R.id.maperas_nested_scroll);

        linearLayoutManager = new LinearLayoutManager(this);
        maperasListAdapter = new MaperasListAdapter(this, maperasArrayList);
        maperasList.setLayoutManager(linearLayoutManager);
        maperasList.setAdapter(maperasListAdapter);
        maperasList.setNestedScrollingEnabled(true);


        refreshMaperas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(loginPreferences.getString("token", "empty"));
            }
        });

        maperasContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        if (isJenisMaperasFilter || isStatusMaperasFilter || isRentangWaktuMaperasFilter) {
            isFilterApplied = true;
            clearFilterMaperasChip.setVisibility(View.VISIBLE);
            getData(loginPreferences.getString("token", "empty"));
        } else {
            isFilterApplied = false;
            clearFilterMaperasChip.setVisibility(View.GONE);
        }
    }

    public void clearFilter() {
        isStatusMaperasFilter = false;
        isJenisMaperasFilter = false;
        isRentangWaktuMaperasFilter = false;
        isTanggalStart = false;
        isTanggalEnd = false;
        filterStatus = null;
        filterStartDate = null;
        filterEndDate = null;
        filterType = null;
        jenisMaperasFilterChip.setText("Semua Jenis");
        statusMaperasFilterChip.setText("Semua Status");
        rentangWaktuMaperasFilter.setText("Semua Waktu");
        getData(loginPreferences.getString("token", "empty"));
        checkForAppliedFilter();
    }

    public void getData(String token) {
        setLoadingContainerVisible();
        maperasContainer.setRefreshing(true);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<MaperasGetResponse> maperasGetResponseCall = getData.getMaperas(
                "Bearer " + token, filterStatus, filterStartDate, filterEndDate, filterType
        );
        maperasGetResponseCall.enqueue(new Callback<MaperasGetResponse>() {
            @Override
            public void onResponse(Call<MaperasGetResponse> call, Response<MaperasGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    maperasArrayList.clear();
                    maperasArrayList.addAll(response.body().getMaperasPaginate().getData());
//                    CacahKramaMipil cacahKramaMipil = maperasArrayList.get(0).getPurusa();
                    maperasListAdapter.notifyDataSetChanged();
                    perkawinanTotalText.setText(String.valueOf(maperasArrayList.size()));
                    if (maperasArrayList.size() == 0) {
                        maperasEmptyContainer.setVisibility(View.VISIBLE);
                    }
                    else {
                        maperasEmptyContainer.setVisibility(GONE);
                        currentPage = response.body().getMaperasPaginate().getCurrentPage();
                        lastPage = response.body().getMaperasPaginate().getLastPage();
                        if (currentPage != lastPage) {
                            nextPage = currentPage+1;
                            maperasNestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                                @Override
                                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                    if (!maperasNestedScroll.canScrollVertically(1)) {
                                        getNextData(token, nextPage);
                                    }
                                }
                            });
                        }
                        else {
                            maperasAllDataLoadedTextView.setVisibility(View.VISIBLE);
                        }
                    }
                    setKramaContainerVisible();
                }
                else {
                    setFailedContainerVisible();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                maperasContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<MaperasGetResponse> call, Throwable t) {
                setFailedContainerVisible();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void getNextData(String token, int page) {
        maperasLoadProgressContainer.setVisibility(View.VISIBLE);
        maperasContainer.setRefreshing(true);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<MaperasGetResponse> maperasGetResponseCall = getData.getMaperasNextPage(
                "Bearer " + token, filterStatus, filterStartDate, filterEndDate, filterType, page
        );
        maperasGetResponseCall.enqueue(new Callback<MaperasGetResponse>() {
            @Override
            public void onResponse(Call<MaperasGetResponse> call, Response<MaperasGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    maperasArrayList.addAll(response.body().getMaperasPaginate().getData());
                    currentPage = response.body().getMaperasPaginate().getCurrentPage();
                    maperasListAdapter.notifyItemInserted(maperasArrayList.size()-1);
                    maperasLoadProgressContainer.setVisibility(GONE);
                    if (currentPage != lastPage) {
                        nextPage++;
                    }
                    else {
                        maperasAllDataLoadedTextView.setVisibility(View.VISIBLE);
                        maperasNestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                            @Override
                            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                            }
                        });
                    }
                    maperasContainer.setRefreshing(false);
                }
                else {
                    maperasLoadProgressContainer.setVisibility(GONE);
                    maperasContainer.setRefreshing(false);
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MaperasGetResponse> call, Throwable t) {
                setFailedContainerVisible();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void setFailedContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(View.VISIBLE);
        maperasContainer.setVisibility(GONE);
    }

    public void setLoadingContainerVisible() {
        loadingContainer.setVisibility(View.VISIBLE);
        failedContainer.setVisibility(GONE);
        maperasContainer.setVisibility(GONE);
    }

    public void setKramaContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(GONE);
        maperasContainer.setVisibility(View.VISIBLE);
    }
}