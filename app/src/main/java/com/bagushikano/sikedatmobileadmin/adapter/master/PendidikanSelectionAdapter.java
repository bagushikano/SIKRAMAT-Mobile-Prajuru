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
import com.bagushikano.sikedatmobileadmin.model.master.DesaAdat;
import com.bagushikano.sikedatmobileadmin.model.master.Pendidikan;

import java.util.ArrayList;
import java.util.List;

public class PendidikanSelectionAdapter extends ArrayAdapter<Pendidikan> {
    private List<Pendidikan> pendidikanList = new ArrayList<>();

    public PendidikanSelectionAdapter(@NonNull Context context, @NonNull List<Pendidikan> pendidikanList) {
        super(context, 0, pendidikanList);
        this.pendidikanList = pendidikanList;
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
        kabupatenName.setText(pendidikanList.get(position).getJenjangPendidikan());
        return convertView;
    }

    @Override
    public int getCount() {
        return pendidikanList.size();
    }

    @Nullable
    @Override
    public Pendidikan getItem(int position) {
        return pendidikanList.get(position);
    }
}

