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
import com.bagushikano.sikedatmobileadmin.model.master.DesaDinas;
import com.bagushikano.sikedatmobileadmin.model.master.Provinsi;

import java.util.ArrayList;
import java.util.List;

public class DesaDinasSelectionAdapter extends ArrayAdapter<DesaDinas> {
    private List<DesaDinas> desaDinasList = new ArrayList<>();

    public DesaDinasSelectionAdapter(@NonNull Context context, @NonNull List<DesaDinas> desaDinasList) {
        super(context, 0, desaDinasList);
        this.desaDinasList = desaDinasList;
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
        kabupatenName.setText(desaDinasList.get(position).getName());
        return convertView;
    }

    @Override
    public int getCount() {
        return desaDinasList.size();
    }

    @Nullable
    @Override
    public DesaDinas getItem(int position) {
        return desaDinasList.get(position);
    }
}
