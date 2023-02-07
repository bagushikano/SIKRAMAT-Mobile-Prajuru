package com.bagushikano.sikedatmobileadmin.adapter.maperas;

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
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.bedabanjar.MaperasBedaBanjarDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.campurankeluar.MaperasCampuranKeluarDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.campuranmasuk.MaperasCampuranMasukDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.satubanjar.MaperasSatuBanjarDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.bedabanjar.PerkawinanBedaBanjarDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.satubanjar.PerkawinanSatuBanjarDetailActivity;
import com.bagushikano.sikedatmobileadmin.model.maperas.Maperas;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.Perkawinan;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;

import java.util.ArrayList;

public class MaperasListAdapter extends RecyclerView.Adapter<MaperasListAdapter.ViewHolder> {
    private Context mContext;
    private final ArrayList<Maperas> maperasArrayList;
    private int position;
    private final String MAPERAS_DETAIL_KEY = "MAPERAS_DETAIL_KEY";

    public MaperasListAdapter(Context context, ArrayList<Maperas> maperasArrayList) {
        mContext = context;
        this.maperasArrayList = maperasArrayList;
    }

    @NonNull
    @Override
    public MaperasListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.maperas_list_card_layout, parent, false);
        return new MaperasListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaperasListAdapter.ViewHolder holder, int position) {

        holder.maperasNo.setText(maperasArrayList.get(holder.getAdapterPosition()).getNomorMaperas());

        if (maperasArrayList.get(holder.getAdapterPosition()).getJenisMaperas().equals("satu_banjar_adat")) {
            holder.maperasJenis.setText("Satu Banjar Adat");
            holder.maperasNamaAnak.setText(StringFormatter.formatNamaWithGelar(
                    maperasArrayList.get(holder.getAdapterPosition()).getCacahKramaMipilBaru().getPenduduk().getNama(),
                    maperasArrayList.get(holder.getAdapterPosition()).getCacahKramaMipilBaru().getPenduduk().getGelarDepan(),
                    maperasArrayList.get(holder.getAdapterPosition()).getCacahKramaMipilBaru().getPenduduk().getGelarBelakang()
            ));
        } else if (maperasArrayList.get(holder.getAdapterPosition()).getJenisMaperas().equals("beda_banjar_adat")) {
            holder.maperasJenis.setText("Beda Banjar Adat");
            holder.maperasNamaAnak.setText(StringFormatter.formatNamaWithGelar(
                    maperasArrayList.get(holder.getAdapterPosition()).getCacahKramaMipilBaru().getPenduduk().getNama(),
                    maperasArrayList.get(holder.getAdapterPosition()).getCacahKramaMipilBaru().getPenduduk().getGelarDepan(),
                    maperasArrayList.get(holder.getAdapterPosition()).getCacahKramaMipilBaru().getPenduduk().getGelarBelakang()
            ));
        } else if (maperasArrayList.get(holder.getAdapterPosition()).getJenisMaperas().equals("campuran_masuk")) {
            holder.maperasJenis.setText("Campuran Masuk");
            holder.maperasNamaAnak.setText(StringFormatter.formatNamaWithGelar(
                    maperasArrayList.get(holder.getAdapterPosition()).getCacahKramaMipilBaru().getPenduduk().getNama(),
                    maperasArrayList.get(holder.getAdapterPosition()).getCacahKramaMipilBaru().getPenduduk().getGelarDepan(),
                    maperasArrayList.get(holder.getAdapterPosition()).getCacahKramaMipilBaru().getPenduduk().getGelarBelakang()
            ));
        } else if (maperasArrayList.get(holder.getAdapterPosition()).getJenisMaperas().equals("campuran_keluar")) {
            holder.maperasJenis.setText("Campuran Keluar");
            holder.maperasNamaAnak.setText(StringFormatter.formatNamaWithGelar(
                    maperasArrayList.get(holder.getAdapterPosition()).getCacahKramaMipilLama().getPenduduk().getNama(),
                    maperasArrayList.get(holder.getAdapterPosition()).getCacahKramaMipilLama().getPenduduk().getGelarDepan(),
                    maperasArrayList.get(holder.getAdapterPosition()).getCacahKramaMipilLama().getPenduduk().getGelarBelakang()
            ));
        }

        /*
            0 = ajuan masuk, 1 ajuan di proses, 2 ajuan di tolak, 3 ajuan di acc
         */

        if (maperasArrayList.get(holder.getAdapterPosition()).getStatusMaperas() == 1) {
            holder.maperasStatus.setText("Terkonfirmasi");
            holder.maperasStatus.setTextColor(mContext.getColor(R.color.yellow));
        } else if (maperasArrayList.get(holder.getAdapterPosition()).getStatusMaperas() == 2) {
            holder.maperasStatus.setText("Tidak Terkonfirmasi");
            holder.maperasStatus.setTextColor(mContext.getColor(R.color.red));
        } else if (maperasArrayList.get(holder.getAdapterPosition()).getStatusMaperas() == 0) {
            holder.maperasStatus.setText("Draft");
            holder.maperasStatus.setTextColor(mContext.getColor(R.color.yellow));
        } else if (maperasArrayList.get(holder.getAdapterPosition()).getStatusMaperas() == 3) {
            holder.maperasStatus.setText("Sah");
            holder.maperasStatus.setTextColor(mContext.getColor(R.color.green));
        }

        holder.maperasDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Maperas maperas = maperasArrayList.get(holder.getAdapterPosition());
                Intent maperasDetail = null;
                if (maperasArrayList.get(holder.getAdapterPosition()).getJenisMaperas().equals("satu_banjar_adat")) {
                    maperasDetail = new Intent(mContext, MaperasSatuBanjarDetailActivity.class);

                } else if (maperasArrayList.get(holder.getAdapterPosition()).getJenisMaperas().equals("beda_banjar_adat")) {
                    maperasDetail = new Intent(mContext, MaperasBedaBanjarDetailActivity.class);
                } else if (maperasArrayList.get(holder.getAdapterPosition()).getJenisMaperas().equals("campuran_masuk")) {
                    maperasDetail = new Intent(mContext, MaperasCampuranMasukDetailActivity.class);
                } else if (maperasArrayList.get(holder.getAdapterPosition()).getJenisMaperas().equals("campuran_keluar")) {
                    maperasDetail = new Intent(mContext, MaperasCampuranKeluarDetailActivity.class);
                }
                maperasDetail.putExtra(MAPERAS_DETAIL_KEY, maperas.getId());
                mContext.startActivity(maperasDetail);
            }
        });
    }


    @Override
    public int getItemCount() {
        return maperasArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView maperasNamaAnak, maperasNo, maperasJenis, maperasStatus;
        private final Button maperasDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            maperasNamaAnak = itemView.findViewById(R.id.maperas_nama);
            maperasStatus = itemView.findViewById(R.id.maperas_status);
            maperasNo = itemView.findViewById(R.id.maperas_no);
            maperasJenis = itemView.findViewById(R.id.maperas_jenis);
            maperasDetail = itemView.findViewById(R.id.maperas_detail);
        }
    }
}