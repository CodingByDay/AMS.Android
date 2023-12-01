package com.example.uhf.custom;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.uhf.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAutocompleteAdapter extends ArrayAdapter<String> {
    private List<String> originalList;
    private List<String> filteredList;
    private final Object lock = new Object();
    private List<String> items;
    private LayoutInflater inflater;
    private AutoCompleteTextView autoCompleteTextView;


    public CustomAutocompleteAdapter(Context context, List<String> items, AutoCompleteTextView autoCompleteTextView) {
        super(context, R.layout.custom_dropdown_item, items); // Replace with your custom layout
        this.originalList = new ArrayList<>(items);
        this.filteredList = new ArrayList<>();
        this.items = items;
        this.inflater = LayoutInflater.from(context); // Initialize the inflater
        this.autoCompleteTextView = autoCompleteTextView;
        // Add TextWatcher to AutoCompleteTextView
        this.autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_dropdown_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.custom_dropdown_item_text); // Replace with your TextView ID
        textView.setText(getItem(position));

        return convertView;
    }



    @NonNull
    @Override
    public Filter getFilter() {
        return new CustomFilter();
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null) {
                synchronized (lock) {
                    filteredList.clear();
                    if (constraint.length() == 0) {
                        filteredList.addAll(originalList);
                    } else {
                        String filterPattern = constraint.toString().toLowerCase().trim();
                        for (String item : originalList) {
                            if (item.toLowerCase().contains(filterPattern)) {
                                filteredList.add(item);
                            }
                        }
                    }
                    results.values = filteredList;
                    results.count = filteredList.size();
                }
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results != null && results.count > 0) {
                clear();
                //noinspection unchecked
                addAll((List<String>) results.values);
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }


            // Show dropdown after each filtering operation
            if (autoCompleteTextView != null) {
                autoCompleteTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        autoCompleteTextView.showDropDown();
                    }
                });
            }
        }
    }
}
