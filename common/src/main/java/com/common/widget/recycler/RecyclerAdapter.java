package com.common.widget.recycler;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by mth on 2017/5/23.
 */

public abstract class RecyclerAdapter<Data> extends
        RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
        implements View.OnClickListener, View.OnLongClickListener,
        AdapterCallBack<Data> {

    private final List<Data> mDataList;
    private AdapterListener mListener;


    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(AdapterListener<Data> listener) {
        this(new ArrayList<Data>(), listener);
    }

    public RecyclerAdapter(List<Data> dataList, AdapterListener listener) {
        this.mDataList = dataList;
        this.mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position));
    }

    @LayoutRes
    protected abstract int getItemViewType(int position, Data data);

    @Override
    public ViewHolder<Data> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(viewType, parent, false);
        ViewHolder<Data> viewHolder = onCreateViewHolder(root, viewType);

        root.setTag(R.id.tag_recycler_holder, viewHolder);
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);

        viewHolder.mUnbinder = ButterKnife.bind(viewHolder, root);
        viewHolder.mCallBack = this;


        return viewHolder;
    }

    protected abstract ViewHolder<Data> onCreateViewHolder(View root, int Type);

    @Override
    public void onBindViewHolder(ViewHolder<Data> holder, int position) {
        Data data = mDataList.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void add(Data data) {
        mDataList.add(data);
        notifyItemInserted(mDataList.size() - 1);
    }

    public void add(Data... datas) {
        if (datas != null && datas.length > 0) {
            Collections.addAll(mDataList, datas);
            notifyItemRangeInserted(mDataList.size(), datas.length);
        }
    }

    public void add(Collection<Data> datas) {
        if (datas != null && datas.size() > 0) {
            mDataList.addAll(datas);
            notifyItemRangeInserted(mDataList.size(), datas.size());
        }
    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    public void replace(Collection<Data> dataList) {
        mDataList.clear();
        if (dataList == null || dataList.size() == 0) {
            return;
        }

        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }


    @Override
    public void onClick(View view) {
        ViewHolder mViewHolder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if (mDataList != null) {
            int pos = mViewHolder.getAdapterPosition();
            mListener.onItemClick(mViewHolder, mDataList.get(pos));
        }
    }

    @Override
    public boolean onLongClick(View view) {
        ViewHolder mViewHolder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if (mDataList != null) {
            int pos = mViewHolder.getAdapterPosition();
            mListener.onLongItemClick(mViewHolder, mDataList.get(pos));
            return true;
        }
        return false;
    }


    public void setListener(AdapterListener<Data> listener){
        this.mListener = listener;
    }

    public interface AdapterListener<Data> {
        void onItemClick(RecyclerAdapter.ViewHolder holder, Data data);

        void onLongItemClick(RecyclerAdapter.ViewHolder holder, Data data);
    }

    public abstract static class ViewHolder<Data> extends RecyclerView.ViewHolder {

        private Unbinder mUnbinder;
        private AdapterCallBack<Data> mCallBack;
        protected Data mData;


        public ViewHolder(View itemView) {
            super(itemView);
        }

        void bind(Data mdata) {
            this.mData = mdata;
        }

        protected abstract void onBind(Data data);

        public void updata(Data data) {
            if (mCallBack != null) {
                mCallBack.updata(data, this);
            }
        }
    }
}
