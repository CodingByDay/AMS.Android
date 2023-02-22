package com.example.uhf.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uhf.R;
import com.example.uhf.interfaces.RecyclerViewInterface;
import com.example.uhf.mvvm.Model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import one.util.streamex.StreamEx;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {
private List<Item> items = new ArrayList<Item>();
private final RecyclerViewInterface recyclerViewInterface;

    public ItemAdapter(RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
    }

    // Here is the sorting method
    public void sortBasedOnLocation(List<Item> items, String location) {


            List<Item> correctItems = new ArrayList<Item>();
            List<Item> wrongItems = new ArrayList<Item>();
            List<Item> emptyItems = new ArrayList<Item>();

        for (Item item :items) {
            if (item.getLocation().equals(location)) {
                correctItems.add(item);
            } else if (!item.getLocation().equals(location) && !item.getLocation().equals("")) {
                wrongItems.add(item);
            } else if (item.getLocation().equals("")) {
                emptyItems.add(item);
            }
          }
            ArrayList<Item> sorted = new ArrayList<Item>();
            sorted.addAll(correctItems);
            sorted.addAll(wrongItems);
            sorted.addAll(emptyItems);
            this.items = sorted;
            notifyDataSetChanged();

    }
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        return new ItemHolder(itemView, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Item current = items.get(position);
        holder.tbItem.setText(current.getEcd());
        holder.tbName.setText(current.getName());
        holder.tbLocation.setText(current.getLocation());
        String qty = String.valueOf(current.getQty());
        holder.tbQty.setText(qty);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }
    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView tbItem;
        private TextView tbName;
        private TextView tbLocation;
        private TextView tbQty;

        private LinearLayout linearLayout;

        public ItemHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            tbItem = (TextView) itemView.findViewById(R.id.tbItem);
            tbName = (TextView) itemView.findViewById(R.id.tbName);
            tbLocation = (TextView) itemView.findViewById(R.id.tbLocation);
            tbQty = (TextView) itemView.findViewById(R.id.tbQty);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ItemAdapter.this.recyclerViewInterface !=null) {
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION) {
                            ItemAdapter.this.recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
