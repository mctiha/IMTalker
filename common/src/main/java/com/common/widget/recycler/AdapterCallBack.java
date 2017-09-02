package com.common.widget.recycler;

/**
 * Created by mth on 2017/5/23.
 */

public interface AdapterCallBack<Data> {
    void updata(Data data, RecyclerAdapter.ViewHolder holder);
}
