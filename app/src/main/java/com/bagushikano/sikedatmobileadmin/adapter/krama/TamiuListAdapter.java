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
import com.bagushikano.sikedatmobileadmin.activity.krama.KramaTamiuDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.krama.TamiuDetailActivity;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaTamiu;
import com.bagushikano.sikedatmobileadmin.model.krama.Tamiu;
import com.google.gson.Gson;

import java.util.ArrayList;

public class TamiuListAdapter extends RecyclerView.Adapter<TamiuListAdapter.ViewHolder> {
    private Context mContext;
    private final ArrayList<Tamiu> tamiuArrayList;
    private int position;
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    public TamiuListAdapter(Context context, ArrayList<Tamiu> tamiuArrayList) {
        mContext = context;
        this.tamiuArrayList = tamiuArrayList;
    }

    @NonNull
    @Override
    public TamiuListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.tamiu_card_layout, parent, false);
        return new TamiuListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TamiuListAdapter.ViewHolder holder, int position) {
        String namaFormated = tamiuArrayList.get(holder.getAdapterPosition()).getCacahTamiu().getPenduduk().getNama();
        if (tamiuArrayList.get(holder.getAdapterPosition()).getCacahTamiu().getPenduduk().getGelarDepan() != null) {
            namaFormated = String.format("%s %s",
                    tamiuArrayList.get(holder.getAdapterPosition()).getCacahTamiu().getPenduduk().getGelarDepan(),
                    tamiuArrayList.get(holder.getAdapterPosition()).getCacahTamiu().getPenduduk().getNama()
            );
        }
        if (tamiuArrayList.get(holder.getAdapterPosition()).getCacahTamiu().getPenduduk().getGelarBelakang() != null) {
            namaFormated = String.format("%s %s",
                    namaFormated,
                    tamiuArrayList.get(holder.getAdapterPosition()).getCacahTamiu().getPenduduk().getGelarBelakang()
            );
        }
        holder.tamiuName.setText(namaFormated);
        holder.tamiuNo.setText(tamiuArrayList.get(holder.getAdapterPosition()).getNomorTamiu());
        holder.tamiuBanjarDinas.setText(tamiuArrayList.get(holder.getAdapterPosition()).getCacahTamiu().getBanjarDinas().getNamaBanjarDinas());
        holder.kramaTamiuDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tamiu tamiu = tamiuArrayList.get(holder.getAdapterPosition());
                Intent kramaDetail = new Intent(mContext, TamiuDetailActivity.class);
                Gson gson = new Gson();
                String kramaJson = gson.toJson(tamiu);
                kramaDetail.putExtra(KRAMA_DETAIL_KEY, tamiu.getId());
                mContext.startActivity(kramaDetail);
            }
        });
    }


    @Override
    public int getItemCount() {
        return tamiuArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView tamiuName, tamiuNo, tamiuBanjarDinas;
        private final Button kramaTamiuDetailButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tamiuName = itemView.findViewById(R.id.krama_nama_text);
            tamiuNo = itemView.findViewById(R.id.krama_no_tamiu_text);
            tamiuBanjarDinas = itemView.findViewById(R.id.krama_banjar_dinas_text);
            kramaTamiuDetailButton = itemView.findViewById(R.id.krama_detail_button);
        }
    }
}