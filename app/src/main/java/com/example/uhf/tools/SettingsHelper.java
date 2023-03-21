package com.example.uhf.tools;

import com.example.uhf.settings.Setting;

import java.util.List;

public class SettingsHelper {
    public static class SettingsHelp {
        public static String returnSettingValue(List<Setting> data, String key) {
            for(Setting setting: data) {
                if(setting.getKey().equals(key)) {
                    return setting.getValue();
                }
            }
            return null;
        }
    }
}
