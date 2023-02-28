package com.example.uhf.api;

import com.example.uhf.mvvm.Model.Item;

import java.util.List;

public interface AsyncCallBack {
    void setResult(Boolean result);

    void setResultListTypeItem(List<Item> items);



    void setResultRoot(Root root);


    void setResultRootLocation(RootLocation rootLocation);


    void setResultRootStatus(RootStatus status);


    void setResultRootAsset(RootAsset asset);
}
