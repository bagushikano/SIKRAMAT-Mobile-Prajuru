package com.bagushikano.sikedatmobileadmin.adapter.master;

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
import com.bagushikano.sikedatmobileadmin.model.master.Kecamatan;

import java.util.ArrayList;
import java.util.List;

public class KecamatanSelectionAdapter extends ArrayAdapter<Kecamatan> {
    private List<Kecamatan> kecamatanList = new ArrayList<>();

    public KecamatanSelectionAdapter(@NonNull Context context, @NonNull List<Kecamatan> kecamatanList) {
        super(context, 0, kecamatanList);
        this.kecamatanList = kecamatanList;
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
        kabupatenName.setText(kecamatanList.get(position).getName());
        return convertView;
    }

    @Override
    public int getCount() {
        return kecamatanList.size();
    }

    @Nullable
    @Override
    public Kecamatan getItem(int position) {
        return kecamatanList.get(position);
    }
}
