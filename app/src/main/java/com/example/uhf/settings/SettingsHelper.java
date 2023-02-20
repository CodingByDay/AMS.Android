package com.example.uhf.settings;

import java.util.List;

public class SettingsHelper {
    // Helper class
    public Setting findSetting(List<Setting> items, String key) {
        Setting setting = new Setting();
        for (Setting settingLoop : items) {
            if(settingLoop.getKey().equals(key)) {
                return settingLoop;
            }
        }
        return setting;
    }
}
