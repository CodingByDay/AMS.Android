package com.example.uhf.fragment;

import android.graphics.Color;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uhf.R;
import com.example.uhf.adapter.ItemLocationAdapter;
import com.example.uhf.adapter.LocationAdapter;
import com.example.uhf.interfaces.RecyclerViewInterface;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.Model.Location;
import com.example.uhf.mvvm.ViewModel.ItemLocationViewModel;
import com.example.uhf.mvvm.ViewModel.LocationViewModel;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;


public class ListingLocationsFragment extends KeyDwonFragment implements RecyclerViewInterface {
private RecyclerView rwItems;
    public LocationAdapter adapter;
    private LocationViewModel locationViewModel;
    private List<Location> itemsClassLevel;
    
    public TextView first;
    private TextView second;
    private TextView third;
    public String currentSearchColumn = "";

    private Button btExit;
    private static ListingLocationsFragment instance;
    private int selected = -1;

    public static ListingLocationsFragment getInstance() {
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        
    }
    private void clearColors() {
        first.setBackgroundColor(Color.TRANSPARENT);
        second.setBackgroundColor(Color.TRANSPARENT);
        third.setBackgroundColor(Color.TRANSPARENT);

    }
    private void initData(View view) {


        first = view.findViewById(R.id.first);
        second = view.findViewById(R.id.second);
        third = view.findViewById(R.id.third);
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //adapter.sortASCandDESC(first.getText().toString());
                clearColors();
                currentSearchColumn = first.getText().toString();
                first.setBackgroundColor(Color.parseColor("#C7E3E1"));
            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //adapter.sortASCandDESC(second.getText().toString());
                clearColors();
                currentSearchColumn = second.getText().toString();
                second.setBackgroundColor(Color.parseColor("#C7E3E1"));
            }
        });
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //adapter.sortASCandDESC(third.getText().toString());
                clearColors();
                currentSearchColumn = third.getText().toString();
                third.setBackgroundColor(Color.parseColor("#C7E3E1"));
            }
        });
        
        rwItems = view.findViewById(R.id.rwItems);
        rwItems.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rwItems.setHasFixedSize(true);
        adapter = new LocationAdapter(this);
        rwItems.setAdapter(adapter);
            locationViewModel = ViewModelProviders.of((FragmentActivity) view.getContext()).get(LocationViewModel.class);
            locationViewModel.getAllItems().observe(this, new Observer<List<Location>>() {
                @Override
                public void onChanged(List<Location> locations) {
                    itemsClassLevel = locations;
                    adapter.setItems(locations);
                }
            });
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listing_locations, container, false);
        initData(view);
        return view;
    }



    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onLongItemClick(int position) {

    }
}