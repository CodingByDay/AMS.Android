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
import java.util.Objects;

public class CustomAutocompleteAdapter<T> extends ArrayAdapter<T> {
    private List<T> originalList;
    private List<T> filteredList;
    private final Object lock = new Object();
    private List<T> items;
    private LayoutInflater inflater;
    private AutoCompleteTextView autoCompleteTextView;

    public CustomAutocompleteAdapter(Context context, List<T> items, AutoCompleteTextView autoCompleteTextView) {
        super(context, R.layout.custom_dropdown_item, items);
        this.originalList = new ArrayList<>(items);
        this.filteredList = new ArrayList<>();
        this.items = items;
        this.inflater = LayoutInflater.from(context);
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
        if(getItem(position)!=null) {
            textView.setText(Objects.requireNonNull(getItem(position)).toString());
        } else {
            textView.setText("");
        }
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
                        for (T item : originalList) {
                            if (item.toString().toLowerCase().contains(filterPattern)) {
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
                addAll((List<T>) results.values);
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