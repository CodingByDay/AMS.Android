package com.example.uhf.api;

import java.time.LocalDateTime;
import java.util.List;

public class ItemResponse {


    private boolean success;
    private String error;
    private List<ItemInner> items;
    private class ItemInner {
        private int qid;
        private String type;
        private String item;
        private String name;
        private float qty;
        private String orderKey;
        private int orderNo;
        private LocalDateTime orderDate;
        private float acqVal;
        private float wrtOffVal;
        private String status;
        private LocalDateTime timeIns;
        private int userIns;
        private LocalDateTime timeChg;
        private int userChg;
        private String note;
    }
}
