package com.bagushikano.sikedatmobileadmin.adapter.maperas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaKramaMipil;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;

import java.util.ArrayList;
import java.util.List;

public class MaperasOrtuSelectionAdapter extends ArrayAdapter<CacahKramaMipil> {
    private List<CacahKramaMipil> cacahKramaMipilArrayList = new ArrayList<>();

    public MaperasOrtuSelectionAdapter(@NonNull Context context, @NonNull List<CacahKramaMipil> cacahKramaMipilArrayList) {
        super(context, 0, cacahKramaMipilArrayList);
        this.cacahKramaMipilArrayList = cacahKramaMipilArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.dropdown_menu_item, parent, false
            );
        }
        TextView kabupatenName = convertView.findViewById(R.id.auto_complete_text);
        kabupatenName.setText(StringFormatter.formatNamaWithGelar(
                cacahKramaMipilArrayList.get(position).getPenduduk().getNama(),
                cacahKramaMipilArrayList.get(position).getPenduduk().getGelarDepan(),
                cacahKramaMipilArrayList.get(position).getPenduduk().getGelarBelakang()
                )
        );
        return convertView;
    }

    @Override
    public int getCount() {
        return cacahKramaMipilArrayList.size();
    }

    @Nullable
    @Override
    public CacahKramaMipil getItem(int position) {
        return cacahKramaMipilArrayList.get(position);
    }
}