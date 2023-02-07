package com.bagushikano.sikedatmobileadmin.adapter.perceraian;

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
import com.bagushikano.sikedatmobileadmin.activity.perceraian.PerceraianDetailActivity;
import com.bagushikano.sikedatmobileadmin.model.perceraian.Perceraian;
import com.bagushikano.sikedatmobileadmin.util.ChangeDateTimeFormat;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;

import java.util.ArrayList;

public class PerceraianListAdapter  extends RecyclerView.Adapter<PerceraianListAdapter.ViewHolder> {
    private Context mContext;
    private final ArrayList<Perceraian> perceraianArrayList;
    private int position;
    private final String PERCERAIAN_DETAIL_KEY = "PERCERAIAN_DETAIL_KEY";


    public PerceraianListAdapter(Context context, ArrayList<Perceraian> perceraianArrayList) {
        mContext = context;
        this.perceraianArrayList = perceraianArrayList;
    }

    @NonNull
    @Override
    public PerceraianListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.perceraian_list_card_layout, parent, false);
        return new PerceraianListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PerceraianListAdapter.ViewHolder holder, int position) {

        holder.perceraianPurusaName.setText(
                StringFormatter.formatNamaWithGelar(
                        perceraianArrayList.get(holder.getAdapterPosition()).getPurusa().getPenduduk().getNama(),
                        perceraianArrayList.get(holder.getAdapterPosition()).getPurusa().getPenduduk().getGelarDepan(),
                        perceraianArrayList.get(holder.getAdapterPosition()).getPurusa().getPenduduk().getGelarBelakang()
                )
        );
        holder.perceraianPradanaName.setText(
                StringFormatter.formatNamaWithGelar(
                        perceraianArrayList.get(holder.getAdapterPosition()).getPradana().getPenduduk().getNama(),
                        perceraianArrayList.get(holder.getAdapterPosition()).getPradana().getPenduduk().getGelarDepan(),
                        perceraianArrayList.get(holder.getAdapterPosition()).getPradana().getPenduduk().getGelarBelakang()
                )
        );

        if (perceraianArrayList.get(holder.getAdapterPosition()).getNomorPerceraian() != null) {
            holder.perceraianNo.setText(perceraianArrayList.get(holder.getAdapterPosition()).getNomorPerceraian());
        }
        else {
            holder.perceraianNo.setText("-");
        }

        holder.perceraianTanggal.setText(ChangeDateTimeFormat.changeDateFormat(
                perceraianArrayList.get(holder.getAdapterPosition()).getTanggalPerceraian()));

        if (perceraianArrayList.get(holder.getAdapterPosition()).getStatusPerceraian() == 1) {
            holder.perceraianStatus.setText("Menunggu Konfirmasi");
            holder.perceraianStatus.setTextColor(mContext.getColor(R.color.yellow));
        } else if (perceraianArrayList.get(holder.getAdapterPosition()).getStatusPerceraian() == 2) {
            holder.perceraianStatus.setText("Tidak Terkonfirmasi");
            holder.perceraianStatus.setTextColor(mContext.getColor(R.color.red));
        } else if (perceraianArrayList.get(holder.getAdapterPosition()).getStatusPerceraian() == 0) {
            holder.perceraianStatus.setText("Draft");
            holder.perceraianStatus.setTextColor(mContext.getColor(R.color.yellow));
        } else if (perceraianArrayList.get(holder.getAdapterPosition()).getStatusPerceraian() == 3) {
            holder.perceraianStatus.setText("Sah");
            holder.perceraianStatus.setTextColor(mContext.getColor(R.color.green));
        }

        holder.perceraianDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Perceraian perceraian = perceraianArrayList.get(holder.getAdapterPosition());
                Intent perceraianDetail = null;
                perceraianDetail = new Intent(mContext, PerceraianDetailActivity.class);
                perceraianDetail.putExtra(PERCERAIAN_DETAIL_KEY, perceraian.getId());
                mContext.startActivity(perceraianDetail);
            }
        });
    }


    @Override
    public int getItemCount() {
        return perceraianArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView perceraianPurusaName, perceraianPradanaName,
                perceraianNo, perceraianTanggal, perceraianStatus;
        private final Button perceraianDetailButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            perceraianPurusaName = itemView.findViewById(R.id.perceraian_purusa_name_text);
            perceraianPradanaName = itemView.findViewById(R.id.perceraian_pradana_name_text);
            perceraianNo = itemView.findViewById(R.id.perceraian_no_text);
            perceraianTanggal = itemView.findViewById(R.id.perceraian_tanggal_text);
            perceraianStatus = itemView.findViewById(R.id.perceraian_status_text);
            perceraianDetailButton = itemView.findViewById(R.id.perceraian_detail_button);
        }
    }
}
