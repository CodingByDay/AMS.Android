package com.example.uhf.fragment;

import static android.app.ProgressDialog.show;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
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
import com.example.uhf.interfaces.RecyclerViewInterface;
import com.example.uhf.adapter.ItemAdapter;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.ViewModel.ItemViewModel;
import com.example.uhf.tools.StringUtils;
import com.example.uhf.tools.UIHelper;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.entity.UHFTAGInfo;
import com.rscja.deviceapi.exception.ConfigurationException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FixedAssetsFragment extends KeyDwonFragment implements RecyclerViewInterface {
private ItemViewModel itemViewModel;
    private RecyclerView recycler;
    private int selected = -1;
    private List<Item> itemsClassLevel;
    private ItemAdapter adapter;
    private String data;
    private static final String TAG = "UHFReadTagFragment";
    private boolean loopFlag = false;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        instance = this;
        View view = inflater.inflate(R.layout.fragment_fixed_assets, container, false);

        initUHF();
        // Getting the caller information
        Bundle arguments = getArguments();
        assert arguments != null;
        String callerID = arguments.getString("callerID");
        switch (callerID) {
            case "InventoryActivity":
                initEmpty();
                break;
            case "ListingActivity":
                init(view);
                break;
        }
        return view;
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
           // mypDialog.cancel();
            if (!result) {
               // Toast.makeText(this.co, "init fail", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
      /*      mypDialog = new ProgressDialog(this;
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("init...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();*/
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            UHFTAGInfo info = (UHFTAGInfo) msg.obj;
        }
    };

    class TagThread extends Thread {
        public void run() {
            UHFTAGInfo uhftagInfo;
            Message msg;
            while (!loopFlag) {

                uhftagInfo = mReader.readTagFromBuffer();
                if (uhftagInfo != null) {
                    msg = handler.obtainMessage();
                    msg.obj = uhftagInfo;
                    handler.sendMessage(msg);
                    // mContext.playSound(1);
                }
            }
        }
    }


    private void initEmpty() {
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

        if (mReader.startInventoryTag()) {
            new TagThread().start();
        }

    }

    // Method to be called from the parent activity and a method that stops the scanning process
    public void stopScanning ()  {
        Toast.makeText(this.getContext(), "Stopped scanning", Toast.LENGTH_SHORT).show();
    }

}