package com.bagushikano.sikedatmobileadmin.activity.perceraian.pendataan;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.perceraian.PerceraianDetailActivity;
import com.bagushikano.sikedatmobileadmin.model.perceraian.Perceraian;
import com.bagushikano.sikedatmobileadmin.util.ChangeDateTimeFormat;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;
import com.google.gson.Gson;

public class PerceraianCompleteActivity extends AppCompatActivity {

    private ImageView checkImage;
    private AnimatedVectorDrawable checkAnim;
    private Button backToLogin;

    private final String PERCERAIAN_DETAIL_KEY = "PERCERAIAN_DETAIL_KEY";

    private TextView perceraianPurusaName, perceraianPradanaName,
            perceraianNo, perceraianJenis, perceraianTanggal, perceraianStatus;
    private Button perceraianDetailButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perceraian_complete);

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
        Perceraian perceraian = gson.fromJson(getIntent().getStringExtra(PERCERAIAN_DETAIL_KEY), Perceraian.class);

        perceraianPurusaName = findViewById(R.id.perceraian_purusa_name_text);
        perceraianPradanaName = findViewById(R.id.perceraian_pradana_name_text);
        perceraianNo = findViewById(R.id.perceraian_no_text);
        perceraianTanggal = findViewById(R.id.perceraian_tanggal_text);
        perceraianStatus = findViewById(R.id.perceraian_status_text);
        perceraianDetailButton = findViewById(R.id.perceraian_detail_button);

        perceraianPurusaName.setText(
                StringFormatter.formatNamaWithGelar(
                        perceraian.getPurusa().getPenduduk().getNama(),
                        perceraian.getPurusa().getPenduduk().getGelarDepan(),
                        perceraian.getPurusa().getPenduduk().getGelarBelakang()
                )
        );
        perceraianPradanaName.setText(
                StringFormatter.formatNamaWithGelar(
                        perceraian.getPradana().getPenduduk().getNama(),
                        perceraian.getPradana().getPenduduk().getGelarDepan(),
                        perceraian.getPradana().getPenduduk().getGelarBelakang()
                )
        );
        if (perceraian.getNomorPerceraian() != null) {
            perceraianNo.setText(perceraian.getNomorPerceraian());
        }
        else {
            perceraianNo.setText("-");
        }

        perceraianTanggal.setText(ChangeDateTimeFormat.changeDateFormat(
                perceraian.getTanggalPerceraian()));

        if (perceraian.getStatusPerceraian() == 0) {
            perceraianStatus.setText("Draft");
            perceraianStatus.setTextColor(getApplicationContext().getColor(R.color.yellow));
        }

        perceraianDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent perceraianDetail = new Intent(getApplicationContext(), PerceraianDetailActivity.class);
                perceraianDetail.putExtra(PERCERAIAN_DETAIL_KEY, perceraian.getId());
                startActivity(perceraianDetail);
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