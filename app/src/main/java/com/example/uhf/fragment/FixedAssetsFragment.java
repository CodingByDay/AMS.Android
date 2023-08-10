package com.example.uhf.fragment;

import static android.app.ProgressDialog.show;
import static android.content.Context.AUDIO_SERVICE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uhf.R;
import com.example.uhf.activity.InventoryActivity;
import com.example.uhf.activity.RegistrationActivity;
import com.example.uhf.adapter.CheckOutAdapter;
import com.example.uhf.adapter.ItemTemporaryAdapter;
import com.example.uhf.interfaces.RecyclerViewInterface;
import com.example.uhf.adapter.ItemLocationAdapter;
import com.example.uhf.mvvm.Model.CheckOut;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.Model.ItemLocationCache;
import com.example.uhf.mvvm.Model.ItemTemporary;
import com.example.uhf.mvvm.ViewModel.CheckOutViewModel;
import com.example.uhf.mvvm.ViewModel.ItemLocationCacheViewModel;
import com.example.uhf.mvvm.ViewModel.ItemLocationViewModel;
import com.example.uhf.mvvm.ViewModel.ItemTemporaryViewModel;
import com.example.uhf.mvvm.ViewModel.ItemViewModel;
import com.example.uhf.tools.StringUtils;
import com.microsoft.appcenter.analytics.Analytics;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.entity.UHFTAGInfo;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FixedAssetsFragment extends KeyDwonFragment implements RecyclerViewInterface {
    private ItemViewModel itemViewModel;
    public ItemTemporaryViewModel temporaryViewModel;
    private RecyclerView recycler;
    private int selected = -1;

    private int count = 0;
    public List<ItemLocation> itemsClassLevel;
    private List<ItemLocation> itemsLocationsClassLevel;
    public CheckOutAdapter adapter;
    public ItemLocationAdapter locationAdapter;
    public ItemTemporaryAdapter temporaryAdapter;
    private String data;
    private static final String TAG = "UHFReadTagFragment";
    private boolean loopFlag = true;
    private int inventoryFlag = 1;
    private List<String> tempDatas = new ArrayList<>();
    private int countList = 0;
    Button BtClear;
    TextView tvTime;
    TextView tv_count;
    TextView tv_total;
    RadioGroup RgInventory;
    RadioButton RbInventorySingle;
    RadioButton RbInventoryLoop;

    Button BtInventory;
    ListView LvTags;
    private InventoryActivity context;
    private RegistrationActivity mContext;
    private HashMap<String, String> map;
    private int total;
    private long time;

    private CheckBox cbFilter;
    private ViewGroup layout_filter;

    public static final String TAG_EPC = "tagEPC";
    public static final String TAG_EPC_TID = "tagEpcTID";
    public static final String TAG_COUNT = "tagCount";
    public static final String TAG_RSSI = "tagRssi";
    private static FixedAssetsFragment instance;
    private RFIDWithUHFUART mReader;
    public List<ItemTemporary> itemsTemporary;
    private String callerID;
    private ItemLocationViewModel itemLocationViewModel;
    private List<ItemLocation> itemsLocation = new ArrayList<ItemLocation>();
    public int index;
    public ItemLocation itemLocationCurrent;
    private ItemTemporary itemsTemporaryCurrent;
    public TextView first;
    public TextView second;
    public TextView third;
    public TextView forth;
    private ItemLocationCacheViewModel itemLocationCacheViewModel;
    private List<ItemLocationCache> cached;

    public String currentSearchColumn  = "";
    private List<ItemLocation> registeredItems;

    private CheckOutViewModel checkOutViewModel;
    private List<CheckOut> checkOutItems;
    private List<CheckOut> checkOuts;
    public ItemLocationAdapter adapterLocation;
    private boolean loopFlagStrongest;
    public ItemLocationAdapter adapterFinal;
    private List<ItemLocation> dataClassLevel = new ArrayList<>();

    private void clearColors() {
        first.setBackgroundColor(Color.TRANSPARENT);
        second.setBackgroundColor(Color.TRANSPARENT);
        third.setBackgroundColor(Color.TRANSPARENT);
        forth.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        instance = this;
        View view = inflater.inflate(R.layout.fragment_fixed_assets, container, false);

        Bundle arguments = getArguments();
        assert arguments != null;

        callerID = arguments.getString("callerID");
        first = view.findViewById(R.id.first);
        second = view.findViewById(R.id.second);
        third = view.findViewById(R.id.third);
        forth = view.findViewById(R.id.forth);


        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearColors();
                currentSearchColumn = first.getText().toString();
                first.setBackgroundColor(Color.parseColor("#C7E3E1"));
            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clearColors();
                currentSearchColumn = second.getText().toString();
                second.setBackgroundColor(Color.parseColor("#C7E3E1"));
            }
        });
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clearColors();
                currentSearchColumn = third.getText().toString();
                third.setBackgroundColor(Color.parseColor("#C7E3E1"));
            }
        });
        forth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clearColors();
                currentSearchColumn = forth.getText().toString();
                forth.setBackgroundColor(Color.parseColor("#C7E3E1"));

            }
        });

        // Initialization
        initSound();
        initUHF();
        // Getting the caller information

        switch (callerID) {
            case "InventoryActivity":
                context = (InventoryActivity) getActivity();
                initEmpty(view);
                initListingForCheck(view);
                first.setText("Lokacija");
                second.setText("Naziv");
                third.setText("Zadolženi");
                forth.setText("EPC");
                break;

            case "ListingActivity":
                initListing(view);
                first.setText("Lokacija");
                second.setText("Naziv");
                third.setText("Zadolženi");
                forth.setText("EPC");
                break;

            case "RegistrationActivity":
                mContext = (RegistrationActivity) getActivity();
                initRegistration(view);
                break;
        }
        // changeUI(view, callerID);

        return view;
    }
    private void changeUI(View view, String callerID) {


        switch (callerID) {
            case "InventoryActivity":
                first.setText("Sredstvo");
                second.setText("Naziv");
                third.setText("Lokacija");
                forth.setText("EPC");
                break;

            case "ListingActivity":
                third.setText("Lokacija");
                first.setText("Naziv");
                second.setText("Zadolženi");
                forth.setText("EPC");
                break;

            case "RegistrationActivity":
                break;
        }
    }

    private void initListing(View view) {
        recycler = (RecyclerView) view.findViewById(R.id.rwItems);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setHasFixedSize(true);
        itemLocationViewModel = ViewModelProviders.of((FragmentActivity) view.getContext()).get(ItemLocationViewModel.class);
        itemLocationViewModel.getItemsThatAreRegistered().observe(this, new Observer<List<ItemLocation>>() {
            @Override
            public void onChanged(List<ItemLocation> items) {
                itemsClassLevel = items;
                adapterFinal = new ItemLocationAdapter(FixedAssetsFragment.this, "ListingAssetsFragment");

                dataClassLevel = items;
                countList += 1;
                if(countList == 2) {
                    countList = 0;
                    dataClassLevel = syncBothLists(itemsClassLevel, checkOuts);
                    recycler.setAdapter(adapterFinal);
                    adapterFinal.setItems(dataClassLevel);
                    adapterFinal.notifyDataSetChanged();

                }
            }
        });


        checkOutViewModel = ViewModelProviders.of((FragmentActivity) view.getContext()).get(CheckOutViewModel.class);
        checkOutViewModel.getAllItems().observe(this, new Observer<List<CheckOut>>() {
            @Override
            public void onChanged(List<CheckOut> checkOutsInner) {
                checkOuts = checkOutsInner;
                countList += 1;
                if(countList == 2) {
                    countList = 0;
                    List<ItemLocation> data = syncBothLists(itemsClassLevel, checkOuts);
                    adapterFinal = new ItemLocationAdapter(FixedAssetsFragment.this, "ListingAssetsFragment");
                    recycler.setAdapter(adapterFinal);
                    adapterFinal.setItems(data);
                    adapterFinal.notifyDataSetChanged();
                }
            }
        });


    }

    private List<ItemLocation> syncBothLists(List<ItemLocation> itemsClassLevelOut, List<CheckOut> checkOuts) {
        for (CheckOut ck : checkOuts) {
            for (ItemLocation item : itemsClassLevelOut) {
                if (item.getQid() == ck.getAnAssetID()) {
                    item.setLocation(ck.getAcLocation());
                }
            }
        }


        return itemsClassLevelOut;
    }



    private void initListingForCheck(View view) {

        itemLocationViewModel = ViewModelProviders.of((FragmentActivity) view.getContext()).get(ItemLocationViewModel.class);
        itemLocationViewModel.getItemsThatAreRegistered().observe(this, new Observer<List<ItemLocation>>() {
            @Override
            public void onChanged(List<ItemLocation> items) {
                itemsClassLevel = items;
            }
        });
    }
    // Init the registration logic
    private void initRegistration(View view) {
        recycler = (RecyclerView) view.findViewById(R.id.rwItems);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setHasFixedSize(true);
        adapterLocation = new ItemLocationAdapter(FixedAssetsFragment.this, callerID);

        itemLocationViewModel = ViewModelProviders.of((FragmentActivity) view.getContext()).get(ItemLocationViewModel.class);
        itemLocationViewModel.getAllItemsNotRegistered().observe(this, new Observer<List<ItemLocation>>() {
            @Override
            public void onChanged(List<ItemLocation> items) {
                recycler.setAdapter(adapterLocation);
                itemsClassLevel = items;

                List<ItemLocation> removeCached = new ArrayList<>();

                if(cached!=null) {
                    removeCached = removeCached(items);
                }
                adapterLocation.setItems(removeCached);
                adapterLocation.notifyDataSetChanged();
            }
        });

        itemLocationViewModel.getItemsThatAreRegistered().observe(this, new Observer<List<ItemLocation>>() {
            @Override
            public void onChanged(List<ItemLocation> items) {

                registeredItems = items;
            }
        });

        temporaryViewModel = ViewModelProviders.of((FragmentActivity) view.getContext()).get(ItemTemporaryViewModel.class);
        temporaryViewModel.deleteAllItems();
        temporaryViewModel.getAllItems().observe(this, new Observer<List<ItemTemporary>>() {
            @Override
            public void onChanged(List<ItemTemporary> items) {
                itemsTemporary = items;
                mContext.scannedItems = items;
            }
        });


        itemLocationCacheViewModel = ViewModelProviders.of(this).get(ItemLocationCacheViewModel.class);

        itemLocationCacheViewModel.getAllItems().observe(this, new Observer<List<ItemLocationCache>>() {
            @Override
            public void onChanged(List<ItemLocationCache> itemLocationCaches) {
                cached = itemLocationCaches;
            }
        });
    }

    private List<ItemLocation> removeCached(List<ItemLocation> items) {
        for(ItemLocationCache item: cached) {
            for (ItemLocation itemInner: items) {
                items.remove(itemInner);
            }
        }

        return items;
    }


    public void playSound(int id) {
        float audioMaxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float audioCurrentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumnRatio = audioCurrentVolume / audioMaxVolume;
        try {
            soundPool.play(soundMap.get(id), volumnRatio,
                    volumnRatio,
                    1,
                    0,
                    1
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class InitTask extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog mypDialog;

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            mReader.free();
            return mReader.init();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            mypDialog.cancel();
            if (!result) {
                Toast.makeText(getActivity(), "init fail", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            mypDialog = new ProgressDialog(getActivity());
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("init...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
        }
    }

    private void initUHF() {
        try {
            mReader = RFIDWithUHFUART.getInstance();
        } catch (Exception ex) {
            return;
        }
        if (mReader != null) {
            new InitTask().execute();
        }
    }

    private List<String> cache;
    private boolean preventDuplicate = false;
    /*
     * Turn scanning on and off from the activity
     * */
    public void toggleScanning(boolean enable) {
        if (enable) {
            startScanning();
        } else {
            stopScanning();
        }
    }


    private ItemLocation filterList(String epc) {
        for (ItemLocation item : itemsLocationsClassLevel) {
            if (item.getEcd().equals(epc)) {
                return item;
            }
        }
        return null;
    }


    private boolean checkifEPCisRegistered(String epc) {
        for(ItemLocation item: registeredItems) {
            if (item.getEcd().equals(epc)) {
                return true;
            }
        }
        return false;
    }

    private boolean preventDuplicateUse = true;
    // TODO: Handlers should be static lest they can have leaks.
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            if (preventDuplicateUse) {
            preventDuplicateUse = false;
            preventDuplicate = true;
            UHFTAGInfo info = (UHFTAGInfo) msg.obj;
            // Binary search check for duplicates
            if(!StringUtils.isEmpty(info.getEPC())) {
                int index = checkIfExist(info.getEPC());
                if(index == -1) {
                    playSound(1);
                    // TODO fix date time to work with earlier versions
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if(callerID.equals("RegistrationActivity")) {
                            // Logic for not registered items -- check in internal structure -- performance friendly
                            if(!checkifEPCisRegistered(info.getEPC())) {
                                 temporaryViewModel.insert(new ItemTemporary(info.getEPC(), "temp", "temp", "temp", 1, Instant.now().toString(), "Janko", info.getRssi(), -1, "temp"));
                            }

                        } else if (callerID.equals("InventoryActivity")) {
                            int location = (int) Math.floor(Math.random() * 5);
                            ItemLocation item = filterList(info.getEPC());
                            if(item!=null && !existsInCheckout(info.getEPC())) {
                                // Query
                                ItemLocation asset = checkExistence(info.getEPC());
                                if(asset!=null ) {
                                    if(location == 0) {
                                    temporaryViewModel.insert(new ItemTemporary(asset.getEcd(), asset.getName(), asset.getCode(), item.getLocation(), 1, Instant.now().toString(), "Janko", info.getRssi(), asset.getQid(), asset.getCaretaker()));
                                } else {
                                    temporaryViewModel.insert(new ItemTemporary(asset.getEcd(), asset.getName(), asset.getCode(), asset.getLocation(), 1, Instant.now().toString(), "Janko", info.getRssi(), asset.getQid(),asset.getCaretaker()));
                                }
                            } else {
                                if(location == 0) {
                                    assert false;
                                    temporaryViewModel.insert(new ItemTemporary(asset.getEcd(), asset.getName(), asset.getCode(), asset.getLocation(), 1, Instant.now().toString(), "Janko", info.getRssi(), asset.getQid(), asset.getCaretaker()));
                                } else {
                                    assert false;
                                    temporaryViewModel.insert(new ItemTemporary(asset.getEcd(), asset.getName(), asset.getCode(), asset.getLocation(), 1, Instant.now().toString(), "Janko", info.getRssi(), asset.getQid(), asset.getCaretaker()));
                                }
                            }
                            } else {
                                return;
                            }
                        }
                    }
                    tempDatas.add(info.getEPC());

                }
            }
            // TODO cleanup button
        }             preventDuplicateUse = true;

        }



    };


    public ItemTemporary strongestRssi = new ItemTemporary("");


    @SuppressLint("HandlerLeak")
    Handler handlerStrongest = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            UHFTAGInfo info = (UHFTAGInfo) msg.obj;
            if (strongestRssi.getRssi().equals("")) {
                strongestRssi.setEcd(info.getEPC());
                strongestRssi.setRssi(info.getRssi());
                return;
            }
            float rssi = Float.parseFloat(strongestRssi.getRssi().replace(",", "."));
            if (Float.parseFloat(info.getRssi().replace(",", ".")) > rssi) {
                strongestRssi.setEcd(info.getEPC());
                strongestRssi.setRssi(info.getRssi());
            }

            // Compare
        }

    };








    private boolean existsInCheckout(String epc) {
        if(checkOutItems!=null && checkOutItems.size()>=1) {
            for (CheckOut check : checkOutItems) {
                if (check.getAcECD().equals(epc)) {
                    return true;
                }
            }
        }
        return false;
    }

    private ItemLocation checkExistence(String epc) {
        if(itemsClassLevel!=null) {
        for(ItemLocation item: itemsClassLevel) {
            if(item.getEcd().equals(epc)) {
                return item;
            }
        }
        }
        return null;
    }


    public int checkIfExist(String epc) {
        if (StringUtils.isEmpty(epc)) {
            return -1;
        }
        return binarySearch(tempDatas, epc);
    }


    static int binarySearch(List<String> array, String src) {
        int left = 0;
        int right = array.size() - 1;

        while (left <= right) {
            if (compareString(array.get(left), src)) {
                return left;
            } else if (left != right) {
                if (compareString(array.get(right), src))
                    return right;
            }
            left++;
            right--;
        }
        return -1;
    }

    static boolean compareString(String str1, String str2) {
        if (str1.length() != str2.length()) {
            return false;
        } else if (str1.hashCode() != str2.hashCode()) {
            return false;
        } else {
            char[] value1 = str1.toCharArray();
            char[] value2 = str2.toCharArray();
            int size = value1.length;
            for (int k = 0; k < size; k++) {
                if (value1[k] != value2[k]) {
                    return false;
                }
            }
            return true;
        }
    }
    private SoundPool soundPool;
    private float volumnRatio;
    private AudioManager am;

    private void initSound() {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
        soundMap.put(1, soundPool.load(getActivity(), R.raw.barcodebeep, 1));
        soundMap.put(2, soundPool.load(getActivity(), R.raw.serror, 1));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am = (AudioManager) Objects.requireNonNull(getActivity()).getSystemService(AUDIO_SERVICE);
        }
    }
    HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();

    private void releaseSoundPool() {
        if(soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

    class TagThread extends Thread {
        public void run() {
            UHFTAGInfo uhftagInfo;
            Message msg;
            while (loopFlag) {
                uhftagInfo = mReader.readTagFromBuffer();
                if (uhftagInfo != null) {
                    msg = handler.obtainMessage();
                    msg.obj = uhftagInfo;
                    handler.sendMessage(msg);
                }
            }
        }
    }

    class TagThreadStrongest extends Thread {
        public void run() {
            UHFTAGInfo uhftagInfo;
            Message msg;
            while (loopFlagStrongest) {
                uhftagInfo = mReader.readTagFromBuffer();
                if (uhftagInfo != null) {
                    msg = handlerStrongest.obtainMessage();
                    msg.obj = uhftagInfo;
                    handlerStrongest.sendMessage(msg);
                }
            }
        }
    }
    private void initEmpty(View view) {

        temporaryAdapter = new ItemTemporaryAdapter(this);
        recycler = (RecyclerView) view.findViewById(R.id.rwItems);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setHasFixedSize(true);
        temporaryAdapter = new ItemTemporaryAdapter(this);
        recycler.setAdapter(temporaryAdapter);
        temporaryViewModel = ViewModelProviders.of((FragmentActivity) view.getContext()).get(ItemTemporaryViewModel.class);
        temporaryViewModel.deleteAllItems();
        temporaryViewModel.getAllItems().observe(this, new Observer<List<ItemTemporary>>() {
            @Override
            public void onChanged(List<ItemTemporary> items) {
                itemsTemporary = items;
                temporaryAdapter.setItems(items);
            }
        });


        itemLocationViewModel = ViewModelProviders.of((FragmentActivity) view.getContext()).get(ItemLocationViewModel.class);
        itemLocationViewModel.getItemsThatAreRegistered().observe(this, new Observer<List<ItemLocation>>() {
            @Override
            public void onChanged(List<ItemLocation> items) {
                itemsLocationsClassLevel = items;
                // locationAdapter.setItems(items);
            }
        });

        checkOutViewModel = ViewModelProviders.of((FragmentActivity) view.getContext()).get(CheckOutViewModel.class);

        checkOutViewModel.getAllItems().observe(this, new Observer<List<CheckOut>>() {
            @Override
            public void onChanged(List<CheckOut> checkOuts) {
                checkOutItems = checkOuts;
            }
        });

    }

    private void init(View view) {
        recycler = (RecyclerView) view.findViewById(R.id.rwItems);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setHasFixedSize(true);
        locationAdapter = new ItemLocationAdapter(this, callerID);
        recycler.setAdapter(locationAdapter);
        itemLocationViewModel = ViewModelProviders.of((FragmentActivity) view.getContext()).get(ItemLocationViewModel.class);
        itemLocationViewModel.getAllItems().observe(this, new Observer<List<ItemLocation>>() {
            @Override
            public void onChanged(List<ItemLocation> items) {
                itemsLocationsClassLevel = items;
                // locationAdapter.setItems(items);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        if(selected==-1) {
            Objects.requireNonNull(Objects.requireNonNull(recycler.getLayoutManager()).findViewByPosition(position)).setBackgroundColor(Color.parseColor("#969595"));
        } else {
            Objects.requireNonNull(Objects.requireNonNull(recycler.getLayoutManager()).findViewByPosition(selected)).setBackgroundColor(Color.TRANSPARENT);
            Objects.requireNonNull(Objects.requireNonNull(recycler.getLayoutManager()).findViewByPosition(position)).setBackgroundColor(Color.parseColor("#969595"));
        }
        selected = position;



        if(callerID.equals("RegistrationActivity") ) {
            ItemLocation location = adapterLocation.items.get(position);
            mContext.currentItem = location;
            itemLocationCurrent = location;


        }

        if(callerID.equals("InventoryActivity")) {

            ItemTemporary location = temporaryAdapter.items.get(position);
            itemsTemporaryCurrent = location;
            context.current = location;
            ItemTemporary tmp = location;



        }

        if(callerID.equals("ListingActivity")) {

            ItemLocation item = adapterFinal.items.get(position);





        }


        index = position;
    }

    @Override
    public void onLongItemClick(int position) {
        if(callerID.equals("RegistrationActivity") ) {
            ItemLocation location = adapterLocation.items.get(position);
            mContext.currentItem = location;
            itemLocationCurrent = location;
            String message = "Lokacija: " + itemLocationCurrent.getLocation() + "\n"
                    + "Ime: " + itemLocationCurrent.getName() + "\n"
                    + "Zadolženi: " + itemLocationCurrent.getCaretaker() + "\n"
                    + "Šifra: " + itemLocationCurrent.getItem();
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


        }

        if(callerID.equals("InventoryActivity")) {

            ItemTemporary location = temporaryAdapter.items.get(position);
            itemsTemporaryCurrent = location;
            context.current = location;
            ItemTemporary tmp = location;

            String message = "Lokacija: " + tmp.getLocation() + "\n"
                    + "Ime: " + tmp.getName() + "\n"
                    + "Zadolženi: " + tmp.getCaretaker() + "\n"
                    + "EPC: " + tmp.getEcd();

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


        }

        if(callerID.equals("ListingActivity")) {

            ItemLocation item = adapterFinal.items.get(position);


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




        }
    }

    private void findTheClosestEPC() {
        // TODO: Loading bar
    }

    public static FixedAssetsFragment getInstance() {
        return instance;
    }

    // This is a method to be called from the parent activity
    public void sortBasedOnLocation(String location) {
        try {
            temporaryAdapter.sortBasedOnLocation(itemsTemporary, location);
        } catch (Exception err) {
            Analytics.trackEvent(err.getLocalizedMessage());
        }
    }

    // Method to be called from the parent activity and a method that starts the scanning process
    public void startScanning() {
        loopFlag = true;
        if (mReader.startInventoryTag()) {
            new TagThread().start();
        }
    }

    // Method to be called from the parent activity and a method that starts the scanning process
    public void startScanningStrongest() {
        loopFlagStrongest = true;
        if (mReader.startInventoryTag()) {
            new TagThreadStrongest().start();
        }
    }
    public void stopScanningStrongest ()  {

        loopFlagStrongest = false;
    }

    // Method to be called from the parent activity and a method that starts the scanning process for locating
    public void startScanningBackground() {
        loopFlag = true;
        if (mReader.startInventoryTag()) {
            new TagThread().start();
        }

        // Delay 5 seconds display the progress

    }
    // Method to be called from the parent activity and a method that stops the scanning process
    public void stopScanning ()  {
        mReader.stopLocation();
        int test = selected;
        if(selected != -1) {
            Objects.requireNonNull(Objects.requireNonNull(recycler.getLayoutManager()).findViewByPosition(selected)).setBackgroundColor(Color.TRANSPARENT);
        }
        selected = -1;
        loopFlag = false;
    }

}