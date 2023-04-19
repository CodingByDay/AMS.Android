package com.example.uhf.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uhf.R;
import com.example.uhf.interfaces.RecyclerViewInterface;
import com.example.uhf.mvvm.Model.CheckOut;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.ViewModel.ItemLocationViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.ItemHolder> {
    private final boolean synchronizeWithUnchanged;
    private List<CheckOut> items = new ArrayList<CheckOut>();
    private final String caller;
    private List<ItemLocation> itemsUnchanged;
    private final RecyclerViewInterface recyclerViewInterface;
    private List<CheckOut> backupForSearching;
    private ItemLocationViewModel itemLocationViewModel;
    public CheckOutAdapter(RecyclerViewInterface recyclerViewInterface, String callerID, boolean synchronizeWithUnchanged, List<ItemLocation> unchanged) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.caller = callerID;
        this.synchronizeWithUnchanged = synchronizeWithUnchanged;
        this.itemsUnchanged = unchanged;
        initializeTheRegisteredItems();
    }

    private void initializeTheRegisteredItems() {


    }

    // Here is the sorting method

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        switch (this.caller) {
            case "InventoryActivity": {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory, parent, false);
                return new CheckOutAdapter.ItemHolder(itemView, recyclerViewInterface);
            }
            case "ListingActivity": {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listing, parent, false);
                return new CheckOutAdapter.ItemHolder(itemView, recyclerViewInterface);
            }
            case "RegistrationActivity": {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
                return new CheckOutAdapter.ItemHolder(itemView, recyclerViewInterface);
            }
            case "ListingAssetsFragment": {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listing_assets, parent, false);
                return new CheckOutAdapter.ItemHolder(itemView, recyclerViewInterface);
            }
        }
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new CheckOutAdapter.ItemHolder(itemView, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        CheckOut current = items.get(position);


        switch (this.caller) {
            case "InventoryActivity": {
                holder.tbItem.setText(current.getAcItem());
                holder.tbName.setText(current.getAcName());
                holder.tbLocation.setText(current.getAcLocation());
                if(current.getAcECD().length()>=6) {
                    holder.tbEpc.setText(current.getAcECD().substring(current.getAcECD().length() - 5));
                } else {
                    holder.tbEpc.setText(current.getAcECD());
                }
                break;
            }
            case "ListingActivity": {
                holder.tbItem.setText(current.getAcItem());
                holder.tbName.setText(current.getAcName());
                holder.tbLocation.setText(current.getAcLocation());
                if(current.getAcECD().length()>=6) {
                    holder.tbEpc.setText(current.getAcECD().substring(current.getAcECD().length() - 6));
                } else {
                    holder.tbEpc.setText(current.getAcECD());
                }
                break;
            }
            case "RegistrationActivity": {
                holder.tbItem.setText(current.getAcCode());
                holder.tbName.setText(current.getAcName());
                holder.tbCode.setText(current.getAcCode());
                // Ask about this
                holder.tbQty.setText("1");
                break;
            }
            case "ListingAssetsFragment": {
                holder.tbItem.setText(current.getAcCode());
                holder.tbName.setText(current.getAcName());
                holder.tbLocation.setText(current.getAcLocation());
                if(current.getAcECD()!=null) {
                    if(current.getAcECD().length()>=6) {
                        holder.tbEpc.setText(current.getAcECD().substring(current.getAcECD().length() - 5));
                    } else {
                        holder.tbEpc.setText(current.getAcECD());
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

    public void setItems(List<CheckOut> items) {
        this.items = items;
        this.backupForSearching = items;
        notifyDataSetChanged();
    }


    // Here is the sorting method
    public void sortBasedOnLocation(List<CheckOut> items, String location) {


        List<CheckOut> correctItems = new ArrayList<CheckOut>();
        List<CheckOut> wrongItems = new ArrayList<CheckOut>();
        List<CheckOut> emptyItems = new ArrayList<CheckOut>();

        for (CheckOut item :items) {
            if (item.getAcLocation().equals(location)) {
                correctItems.add(item);
            } else if (!item.getAcLocation().equals(location) && !item.getAcLocation().equals("")) {
                wrongItems.add(item);
            } else if (item.getAcLocation().equals("")) {
                emptyItems.add(item);
            }
        }

        ArrayList<CheckOut> sorted = new ArrayList<CheckOut>();
        sorted.addAll(correctItems);
        sorted.addAll(wrongItems);
        sorted.addAll(emptyItems);
        this.items = sorted;
        notifyDataSetChanged();

    }
    public void searchByField(String field, String searchBy) {
        List<CheckOut> sorted = new ArrayList<>();
        if(this.backupForSearching!=null) {
            for (CheckOut item : this.backupForSearching) {
                switch (field) {
                    case "Sredstvo":
                        if (item.getAcItem().toLowerCase().contains(searchBy.toLowerCase())) {
                            sorted.add(item);
                        }
                        break;
                    case "Naziv":
                        if (item.getAcName().toLowerCase().contains(searchBy.toLowerCase())) {
                            sorted.add(item);
                        }
                        break;
                    case "Ident":
                        if (item.getAcCode().toLowerCase().contains(searchBy.toLowerCase())) {
                            sorted.add(item);
                        }
                    case "Lokacija":
                        if (item.getAcLocation().toLowerCase().contains(searchBy.toLowerCase())) {
                            sorted.add(item);
                        }
                        break;
                    case "EPC":
                        if (item.getAcECD().toLowerCase().contains(searchBy.toLowerCase())) {
                            sorted.add(item);
                        }
                        break;
                }
                this.items = sorted;
                notifyDataSetChanged();
            }
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
                    items.sort(Comparator.comparing(CheckOut::getAcItem).reversed());
                    itemASC = false;
                } else {
                    items.sort(Comparator.comparing(CheckOut::getAcItem));
                    itemASC = true;
                }
                break;
            case "Naziv":
                if(nameASC) {
                    items.sort(Comparator.comparing(CheckOut::getAcName));
                    nameASC = false;
                } else {
                    items.sort(Comparator.comparing(CheckOut::getAcName).reversed());
                    nameASC = true;
                }
                break;
            case "Ident":
                if(identASC) {
                    items.sort(Comparator.comparing(CheckOut::getAcCode));
                    identASC = false;
                } else {
                    items.sort(Comparator.comparing(CheckOut::getAcCode).reversed());
                    identASC = true;
                }
                break;
            case "Lokacija":
                if(locationASC) {
                    items.sort(Comparator.comparing(CheckOut::getAcLocation));
                    locationASC = false;
                } else {
                    items.sort(Comparator.comparing(CheckOut::getAcLocation).reversed());
                    locationASC = true;
                }
                break;
            case "EPC":
                if(epcASC) {
                    items.sort(Comparator.comparing(CheckOut::getAcECD));
                    itemASC = false;
                } else {
                    items.sort(Comparator.comparing(CheckOut::getAcECD).reversed());
                    itemASC = true;
                }
                break;
        }
        notifyDataSetChanged();
    }

    public void findIdent(String result) {
        List<CheckOut> sorted = new ArrayList<>();

        for (CheckOut item : this.backupForSearching) {

            if (item.getAcCode().toLowerCase().contains(result.toLowerCase())) {
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

        private TextView tbEpc;

        private LinearLayout linearLayout;

        public ItemHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            // switch the context
            switch (caller) {
                case "InventoryActivity": {
                    tbItem = itemView.findViewById(R.id.tbItem);
                    tbName = itemView.findViewById(R.id.tbName);
                    tbLocation = itemView.findViewById(R.id.tbLocation);
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
                    tbCode = itemView.findViewById(R.id.tbCode);
                    tbQty =  itemView.findViewById(R.id.tbQty);
                    break;
                }
                case "ListingAssetsFragment": {
                    tbItem = itemView.findViewById(R.id.tbItem);
                    tbName = itemView.findViewById(R.id.tbName);
                    tbLocation= itemView.findViewById(R.id.tbLocation);
                    tbEpc = itemView.findViewById(R.id.tbEPC);
                }
            }

            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (CheckOutAdapter.this.recyclerViewInterface !=null) {
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION) {
                            CheckOutAdapter.this.recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }

    }
}
