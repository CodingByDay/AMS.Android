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

    public ItemLocationAdapter(RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        return new ItemHolder(itemView, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        ItemLocation current = items.get(position);
        holder.tbItem.setText(current.getCode());
       // holder.tbName.setText(current.getName());
        holder.tbLocation.setText(current.getLocation());
      //  String qty = String.valueOf(current.getQty());
        holder.tbECD.setText(current.getEcd());
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
        private TextView tbECD;

        private LinearLayout linearLayout;

        public ItemHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            tbItem = (TextView) itemView.findViewById(R.id.tbItem);
            tbName = (TextView) itemView.findViewById(R.id.tbName);
            tbLocation = (TextView) itemView.findViewById(R.id.tbLocation);
          //  tbECD = (TextView) itemView.findViewById(R.id.tbECD);
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
