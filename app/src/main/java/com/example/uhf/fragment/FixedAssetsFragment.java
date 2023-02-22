package com.example.uhf.fragment;

import static android.app.ProgressDialog.show;
import static android.content.Context.AUDIO_SERVICE;

import static java.util.Objects.checkIndex;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uhf.R;
import com.example.uhf.activity.BaseTabFragmentActivity;
import com.example.uhf.activity.InventoryActivity;
import com.example.uhf.activity.UHFMainActivity;
import com.example.uhf.adapter.ItemTemporaryAdapter;
import com.example.uhf.interfaces.RecyclerViewInterface;
import com.example.uhf.adapter.ItemAdapter;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.Model.ItemTemporary;
import com.example.uhf.mvvm.ViewModel.ItemTemporaryViewModel;
import com.example.uhf.mvvm.ViewModel.ItemViewModel;
import com.example.uhf.tools.StringUtils;
import com.example.uhf.tools.UIHelper;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.entity.UHFTAGInfo;
import com.rscja.deviceapi.exception.ConfigurationException;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FixedAssetsFragment extends KeyDwonFragment implements RecyclerViewInterface {
    private ItemViewModel itemViewModel;
    private ItemTemporaryViewModel temporaryViewModel;
    private RecyclerView recycler;
    private int selected = -1;

    private int count = 0;
    private List<Item> itemsClassLevel;
    private ItemAdapter adapter;
    private ItemTemporaryAdapter temporaryAdapter;
    private String data;
    private static final String TAG = "UHFReadTagFragment";
    private boolean loopFlag = true;
    private int inventoryFlag = 1;
    private List<String> tempDatas = new ArrayList<>();

    Button BtClear;
    TextView tvTime;
    TextView tv_count;
    TextView tv_total;
    RadioGroup RgInventory;
    RadioButton RbInventorySingle;
    RadioButton RbInventoryLoop;

    Button BtInventory;
    ListView LvTags;
    private InventoryActivity mContext;
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
    private List<ItemTemporary> itemsTemporary;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        instance = this;
        View view = inflater.inflate(R.layout.fragment_fixed_assets, container, false);

        // Initialization
        initSound();
        initUHF();

        // Getting the caller information
        Bundle arguments = getArguments();
        assert arguments != null;
        String callerID = arguments.getString("callerID");
        switch (callerID) {
            case "InventoryActivity":
                initEmpty(view);
                break;
            case "ListingActivity":
                init(view);
                break;
            case "RegistrationActivity":
                initRegistration(view);
                break;
        }
        return view;
    }

    // Init the registration logic
    private void initRegistration(View view) {
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
    // TODO: Handlers should be static lest they can have leaks.
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
               preventDuplicate = true;
               UHFTAGInfo info = (UHFTAGInfo) msg.obj;
               // Binary search check for duplicates
            if(!StringUtils.isEmpty(info.getEPC())) {
                int index = checkIfExist(info.getEPC());
                if(index == -1) {
                playSound(1);
                // TODO fix date time to work with earlier versions
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        temporaryViewModel.insert(new ItemTemporary(info.getEPC(), "test", "test", "01", 3, Instant.now() .toString() , "Janko"));
                    }
                    tempDatas.add(info.getEPC());
                }
            }
            // TODO cleanup button
        }
    };




    public int checkIfExist(String epc) {
        if (StringUtils.isEmpty(epc)) {
            return -1;
        }
        return binarySearch(tempDatas, epc);
    }


    static int binarySearch(List<String> array, String src) {
        int left = 0;
        int right = array.size() - 1;
        // 这里必须是 <=
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
            am = (AudioManager) Objects.requireNonNull(getActivity()).getSystemService(AUDIO_SERVICE);// 实例化AudioManager对象
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
    }

    private void init(View view) {
        recycler = (RecyclerView) view.findViewById(R.id.rwItems);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setHasFixedSize(true);
        adapter = new ItemAdapter(this);
        recycler.setAdapter(adapter);
        itemViewModel = ViewModelProviders.of((FragmentActivity) view.getContext()).get(ItemViewModel.class);
        itemViewModel.getAllItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                itemsClassLevel = items;
                adapter.setItems(items);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        if(selected==-1) {
            Objects.requireNonNull(Objects.requireNonNull(recycler.getLayoutManager()).findViewByPosition(position)).setBackgroundColor(Color.BLUE);
        } else {
            Objects.requireNonNull(Objects.requireNonNull(recycler.getLayoutManager()).findViewByPosition(selected)).setBackgroundColor(Color.TRANSPARENT);
            Objects.requireNonNull(Objects.requireNonNull(recycler.getLayoutManager()).findViewByPosition(position)).setBackgroundColor(Color.BLUE);
        }
        selected = position;
    }
    public static FixedAssetsFragment getInstance() {
        return instance;
    }

    // This is a method to be called from the parent activity
    public void sortBasedOnLocation(String location) {
        adapter.sortBasedOnLocation(itemsClassLevel, location);
    }

    // Method to be called from the parent activity and a method that starts the scanning process
    public void startScanning() {
        Toast.makeText(this.getContext(), "Started scanning", Toast.LENGTH_SHORT).show();
        loopFlag = true;
        if (mReader.startInventoryTag()) {
            new TagThread().start();
        }
    }

    // Method to be called from the parent activity and a method that stops the scanning process
    public void stopScanning ()  {
        loopFlag = false;
        Toast.makeText(this.getContext(), "Stopped scanning", Toast.LENGTH_SHORT).show();
    }

}