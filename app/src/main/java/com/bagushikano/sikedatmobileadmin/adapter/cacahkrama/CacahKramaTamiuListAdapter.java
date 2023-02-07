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
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaTamiuDetailActivity;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaTamiu;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CacahKramaTamiuListAdapter extends RecyclerView.Adapter<CacahKramaTamiuListAdapter.ViewHolder> {
    private Context mContext;
    private final ArrayList<CacahKramaTamiu> cacahKramaTamiuArrayList;
    private int position;
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    public CacahKramaTamiuListAdapter(Context context, ArrayList<CacahKramaTamiu> cacahKramaTamiuArrayList) {
        mContext = context;
        this.cacahKramaTamiuArrayList = cacahKramaTamiuArrayList;
    }

    @NonNull
    @Override
    public CacahKramaTamiuListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cacah_krama_tamiu_card_layout, parent, false);
        return new CacahKramaTamiuListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CacahKramaTamiuListAdapter.ViewHolder holder, int position) {
        holder.kramaMipilName.setText(cacahKramaTamiuArrayList.get(holder.getAdapterPosition()).getPenduduk().getNama());
        holder.kramaMipilNik.setText(cacahKramaTamiuArrayList.get(holder.getAdapterPosition()).getPenduduk().getNik());
        holder.noKramaMipil.setText(cacahKramaTamiuArrayList.get(holder.getAdapterPosition()).getNomorCacahKramaTamiu());
        holder.kramaMipilDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CacahKramaTamiu cacahKramaTamiu = cacahKramaTamiuArrayList.get(holder.getAdapterPosition());
                Intent kramaDetail = new Intent(mContext, CacahKramaTamiuDetailActivity.class);
                Gson gson = new Gson();
                String kramaJson = gson.toJson(cacahKramaTamiu);
                kramaDetail.putExtra(KRAMA_DETAIL_KEY, cacahKramaTamiu.getId());
                mContext.startActivity(kramaDetail);
            }
        });
    }


    @Override
    public int getItemCount() {
        return cacahKramaTamiuArrayList.size();
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