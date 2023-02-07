package com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.satubanjar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.bedabanjar.PerkawinanBedaBanjarDetailActivity;
import com.bagushikano.sikedatmobileadmin.model.maperas.Maperas;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.Perkawinan;
import com.google.gson.Gson;

public class MaperasSatuBanjarCompleteActivity extends AppCompatActivity {

    private TextView maperasNamaAnak, maperasNo, maperasJenis, maperasStatus;
    private Button maperasDetail;
    private final String MAPERAS_DETAIL_KEY = "MAPERAS_DETAIL_KEY";

    private ImageView checkImage;
    private AnimatedVectorDrawable checkAnim;
    private Button backToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maperas_satu_banjar_complete);

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
        Maperas maperas = gson.fromJson(getIntent().getStringExtra("MAPERAS_KEY"), Maperas.class);

        maperasNamaAnak = findViewById(R.id.maperas_nama);
        maperasStatus = findViewById(R.id.maperas_status);
        maperasNo = findViewById(R.id.maperas_no);
        maperasJenis = findViewById(R.id.maperas_jenis);
        maperasDetail = findViewById(R.id.maperas_detail);

        maperasNamaAnak.setText(maperas.getCacahKramaMipilBaru().getPenduduk().getNama());
        maperasNo.setText(maperas.getNomorMaperas());

        /*
            0 = ajuan masuk, 1 ajuan di proses, 2 ajuan di tolak, 3 ajuan di acc
         */

        if (maperas.getStatusMaperas() == 1) {

        } else if (maperas.getStatusMaperas() == 2) {

        } else if (maperas.getStatusMaperas() == 0) {

        } else if (maperas.getStatusMaperas() == 3) {
            maperasStatus.setText("Sah");
            maperasStatus.setTextColor(getApplicationContext().getColor(R.color.green));
        }

        maperasDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent perkawinanDetail = new Intent(getApplicationContext(), MaperasSatuBanjarDetailActivity.class);
                perkawinanDetail.putExtra(MAPERAS_DETAIL_KEY, maperas.getId());
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