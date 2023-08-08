package com.example.uhf.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.uhf.R;
import com.example.uhf.api.AsyncCallBack;
import com.example.uhf.api.Communicator;
import com.example.uhf.api.Root;
import com.example.uhf.api.RootAsset;
import com.example.uhf.api.RootLocation;
import com.example.uhf.api.RootStatus;
import com.example.uhf.custom.CustomSearchableSpinner;
import com.example.uhf.database.ImportExportData;
import com.example.uhf.fragment.FixedAssetsFragment;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.Model.ItemLocationCache;
import com.example.uhf.mvvm.Model.Location;
import com.example.uhf.mvvm.ViewModel.ItemLocationCacheViewModel;
import com.example.uhf.mvvm.ViewModel.ItemLocationViewModel;
import com.example.uhf.mvvm.ViewModel.LocationViewModel;
import com.example.uhf.mvvm.ViewModel.SettingsViewModel;
import com.example.uhf.settings.Setting;
import com.example.uhf.tools.SettingsHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.microsoft.appcenter.distribute.Distribute;
import com.microsoft.appcenter.distribute.DistributeListener;
import com.microsoft.appcenter.distribute.ReleaseDetails;
import com.microsoft.appcenter.distribute.UpdateAction;
import com.microsoft.appcenter.utils.async.AppCenterFuture;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoginActivityMain extends AppCompatActivity implements AsyncCallBack, DistributeListener {
private TextView login;
    private ProgressDialog mypDialog;
    private EditText tbCompany;
    private EditText tbUname;
    private EditText tbPassword;
    private List<Setting> settingsList = new ArrayList<Setting>();
    private Communicator client;
    public String token = "";
    private SettingsViewModel settingsView;
    // Jackson properties
    private Root root;
    private RootLocation rootLocation;
    private RootAsset rootAsset;


    private ItemLocationViewModel itemLocationViewModel;
    private ItemLocationCacheViewModel itemLocationCacheViewModel;
    private List<ItemLocationCache> itemsLocationsCacheClassLevel;
    // Jackson properties

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AppCenter.start(getApplication(), "9997086a-90d2-4150-b81a-4cd67bc6c2b1",
                Analytics.class, Crashes.class, Distribute.class);
        // Distribute.setEnabledForDebuggableBuild(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        client = new Communicator();
        getSupportActionBar().show();
        getSupportActionBar().setTitle("RF Inventura");
        initSettings();
        initPageViews();
        initSynchronization();
        boolean start = Distribute.isEnabled().get();



    }



    private void checkIdCreateIfNecessary() {
       String id = com.example.uhf.settings.SettingsHelper.Helper.findSetting(settingsList, "device").getValue();
       if (id.equals("")) {
            String guid = java.util.UUID.randomUUID().toString();
            settingsView.insert(new Setting("device", guid));
       }
    }

    private void initSynchronization() {
        itemLocationViewModel = ViewModelProviders.of(this).get(ItemLocationViewModel.class);
        // if cached table is not empty and there is internet sync the data
        itemLocationCacheViewModel = ViewModelProviders.of(this).get(ItemLocationCacheViewModel.class);
        itemLocationCacheViewModel.getAllItems().observe(this, new Observer<List<ItemLocationCache>>() {
            @Override
            public void onChanged(List<ItemLocationCache> items) {
                 itemsLocationsCacheClassLevel = items;
                 if(itemsLocationsCacheClassLevel.size() > 0) {
                     syncDatabase(itemsLocationsCacheClassLevel);
                 }
            }
        });


        itemLocationViewModel = ViewModelProviders.of(this).get(ItemLocationViewModel.class);
        // if cached table is not empty and there is internet sync the data
        itemLocationViewModel = ViewModelProviders.of(this).get(ItemLocationViewModel.class);
        itemLocationViewModel.getAllItems().observe(this, new Observer<List<ItemLocation>>() {
            @Override
            public void onChanged(List<ItemLocation> items) {
                int r = 9+9;
            }
        });

    }

    private void syncDatabase(List<ItemLocationCache> itemsLocationsCacheClassLevel) {
        for (ItemLocationCache itemLocationCache: itemsLocationsCacheClassLevel) {
            itemLocationViewModel.updateEPCByID(itemLocationCache.getID(), itemLocationCache.getEcd());
            itemLocationCacheViewModel.delete(itemLocationCache);

        }

    }

    private void initSettings() {
        settingsView = ViewModelProviders.of(this).get(SettingsViewModel.class);
        settingsView.getAllItems().observe(this, new Observer<List<Setting>>() {
            @Override
            public void onChanged(List<Setting> settings) {
                settingsList = settings;

                checkIdCreateIfNecessary();

            }
        });
    }
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                break;
            case R.id.UHF_ver:
                String versionName = "";
                try {
                    versionName = this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), 0).versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    break;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("O aplikaciji");
                builder.setMessage("Inventura verzija: " + versionName);
                builder.setPositiveButton("Zapri", ((dialogInterface, i) -> {
                }));
                builder.show();
                break;

            case R.id.settings:
                Toast.makeText(this, "Nastavitve", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                //myIntent.putExtra("fragment", "entry_menu");
                startActivity(myIntent);
                break;
            default:
                break;
        }
        return true;
    }
    private void SyncData() throws JsonProcessingException {
        boolean connected = client.isDeviceConnected(this);
        if(!connected) {
        Toast.makeText(this, "Ni povezave!", Toast.LENGTH_SHORT).show();
         return;
        }
        mypDialog = new ProgressDialog(this);
        mypDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mypDialog.setMax(100);
        mypDialog.setMessage("Pridobivanje podatkov...");
        mypDialog.setCanceledOnTouchOutside(false);
        mypDialog.show();
        // Continue here
        client.retrieveItems(LoginActivityMain.this, settingsList);
        client.retrieveLocations(LoginActivityMain.this, settingsList);
        client.retrieveAssets(LoginActivityMain.this, settingsList);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        supportInvalidateOptionsMenu();
        invalidateOptionsMenu();
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    private void initPageViews() {
        login = (TextView) findViewById(R.id.login);
        tbCompany = (EditText) findViewById(R.id.tbCompany);
        tbUname = (EditText) findViewById(R.id.tbUname);
        tbPassword = (EditText) findViewById(R.id.tbPassword);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mypDialog = new ProgressDialog(LoginActivityMain.this);
                mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mypDialog.setMessage("Prijava...");
                mypDialog.setCanceledOnTouchOutside(false);
                mypDialog.show();
                // Data
                if(SettingsHelper.SettingsHelp.returnSettingValue(settingsList, "company")!=null) {
                String company = SettingsHelper.SettingsHelp.returnSettingValue(settingsList, "company");
                String uname = tbUname.getText().toString();
                String password = tbPassword.getText().toString();
                    assert company != null;
                    if(company.isEmpty()||uname.isEmpty()||password.isEmpty()) {
                    return;
                }
                // Call the API
                boolean success = false;
                try {
                    client.login(LoginActivityMain.this, settingsList, company, uname, password);
                } catch (JsonProcessingException e) {
                    Toast.makeText(LoginActivityMain.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    JsonProcessingException error = e;
                    Analytics.trackEvent(e.getLocalizedMessage());
                }
                }  else {
                    Toast.makeText(LoginActivityMain.this, "Ni podatka o podjetju", Toast.LENGTH_SHORT).show();
                    mypDialog.cancel();
                }
            }
        });
    }

    @Override
    public void setResult(Boolean result) {
        mypDialog.cancel();
        if(result) {
            if(!token.equals("")) {
                settingsView.insert(new Setting("token", token));
                settingsView.insert(new Setting("user", tbUname.getText().toString()));
            }
            Intent myIntent = new Intent(getApplicationContext(), EntryInitialActivity.class);
            startActivity(myIntent);
        } else {
            Toast.makeText(this, "Napačni podatki.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void setResultListTypeItem(List<Item> items) {
        int result = items.size();
    }

    @Override
    public void setResultRoot(Root root) {


    }

    @Override
    public void setResultRootLocation(RootLocation rootLocation) {
    }

    @Override
    public void setResultRootStatus(RootStatus status) {
    }

    @Override
    public void setResultRootAsset(RootAsset asset) {
    }
    @Override
    public void setProgressValue(int progress) {

    }


    @Override
    public boolean onReleaseAvailable(Activity activity, ReleaseDetails releaseDetails) {

        // Look at releaseDetails public methods to get version information, release notes text or release notes URL
        String versionName = releaseDetails.getShortVersion();
        int versionCode = releaseDetails.getVersion();
        String releaseNotes = releaseDetails.getReleaseNotes();
        Uri releaseNotesUrl = releaseDetails.getReleaseNotesUrl();

        // Build our own dialog title and message
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setTitle("Version " + versionName + " available!"); // you should use a string resource instead, this is just a simple example
        dialogBuilder.setMessage("Please update the application.");


        // This is the update text to

        // Mimic default SDK buttons
        dialogBuilder.setPositiveButton(com.microsoft.appcenter.distribute.R.string.appcenter_distribute_update_dialog_download, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // This method is used to tell the SDK what button was clicked
                Distribute.notifyUpdateAction(UpdateAction.UPDATE);
            }
        });

        // We can postpone the release only if the update isn't mandatory
        if (!releaseDetails.isMandatoryUpdate()) {
            dialogBuilder.setNegativeButton(com.microsoft.appcenter.distribute.R.string.appcenter_distribute_update_dialog_postpone, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // This method is used to tell the SDK what button was clicked
                    Distribute.notifyUpdateAction(UpdateAction.POSTPONE);
                }
            });
        }
        dialogBuilder.setCancelable(false); // if it's cancelable you should map cancel to postpone, but only for optional updates
        dialogBuilder.create().show();

        // Return true if you're using your own dialog, false otherwise
        return true;
    }

    @Override
    public void onNoReleaseAvailable(Activity activity) {
        Toast.makeText(activity, activity.getString(R.string.no_updates_available), Toast.LENGTH_LONG).show();
    }

}