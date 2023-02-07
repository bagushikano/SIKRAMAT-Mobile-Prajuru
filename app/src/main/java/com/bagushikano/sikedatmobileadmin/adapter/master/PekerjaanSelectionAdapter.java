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
import com.bagushikano.sikedatmobileadmin.model.master.Pekerjaan;
import com.bagushikano.sikedatmobileadmin.model.master.Pendidikan;

import java.util.ArrayList;
import java.util.List;

public class PekerjaanSelectionAdapter extends ArrayAdapter<Pekerjaan> {
    private List<Pekerjaan> pekerjaanList = new ArrayList<>();

    public PekerjaanSelectionAdapter(@NonNull Context context, @NonNull List<Pekerjaan> pekerjaanList) {
        super(context, 0, pekerjaanList);
        this.pekerjaanList = pekerjaanList;
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
        kabupatenName.setText(pekerjaanList.get(position).getProfesi());
        return convertView;
    }

    @Override
    public int getCount() {
        return pekerjaanList.size();
    }

    @Nullable
    @Override
    public Pekerjaan getItem(int position) {
        return pekerjaanList.get(position);
    }
}
