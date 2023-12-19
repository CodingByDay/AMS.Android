package com.example.uhf.api;

public class SyncError {
    private int index;
    private int qid;
    private String message;

    public SyncError(int index, int qid, String message) {
        this.index = index;
        this.qid = qid;
        this.message = message;
    }

    public int getIndex() {
        return index;
    }

    public int getQid() {
        return qid;
    }

    public String getMessage() {
        return message;
    }
}
