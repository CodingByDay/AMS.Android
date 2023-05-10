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
import com.example.uhf.mvvm.Model.ItemTemporary;
import com.example.uhf.mvvm.Model.Location;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ItemHolder> {
    private List<Location> items = new ArrayList<Location>();
    private final RecyclerViewInterface recyclerViewInterface;



    public LocationAdapter(RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item, parent, false);
        return new ItemHolder(itemView, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Location current = items.get(position);
        holder.tbName.setText(current.getName());
        holder.tbLocation.setText(current.getLocation());
        holder.tbDept.setText(current.getDept());
    }

    private List<Location> backupForSearching = new ArrayList<>();
    private boolean tbLocationASC;
    private boolean tbNameASC;
    private boolean tbCodeASC;

    public void searchByField(String field, String searchBy) {
        List<Location> sorted = new ArrayList<>();
        if(this.backupForSearching!=null) {
            for (Location item : this.backupForSearching) {
                switch (field) {
                    case "Lokacija":
                        if (item.getLocation().toLowerCase().contains(searchBy.toLowerCase())) {
                            sorted.add(item);
                        }
                        break;
                    case "Naziv":
                        if (item.getName().toLowerCase().contains(searchBy.toLowerCase())) {
                            sorted.add(item);
                        }
                        break;
                    case "Oddelek":
                        if (item.getCode().toLowerCase().contains(searchBy.toLowerCase())) {
                            sorted.add(item);
                        }

                }
                this.items = sorted;
                notifyDataSetChanged();
            }
        }
    }

    public void sortASCandDESC(String field) {
        switch (field) {
            case "Lokacija":
                if(tbLocationASC) {
                    items.sort(Comparator.comparing(Location::getLocation).reversed());
                    tbLocationASC = false;
                } else {
                    items.sort(Comparator.comparing(Location::getLocation));
                    tbLocationASC = true;
                }
                break;
            case "Naziv":
                if(tbNameASC) {
                    items.sort(Comparator.comparing(Location::getName));
                    tbNameASC = false;
                } else {
                    items.sort(Comparator.comparing(Location::getName).reversed());
                    tbNameASC = true;
                }
                break;
            case "Oddelek":
                if(tbCodeASC) {
                    items.sort(Comparator.comparing(Location::getCode));
                    tbCodeASC = false;
                } else {
                    items.sort(Comparator.comparing(Location::getCode).reversed());
                    tbLocationASC = true;
                }
                break;
        }
        notifyDataSetChanged();
    }


    public List<Location> getItems() {
        return items;
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Location> items) {
        this.items = items;
        this.backupForSearching = items;
        notifyDataSetChanged();
    }
    class ItemHolder extends RecyclerView.ViewHolder {


        private TextView tbLocation;
        private TextView tbName;

        private TextView tbDept;

        private LinearLayout linearLayout;

        public ItemHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            tbLocation = (TextView) itemView.findViewById(R.id.tbLocation);
            tbName = (TextView) itemView.findViewById(R.id.tbName);
            tbDept = (TextView) itemView.findViewById(R.id.tbDept);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (LocationAdapter.this.recyclerViewInterface !=null) {
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION) {
                            LocationAdapter.this.recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
