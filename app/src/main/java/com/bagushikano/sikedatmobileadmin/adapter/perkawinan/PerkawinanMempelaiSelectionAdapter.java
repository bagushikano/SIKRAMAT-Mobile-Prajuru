package com.bagushikano.sikedatmobileadmin.adapter.perkawinan;

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
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;

import java.util.ArrayList;
import java.util.List;

public class PerkawinanMempelaiSelectionAdapter extends ArrayAdapter<CacahKramaMipil> {
    private List<CacahKramaMipil> cacahKramaMipilList = new ArrayList<>();

    public PerkawinanMempelaiSelectionAdapter(@NonNull Context context, @NonNull List<CacahKramaMipil> cacahKramaMipilList) {
        super(context, 0, cacahKramaMipilList);
        this.cacahKramaMipilList = cacahKramaMipilList;
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
                cacahKramaMipilList.get(position).getPenduduk().getNama(),
                cacahKramaMipilList.get(position).getPenduduk().getGelarDepan(),
                cacahKramaMipilList.get(position).getPenduduk().getGelarBelakang()
                )
        );
        return convertView;
    }

    @Override
    public int getCount() {
        return cacahKramaMipilList.size();
    }

    @Nullable
    @Override
    public CacahKramaMipil getItem(int position) {
        return cacahKramaMipilList.get(position);
    }
}
