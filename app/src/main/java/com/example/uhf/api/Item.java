package com.example.uhf.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    public int qid;
    public String type;
    public String item;
    public String name;
    public Object qty;
    public String orderKey;
    public int orderNo;
    public Date orderDate;
    public double acqVal;
    public double wrtOffVal;
    public String status;
    public Date timeIns;
    public int userIns;
    public Date timeChg;
    public int userChg;
    public Object note;
}




