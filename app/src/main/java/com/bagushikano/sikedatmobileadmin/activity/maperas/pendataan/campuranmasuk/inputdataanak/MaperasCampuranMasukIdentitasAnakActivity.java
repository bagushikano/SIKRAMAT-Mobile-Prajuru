package com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.campuranmasuk.inputdataanak;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

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

public class MaperasCampuranMasukIdentitasAnakActivity extends AppCompatActivity {

    private Button maperasAnakNext, maperasAnakProfilePictSelectButton, maperasAnakProfilePictCaptureButton;
    private TextInputEditText maperasAnakNikField, maperasAnakNamaField,
            maperasAnakGelarDepanField, maperasAnakGelarBelakangField, maperasAnakNamaAliasField;

    private TextInputLayout maperasAnakNikLayout, maperasAnakNamaLayout,
            maperasAnakGelarDepanLayout, maperasAnakGelarBelakangLayout, maperasAnakNamaAliasLayout;

    Uri resultUri, tempUri;

    private ImageView maperasAnakFotoImage;
    UCrop.Options options = new UCrop.Options();


    CacahKramaMipil cacahKramaPradana = new CacahKramaMipil();
    Penduduk anakPenduduk = new Penduduk();


    private boolean isNamaValid = false, isNikValid = false;

    private static final int REQUEST_CAMERA = 100;
    private static final int MY_PERMISSION_REQUEST_CAMERA = 101;

    private Toolbar homeToolbar;

    private final String ANAK_CACAH_KEY = "ANAK_CACAH_KEY";
    private final String ANAK_CACAH_FOTO_KEY = "ANAK_CACAH_FOTO_KEY";

    Gson gson = new Gson();

    ActivityResultLauncher<Intent> startActivityIntent;
    private TextView namaTercetak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maperas_campuran_masuk_identitas_anak);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cacahKramaPradana.setPenduduk(anakPenduduk);
        namaTercetak = findViewById(R.id.perkawinan_campuran_masuk_pradana_nama_tercetak);

        maperasAnakNext = findViewById(R.id.maperas_campuran_masuk_anak_next_button);
        maperasAnakProfilePictSelectButton = findViewById(R.id.maperas_campuran_masuk_anak_select_foto_button);
        maperasAnakProfilePictCaptureButton = findViewById(R.id.maperas_campuran_masuk_anak_capture_foto_button);

        maperasAnakFotoImage = findViewById(R.id.maperas_campuran_masuk_anak_foto);

        maperasAnakNikField = findViewById(R.id.maperas_campuran_masuk_anak_nik_field);
        maperasAnakNamaField = findViewById(R.id.maperas_campuran_masuk_anak_nama_field);
        maperasAnakGelarDepanField = findViewById(R.id.maperas_campuran_masuk_anak_gelar_depan_field);
        maperasAnakGelarBelakangField = findViewById(R.id.maperas_campuran_masuk_anak_gelar_belakang_field);
        maperasAnakNamaAliasField = findViewById(R.id.maperas_campuran_masuk_anak_nama_alias_field);

        maperasAnakNikLayout = findViewById(R.id.maperas_campuran_masuk_anak_nik_layout);
        maperasAnakNamaLayout = findViewById(R.id.maperas_campuran_masuk_anak_nama_layout);
        maperasAnakGelarDepanLayout = findViewById(R.id.maperas_campuran_masuk_anak_gelar_depan_layout);
        maperasAnakGelarBelakangLayout = findViewById(R.id.maperas_campuran_masuk_anak_gelar_belakang_layout);
        maperasAnakNamaAliasLayout = findViewById(R.id.maperas_campuran_masuk_anak_nama_alias_layout);


        maperasAnakNikField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (maperasAnakNikField.getText().toString().length() < 16) {
                    maperasAnakNikLayout.setError("NIK tidak boleh kurang dari 16 karakter");
                    isNikValid = false;
                }
                else if (maperasAnakNikField.getText().toString().length() > 16) {
                    maperasAnakNikLayout.setError("NIK tidak boleh lebih dari 16 karakter");
                    isNikValid = false;
                }
                else {
                    maperasAnakNikLayout.setError(null);
                    isNikValid = true;
                }
            }
        });

        maperasAnakNamaField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(maperasAnakNamaField.getText().toString().length() == 0) {
                    maperasAnakNamaLayout.setError("Nama tidak boleh kosong");
                    maperasAnakNamaLayout.setErrorEnabled(true);
                    isNamaValid = false;
                }
                else {
                    if (maperasAnakNamaField.getText().toString().length() < 2) {
                        maperasAnakNamaLayout.setError("Nama tidak boleh kurang dari 2 karakter");
                        maperasAnakNamaLayout.setErrorEnabled(true);
                        isNamaValid = false;
                    }
                    else {
                        maperasAnakNamaLayout.setError(null);
                        maperasAnakNamaLayout.setErrorEnabled(false);
                        isNamaValid = true;
                    }
                }
                updateNamaTercetak();
            }
        });

       maperasAnakGelarDepanField.addTextChangedListener(new TextWatcher() {
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

        maperasAnakGelarBelakangField.addTextChangedListener(new TextWatcher() {
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

        maperasAnakProfilePictSelectButton.setOnClickListener(new View.OnClickListener() {
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

        maperasAnakProfilePictCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPermissionCameraGranted()) {
                    captureImage();
                }
            }
        });

        maperasAnakNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNamaValid && isNikValid) {
                    Intent maperasAnakNextIntent = new Intent(getApplicationContext(), MaperasCampuranMasukDataDiriAnakActivity.class);
                    cacahKramaPradana.penduduk.setNama(maperasAnakNamaField.getText().toString());
                    cacahKramaPradana.penduduk.setNik(maperasAnakNikField.getText().toString());
                    if (maperasAnakGelarDepanField.getText().toString().length() != 0) {
                        cacahKramaPradana.penduduk.setGelarDepan(maperasAnakGelarDepanField.getText().toString());
                    }
                    if (maperasAnakGelarBelakangField.getText().toString().length() != 0) {
                        cacahKramaPradana.penduduk.setGelarBelakang(maperasAnakGelarBelakangField.getText().toString());
                    }
                    if (maperasAnakNamaAliasField.getText().toString().length() != 0) {
                        cacahKramaPradana.penduduk.setNamaAlias(maperasAnakNamaAliasField.getText().toString());
                    }
                    if (tempUri != null) {
                        maperasAnakNextIntent.putExtra(ANAK_CACAH_FOTO_KEY, resultUri.toString());
                    }
                    maperasAnakNextIntent.putExtra(ANAK_CACAH_KEY, gson.toJson(cacahKramaPradana));
                    startActivityIntent.launch(maperasAnakNextIntent);
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
                maperasAnakFotoImage.setImageBitmap(bitmap);
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
            File.createTempFile("profile_anak.jpg", null, getApplicationContext().getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
        File profilePictTemp = new File(getApplicationContext().getCacheDir(), "profile_anak.jpg");
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
                maperasAnakNamaField.getText().toString(),
                maperasAnakGelarDepanField.getText().toString(),
                maperasAnakGelarBelakangField.getText().toString()
        ));
    }
}