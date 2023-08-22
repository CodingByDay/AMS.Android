package com.example.uhf.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uhf.R;
import com.example.uhf.interfaces.RecyclerViewInterface;
import com.example.uhf.mvvm.Model.Asset;
import com.example.uhf.mvvm.Model.Item;

import java.util.ArrayList;
import java.util.List;

public class AssetAdapter  extends RecyclerView.Adapter<AssetAdapter.ItemHolder> {
    private List<Asset> items = new ArrayList<Asset>();
    private final RecyclerViewInterface recyclerViewInterface;
    private View previous = null;

    public AssetAdapter(RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
    }
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory, parent, false);
        return new AssetAdapter.ItemHolder(itemView, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Asset current = items.get(position);
        holder.tbItem.setText(current.getItem());
        holder.tbName.setText(current.getName());
        holder.tbLocation.setText(current.getLocation());
        holder.tbEpc.setText(current.getEpc().substring(current.getEpc().length() - 5));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Asset> items) {
        this.items = items;
        notifyDataSetChanged();
    }
    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView tbItem;
        private TextView tbName;
        private TextView tbLocation;
        private TextView tbCode;
        private TextView tbQty;
        
        private TextView tbUser;

        private TextView tbPassword;
        private TextView tbEpc;
        private LinearLayout linearLayout;

        public ItemHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            tbItem = itemView.findViewById(R.id.tbItem);
            tbName = itemView.findViewById(R.id.tbName);
            tbUser = itemView.findViewById(R.id.tbUser);
            tbQty = itemView.findViewById(R.id.tbQty);
            tbLocation = itemView.findViewById(R.id.tbLocation);
            tbEpc = itemView.findViewById(R.id.tbEpc);
            tbPassword = itemView.findViewById(R.id.tbPassword);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (AssetAdapter.this.recyclerViewInterface !=null) {
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION) {
                            AssetAdapter.this.recyclerViewInterface.onItemClick(position);


                            if(previous!=null) {

                                previous.setBackgroundColor(Color.TRANSPARENT);
                                view.setBackgroundColor(Color.parseColor("#969595"));
                            } else {

                                view.setBackgroundColor(Color.parseColor("#969595"));
                            }
                            previous = view;

                        }
                    }
                }
            });
        }
    }
}
