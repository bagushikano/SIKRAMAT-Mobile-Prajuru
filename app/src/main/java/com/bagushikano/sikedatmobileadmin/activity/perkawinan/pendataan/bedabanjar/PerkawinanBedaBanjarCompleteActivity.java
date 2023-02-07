package com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.bedabanjar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.satubanjar.PerkawinanSatuBanjarDetailActivity;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.Perkawinan;
import com.bagushikano.sikedatmobileadmin.util.ChangeDateTimeFormat;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;
import com.google.gson.Gson;

public class PerkawinanBedaBanjarCompleteActivity extends AppCompatActivity {


    private ImageView checkImage;
    private AnimatedVectorDrawable checkAnim;
    private Button backToLogin;

    private final String PERKAWINAN_DETAIL_KEY = "PERKAWINAN_DETAIL_KEY";

    private TextView perkawinanPurusaName, perkawinanPradanaName,
            perkawinanNo, perkawinanJenis, perkawinanTanggal, perkawinanStatus;
    private Button perkawinanDetailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perkawinan_beda_banjar_complete);

        backToLogin = findViewById(R.id.register_complete_button);
        checkImage = findViewById(R.id.register_check_image);
        Drawable d = checkImage.getDrawable();
        if (d instanceof AnimatedVectorDrawable) {
            checkAnim = (AnimatedVectorDrawable) d;
            checkAnim.start();
        }
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Gson gson = new Gson();
        Perkawinan perkawinan = gson.fromJson(getIntent().getStringExtra("PERKAWINAN_KEY"), Perkawinan.class);

        perkawinanPurusaName = findViewById(R.id.perkawinan_purusa_name_text);
        perkawinanPradanaName = findViewById(R.id.perkawinan_pradana_name_text);
        perkawinanNo = findViewById(R.id.perkawinan_no_text);
        perkawinanJenis = findViewById(R.id.perkawinan_type_text);
        perkawinanTanggal = findViewById(R.id.perkawinan_tanggal_text);
        perkawinanStatus = findViewById(R.id.perkawinan_status_text);
        perkawinanDetailButton = findViewById(R.id.perkawinan_detail_button);

        perkawinanPurusaName.setText(
                StringFormatter.formatNamaWithGelar(
                        perkawinan.getPurusa().getPenduduk().getNama(),
                        perkawinan.getPurusa().getPenduduk().getGelarDepan(),
                        perkawinan.getPurusa().getPenduduk().getGelarBelakang()
                )
        );
        perkawinanPradanaName.setText(
                StringFormatter.formatNamaWithGelar(
                        perkawinan.getPradana().getPenduduk().getNama(),
                        perkawinan.getPradana().getPenduduk().getGelarDepan(),
                        perkawinan.getPradana().getPenduduk().getGelarBelakang()
                )
        );
        if (perkawinan.getNomorPerkawinan() != null) {
            perkawinanNo.setText(perkawinan.getNomorPerkawinan());
        }
        else {
            perkawinanNo.setText("-");
        }

        perkawinanTanggal.setText(ChangeDateTimeFormat.changeDateFormat(
                perkawinan.getTanggalPerkawinan()));

        if (perkawinan.getStatusPerkawinan() == 0) {
            perkawinanStatus.setText("Draft");
            perkawinanStatus.setTextColor(getApplicationContext().getColor(R.color.yellow));
        } else if (perkawinan.getStatusPerkawinan() == 3) {
            perkawinanStatus.setText("Sah");
            perkawinanStatus.setTextColor(getApplicationContext().getColor(R.color.green));
        }

        perkawinanJenis.setText("Beda Banjar Adat");

        perkawinanDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent perkawinanDetail = new Intent(getApplicationContext(), PerkawinanBedaBanjarDetailActivity.class);
                perkawinanDetail.putExtra(PERKAWINAN_DETAIL_KEY, perkawinan.getId());
                startActivity(perkawinanDetail);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}