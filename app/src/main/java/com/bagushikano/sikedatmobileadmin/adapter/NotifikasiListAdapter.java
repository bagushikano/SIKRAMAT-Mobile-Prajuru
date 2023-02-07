package com.bagushikano.sikedatmobileadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.kelahiran.KelahiranAjuanDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.kematian.KematianAjuanDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.bedabanjar.MaperasBedaBanjarDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.perceraian.PerceraianDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.bedabanjar.PerkawinanBedaBanjarDetailActivity;
import com.bagushikano.sikedatmobileadmin.model.notifikasi.Notifikasi;

import java.util.ArrayList;

public class NotifikasiListAdapter extends RecyclerView.Adapter<NotifikasiListAdapter.ViewHolder>{
    private Context mContext;
    private final ArrayList<Notifikasi> notifikasiArrayList;
    private int position;
    private final String KEMATIAN_DETAIL_KEY = "KEMATIAN_DETAIL_KEY";
    private final String KELAHIRAN_DETAIL_KEY = "KELAHIRAN_DETAIL_KEY";
    private final String NOTIFIKASI_KEY = "NOTIFIKASI_KEY";

    public NotifikasiListAdapter(Context context, ArrayList<Notifikasi> notifikasiArrayList) {
        mContext = context;
        this.notifikasiArrayList = notifikasiArrayList;
    }

    @NonNull
    @Override
    public NotifikasiListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.notifikasi_card_layout, parent, false);
        return new NotifikasiListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifikasiListAdapter.ViewHolder holder, int position) {
        holder.notifikasiContent.setText(notifikasiArrayList.get(holder.getAdapterPosition()).getKonten());
        if ((notifikasiArrayList.get(holder.getAdapterPosition()).getSubJenis().equals("kelahiran"))) {
            holder.notifikasiTitle.setText("Pengajuan Kelahiran");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Notifikasi notifikasi = notifikasiArrayList.get(holder.getAdapterPosition());
                    Intent kelahiranDetail = new Intent(mContext, KelahiranAjuanDetailActivity.class);
                    kelahiranDetail.putExtra(KELAHIRAN_DETAIL_KEY, notifikasi.getDataId());
                    mContext.startActivity(kelahiranDetail);
                }
            });
        }
        else if ((notifikasiArrayList.get(holder.getAdapterPosition()).getSubJenis().equals("kematian"))) {
            holder.notifikasiTitle.setText("Pengajuan Kematian");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Notifikasi notifikasi = notifikasiArrayList.get(holder.getAdapterPosition());
                    Intent kelahiranDetail = new Intent(mContext, KematianAjuanDetailActivity.class);
                    kelahiranDetail.putExtra(KEMATIAN_DETAIL_KEY, notifikasi.getDataId());
                    mContext.startActivity(kelahiranDetail);
                }
            });
        }

        else if ((notifikasiArrayList.get(holder.getAdapterPosition()).getSubJenis().equals("perkawinan"))) {
            holder.notifikasiTitle.setText("Pendataan Perkawinan");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Notifikasi notifikasi = notifikasiArrayList.get(holder.getAdapterPosition());
                    Intent kelahiranDetail = new Intent(mContext, PerkawinanBedaBanjarDetailActivity.class);
                    kelahiranDetail.putExtra("PERKAWINAN_DETAIL_KEY", notifikasi.getDataId());
                    mContext.startActivity(kelahiranDetail);
                }
            });
        }

        else if ((notifikasiArrayList.get(holder.getAdapterPosition()).getSubJenis().equals("perceraian"))) {
            holder.notifikasiTitle.setText("Pendataan Perceraian");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Notifikasi notifikasi = notifikasiArrayList.get(holder.getAdapterPosition());
                    Intent kelahiranDetail = new Intent(mContext, PerceraianDetailActivity.class);
                    kelahiranDetail.putExtra("PERCERAIAN_DETAIL_KEY", notifikasi.getDataId());
                    mContext.startActivity(kelahiranDetail);
                }
            });
        }

        else if ((notifikasiArrayList.get(holder.getAdapterPosition()).getSubJenis().equals("maperas"))) {
            holder.notifikasiTitle.setText("Pendataan Maperas");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Notifikasi notifikasi = notifikasiArrayList.get(holder.getAdapterPosition());
                    Intent kelahiranDetail = new Intent(mContext, MaperasBedaBanjarDetailActivity.class);
                    kelahiranDetail.putExtra("MAPERAS_DETAIL_KEY", notifikasi.getDataId());
                    mContext.startActivity(kelahiranDetail);
                }
            });
        }

        holder.notifikasiDate.setText(notifikasiArrayList.get(holder.getAdapterPosition()).getConvertedCreatedAt());
    }


    @Override
    public int getItemCount() {
        return notifikasiArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView notifikasiTitle, notifikasiDate, notifikasiContent;
        private final LinearLayout notifikasiCardContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notifikasiTitle = itemView.findViewById(R.id.notifikasi_title_text);
            notifikasiDate = itemView.findViewById(R.id.notifikasi_date_text);
            notifikasiContent = itemView.findViewById(R.id.notifikasi_content_text);
            notifikasiCardContainer = itemView.findViewById(R.id.notifikasi_card_container);
        }
    }
}
