package com.bagushikano.sikedatmobileadmin.adapter.maperas;

import android.app.Activity;
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
import com.bagushikano.sikedatmobileadmin.adapter.krama.KramaMipilListAdapter;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipil;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MaperasKramaMipilListAdapter extends RecyclerView.Adapter<MaperasKramaMipilListAdapter.ViewHolder> {
    private Context mContext;
    private final ArrayList<KramaMipil> kramaMipilArrayList;
    private int position;
    private final String KRAMA_MIPIL_SELECT_KEY = "KRAMA_MIPIL_SELECT_KEY";
    private final String KRAMA_MIPIL_DETAIL_KEY = "KRAMA_MIPIL_DETAIL_KEY";

    public MaperasKramaMipilListAdapter(Context context, ArrayList<KramaMipil> kramaMipilArrayList) {
        mContext = context;
        this.kramaMipilArrayList = kramaMipilArrayList;
    }

    @NonNull
    @Override
    public MaperasKramaMipilListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.maperas_krama_mipil_card_layout, parent, false);
        return new MaperasKramaMipilListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaperasKramaMipilListAdapter.ViewHolder holder, int position) {
        holder.kramaMipilName.setText(StringFormatter.formatNamaWithGelar(
                kramaMipilArrayList.get(holder.getAdapterPosition()).getCacahKramaMipil().getPenduduk().getNama(),
                kramaMipilArrayList.get(holder.getAdapterPosition()).getCacahKramaMipil().getPenduduk().getGelarDepan(),
                kramaMipilArrayList.get(holder.getAdapterPosition()).getCacahKramaMipil().getPenduduk().getGelarBelakang()
        ));
        holder.kramaMipilNo.setText(kramaMipilArrayList.get(holder.getAdapterPosition()).getNomorKramaMipil());
        holder.kramaMipilBanjarAdat.setText(kramaMipilArrayList.get(holder.getAdapterPosition()).getBanjarAdat().getNamaBanjarAdat());
        holder.kramaMipilDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KramaMipil kramaMipil = kramaMipilArrayList.get(holder.getAdapterPosition());
                Intent kramaDetail = new Intent(mContext, KramaMipilDetailActivity.class);
                Gson gson = new Gson();
                String kramaJson = gson.toJson(kramaMipil);
                kramaDetail.putExtra(KRAMA_MIPIL_DETAIL_KEY, kramaMipil.getId());
                mContext.startActivity(kramaDetail);
            }
        });

        holder.kramaMipilSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KramaMipil kramaMipil = kramaMipilArrayList.get(holder.getAdapterPosition());
                Intent selected = new Intent();
                Gson gson = new Gson();
                String kramaJson = gson.toJson(kramaMipil);
                selected.putExtra(KRAMA_MIPIL_SELECT_KEY, kramaJson);
                ((Activity) mContext).setResult(200, selected);
                ((Activity) mContext).finish();
            }
        });
    }


    @Override
    public int getItemCount() {
        return kramaMipilArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView kramaMipilName, kramaMipilNo, kramaMipilBanjarAdat;
        private final Button kramaMipilDetailButton, kramaMipilSelectButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kramaMipilName = itemView.findViewById(R.id.krama_nama_text);
            kramaMipilNo = itemView.findViewById(R.id.krama_no_mipil_text);
            kramaMipilBanjarAdat = itemView.findViewById(R.id.krama_banjar_adat_text);
            kramaMipilDetailButton = itemView.findViewById(R.id.krama_detail_button);
            kramaMipilSelectButton = itemView.findViewById(R.id.krama_select_button);
        }
    }
}