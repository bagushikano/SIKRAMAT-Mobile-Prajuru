package com.bagushikano.sikedatmobileadmin.adapter.banjar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarAdat;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarDinas;

import java.util.ArrayList;

public class BanjarAdatListAdapter extends RecyclerView.Adapter<BanjarAdatListAdapter.ViewHolder> {
    private Context mContext;
    private final ArrayList<BanjarAdat> banjarAdatArrayList;


    public BanjarAdatListAdapter(Context context, ArrayList<BanjarAdat> banjarAdatArrayList) {
        mContext = context;
        this.banjarAdatArrayList = banjarAdatArrayList;
    }

    @NonNull
    @Override
    public BanjarAdatListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.banjar_dinas_card_layout, parent, false);
        return new BanjarAdatListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BanjarAdatListAdapter.ViewHolder holder, int position) {
        holder.banjarDinasNo.setText(banjarAdatArrayList.get(holder.getAdapterPosition()).getKodeBanjarAdat());
        holder.banjarDinasName.setText(banjarAdatArrayList.get(holder.getAdapterPosition()).getNamaBanjarAdat());
    }


    @Override
    public int getItemCount() {
        return banjarAdatArrayList.size();
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
