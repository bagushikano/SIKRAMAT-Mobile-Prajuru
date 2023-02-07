package com.bagushikano.sikedatmobileadmin.adapter.perceraian;

import static android.view.View.GONE;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaKramaMipil;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;

import java.util.ArrayList;

public class PerceraianAnggotaSelectStatusAdapter  extends RecyclerView.Adapter<PerceraianAnggotaSelectStatusAdapter.ViewHolder> {
    private Context mContext;
    private final ArrayList<AnggotaKramaMipil> anggotaKramaMipilArrayList;
    private int position;
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    public PerceraianAnggotaSelectStatusAdapter(Context context, ArrayList<AnggotaKramaMipil> anggotaKramaMipilArrayList) {
        mContext = context;
        this.anggotaKramaMipilArrayList = anggotaKramaMipilArrayList;
    }

    @NonNull
    @Override
    public PerceraianAnggotaSelectStatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.perceraian_anggota_select_status_card, parent, false);
        return new PerceraianAnggotaSelectStatusAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PerceraianAnggotaSelectStatusAdapter.ViewHolder holder, int position) {
        holder.kramaMipilName.setText(StringFormatter.formatNamaWithGelar(
                anggotaKramaMipilArrayList.get(holder.getAdapterPosition()).getCacahKramaMipil().getPenduduk().getNama(),
                anggotaKramaMipilArrayList.get(holder.getAdapterPosition()).getCacahKramaMipil().getPenduduk().getGelarDepan(),
                anggotaKramaMipilArrayList.get(holder.getAdapterPosition()).getCacahKramaMipil().getPenduduk().getGelarBelakang()
        ));
        holder.kramaMipilAnggotaHubungan.setText(anggotaKramaMipilArrayList.get(holder.getAdapterPosition()).getStatusHubungan());

        holder.radioStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.perkawinan_anggota_ikut_purusa_radio) {
                    anggotaKramaMipilArrayList.get(holder.getAdapterPosition()).setStatusBaru("ikut_purusa");
                } else if (i == R.id.perkawinan_anggota_ikut_pradana_radio) {
                    anggotaKramaMipilArrayList.get(holder.getAdapterPosition()).setStatusBaru("ikut_pradana");
                }
            }
        });
        anggotaKramaMipilArrayList.get(holder.getAdapterPosition()).setStatusBaru("ikut_purusa");
    }


    @Override
    public int getItemCount() {
        return anggotaKramaMipilArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView kramaMipilName, kramaMipilAnggotaHubungan;
        private final RadioGroup radioStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kramaMipilName = itemView.findViewById(R.id.anggota_krama_nama_text);
            kramaMipilAnggotaHubungan = itemView.findViewById(R.id.anggota_krama_mipil_hubungan_text);
            radioStatus = itemView.findViewById(R.id.perkawinan_anggota_status_radio_group);
        }
    }
}

