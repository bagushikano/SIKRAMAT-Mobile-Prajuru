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
import com.bagushikano.sikedatmobileadmin.model.master.Provinsi;

import java.util.ArrayList;
import java.util.List;

public class ProvinsiSelectionAdapter extends ArrayAdapter<Provinsi> {
    private List<Provinsi> provinsiList = new ArrayList<>();

    public ProvinsiSelectionAdapter(@NonNull Context context, @NonNull List<Provinsi> provinsiList) {
        super(context, 0, provinsiList);
        this.provinsiList = provinsiList;
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
        kabupatenName.setText(provinsiList.get(position).getName());
        return convertView;
    }

    @Override
    public int getCount() {
        return provinsiList.size();
    }

    @Nullable
    @Override
    public Provinsi getItem(int position) {
        return provinsiList.get(position);
    }
}
