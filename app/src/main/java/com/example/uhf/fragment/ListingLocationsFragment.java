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
import android.widget.Toast;

import com.example.uhf.R;
import com.example.uhf.adapter.ItemLocationAdapter;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.ViewModel.ItemLocationViewModel;

import java.util.List;


public class ListingLocationsFragment extends Fragment {
private RecyclerView rwItems;
    private ItemLocationAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    private void initData(View view) {
        rwItems = view.findViewById(R.id.rwItems);


//    rwItems.setLayoutManager(new LinearLayoutManager(view.getContext()));
//    rwItems.setHasFixedSize(true);
//    adapter = new ItemLocationAdapter(this, callerID);
//    rwItems.setAdapter(adapter);
//    itemLocationViewModel = ViewModelProviders.of((FragmentActivity) view.getContext()).get(ItemLocationViewModel.class);
//    itemLocationViewModel.getItemsThatAreRegistered().observe(this, new Observer<List<ItemLocation>>() {
//        @Override
//        public void onChanged(List<ItemLocation> items) {
//            itemsClassLevel = items;
//            adapter.setItems(items);
//        }
//    });
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listing_locations, container, false);
        initData(view);
        return view;
    }
}