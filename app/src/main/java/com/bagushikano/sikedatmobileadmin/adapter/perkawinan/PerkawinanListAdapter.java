package com.bagushikano.sikedatmobileadmin.adapter.perkawinan;

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
import com.bagushikano.sikedatmobileadmin.activity.kelahiran.KelahiranAjuanDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.PerkawinanDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.bedabanjar.PerkawinanBedaBanjarDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.campurankeluar.PerkawinanCampuranKeluarDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.campuranmasuk.PerkawinanCampuranMasukDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.satubanjar.PerkawinanSatuBanjarDetailActivity;
import com.bagushikano.sikedatmobileadmin.adapter.kelahiran.KelahiranAjuanListAdapter;
import com.bagushikano.sikedatmobileadmin.model.kelahiran.KelahiranAjuan;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.Perkawinan;
import com.bagushikano.sikedatmobileadmin.util.ChangeDateTimeFormat;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;

import java.util.ArrayList;

public class PerkawinanListAdapter extends RecyclerView.Adapter<PerkawinanListAdapter.ViewHolder> {
    private Context mContext;
    private final ArrayList<Perkawinan> perkawinanArrayList;
    private int position;
    private final String PERKAWINAN_DETAIL_KEY = "PERKAWINAN_DETAIL_KEY";


    public PerkawinanListAdapter(Context context, ArrayList<Perkawinan> perkawinanArrayList) {
        mContext = context;
        this.perkawinanArrayList = perkawinanArrayList;
    }

    @NonNull
    @Override
    public PerkawinanListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.perkawinan_list_card_layout, parent, false);
        return new PerkawinanListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PerkawinanListAdapter.ViewHolder holder, int position) {

        if (perkawinanArrayList.get(holder.getAdapterPosition()).getJenisPerkawinan().equals("campuran_keluar")) {
            holder.perkawinanPurusaName.setText(
                    StringFormatter.formatNamaWithGelar(
                            perkawinanArrayList.get(holder.getAdapterPosition()).getPradana().getPenduduk().getNama(),
                            perkawinanArrayList.get(holder.getAdapterPosition()).getPradana().getPenduduk().getGelarDepan(),
                            perkawinanArrayList.get(holder.getAdapterPosition()).getPradana().getPenduduk().getGelarBelakang()
                    )
            );
            holder.purusaTitle.setText("Nama Cacah Krama Mipil");
            holder.pradanaTitle.setText("Nama Pasangan");
            holder.perkawinanPradanaName.setText(perkawinanArrayList.get(holder.getAdapterPosition()).getNamaPasangan().toString());
        } else {
            holder.perkawinanPurusaName.setText(
                    StringFormatter.formatNamaWithGelar(
                            perkawinanArrayList.get(holder.getAdapterPosition()).getPurusa().getPenduduk().getNama(),
                            perkawinanArrayList.get(holder.getAdapterPosition()).getPurusa().getPenduduk().getGelarDepan(),
                            perkawinanArrayList.get(holder.getAdapterPosition()).getPurusa().getPenduduk().getGelarBelakang()
                    )
            );
            holder.perkawinanPradanaName.setText(
                    StringFormatter.formatNamaWithGelar(
                            perkawinanArrayList.get(holder.getAdapterPosition()).getPradana().getPenduduk().getNama(),
                            perkawinanArrayList.get(holder.getAdapterPosition()).getPradana().getPenduduk().getGelarDepan(),
                            perkawinanArrayList.get(holder.getAdapterPosition()).getPradana().getPenduduk().getGelarBelakang()
                    )
            );
        }
        if (perkawinanArrayList.get(holder.getAdapterPosition()).getNomorPerkawinan() != null) {
            holder.perkawinanNo.setText(perkawinanArrayList.get(holder.getAdapterPosition()).getNomorPerkawinan());
        }
        else {
            holder.perkawinanNo.setText("-");
        }

        holder.perkawinanTanggal.setText(ChangeDateTimeFormat.changeDateFormat(
                perkawinanArrayList.get(holder.getAdapterPosition()).getTanggalPerkawinan()));

        if (perkawinanArrayList.get(holder.getAdapterPosition()).getStatusPerkawinan() == 1) {
            holder.perkawinanStatus.setText("Terkonfirmasi");
            holder.perkawinanStatus.setTextColor(mContext.getColor(R.color.yellow));
        } else if (perkawinanArrayList.get(holder.getAdapterPosition()).getStatusPerkawinan() == 2) {
            holder.perkawinanStatus.setText("Tidak Terkonfirmasi");
            holder.perkawinanStatus.setTextColor(mContext.getColor(R.color.red));
        } else if (perkawinanArrayList.get(holder.getAdapterPosition()).getStatusPerkawinan() == 0) {
            holder.perkawinanStatus.setText("Draft");
            holder.perkawinanStatus.setTextColor(mContext.getColor(R.color.yellow));
        } else if (perkawinanArrayList.get(holder.getAdapterPosition()).getStatusPerkawinan() == 3) {
            holder.perkawinanStatus.setText("Sah");
            holder.perkawinanStatus.setTextColor(mContext.getColor(R.color.green));
        }

        if (perkawinanArrayList.get(holder.getAdapterPosition()).getJenisPerkawinan().equals("satu_banjar_adat")) {
            holder.perkawinanJenis.setText("Satu Banjar Adat");

        } else if (perkawinanArrayList.get(holder.getAdapterPosition()).getJenisPerkawinan().equals("beda_banjar_adat")) {
            holder.perkawinanJenis.setText("Beda Banjar Adat");

        } else if (perkawinanArrayList.get(holder.getAdapterPosition()).getJenisPerkawinan().equals("campuran_masuk")) {
            holder.perkawinanJenis.setText("Campuran Masuk");

        } else if (perkawinanArrayList.get(holder.getAdapterPosition()).getJenisPerkawinan().equals("campuran_keluar")) {
            holder.perkawinanJenis.setText("Campuran Keluar");
        }
        holder.perkawinanDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Perkawinan perkawinan = perkawinanArrayList.get(holder.getAdapterPosition());
                Intent perkawinanDetail = null;
                if (perkawinanArrayList.get(holder.getAdapterPosition()).getJenisPerkawinan().equals("satu_banjar_adat")) {
                    perkawinanDetail = new Intent(mContext, PerkawinanSatuBanjarDetailActivity.class);

                } else if (perkawinanArrayList.get(holder.getAdapterPosition()).getJenisPerkawinan().equals("beda_banjar_adat")) {
                    perkawinanDetail = new Intent(mContext, PerkawinanBedaBanjarDetailActivity.class);
                } else if (perkawinanArrayList.get(holder.getAdapterPosition()).getJenisPerkawinan().equals("campuran_masuk")) {
                    perkawinanDetail = new Intent(mContext, PerkawinanCampuranMasukDetailActivity.class);
                } else if (perkawinanArrayList.get(holder.getAdapterPosition()).getJenisPerkawinan().equals("campuran_keluar")) {
                    perkawinanDetail = new Intent(mContext, PerkawinanCampuranKeluarDetailActivity.class);
                }
                perkawinanDetail.putExtra(PERKAWINAN_DETAIL_KEY, perkawinan.getId());
                mContext.startActivity(perkawinanDetail);
            }
        });
    }


    @Override
    public int getItemCount() {
        return perkawinanArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView perkawinanPurusaName, perkawinanPradanaName,
                perkawinanNo, perkawinanJenis, perkawinanTanggal, perkawinanStatus, purusaTitle, pradanaTitle;
        private final Button perkawinanDetailButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            perkawinanPurusaName = itemView.findViewById(R.id.perkawinan_purusa_name_text);
            perkawinanPradanaName = itemView.findViewById(R.id.perkawinan_pradana_name_text);
            perkawinanNo = itemView.findViewById(R.id.perkawinan_no_text);
            perkawinanJenis = itemView.findViewById(R.id.perkawinan_type_text);
            perkawinanTanggal = itemView.findViewById(R.id.perkawinan_tanggal_text);
            perkawinanStatus = itemView.findViewById(R.id.perkawinan_status_text);
            perkawinanDetailButton = itemView.findViewById(R.id.perkawinan_detail_button);
            purusaTitle = itemView.findViewById(R.id.purusa_title);
            pradanaTitle = itemView.findViewById(R.id.pradana_title);
        }
    }
}
