package com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.campuranmasuk.inputdatapradana;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.master.Penduduk;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

public class PerkawinanCampuranMasukIdentitasPradanaActivity extends AppCompatActivity {

    private Button perkawinanPradanaNext, perkawinanPradanaProfilePictSelectButton, perkawinanPradanaProfilePictCaptureButton;
    private TextInputEditText perkawinanPradanaNikField, perkawinanPradanaNamaField,
            perkawinanPradanaGelarDepanField, perkawinanPradanaGelarBelakangField, perkawinanPradanaNamaAliasField;

    private TextInputLayout perkawinanPradanaNikLayout, perkawinanPradanaNamaLayout,
            perkawinanPradanaGelarDepanLayout, perkawinanPradanaGelarBelakangLayout, perkawinanPradanaNamaAliasLayout;

    Uri resultUri, tempUri;

    private ImageView perkawinanPradanaFotoImage;
    UCrop.Options options = new UCrop.Options();


    CacahKramaMipil cacahKramaPradana = new CacahKramaMipil();
    Penduduk pradanaPenduduk = new Penduduk();


    private boolean isNamaValid = false, isNikValid = false;

    private static final int REQUEST_CAMERA = 100;
    private static final int MY_PERMISSION_REQUEST_CAMERA = 101;

    private Toolbar homeToolbar;

    private final String PRADANA_CACAH_KEY = "PRADANA_CACAH_KEY";
    private final String PRADANA_CACAH_FOTO_KEY = "PRADANA_CACAH_FOTO_KEY";

    Gson gson = new Gson();

    ActivityResultLauncher<Intent> startActivityIntent;

    private TextView namaTercetak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perkawinan_campuran_masuk_identitas_pradana);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cacahKramaPradana.setPenduduk(pradanaPenduduk);

        namaTercetak = findViewById(R.id.perkawinan_campuran_masuk_pradana_nama_tercetak);

        perkawinanPradanaNext = findViewById(R.id.perkawinan_campuran_masuk_pradana_next_button);
        perkawinanPradanaProfilePictSelectButton = findViewById(R.id.perkawinan_campuran_masuk_pradana_select_foto_button);
        perkawinanPradanaProfilePictCaptureButton = findViewById(R.id.perkawinan_campuran_masuk_pradana_capture_foto_button);

        perkawinanPradanaFotoImage = findViewById(R.id.perkawinan_campuran_masuk_pradana_foto);

        perkawinanPradanaNikField = findViewById(R.id.perkawinan_campuran_masuk_pradana_nik_field);
        perkawinanPradanaNamaField = findViewById(R.id.perkawinan_campuran_masuk_pradana_nama_field);
        perkawinanPradanaGelarDepanField = findViewById(R.id.perkawinan_campuran_masuk_pradana_gelar_depan_field);
        perkawinanPradanaGelarBelakangField = findViewById(R.id.perkawinan_campuran_masuk_pradana_gelar_belakang_field);
        perkawinanPradanaNamaAliasField = findViewById(R.id.perkawinan_campuran_masuk_pradana_nama_alias_field);

        perkawinanPradanaNikLayout = findViewById(R.id.perkawinan_campuran_masuk_pradana_nik_layout);
        perkawinanPradanaNamaLayout = findViewById(R.id.perkawinan_campuran_masuk_pradana_nama_layout);
        perkawinanPradanaGelarDepanLayout = findViewById(R.id.perkawinan_campuran_masuk_pradana_gelar_depan_layout);
        perkawinanPradanaGelarBelakangLayout = findViewById(R.id.perkawinan_campuran_masuk_pradana_gelar_belakang_layout);
        perkawinanPradanaNamaAliasLayout = findViewById(R.id.perkawinan_campuran_masuk_pradana_nama_alias_layout);


        perkawinanPradanaNikField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (perkawinanPradanaNikField.getText().toString().length() < 16) {
                    perkawinanPradanaNikLayout.setError("NIK tidak boleh kurang dari 16 karakter");
                    isNikValid = false;
                }
                else if (perkawinanPradanaNikField.getText().toString().length() > 16) {
                    perkawinanPradanaNikLayout.setError("NIK tidak boleh lebih dari 16 karakter");
                    isNikValid = false;
                }
                else {
                    perkawinanPradanaNikLayout.setError(null);
                    isNikValid = true;
                }
            }
        });

        perkawinanPradanaNamaField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(perkawinanPradanaNamaField.getText().toString().length() == 0) {
                    perkawinanPradanaNamaLayout.setError("Nama tidak boleh kosong");
                    perkawinanPradanaNamaLayout.setErrorEnabled(true);
                    isNamaValid = false;
                }
                else {
                    if (perkawinanPradanaNamaField.getText().toString().length() < 2) {
                        perkawinanPradanaNamaLayout.setError("Nama tidak boleh kurang dari 2 karakter");
                        perkawinanPradanaNamaLayout.setErrorEnabled(true);
                        isNamaValid = false;
                    }
                    else {
                        perkawinanPradanaNamaLayout.setError(null);
                        perkawinanPradanaNamaLayout.setErrorEnabled(false);
                        isNamaValid = true;
                    }
                }
                updateNamaTercetak();
            }
        });

        perkawinanPradanaGelarDepanField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateNamaTercetak();
            }
        });

        perkawinanPradanaGelarBelakangField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateNamaTercetak();
            }
        });

        perkawinanPradanaProfilePictSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                String[] mimeTypes = {"image/png", "image/jpg", "image/jpeg"};
                chooseFile.setType("*/*");
                chooseFile.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                chooseFile = Intent.createChooser(chooseFile, "Pilih foto");
                startActivityForResult(chooseFile, 1);
            }
        });

        perkawinanPradanaProfilePictCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPermissionCameraGranted()) {
                    captureImage();
                }
            }
        });

        perkawinanPradanaNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNamaValid && isNikValid) {
                    Intent perkawinanPradanaNextIntent = new Intent(getApplicationContext(), PerkawinanCampuranMasukDataDiriPradanaActivity.class);
                    cacahKramaPradana.penduduk.setNama(perkawinanPradanaNamaField.getText().toString());
                    cacahKramaPradana.penduduk.setNik(perkawinanPradanaNikField.getText().toString());
                    if (perkawinanPradanaGelarDepanField.getText().toString().length() != 0) {
                        cacahKramaPradana.penduduk.setGelarDepan(perkawinanPradanaGelarDepanField.getText().toString());
                    }
                    if (perkawinanPradanaGelarBelakangField.getText().toString().length() != 0) {
                        cacahKramaPradana.penduduk.setGelarBelakang(perkawinanPradanaGelarBelakangField.getText().toString());
                    }
                    if (perkawinanPradanaNamaAliasField.getText().toString().length() != 0) {
                        cacahKramaPradana.penduduk.setNamaAlias(perkawinanPradanaNamaAliasField.getText().toString());
                    }
                    if (tempUri != null) {
                        perkawinanPradanaNextIntent.putExtra(PRADANA_CACAH_FOTO_KEY, resultUri.toString());
                    }
                    if (getIntent().hasExtra("JK")) {
                        perkawinanPradanaNextIntent.putExtra("JK", getIntent().getStringExtra("JK"));
                    }
                    perkawinanPradanaNextIntent.putExtra(PRADANA_CACAH_KEY, gson.toJson(cacahKramaPradana));
                    startActivityIntent.launch(perkawinanPradanaNextIntent);
                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Terdapat data yang belum valid. Silahkan periksa kembali", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 700) {
                            setResult(700, result.getData());
                            finish();
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            resultUri = UCrop.getOutput(data);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                tempUri = resultUri;
                perkawinanPradanaFotoImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }

        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            File out = new File(getApplicationContext().getCacheDir(), "profile_camera.jpg");
            cropPicture(Uri.fromFile(out));
        }

        if (resultCode == RESULT_OK && requestCode == 1) {
            tempUri = data.getData();
            cropPicture(tempUri);
        }
    }

    public void cropPicture(Uri sourceUri) {
        try {
            File.createTempFile("profile_pradana.jpg", null, getApplicationContext().getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
        File profilePictTemp = new File(getApplicationContext().getCacheDir(), "profile_pradana.jpg");
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(90);
        UCrop.of(sourceUri, Uri.fromFile(profilePictTemp))
                .withOptions(options)
                .withAspectRatio(3, 4)
                .withMaxResultSize(600, 800)
                .start(this);
    }

    private void captureImage() {
        try {
            File.createTempFile("profile_camera.jpg", null, getApplicationContext().getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
        File profilePictCamera = new File(getApplicationContext().getCacheDir(), "profile_camera.jpg");
        PackageManager pm = getPackageManager();
        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
            i.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                    getApplicationContext(),
                    "com.bagushikano.sikedatmobileadmin.fileprovider",
                    profilePictCamera));
            startActivityForResult(i, REQUEST_CAMERA);
        } else {
            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Kamera tidak tersedia", Snackbar.LENGTH_SHORT).show();
        }
    }

    private boolean isPermissionCameraGranted() {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Log.v("TAG", "Permission is granted");
            return true;
        } else {
            Log.v("TAG", "Permission is revoked");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImage();
            } else {
                Log.d("camera_permission", "REQUEST_CAMERA :: " + "not granted");
            }
        }
    }

    public void updateNamaTercetak() {
        namaTercetak.setText(StringFormatter.formatNamaWithGelar(
                perkawinanPradanaNamaField.getText().toString(),
                perkawinanPradanaGelarDepanField.getText().toString(),
                perkawinanPradanaGelarBelakangField.getText().toString()
                ));
    }
}