package com.bagushikano.sikedatmobileadmin.activity.perceraian;

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
import com.bagushikano.sikedatmobileadmin.activity.perceraian.pendataan.PerceraianDataFirstActivity;
import com.bagushikano.sikedatmobileadmin.adapter.perceraian.PerceraianListAdapter;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.perceraian.Perceraian;
import com.bagushikano.sikedatmobileadmin.model.perceraian.PerceraianGetResponse;
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

public class PerceraianDaftarActivity extends AppCompatActivity {


    private Toolbar homeToolbar;

    private Boolean isFilterApplied = false, isStatusPerceraianFilter = false,
            isJenisPerceraianFilter = false, isRentangWaktuPerceraianFilter = false;

    private Chip statusPerceraianFilterChip, jenisPerceraianFilterChip,
            rentangWaktuPerceraianFilter, clearFilterPerceraianChip;

    // status perceraian
    View dialogFilterPerceraianStatusView;
    RadioGroup perceraianStatusRadioGroup;
    RadioButton perceraianSahRadio, perceraianDraftRadio,
            perceraianTerkonfirmasiRadio, perceraianTidakTerkonfirmasiRadio, perceraianAllStatusRadio;
    MaterialAlertDialogBuilder dialogFilterStatusPerceraian;

    // rentang waktu
    View dialogFilterPerceraianWaktuView;
    TextInputEditText perceraianWaktuStartField, perceraianWaktuEndField;
    TextInputLayout perceraianWaktuStartLayout, perceraianWaktuEndLayout;
    MaterialAlertDialogBuilder dialogFilterWaktuPerceraian;
    CheckBox perceraianWaktuAllCheckBox;
    Boolean isTanggalStart = false, isTanggalEnd = false;

    private String filterStatus, filterStartDate, filterEndDate, filterType;

    RecyclerView perceraianList;
    ArrayList<Perceraian> perceraianArrayList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    PerceraianListAdapter perceraianListAdapter;

    LinearLayout loadingContainer, failedContainer, perceraianEmptyContainer;
    SwipeRefreshLayout perceraianContainer;
    Button refreshPerceraian;
    SharedPreferences loginPreferences;

    LinearLayout perceraianLoadProgressContainer;
    NestedScrollView perceraianNestedScroll;

    TextView perceraianAllDataLoadedTextView;
    int currentPage;
    int nextPage;
    int lastPage;

    private Button perceraianDataBaruButton;
    ActivityResultLauncher<Intent> startActivityIntent;


    TextView perkawinanTotalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perceraian_daftar);

        perkawinanTotalText = findViewById(R.id.perceraian_total_text);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        perceraianDataBaruButton = findViewById(R.id.perceraian_data_baru_button);
        perceraianDataBaruButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent perceraitanNew = new Intent(getApplicationContext(), PerceraianDataFirstActivity.class);
                startActivityIntent.launch(perceraitanNew);
            }
        });

        statusPerceraianFilterChip = findViewById(R.id.perceraian_status_filter_chip);
        jenisPerceraianFilterChip = findViewById(R.id.perceraian_jenis_filter_chip);
        rentangWaktuPerceraianFilter = findViewById(R.id.perceraian_waktu_filter_chip);
        clearFilterPerceraianChip = findViewById(R.id.perceraian_reset_filter_chip);

        //status perceraian
        dialogFilterPerceraianStatusView = LayoutInflater.from(this).inflate(R.layout.dialog_filter_perceraian_status,
                getWindow().getDecorView().findViewById(android.R.id.content), false);
        perceraianStatusRadioGroup = dialogFilterPerceraianStatusView.findViewById(R.id.perceraian_status_radio_group);
        perceraianSahRadio = dialogFilterPerceraianStatusView.findViewById(R.id.perceraian_status_sah_radio);
        perceraianAllStatusRadio = dialogFilterPerceraianStatusView.findViewById(R.id.perceraian_all_status_radio);
        perceraianDraftRadio = dialogFilterPerceraianStatusView.findViewById(R.id.perceraian_status_draft_radio);
        perceraianTerkonfirmasiRadio = dialogFilterPerceraianStatusView.findViewById(R.id.perceraian_status_terkonfirmasi_radio);
        perceraianTidakTerkonfirmasiRadio = dialogFilterPerceraianStatusView.findViewById(R.id.perceraian_status_tidak_terkonfirmasi_radio);


        dialogFilterStatusPerceraian =  new MaterialAlertDialogBuilder(this)
                .setTitle("Pilih Status Perceraian")
                .setView(dialogFilterPerceraianStatusView)
                .setPositiveButton("Terapkan Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (perceraianStatusRadioGroup.getCheckedRadioButtonId() == R.id.perceraian_status_sah_radio) {
                            statusPerceraianFilterChip.setText("Sah");
                            isStatusPerceraianFilter = true;
                            filterStatus = "3";
                        } else if (perceraianStatusRadioGroup.getCheckedRadioButtonId() == R.id.perceraian_status_draft_radio) {
                            statusPerceraianFilterChip.setText("Draft");
                            isStatusPerceraianFilter = true;
                            filterStatus = "0";
                        } else if (perceraianStatusRadioGroup.getCheckedRadioButtonId() == R.id.perceraian_status_terkonfirmasi_radio) {
                            statusPerceraianFilterChip.setText("Terkonfirmasi");
                            isStatusPerceraianFilter = true;
                            filterStatus = "1";
                        } else if (perceraianStatusRadioGroup.getCheckedRadioButtonId() == R.id.perceraian_status_tidak_terkonfirmasi_radio) {
                            statusPerceraianFilterChip.setText("Tidak Terkonfirmasi");
                            isStatusPerceraianFilter = true;
                            filterStatus = "2";
                        } else {
                            statusPerceraianFilterChip.setText("Semua Status");
                            isStatusPerceraianFilter = false;
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

        statusPerceraianFilterChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogFilterPerceraianStatusView.getParent() != null) {
                    ((ViewGroup) dialogFilterPerceraianStatusView.getParent()).removeView(dialogFilterPerceraianStatusView);
                }
                dialogFilterStatusPerceraian.show();
            }
        });


        //rentang tanggal


        dialogFilterPerceraianWaktuView = LayoutInflater.from(this).inflate(R.layout.dialog_filter_perceraian_waktu,
                getWindow().getDecorView().findViewById(android.R.id.content), false);
        perceraianWaktuStartField = dialogFilterPerceraianWaktuView.findViewById(R.id.perceraian_filter_tanggal_mulai_field);
        perceraianWaktuStartLayout = dialogFilterPerceraianWaktuView.findViewById(R.id.perceraian_filter_tanggal_mulai_form);
        perceraianWaktuEndField = dialogFilterPerceraianWaktuView.findViewById(R.id.perceraian_filter_tanggal_akhir_field);
        perceraianWaktuEndLayout = dialogFilterPerceraianWaktuView.findViewById(R.id.perceraian_filter_tanggal_akhir_form);
        perceraianWaktuAllCheckBox = dialogFilterPerceraianWaktuView.findViewById(R.id.perceraian_filter_tanggal_all_checkbox);

        MaterialDatePicker.Builder<Long> datePickerBuilderPerceraianStartDate = MaterialDatePicker.Builder.datePicker();
        datePickerBuilderPerceraianStartDate.setTitleText("Pilih tanggal");
        final MaterialDatePicker<Long> datePickerPerceraianStartDate = datePickerBuilderPerceraianStartDate.build();

        datePickerPerceraianStartDate.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selectedDate) {
                // link: https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
                TimeZone timeZoneUTC = TimeZone.getDefault();
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                SimpleDateFormat simpleFormat = new SimpleDateFormat("EEE, dd-MM-yyyy", Locale.US);
                Date date = new Date(selectedDate + offsetFromUTC);
                perceraianWaktuStartField.setText(simpleFormat.format(date));
                isTanggalStart = true;
                perceraianWaktuAllCheckBox.setChecked(false);
            }
        });

        perceraianWaktuStartField.setShowSoftInputOnFocus(false);
        perceraianWaktuStartField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(datePickerPerceraianStartDate.isVisible())) {
                    datePickerPerceraianStartDate.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                }
            }
        });

        perceraianWaktuStartField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    if (!(datePickerPerceraianStartDate.isVisible())) {
                        datePickerPerceraianStartDate.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                }
            }
        });

        MaterialDatePicker.Builder<Long> datePickerBuilderPerceraianEndDate = MaterialDatePicker.Builder.datePicker();
        datePickerBuilderPerceraianEndDate.setTitleText("Pilih tanggal");
        final MaterialDatePicker<Long> datePickerPerceraianEndDate = datePickerBuilderPerceraianEndDate.build();

        datePickerPerceraianEndDate.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selectedDate) {
                // link: https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
                TimeZone timeZoneUTC = TimeZone.getDefault();
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                SimpleDateFormat simpleFormat = new SimpleDateFormat("EEE, dd-MM-yyyy", Locale.US);
                Date date = new Date(selectedDate + offsetFromUTC);
                perceraianWaktuEndField.setText(simpleFormat.format(date));
                isTanggalEnd = true;
                perceraianWaktuAllCheckBox.setChecked(false);
            }
        });

        perceraianWaktuEndField.setShowSoftInputOnFocus(false);
        perceraianWaktuEndField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(datePickerPerceraianEndDate.isVisible())) {
                    datePickerPerceraianEndDate.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                }
            }
        });

        perceraianWaktuEndField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    if (!(datePickerPerceraianEndDate.isVisible())) {
                        datePickerPerceraianEndDate.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                }
            }
        });

        perceraianWaktuAllCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    perceraianWaktuEndField.setText(null);
                    perceraianWaktuStartField.setText(null);
                    perceraianWaktuStartField.clearFocus();
                    perceraianWaktuEndField.clearFocus();
                    isTanggalEnd = false;
                    isTanggalStart = false;
                }
            }
        });


        dialogFilterWaktuPerceraian =  new MaterialAlertDialogBuilder(this)
                .setTitle("Pilih Rentang Waktu")
                .setView(dialogFilterPerceraianWaktuView)
                .setPositiveButton("Terapkan Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (perceraianWaktuAllCheckBox.isChecked()) {
                            isRentangWaktuPerceraianFilter = false;
                            rentangWaktuPerceraianFilter.setText("Semua Waktu");
                            filterStartDate = null;
                            filterEndDate = null;
                            getData(loginPreferences.getString("token", "empty"));
                        } else {
                            if (isTanggalEnd && isTanggalStart) {
                                isRentangWaktuPerceraianFilter = true;
                                rentangWaktuPerceraianFilter.setText(perceraianWaktuStartField.getText()
                                        + " - " + perceraianWaktuEndField.getText());
                                filterStartDate = ChangeDateTimeFormat.changeDateFormatForForm(perceraianWaktuStartField.getText().toString());
                                filterEndDate = ChangeDateTimeFormat.changeDateFormatForForm(perceraianWaktuEndField.getText().toString());
                            } else {
                                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                        "Rentang waktu tidak valid.", Snackbar.LENGTH_LONG).show();
                                isRentangWaktuPerceraianFilter = false;
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

        rentangWaktuPerceraianFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogFilterPerceraianWaktuView.getParent() != null) {
                    ((ViewGroup) dialogFilterPerceraianWaktuView.getParent()).removeView(dialogFilterPerceraianWaktuView);
                }
                dialogFilterWaktuPerceraian.show();
            }
        });

        clearFilterPerceraianChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFilter();
            }
        });

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        loadingContainer = findViewById(R.id.perceraian_loading_container);
        failedContainer = findViewById(R.id.perceraian_failed_container);
        perceraianContainer = findViewById(R.id.perceraian_container);
        refreshPerceraian = findViewById(R.id.perceraian_refresh);
        perceraianList = findViewById(R.id.perceraian_list);
        perceraianAllDataLoadedTextView = findViewById(R.id.all_data_loaded_perceraian_text);
        perceraianEmptyContainer = findViewById(R.id.perceraian_empty_container);
        perceraianLoadProgressContainer = findViewById(R.id.perceraian_load_progress);
        perceraianNestedScroll = findViewById(R.id.perceraian_nested_scroll);

        linearLayoutManager = new LinearLayoutManager(this);
        perceraianListAdapter = new PerceraianListAdapter(this, perceraianArrayList);
        perceraianList.setLayoutManager(linearLayoutManager);
        perceraianList.setAdapter(perceraianListAdapter);
        perceraianList.setNestedScrollingEnabled(true);


        refreshPerceraian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(loginPreferences.getString("token", "empty"));
            }
        });

        perceraianContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        if (isJenisPerceraianFilter || isStatusPerceraianFilter || isRentangWaktuPerceraianFilter) {
            isFilterApplied = true;
            clearFilterPerceraianChip.setVisibility(View.VISIBLE);
            getData(loginPreferences.getString("token", "empty"));
        } else {
            isFilterApplied = false;
            clearFilterPerceraianChip.setVisibility(View.GONE);
        }
    }

    public void clearFilter() {
        isStatusPerceraianFilter = false;
        isJenisPerceraianFilter = false;
        isRentangWaktuPerceraianFilter = false;
        isTanggalStart = false;
        isTanggalEnd = false;
        filterStatus = null;
        filterStartDate = null;
        filterEndDate = null;
        filterType = null;
        jenisPerceraianFilterChip.setText("Semua Jenis");
        statusPerceraianFilterChip.setText("Semua Status");
        rentangWaktuPerceraianFilter.setText("Semua Waktu");
        getData(loginPreferences.getString("token", "empty"));
        checkForAppliedFilter();
    }

    public void getData(String token) {
        setLoadingContainerVisible();
        perceraianContainer.setRefreshing(true);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<PerceraianGetResponse> perceraianGetResponseCall = getData.getPerceraian(
                "Bearer " + token, filterStatus, filterStartDate, filterEndDate
        );
        perceraianGetResponseCall.enqueue(new Callback<PerceraianGetResponse>() {
            @Override
            public void onResponse(Call<PerceraianGetResponse> call, Response<PerceraianGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    perceraianArrayList.clear();
                    perceraianArrayList.addAll(response.body().getPerceraianPaginate().getData());
//                    CacahKramaMipil cacahKramaMipil = perceraianArrayList.get(0).getPurusa();
                    perceraianListAdapter.notifyDataSetChanged();
                    perkawinanTotalText.setText(String.valueOf(perceraianArrayList.size()));
                    if (perceraianArrayList.size() == 0) {
                        perceraianEmptyContainer.setVisibility(View.VISIBLE);
                    }
                    else {
                        perceraianEmptyContainer.setVisibility(GONE);
                        currentPage = response.body().getPerceraianPaginate().getCurrentPage();
                        lastPage = response.body().getPerceraianPaginate().getLastPage();
                        if (currentPage != lastPage) {
                            nextPage = currentPage+1;
                            perceraianNestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                                @Override
                                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                    if (!perceraianNestedScroll.canScrollVertically(1)) {
                                        getNextData(token, nextPage);
                                    }
                                }
                            });
                        }
                        else {
                            perceraianAllDataLoadedTextView.setVisibility(View.VISIBLE);
                        }
                    }
                    setKramaContainerVisible();
                }
                else {
                    setFailedContainerVisible();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                perceraianContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<PerceraianGetResponse> call, Throwable t) {
                setFailedContainerVisible();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void getNextData(String token, int page) {
        perceraianLoadProgressContainer.setVisibility(View.VISIBLE);
        perceraianContainer.setRefreshing(true);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<PerceraianGetResponse> perceraianGetResponseCall = getData.getPerceraianNextPage(
                "Bearer " + token, filterStatus, filterStartDate, filterEndDate, page);
        perceraianGetResponseCall.enqueue(new Callback<PerceraianGetResponse>() {
            @Override
            public void onResponse(Call<PerceraianGetResponse> call, Response<PerceraianGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    perceraianArrayList.addAll(response.body().getPerceraianPaginate().getData());
                    currentPage = response.body().getPerceraianPaginate().getCurrentPage();
                    perceraianListAdapter.notifyItemInserted(perceraianArrayList.size()-1);
                    perceraianLoadProgressContainer.setVisibility(GONE);
                    if (currentPage != lastPage) {
                        nextPage++;
                    }
                    else {
                        perceraianAllDataLoadedTextView.setVisibility(View.VISIBLE);
                        perceraianNestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                            @Override
                            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                            }
                        });
                    }
                    perceraianContainer.setRefreshing(false);
                }
                else {
                    perceraianLoadProgressContainer.setVisibility(GONE);
                    perceraianContainer.setRefreshing(false);
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PerceraianGetResponse> call, Throwable t) {
                setFailedContainerVisible();
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