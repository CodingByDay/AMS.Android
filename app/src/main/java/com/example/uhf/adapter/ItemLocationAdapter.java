package com.example.uhf.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uhf.R;
import com.example.uhf.interfaces.RecyclerViewInterface;
import com.example.uhf.mvvm.Model.ItemLocation;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import io.sentry.Sentry;

public class ItemLocationAdapter extends RecyclerView.Adapter<ItemLocationAdapter.ItemHolder> {
public List<ItemLocation> items = new ArrayList<ItemLocation>();
private final RecyclerViewInterface recyclerViewInterface;
    private static String calllerID;
    private int selected = -1;
private final String caller;
    private List<ItemLocation> backupForSearching;
    View previous = null;

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
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory, parent, false);
                return new ItemHolder(itemView, recyclerViewInterface);
            }
            case "ListingActivity": {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listing, parent, false);
                return new ItemHolder(itemView, recyclerViewInterface);
            }
            case "RegistrationActivity": {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
                return new ItemHolder(itemView, recyclerViewInterface);
            }
            case "ListingAssetsFragment": {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listing_assets, parent, false);
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
                holder.tbLocation.setText(current.getLocation());
                holder.tbUser.setText(current.getCaretaker());
                holder.tbName.setText(current.getName());

                if(current.getEcd().length()>=6) {
                    holder.tbEpc.setText(current.getEcd().substring(current.getEcd().length() - 5));
                } else {
                    holder.tbEpc.setText(current.getEcd());
                }
                break;
            }
            case "ListingActivity": {
                holder.tbLocation.setText(current.getLocation());
                holder.tbUser.setText(current.getCaretaker());
                holder.tbName.setText(current.getName());
                if(current.getEcd().length()>=6) {
                    holder.tbEpc.setText(current.getEcd().substring(current.getEcd().length() - 6));
                } else {
                    holder.tbEpc.setText(current.getEcd());
                }
                break;
            }
            case "RegistrationActivity": {

                holder.tbLocation.setText(current.getLocation());
                holder.tbName.setText(current.getName());
                holder.tbUser.setText(current.getCaretaker());
                holder.tbPassword.setText(current.getItem());
                break;
            }
            case "ListingAssetsFragment": {
                holder.tbLocation.setText(current.getLocation());
                holder.tbUser.setText(current.getCaretaker());
                holder.tbName.setText(current.getName());
                if(current.getEcd()!=null) {
                if(current.getEcd().length()>=6) {
                    holder.tbEpc.setText(current.getEcd().substring(current.getEcd().length() - 5));
                } else {
                    holder.tbEpc.setText(current.getEcd());
                }
                } else {
                    holder.tbEpc.setText("");
                }
            }
        }



    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void setItems(List<ItemLocation> items) {
        this.items = items;
        this.backupForSearching = items;
        notifyDataSetChanged();
    }

    public void searchByField(String field, String searchBy) {
        try {
            List<ItemLocation> sorted = new ArrayList<>();
            if (this.backupForSearching != null) {
                for (ItemLocation item : this.backupForSearching) {
                    switch (field) {
                        case "Sredstvo":
                            if (item.getItem().toLowerCase().contains(searchBy.toLowerCase())) {
                                sorted.add(item);
                            }
                            break;
                        case "Naziv":
                            if (item.getName().toLowerCase().contains(searchBy.toLowerCase())) {
                                sorted.add(item);
                            }
                            break;
                        case "Ident":
                            if (item.getCode().toLowerCase().contains(searchBy.toLowerCase())) {
                                sorted.add(item);
                            }
                        case "Lokacija":
                            if (item.getLocation().toLowerCase().contains(searchBy.toLowerCase())) {
                                sorted.add(item);
                            }
                            break;
                        case "EPC":
                            if (item.getEcd().toLowerCase().contains(searchBy.toLowerCase())) {
                                sorted.add(item);
                            }
                        case "Zadolženi":
                            if (item.getCaretaker().toLowerCase().contains(searchBy.toLowerCase())) {
                                sorted.add(item);
                            }
                            break;
                        case "Šifra":
                            if (item.getItem().toLowerCase().contains(searchBy.toLowerCase())) {
                                sorted.add(item);
                            }
                            break;

                    }
                    this.items = sorted;
                    notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            Sentry.captureException(e);
        }
    }





    boolean itemASC = true;
    boolean nameASC = true;
    boolean identASC = true;
    boolean locationASC = true;
    boolean epcASC = true;

    public void sortASCandDESC(String field) {
        switch (field) {
            case "Sredstvo":
                if(itemASC) {
                    items.sort(Comparator.comparing(ItemLocation::getItem).reversed());
                    itemASC = false;
                } else {
                    items.sort(Comparator.comparing(ItemLocation::getItem));
                    itemASC = true;
                }
                break;
            case "Naziv":
                if(nameASC) {
                    items.sort(Comparator.comparing(ItemLocation::getName));
                    nameASC = false;
                } else {
                    items.sort(Comparator.comparing(ItemLocation::getName).reversed());
                    nameASC = true;
                }
                break;
            case "Ident":
                if(identASC) {
                    items.sort(Comparator.comparing(ItemLocation::getCode));
                    identASC = false;
                } else {
                    items.sort(Comparator.comparing(ItemLocation::getCode).reversed());
                    identASC = true;
                }
                break;
            case "Lokacija":
                if(locationASC) {
                    items.sort(Comparator.comparing(ItemLocation::getLocation));
                    locationASC = false;
                } else {
                    items.sort(Comparator.comparing(ItemLocation::getLocation).reversed());
                    locationASC = true;
                }
                break;
            case "EPC":
                if(epcASC) {
                    items.sort(Comparator.comparing(ItemLocation::getEcd));
                    itemASC = false;
                } else {
                    items.sort(Comparator.comparing(ItemLocation::getEcd).reversed());
                    itemASC = true;
                }
                break;
        }
        notifyDataSetChanged();
    }

    public void findIdent(String result) {
        List<ItemLocation> sorted = new ArrayList<>();

        for (ItemLocation item : this.backupForSearching) {

            if (item.getCode().toLowerCase().contains(result.toLowerCase())) {
                sorted.add(item);
            }
        }
            this.items = sorted;
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

            // switch the context
            switch (caller) {
                case "InventoryActivity": {
                    tbItem = itemView.findViewById(R.id.tbItem);
                    tbName = itemView.findViewById(R.id.tbName);
                    tbUser = itemView.findViewById(R.id.tbUser);
                    tbPassword =  itemView.findViewById(R.id.tbPassword);
                    tbLocation= itemView.findViewById(R.id.tbLocation);
                    tbEpc = itemView.findViewById(R.id.tbEpc);
                    break;
                }
                case "ListingActivity": {
                    tbItem = itemView.findViewById(R.id.tbItem);
                    tbName = itemView.findViewById(R.id.tbName);
                    tbLocation= itemView.findViewById(R.id.tbLocation);
                    tbEpc = itemView.findViewById(R.id.tbEpc);
                    break;
                }
                case "RegistrationActivity": {
                    boolean different = true;
                    tbItem = itemView.findViewById(R.id.tbItem);
                    tbName = itemView.findViewById(R.id.tbName);
                    tbUser = itemView.findViewById(R.id.tbUser);
                    tbPassword =  itemView.findViewById(R.id.tbPassword);
                    tbLocation= itemView.findViewById(R.id.tbLocation);

                    break;
                }
                case "ListingAssetsFragment": {
                    tbItem = itemView.findViewById(R.id.tbItem);
                    tbName = itemView.findViewById(R.id.tbName);
                    tbUser = itemView.findViewById(R.id.tbUser);
                    tbLocation= itemView.findViewById(R.id.tbLocation);
                    tbEpc = itemView.findViewById(R.id.tbEPC);
                }
            }

            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);


           itemView.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View view) {
                   if (ItemLocationAdapter.this.recyclerViewInterface !=null) {
                       int position = getAdapterPosition();
                       if(position!=RecyclerView.NO_POSITION) {
                           ItemLocationAdapter.this.recyclerViewInterface.onLongItemClick(position);


                       }
                   }
                   return false;
               }
           });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ItemLocationAdapter.this.recyclerViewInterface !=null) {
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION) {
                            ItemLocationAdapter.this.recyclerViewInterface.onItemClick(position);
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
