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
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaTamiuDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahTamiuDetailActivity;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaTamiu;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahTamiu;
import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaKramaTamiu;
import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaTamiu;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AnggotaTamiuListAdapter extends RecyclerView.Adapter<AnggotaTamiuListAdapter.ViewHolder> {
    private Context mContext;
    private final ArrayList<AnggotaTamiu> anggotaTamiuArrayList;
    private int position;
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    public AnggotaTamiuListAdapter(Context context, ArrayList<AnggotaTamiu> anggotaTamiuArrayList) {
        mContext = context;
        this.anggotaTamiuArrayList = anggotaTamiuArrayList;
    }

    @NonNull
    @Override
    public AnggotaTamiuListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.anggota_tamiu_card_layout, parent, false);
        return new AnggotaTamiuListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnggotaTamiuListAdapter.ViewHolder holder, int position) {
        String namaFormated = anggotaTamiuArrayList.get(holder.getAdapterPosition()).getCacahTamiu().getPenduduk().getNama();
        if (anggotaTamiuArrayList.get(holder.getAdapterPosition()).getCacahTamiu().getPenduduk().getGelarDepan() != null) {
            namaFormated = String.format("%s %s",
                    anggotaTamiuArrayList.get(holder.getAdapterPosition()).getCacahTamiu().getPenduduk().getGelarDepan(),
                    anggotaTamiuArrayList.get(holder.getAdapterPosition()).getCacahTamiu().getPenduduk().getNama()
            );
        }
        if (anggotaTamiuArrayList.get(holder.getAdapterPosition()).getCacahTamiu().getPenduduk().getGelarBelakang() != null) {
            namaFormated = String.format("%s %s",
                    namaFormated,
                    anggotaTamiuArrayList.get(holder.getAdapterPosition()).getCacahTamiu().getPenduduk().getGelarBelakang()
            );
        }
        holder.kramaTamiuName.setText(namaFormated);
        holder.kramaTamiuAnggotaHubungan.setText(anggotaTamiuArrayList.get(holder.getAdapterPosition()).getStatusHubungan());
        holder.kramaTamiuDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CacahTamiu cacahTamiu = anggotaTamiuArrayList.get(holder.getAdapterPosition()).getCacahTamiu();
                Intent kramaDetail = new Intent(mContext, CacahTamiuDetailActivity.class);
                Gson gson = new Gson();
                String kramaJson = gson.toJson(cacahTamiu);
                kramaDetail.putExtra(KRAMA_DETAIL_KEY, cacahTamiu.getId());
                mContext.startActivity(kramaDetail);
            }
        });
    }


    @Override
    public int getItemCount() {
        return anggotaTamiuArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView kramaTamiuName, kramaTamiuAnggotaHubungan;
        private final Button kramaTamiuDetailButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kramaTamiuName = itemView.findViewById(R.id.anggota_krama_nama_text);
            kramaTamiuAnggotaHubungan = itemView.findViewById(R.id.anggota_krama_tamiu_hubungan_text);
            kramaTamiuDetailButton = itemView.findViewById(R.id.anggota_krama_tamiu_detail_button);
        }
    }
}
