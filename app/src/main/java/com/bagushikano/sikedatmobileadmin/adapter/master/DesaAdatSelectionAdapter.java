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
import com.bagushikano.sikedatmobileadmin.model.master.DesaAdat;

import java.util.ArrayList;
import java.util.List;

public class DesaAdatSelectionAdapter extends ArrayAdapter<DesaAdat> {
    private List<DesaAdat> desaAdatList = new ArrayList<>();

    public DesaAdatSelectionAdapter(@NonNull Context context, @NonNull List<DesaAdat> desaAdatList) {
        super(context, 0, desaAdatList);
        this.desaAdatList = desaAdatList;
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
        kabupatenName.setText(desaAdatList.get(position).getDesadatNama());
        return convertView;
    }

    @Override
    public int getCount() {
        return desaAdatList.size();
    }

    @Nullable
    @Override
    public DesaAdat getItem(int position) {
        return desaAdatList.get(position);
    }
}
