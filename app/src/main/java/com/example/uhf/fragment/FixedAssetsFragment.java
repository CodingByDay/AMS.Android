package com.example.uhf.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uhf.R;
import com.example.uhf.adapter.ItemAdapter;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.ViewModel.ItemViewModel;

import java.util.List;

public class FixedAssetsFragment extends Fragment {
private ItemViewModel itemViewModel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fixed_assets, container, false);
        inits(view);
        return view;
    }

    private void inits(View view) {
        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.rwItems);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setHasFixedSize(true);
        final ItemAdapter adapter = new ItemAdapter();
        recycler.setAdapter(adapter);
        itemViewModel = ViewModelProviders.of((FragmentActivity) view.getContext()).get(ItemViewModel.class);
        itemViewModel.getAllItems().observe(this, new Observer<List<Item>>() {

            @Override
            public void onChanged(List<Item> items) {
                adapter.setItems(items);
            }
        });
    }
}