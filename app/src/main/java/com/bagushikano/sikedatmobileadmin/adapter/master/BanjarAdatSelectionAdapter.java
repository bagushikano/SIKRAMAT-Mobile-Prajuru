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
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarAdat;

import java.util.ArrayList;
import java.util.List;

public class BanjarAdatSelectionAdapter extends ArrayAdapter<BanjarAdat> {
    private List<BanjarAdat> banjarAdatList = new ArrayList<>();

    public BanjarAdatSelectionAdapter(@NonNull Context context, @NonNull List<BanjarAdat> banjarAdatList) {
        super(context, 0, banjarAdatList);
        this.banjarAdatList = banjarAdatList;
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
        kabupatenName.setText(banjarAdatList.get(position).getNamaBanjarAdat());
        return convertView;
    }

    @Override
    public int getCount() {
        return banjarAdatList.size();
    }

    @Nullable
    @Override
    public BanjarAdat getItem(int position) {
        return banjarAdatList.get(position);
    }
}
