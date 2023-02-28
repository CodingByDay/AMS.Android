package com.example.uhf.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Status{
    public String status;
    public String name;
    public String isDeprec;
    public String isReval;
    public Date timeIns;
    public Date timeChg;
    public int userIns;
    public int userChg;
    @JsonProperty("default")
    public String mydefault;
    public int qid;
}