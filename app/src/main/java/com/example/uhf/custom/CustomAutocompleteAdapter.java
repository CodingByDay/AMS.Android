package com.example.uhf.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.uhf.R;

import java.util.List;

public class CustomAutocompleteAdapter extends ArrayAdapter<String> {

    private List<String> items;
    private LayoutInflater inflater;

    public CustomAutocompleteAdapter(Context context, List<String> items) {
        super(context, R.layout.custom_dropdown_item, items); // Replace with your custom layout
        this.items = items;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_dropdown_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.custom_dropdown_item_text); // Replace with your TextView ID
        textView.setText(items.get(position));

        return convertView;
    }
}
