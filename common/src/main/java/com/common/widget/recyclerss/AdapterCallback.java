package com.common.widget.recyclerss;

/**
 * Created by mth on 2017/12/31.
 */

interface AdapterCallback<Data> {
    void update(Data data, RecyclerAdapter.ViewHolder<Data> viewHolder);
}
