package com.bagushikano.sikedatmobileadmin.adapter.maperas;

import android.app.Activity;
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
import com.bagushikano.sikedatmobileadmin.adapter.perkawinan.PerkawinanListMempelaiAdapter;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MaperasAnakListAdapter extends RecyclerView.Adapter<MaperasAnakListAdapter.ViewHolder> {
    private Context mContext;
    private final ArrayList<CacahKramaMipil> cacahKramaMipilArrayList;
    private int position;
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";
    private final String KRAMA_SELECT_KEY = "KRAMA_SELECT_KEY";

    public MaperasAnakListAdapter(Context context, ArrayList<CacahKramaMipil> cacahKramaMipilArrayList) {
        mContext = context;
        this.cacahKramaMipilArrayList = cacahKramaMipilArrayList;
    }

    @NonNull
    @Override
    public MaperasAnakListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.perkawinan_mempelai_card_layout, parent, false);
        return new MaperasAnakListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaperasAnakListAdapter.ViewHolder holder, int position) {

        holder.kramaMipilSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CacahKramaMipil cacahKramaMipil = cacahKramaMipilArrayList.get(holder.getAdapterPosition());
                Intent selected = new Intent();
                Gson gson = new Gson();
                String kramaJson = gson.toJson(cacahKramaMipil);
                selected.putExtra(KRAMA_SELECT_KEY, kramaJson);
                ((Activity) mContext).setResult(100, selected);
                ((Activity) mContext).finish();
            }
        });
        holder.kramaMipilName.setText(StringFormatter.formatNamaWithGelar(
                cacahKramaMipilArrayList.get(holder.getAdapterPosition()).getPenduduk().getNama(),
                cacahKramaMipilArrayList.get(holder.getAdapterPosition()).getPenduduk().getGelarDepan(),
                cacahKramaMipilArrayList.get(holder.getAdapterPosition()).getPenduduk().getGelarBelakang()
        ));
        holder.kramaMipilNik.setText(cacahKramaMipilArrayList.get(holder.getAdapterPosition()).getPenduduk().getNik());
        holder.noKramaMipil.setText(cacahKramaMipilArrayList.get(holder.getAdapterPosition()).getNomorCacahKramaMipil());
        holder.kramaMipilDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CacahKramaMipil cacahKramaMipil = cacahKramaMipilArrayList.get(holder.getAdapterPosition());
                Intent kramaDetail = new Intent(mContext, CacahKramaMipilDetailActivity.class);
                Gson gson = new Gson();
                String kramaJson = gson.toJson(cacahKramaMipil);
                kramaDetail.putExtra(KRAMA_DETAIL_KEY, cacahKramaMipil.getId());
                mContext.startActivity(kramaDetail);
            }
        });
        if (cacahKramaMipilArrayList.get(holder.getAdapterPosition()).getPenduduk().getFoto() != null) {
            holder.kramaImageLoadingContainer.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(mContext.getResources().getString(R.string.image_endpoint) +
                            cacahKramaMipilArrayList.get(holder.getAdapterPosition()).getPenduduk().getFoto())
                    .fit().centerCrop()
                    .into(holder.kramaImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            holder.kramaImageLoadingContainer.setVisibility(View.GONE);
                            holder.kramaImage.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.kramaImageLoadingContainer.setVisibility(View.GONE);
                            Picasso.get().load(R.drawable.paceholder_krama_pict).into(holder.kramaImage);
                        }
                    });
        }
        else {
            Picasso.get().load(R.drawable.paceholder_krama_pict).into(holder.kramaImage);
            holder.kramaImage.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return cacahKramaMipilArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView kramaMipilName, kramaMipilNik, noKramaMipil;
        private final Button kramaMipilDetailButton, kramaMipilSelectButton;
        private final LinearLayout kramaImageLoadingContainer;
        private final ImageView kramaImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kramaMipilName = itemView.findViewById(R.id.krama_nama_text);
            kramaMipilNik = itemView.findViewById(R.id.krama_nik_text);
            noKramaMipil = itemView.findViewById(R.id.krama_no_mipil_text);
            kramaMipilDetailButton = itemView.findViewById(R.id.krama_detail_button);
            kramaImageLoadingContainer = itemView.findViewById(R.id.krama_image_loading_container);
            kramaMipilSelectButton= itemView.findViewById(R.id.krama_select_button);
            kramaImage = itemView.findViewById(R.id.krama_image);
        }
    }
}
