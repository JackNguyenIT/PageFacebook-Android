package com.jack.demopagefacebook.abstracts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Jack on 3/18/17.
 */

public abstract class AActivity extends AppCompatActivity {
    protected abstract void initData(Bundle savedInstanceState);

    protected abstract void initRootView(Bundle savedInstanceState);

    protected abstract void initUI(Bundle savedInstanceState);

    protected abstract void loadData(Bundle savedInstanceState);

    protected void initActionBar(@NonNull ActionBar actionBar) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initData(savedInstanceState);
        super.onCreate(savedInstanceState);
        initRootView(savedInstanceState);
        initUI(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(0);
            initActionBar(actionBar);
        }
        loadData(savedInstanceState);
    }


    public AActivity getContext() {
        return this;
    }
}
