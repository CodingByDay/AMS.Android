package com.example.uhf.mvvm.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;



@Entity(tableName = "check_out")
public class CheckOut {

    @PrimaryKey(autoGenerate = true)
    private int anQId;
    @ColumnInfo( defaultValue = "empty")
    private int anInventory;
    @ColumnInfo( defaultValue = "empty")
    private int anAssetID;
    @ColumnInfo( defaultValue = "empty")
    private String acItem;
    @ColumnInfo( defaultValue = "empty")
    private String acLocation;
    @ColumnInfo( defaultValue = "empty")
    private String acCode;
    @ColumnInfo( defaultValue = "empty")
    private String acECD;
    @ColumnInfo( defaultValue = "empty")
    private String acName;
    @ColumnInfo( defaultValue = "empty")
    private String acName2;
    @ColumnInfo( defaultValue = "empty")
    private Date adDateCheck;
    @ColumnInfo( defaultValue = "empty")
    private int anUserCheck;
    @ColumnInfo( defaultValue = "empty")
    private Date adDateConfirm;
    @ColumnInfo( defaultValue = "empty")
    private int anUserConfirm;
    @ColumnInfo( defaultValue = "empty")
    private Date adTimeIns;
    @ColumnInfo( defaultValue = "empty")
    private int anUserIns;
    @ColumnInfo( defaultValue = "empty")
    private Date adTimeChg;
    @ColumnInfo( defaultValue = "empty")
    private int anUserChg;
    @ColumnInfo( defaultValue = "empty")
    private String acNote;


    public CheckOut(int anInventory, int anAssetID, String acItem, String acLocation, String acCode, String acECD, String acName, String acName2, Date adDateCheck, int anUserCheck, Date adDateConfirm, int anUserConfirm, Date adTimeIns, int anUserIns, Date adTimeChg, int anUserChg, String acNote) {
        this.anInventory = anInventory;
        this.anAssetID = anAssetID;
        this.acItem = acItem;
        this.acLocation = acLocation;
        this.acCode = acCode;
        this.acECD = acECD;
        this.acName = acName;
        this.acName2 = acName2;
        this.adDateCheck = adDateCheck;
        this.anUserCheck = anUserCheck;
        this.adDateConfirm = adDateConfirm;
        this.anUserConfirm = anUserConfirm;
        this.adTimeIns = adTimeIns;
        this.anUserIns = anUserIns;
        this.adTimeChg = adTimeChg;
        this.anUserChg = anUserChg;
        this.acNote = acNote;
    }


    public int getAnQId() {
        return anQId;
    }

    public void setAnQId(int anQId) {
        this.anQId = anQId;
    }

    public int getAnInventory() {
        return anInventory;
    }

    public void setAnInventory(int anInventory) {
        this.anInventory = anInventory;
    }

    public int getAnAssetID() {
        return anAssetID;
    }

    public void setAnAssetID(int anAssetID) {
        this.anAssetID = anAssetID;
    }

    public String getAcItem() {
        return acItem;
    }

    public void setAcItem(String acItem) {
        this.acItem = acItem;
    }

    public String getAcLocation() {
        return acLocation;
    }

    public void setAcLocation(String acLocation) {
        this.acLocation = acLocation;
    }

    public String getAcCode() {
        return acCode;
    }

    public void setAcCode(String acCode) {
        this.acCode = acCode;
    }

    public String getAcECD() {
        return acECD;
    }

    public void setAcECD(String acECD) {
        this.acECD = acECD;
    }

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public String getAcName2() {
        return acName2;
    }

    public void setAcName2(String acName2) {
        this.acName2 = acName2;
    }

    public Date getAdDateCheck() {
        return adDateCheck;
    }

    public void setAdDateCheck(Date adDateCheck) {
        this.adDateCheck = adDateCheck;
    }

    public int getAnUserCheck() {
        return anUserCheck;
    }

    public void setAnUserCheck(int anUserCheck) {
        this.anUserCheck = anUserCheck;
    }

    public Date getAdDateConfirm() {
        return adDateConfirm;
    }

    public void setAdDateConfirm(Date adDateConfirm) {
        this.adDateConfirm = adDateConfirm;
    }

    public int getAnUserConfirm() {
        return anUserConfirm;
    }

    public void setAnUserConfirm(int anUserConfirm) {
        this.anUserConfirm = anUserConfirm;
    }

    public Date getAdTimeIns() {
        return adTimeIns;
    }

    public void setAdTimeIns(Date adTimeIns) {
        this.adTimeIns = adTimeIns;
    }

    public int getAnUserIns() {
        return anUserIns;
    }

    public void setAnUserIns(int anUserIns) {
        this.anUserIns = anUserIns;
    }

    public Date getAdTimeChg() {
        return adTimeChg;
    }

    public void setAdTimeChg(Date adTimeChg) {
        this.adTimeChg = adTimeChg;
    }

    public int getAnUserChg() {
        return anUserChg;
    }

    public void setAnUserChg(int anUserChg) {
        this.anUserChg = anUserChg;
    }

    public String getAcNote() {
        return acNote;
    }

    public void setAcNote(String acNote) {
        this.acNote = acNote;
    }
}
