package com.italker.common.widget.recycler;

/**
 * Created by mth on 2017/5/23.
 */

public interface AdapterCallback<Data> {
    void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);
}
