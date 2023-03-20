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
import com.example.uhf.mvvm.Model.ItemLocation;

import java.util.ArrayList;
import java.util.List;

public class ItemLocationAdapter extends RecyclerView.Adapter<ItemLocationAdapter.ItemHolder> {
private List<ItemLocation> items = new ArrayList<ItemLocation>();
private final RecyclerViewInterface recyclerViewInterface;
    private static String calllerID;
private final String caller;


    public ItemLocationAdapter(RecyclerViewInterface recyclerViewInterface, String callerID) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.caller = callerID;

    }

    // Here is the sorting method
    public void sortBasedOnLocation(List<ItemLocation> items, String location) {


            List<ItemLocation> correctItems = new ArrayList<ItemLocation>();
            List<ItemLocation> wrongItems = new ArrayList<ItemLocation>();
            List<ItemLocation> emptyItems = new ArrayList<ItemLocation>();

        for (ItemLocation item :items) {
            if (item.getLocation().equals(location)) {
                correctItems.add(item);
            } else if (!item.getLocation().equals(location) && !item.getLocation().equals("")) {
                wrongItems.add(item);
            } else if (item.getLocation().equals("")) {
                emptyItems.add(item);
            }
          }
            ArrayList<ItemLocation> sorted = new ArrayList<ItemLocation>();
            sorted.addAll(correctItems);
            sorted.addAll(wrongItems);
            sorted.addAll(emptyItems);
            this.items = sorted;
            notifyDataSetChanged();

    }
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        switch (this.caller) {
            case "InventoryActivity": {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
                return new ItemHolder(itemView, recyclerViewInterface);
            }
            case "ListingActivity": {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listing, parent, false);
                return new ItemHolder(itemView, recyclerViewInterface);
            }
            case "RegistrationActivity": {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory, parent, false);
                return new ItemHolder(itemView, recyclerViewInterface);
            }
        }
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemHolder(itemView, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        ItemLocation current = items.get(position);


        switch (this.caller) {
            case "InventoryActivity": {
                holder.tbItem.setText(current.getItem());
                holder.tbName.setText(current.getName());
                holder.tbCode.setText(current.getCode());
                holder.tbQty.setText("1");
                break;
            }
            case "ListingActivity": {
                holder.tbItem.setText(current.getItem());
                holder.tbName.setText(current.getName());
                holder.tbLocation.setText(current.getLocation());
                holder.tbEpc.setText(current.getEcd().substring(current.getEcd().length() - 5));
                break;
            }
            case "RegistrationActivity": {
                holder.tbItem.setText(current.getItem());
                holder.tbName.setText(current.getName());
                holder.tbCode.setText(current.getCode());
                holder.tbEpc.setText(current.getEcd().substring(current.getEcd().length() - 5));
                break;
            }
        }



    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<ItemLocation> items) {
        this.items = items;
        notifyDataSetChanged();
    }
    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView tbItem;
        private TextView tbName;
        private TextView tbLocation;
        private TextView tbCode;
        private TextView tbQty;

        private TextView tbEpc;

        private LinearLayout linearLayout;

        public ItemHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            // switch the context
            switch (caller) {
                case "InventoryActivity": {
                    tbItem = itemView.findViewById(R.id.tbItem);
                    tbName = itemView.findViewById(R.id.tbName);
                    tbCode = itemView.findViewById(R.id.tbCode);
                    tbQty = itemView.findViewById(R.id.tbQty);
                    break;
                }
                case "ListingActivity": {
                    tbItem = itemView.findViewById(R.id.tbItem);
                    tbName = itemView.findViewById(R.id.tbName);
                    tbLocation = itemView.findViewById(R.id.tbLocation);
                    tbEpc = itemView.findViewById(R.id.tbEpc);
                    break;
                }
                case "RegistrationActivity": {
                    boolean different = true;
                    tbItem = itemView.findViewById(R.id.tbItem);
                    tbName = itemView.findViewById(R.id.tbName);
                    tbCode = itemView.findViewById(R.id.tbCode);
                    tbQty =  itemView.findViewById(R.id.tbQty);
                    break;
                }
            }

            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ItemLocationAdapter.this.recyclerViewInterface !=null) {
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION) {
                            ItemLocationAdapter.this.recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
