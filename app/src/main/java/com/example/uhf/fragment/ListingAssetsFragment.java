package com.example.uhf.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

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

import com.example.uhf.R;
import com.example.uhf.adapter.ItemLocationAdapter;
import com.example.uhf.interfaces.RecyclerViewInterface;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.ViewModel.ItemLocationViewModel;

import java.util.List;
import java.util.Objects;

public class ListingAssetsFragment extends KeyDwonFragment implements RecyclerViewInterface {



    private RecyclerView rwItems;
    public ItemLocationAdapter adapter;
    private ItemLocationViewModel locationViewModel;
    private List<ItemLocation> itemsClassLevel;

    public TextView first;
    private TextView second;
    private TextView third;
    private TextView forth;
    public String currentSearchColumn = "";

    private Button btExit;
    private static ListingAssetsFragment instance;
    private int selected = -1;

    public static ListingAssetsFragment getInstance() {
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
        forth.setBackgroundColor(Color.TRANSPARENT);

    }
    private void initData(View view) {


        first = view.findViewById(R.id.first);
        second = view.findViewById(R.id.second);
        third = view.findViewById(R.id.third);
        forth = view.findViewById(R.id.forth);

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
                // adapter.sortASCandDESC(second.getText().toString());
                clearColors();
                currentSearchColumn = second.getText().toString();
                second.setBackgroundColor(Color.parseColor("#C7E3E1"));
            }
        });
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // adapter.sortASCandDESC(third.getText().toString());
                clearColors();
                currentSearchColumn = third.getText().toString();
                third.setBackgroundColor(Color.parseColor("#C7E3E1"));
            }
        });
        forth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //adapter.sortASCandDESC(forth.getText().toString());
                clearColors();
                currentSearchColumn = forth.getText().toString();
                forth.setBackgroundColor(Color.parseColor("#C7E3E1"));
            }
        });

        rwItems = view.findViewById(R.id.rwItems);
        rwItems.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rwItems.setHasFixedSize(true);

        adapter = new ItemLocationAdapter(this, "ListingAssetsFragment");
        rwItems.setAdapter(adapter);
        locationViewModel = ViewModelProviders.of((FragmentActivity) view.getContext()).get(ItemLocationViewModel.class);
        locationViewModel.getAllItems().observe(this, new Observer<List<ItemLocation>>() {
            @Override
            public void onChanged(List<ItemLocation> locations) {
                itemsClassLevel = locations;
                adapter.setItems(locations);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listing_assets, container, false);
        initData(view);
        return view;
    }

    @Override
    public void onItemClick(int position) {

        ItemLocation item = adapter.items.get(position);
        String message = "Lokacija: " + item.getLocation() + "\n"
                + "Ime: " + item.getName() + "\n"
                + "Zadolženi: " + item.getCaretaker() + "\n"
                + "EPC: " + item.getEcd();
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Podatki");
        builder.setMessage(message);


        builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
        if(selected==-1) {
            Objects.requireNonNull(Objects.requireNonNull(rwItems.getLayoutManager()).findViewByPosition(position)).setBackgroundColor(Color.parseColor("#C7E3E1"));
        } else {
            Objects.requireNonNull(Objects.requireNonNull(rwItems.getLayoutManager()).findViewByPosition(selected)).setBackgroundColor(Color.TRANSPARENT);
            Objects.requireNonNull(Objects.requireNonNull(rwItems.getLayoutManager()).findViewByPosition(position)).setBackgroundColor(Color.parseColor("#C7E3E1"));
        }
        selected = position;
    }
}