package com.bagushikano.sikedatmobileadmin.adapter.krama;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipil;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AnggotaKramaListAdapter extends RecyclerView.Adapter<AnggotaKramaListAdapter.ViewHolder> {
    private Context mContext;
    private final ArrayList<AnggotaKramaMipil> anggotaKramaMipilArrayList;
    private int position;
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    public AnggotaKramaListAdapter(Context context, ArrayList<AnggotaKramaMipil> anggotaKramaMipilArrayList) {
        mContext = context;
        this.anggotaKramaMipilArrayList = anggotaKramaMipilArrayList;
    }

    @NonNull
    @Override
    public AnggotaKramaListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.anggota_krama_mipil_card_layout, parent, false);
        return new AnggotaKramaListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnggotaKramaListAdapter.ViewHolder holder, int position) {
        holder.kramaMipilName.setText(StringFormatter.formatNamaWithGelar(
                anggotaKramaMipilArrayList.get(holder.getAdapterPosition()).getCacahKramaMipil().getPenduduk().getNama(),
                anggotaKramaMipilArrayList.get(holder.getAdapterPosition()).getCacahKramaMipil().getPenduduk().getGelarDepan(),
                anggotaKramaMipilArrayList.get(holder.getAdapterPosition()).getCacahKramaMipil().getPenduduk().getGelarBelakang()
        ));
        holder.kramaMipilAnggotaHubungan.setText(anggotaKramaMipilArrayList.get(holder.getAdapterPosition()).getStatusHubungan());
        holder.kramaMipilDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CacahKramaMipil cacahKramaMipil = anggotaKramaMipilArrayList.get(holder.getAdapterPosition()).getCacahKramaMipil();
                Intent kramaDetail = new Intent(mContext, CacahKramaMipilDetailActivity.class);
                Gson gson = new Gson();
                String kramaJson = gson.toJson(cacahKramaMipil);
                kramaDetail.putExtra(KRAMA_DETAIL_KEY, cacahKramaMipil.getId());
                mContext.startActivity(kramaDetail);
            }
        });
    }


    @Override
    public int getItemCount() {
        return anggotaKramaMipilArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView kramaMipilName, kramaMipilAnggotaHubungan;
        private final Button kramaMipilDetailButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kramaMipilName = itemView.findViewById(R.id.anggota_krama_nama_text);
            kramaMipilAnggotaHubungan = itemView.findViewById(R.id.anggota_krama_mipil_hubungan_text);
            kramaMipilDetailButton = itemView.findViewById(R.id.anggota_krama_mipil_detail_button);
        }
    }
}
