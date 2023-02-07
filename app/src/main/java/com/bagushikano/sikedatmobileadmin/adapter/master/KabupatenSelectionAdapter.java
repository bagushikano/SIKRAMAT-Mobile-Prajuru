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
import com.bagushikano.sikedatmobileadmin.model.master.Kabupaten;

import java.util.ArrayList;
import java.util.List;

public class KabupatenSelectionAdapter extends ArrayAdapter<Kabupaten> {
    private List<Kabupaten> kabupatenList = new ArrayList<>();

    public KabupatenSelectionAdapter(@NonNull Context context, @NonNull List<Kabupaten> kabupatenList) {
        super(context, 0, kabupatenList);
        this.kabupatenList = kabupatenList;
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
        kabupatenName.setText(kabupatenList.get(position).getName());
        return convertView;
    }

    @Override
    public int getCount() {
        return kabupatenList.size();
    }

    @Nullable
    @Override
    public Kabupaten getItem(int position) {
        return kabupatenList.get(position);
    }
}
