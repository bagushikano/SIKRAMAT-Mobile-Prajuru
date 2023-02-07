package com.bagushikano.sikedatmobileadmin.adapter.cacahkrama;

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
import com.google.gson.Gson;

import java.util.ArrayList;

public class CacahTamiuListAdapter extends RecyclerView.Adapter<CacahTamiuListAdapter.ViewHolder> {
    private Context mContext;
    private final ArrayList<CacahTamiu> cacahTamiuArrayList;
    private int position;
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    public CacahTamiuListAdapter(Context context, ArrayList<CacahTamiu> cacahTamiuArrayList) {
        mContext = context;
        this.cacahTamiuArrayList = cacahTamiuArrayList;
    }

    @NonNull
    @Override
    public CacahTamiuListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cacah_tamiu_card_layout, parent, false);
        return new CacahTamiuListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CacahTamiuListAdapter.ViewHolder holder, int position) {
        holder.kramaMipilName.setText(cacahTamiuArrayList.get(holder.getAdapterPosition()).getPenduduk().getNama());
        holder.kramaMipilNik.setText(cacahTamiuArrayList.get(holder.getAdapterPosition()).getPenduduk().getNik());
        holder.noKramaMipil.setText(cacahTamiuArrayList.get(holder.getAdapterPosition()).getNomorCacahTamiu());
        holder.kramaMipilDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CacahTamiu cacahTamiu = cacahTamiuArrayList.get(holder.getAdapterPosition());
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
        return cacahTamiuArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView kramaMipilName, kramaMipilNik, noKramaMipil;
        private final Button kramaMipilDetailButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kramaMipilName = itemView.findViewById(R.id.krama_nama_text);
            kramaMipilNik = itemView.findViewById(R.id.krama_nik_text);
            noKramaMipil = itemView.findViewById(R.id.krama_no_mipil_text);
            kramaMipilDetailButton = itemView.findViewById(R.id.krama_detail_button);
        }
    }
}