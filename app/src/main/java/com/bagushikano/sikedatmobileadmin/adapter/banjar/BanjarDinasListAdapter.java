package com.bagushikano.sikedatmobileadmin.adapter.banjar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarDinas;

import java.util.ArrayList;

public class BanjarDinasListAdapter extends RecyclerView.Adapter<BanjarDinasListAdapter.ViewHolder> {
    private Context mContext;
    private final ArrayList<BanjarDinas> banjarDinasArrayList;


    public BanjarDinasListAdapter(Context context, ArrayList<BanjarDinas> banjarDinasArrayList) {
        mContext = context;
        this.banjarDinasArrayList = banjarDinasArrayList;
    }

    @NonNull
    @Override
    public BanjarDinasListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.banjar_dinas_card_layout, parent, false);
        return new BanjarDinasListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BanjarDinasListAdapter.ViewHolder holder, int position) {
       holder.banjarDinasNo.setText(banjarDinasArrayList.get(holder.getAdapterPosition()).getKodeBanjarDinas());
       holder.banjarDinasName.setText(banjarDinasArrayList.get(holder.getAdapterPosition()).getNamaBanjarDinas());
    }


    @Override
    public int getItemCount() {
        return banjarDinasArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView banjarDinasName, banjarDinasNo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            banjarDinasName = itemView.findViewById(R.id.banjar_dinas_nama_text);
            banjarDinasNo = itemView.findViewById(R.id.banjar_dinas_no_text);
        }
    }
}
