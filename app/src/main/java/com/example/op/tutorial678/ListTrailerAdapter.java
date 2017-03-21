package com.example.op.tutorial678;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by OP on 3/14/2017.
 */

public class ListTrailerAdapter extends ArrayAdapter<Trailer> {
    Context context;
    public ListTrailerAdapter(@NonNull Context context) {
        super(context, 0);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view ==null) {
            LayoutInflater layoutInflater = LayoutInflater.from(this.context);
            view = layoutInflater.inflate(R.layout.trailer_row, null);
        }
        Trailer trailer = getItem(position);

        if (trailer != null)
        {
            ((TextView) view.findViewById(R.id.tv_name)).setText(trailer.getName());
            ((ImageView)view.findViewById(R.id.img_view)).setImageBitmap(trailer.getImage());

        }

        return view;
    }
}
