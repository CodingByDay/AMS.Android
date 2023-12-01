package com.example.uhf.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ListAdapter;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

public class CustomAutoCompleteTextView extends AppCompatAutoCompleteTextView {

    public CustomAutoCompleteTextView(Context context) {
        super(context);
        initialize();
    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initialize() {
        setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDropDownIfMultipleItems();
            }
        });
    }

    private void showDropDownIfMultipleItems() {
        ListAdapter adapter = getAdapter();
        if (adapter != null && adapter.getCount() > 1) {
            showDropDown();
        } else {
            dismissDropDown();
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            showDropDownIfMultipleItems();
        }
    }
}
