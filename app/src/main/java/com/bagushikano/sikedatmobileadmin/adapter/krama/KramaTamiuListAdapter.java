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
import com.bagushikano.sikedatmobileadmin.activity.krama.KramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.krama.KramaTamiuDetailActivity;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaTamiu;
import com.google.gson.Gson;

import java.util.ArrayList;

public class KramaTamiuListAdapter extends RecyclerView.Adapter<KramaTamiuListAdapter.ViewHolder> {
    private Context mContext;
    private final ArrayList<KramaTamiu> kramaTamiuArrayList;
    private int position;
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    public KramaTamiuListAdapter(Context context, ArrayList<KramaTamiu> kramaTamiuArrayList) {
        mContext = context;
        this.kramaTamiuArrayList = kramaTamiuArrayList;
    }

    @NonNull
    @Override
    public KramaTamiuListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.krama_tamiu_card_layout, parent, false);
        return new KramaTamiuListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KramaTamiuListAdapter.ViewHolder holder, int position) {
        String namaFormated = kramaTamiuArrayList.get(holder.getAdapterPosition()).getCacahKramaTamiu().getPenduduk().getNama();
        if (kramaTamiuArrayList.get(holder.getAdapterPosition()).getCacahKramaTamiu().getPenduduk().getGelarDepan() != null) {
            namaFormated = String.format("%s %s",
                    kramaTamiuArrayList.get(holder.getAdapterPosition()).getCacahKramaTamiu().getPenduduk().getGelarDepan(),
                    kramaTamiuArrayList.get(holder.getAdapterPosition()).getCacahKramaTamiu().getPenduduk().getNama()
            );
        }
        if (kramaTamiuArrayList.get(holder.getAdapterPosition()).getCacahKramaTamiu().getPenduduk().getGelarBelakang() != null) {
            namaFormated = String.format("%s %s",
                    namaFormated,
                    kramaTamiuArrayList.get(holder.getAdapterPosition()).getCacahKramaTamiu().getPenduduk().getGelarBelakang()
            );
        }
        holder.kramaTamiuName.setText(namaFormated);
        holder.kramaTamiuNo.setText(kramaTamiuArrayList.get(holder.getAdapterPosition()).getNomorKramaTamiu());
        holder.kramaTamiuBanjarDinas.setText(kramaTamiuArrayList.get(holder.getAdapterPosition()).getCacahKramaTamiu().getBanjarDinas().getNamaBanjarDinas());
        holder.kramaTamiuDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KramaTamiu kramaTamiu = kramaTamiuArrayList.get(holder.getAdapterPosition());
                Intent kramaDetail = new Intent(mContext, KramaTamiuDetailActivity.class);
                Gson gson = new Gson();
                String kramaJson = gson.toJson(kramaTamiu);
                kramaDetail.putExtra(KRAMA_DETAIL_KEY, kramaTamiu.getId());
                mContext.startActivity(kramaDetail);
            }
        });
    }


    @Override
    public int getItemCount() {
        return kramaTamiuArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView kramaTamiuName, kramaTamiuNo, kramaTamiuBanjarDinas;
        private final Button kramaTamiuDetailButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kramaTamiuName = itemView.findViewById(R.id.krama_nama_text);
            kramaTamiuNo = itemView.findViewById(R.id.krama_no_krama_tamiu_text);
            kramaTamiuBanjarDinas = itemView.findViewById(R.id.krama_banjar_dinas_text);
            kramaTamiuDetailButton = itemView.findViewById(R.id.krama_detail_button);
        }
    }
}