package com.example.uhf.mvvm.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;





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
    private String adDateCheck;
    @ColumnInfo( defaultValue = "empty")
    private int anUserCheck;
    @ColumnInfo( defaultValue = "empty")
    private String adStringConfirm;
    @ColumnInfo( defaultValue = "empty")
    private int anUserConfirm;
    @ColumnInfo( defaultValue = "empty")
    private String adTimeIns;
    @ColumnInfo( defaultValue = "empty")
    private int anUserIns;
    @ColumnInfo( defaultValue = "empty")
    private String adTimeChg;
    @ColumnInfo( defaultValue = "empty")
    private int anUserChg;
    @ColumnInfo( defaultValue = "empty")
    private String acNote;

    @ColumnInfo( defaultValue = "empty")
    private String acCareTaker;




    public CheckOut(int anInventory, int anAssetID, String acItem, String acLocation, String acCode, String acECD, String acName, String acName2, String adDateCheck, int anUserCheck, String adStringConfirm, int anUserConfirm, String adTimeIns, int anUserIns, String adTimeChg, int anUserChg, String acNote) {
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
        this.adStringConfirm = adStringConfirm;
        this.anUserConfirm = anUserConfirm;
        this.adTimeIns = adTimeIns;
        this.anUserIns = anUserIns;
        this.adTimeChg = adTimeChg;
        this.anUserChg = anUserChg;
        this.acNote = acNote;
    }


    @Ignore
    public CheckOut(int anInventory, int anAssetID, String acItem, String acLocation, String acCode, String acECD, String acName, String acName2, String adDateCheck, int anUserCheck, String adStringConfirm, int anUserConfirm, String adTimeIns, int anUserIns, String adTimeChg, int anUserChg, String acNote, String acCareTaker) {
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
        this.adStringConfirm = adStringConfirm;
        this.anUserConfirm = anUserConfirm;
        this.adTimeIns = adTimeIns;
        this.anUserIns = anUserIns;
        this.adTimeChg = adTimeChg;
        this.anUserChg = anUserChg;
        this.acNote = acNote;
        this.acCareTaker = acCareTaker;
    }


    public String getAcCareTaker() {
        return acCareTaker;
    }

    public void setAcCareTaker(String acCareTaker) {
        this.acCareTaker = acCareTaker;
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

    public String getAdDateCheck() {
        return adDateCheck;
    }

    public void setAdDateCheck(String adDateCheck) {
        this.adDateCheck = adDateCheck;
    }

    public int getAnUserCheck() {
        return anUserCheck;
    }

    public void setAnUserCheck(int anUserCheck) {
        this.anUserCheck = anUserCheck;
    }

    public String getAdStringConfirm() {
        return adStringConfirm;
    }

    public void setAdStringConfirm(String adStringConfirm) {
        this.adStringConfirm = adStringConfirm;
    }

    public int getAnUserConfirm() {
        return anUserConfirm;
    }

    public void setAnUserConfirm(int anUserConfirm) {
        this.anUserConfirm = anUserConfirm;
    }

    public String getAdTimeIns() {
        return adTimeIns;
    }

    public void setAdTimeIns(String adTimeIns) {
        this.adTimeIns = adTimeIns;
    }

    public int getAnUserIns() {
        return anUserIns;
    }

    public void setAnUserIns(int anUserIns) {
        this.anUserIns = anUserIns;
    }

    public String getAdTimeChg() {
        return adTimeChg;
    }

    public void setAdTimeChg(String adTimeChg) {
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


    @NonNull
    @Override
    public String toString() {
        return "CheckOut{" +
                "anQId=" + anQId +
                ", anInventory=" + anInventory +
                ", anAssetID=" + anAssetID +
                ", acItem='" + acItem + '\'' +
                ", acLocation='" + acLocation + '\'' +
                ", acCode='" + acCode + '\'' +
                ", acECD='" + acECD + '\'' +
                ", acName='" + acName + '\'' +
                ", acName2='" + acName2 + '\'' +
                ", adDateCheck='" + adDateCheck + '\'' +
                ", anUserCheck=" + anUserCheck +
                ", adStringConfirm='" + adStringConfirm + '\'' +
                ", anUserConfirm=" + anUserConfirm +
                ", adTimeIns='" + adTimeIns + '\'' +
                ", anUserIns=" + anUserIns +
                ", adTimeChg='" + adTimeChg + '\'' +
                ", anUserChg=" + anUserChg +
                ", acNote='" + acNote + '\'' +
                '}';
    }
}
