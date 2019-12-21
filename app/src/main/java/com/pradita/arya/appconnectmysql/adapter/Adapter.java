package com.pradita.arya.appconnectmysql.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pradita.arya.appconnectmysql.R;
import com.pradita.arya.appconnectmysql.data.Data;

import java.util.List;

public class Adapter extends BaseAdapter {
    private Activity activity;
    private List<Data> items;
    private LayoutInflater inflater;

    public Adapter(Activity activity, List<Data> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = inflater.inflate(R.layout.list_row, null);
        TextView nim = view.findViewById(R.id.nim);
        TextView nama = view.findViewById(R.id.nama);
        TextView alamat = view.findViewById(R.id.alamat);
        Data data = items.get(position);
        nim.setText(data.getNim());
        nama.setText(data.getNama());
        alamat.setText(data.getAlamat());
        return view;
    }
}
