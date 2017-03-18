package com.jack.demopagefacebook.abstracts;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jack on 3/18/17.
 */

public abstract class AViewHolder extends RecyclerView.ViewHolder {
    private final RecyclerView.Adapter<? extends AViewHolder> adapter;
    private final Context context;

    public AViewHolder(RecyclerView.Adapter<? extends AViewHolder> adapter, View itemView) {
        super(itemView);
        this.adapter = adapter;
        this.context = itemView.getContext();
        initUI();
    }

    public AViewHolder(RecyclerView.Adapter<? extends AViewHolder> adapter, ViewGroup parent, @LayoutRes int layout) {
        this(adapter, LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    public RecyclerView.Adapter<? extends AViewHolder> getAdapter() {
        return adapter;
    }

    public final Context getContext() {
        return context;
    }

    protected abstract void initUI();
}
