package com.bagushikano.sikedatmobileadmin.adapter.krama;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.krama.KramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.adapter.cacahkrama.CacahKramaMipilListAdapter;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipil;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class KramaMipilListAdapter extends RecyclerView.Adapter<KramaMipilListAdapter.ViewHolder> {
    private Context mContext;
    private final ArrayList<KramaMipil> kramaMipilArrayList;
    private int position;
    private final String KRAMA_DETAIL_KEY = "KRAMA_MIPIL_DETAIL_KEY";

    public KramaMipilListAdapter(Context context, ArrayList<KramaMipil> kramaMipilArrayList) {
        mContext = context;
        this.kramaMipilArrayList = kramaMipilArrayList;
    }

    @NonNull
    @Override
    public KramaMipilListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.krama_mipil_card_layout, parent, false);
        return new KramaMipilListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KramaMipilListAdapter.ViewHolder holder, int position) {
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
                kramaDetail.putExtra(KRAMA_DETAIL_KEY, kramaMipil.getId());
                mContext.startActivity(kramaDetail);
            }
        });
    }


    @Override
    public int getItemCount() {
        return kramaMipilArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView kramaMipilName, kramaMipilNo, kramaMipilBanjarAdat;
        private final Button kramaMipilDetailButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kramaMipilName = itemView.findViewById(R.id.krama_nama_text);
            kramaMipilNo = itemView.findViewById(R.id.krama_no_mipil_text);
            kramaMipilBanjarAdat = itemView.findViewById(R.id.krama_banjar_adat_text);
            kramaMipilDetailButton = itemView.findViewById(R.id.krama_detail_button);
        }
    }
}