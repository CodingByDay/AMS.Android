package com.example.uhf.barcode;

import android.content.Context;

import com.rscja.barcode.BarcodeDecoder;
import com.rscja.barcode.BarcodeFactory;
import com.rscja.deviceapi.entity.BarcodeEntity;

public class BarcodeUtility {
    private final Barcode barcodeInterface;
    BarcodeDecoder barcodeDecoder;
    Context context;
    public BarcodeUtility(Barcode barcodeInterface, Context context) {
        this.barcodeInterface = barcodeInterface;
        barcodeDecoder = BarcodeFactory.getInstance().getBarcodeDecoder();
        barcodeDecoder.open(context);
        barcodeDecoder.setDecodeCallback(new BarcodeDecoder.DecodeCallback() {
            @Override
            public void onDecodeComplete(BarcodeEntity barcodeEntity) {
                barcodeInterface.getResult(barcodeEntity.getBarcodeData());
            }
        });
    }




}
