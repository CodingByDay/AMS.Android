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
import com.example.uhf.mvvm.Model.ItemTemporary;

import java.util.ArrayList;
import java.util.List;

public class ItemTemporaryAdapter extends RecyclerView.Adapter<ItemTemporaryAdapter.ItemHolder> {
    private List<ItemTemporary> items = new ArrayList<ItemTemporary>();
    private final RecyclerViewInterface recyclerViewInterface;



    public ItemTemporaryAdapter(RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
    }

    // Here is the sorting method
    public void sortBasedOnLocation(List<ItemTemporary> items, String location) {


        List<ItemTemporary> correctItems = new ArrayList<ItemTemporary>();
        List<ItemTemporary> wrongItems = new ArrayList<ItemTemporary>();
        List<ItemTemporary> emptyItems = new ArrayList<ItemTemporary>();

        for (ItemTemporary item :items) {
            if (item.getLocation().equals(location)) {
                correctItems.add(item);
            } else if (!item.getLocation().equals(location) && !item.getLocation().equals("")) {
                wrongItems.add(item);
            } else if (item.getLocation().equals("")) {
                emptyItems.add(item);
            }
        }
        ArrayList<ItemTemporary> sorted = new ArrayList<ItemTemporary>();
        sorted.addAll(correctItems);
        sorted.addAll(wrongItems);
        sorted.addAll(emptyItems);
        this.items = sorted;
        notifyDataSetChanged();

    }
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory, parent, false);

        return new ItemHolder(itemView, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        ItemTemporary current = items.get(position);
        holder.tbItem.setText(current.getEcd().substring(current.getEcd().length() - 5));
        holder.tbName.setText(current.getName());
        holder.tbLocation.setText(current.getLocation());
        holder.tbEpc.setText(current.getEcd());

    }

    public List<ItemTemporary> getItems() {
        return items;
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<ItemTemporary> items) {
        this.items = items;
        notifyDataSetChanged();
    }
    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView tbItem;
        private TextView tbName;
        private TextView tbLocation;
        private TextView tbQty;

        private TextView tbEpc;

        private LinearLayout linearLayout;

        public ItemHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            tbItem = (TextView) itemView.findViewById(R.id.tbItem);
            tbName = (TextView) itemView.findViewById(R.id.tbName);
            tbLocation = (TextView) itemView.findViewById(R.id.tbLocation);
            tbEpc = (TextView) itemView.findViewById(R.id.tbEpc);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ItemTemporaryAdapter.this.recyclerViewInterface !=null) {
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION) {
                            ItemTemporaryAdapter.this.recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
