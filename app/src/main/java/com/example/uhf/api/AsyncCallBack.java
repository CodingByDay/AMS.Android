package com.example.uhf.api;

import com.example.uhf.mvvm.Model.Item;

import java.util.List;

public interface AsyncCallBack {
    void setResult(Boolean result);

    void setResultListTypeItem(List<Item> items);
}
