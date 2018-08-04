package com.italker.push.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.italker.R;

/**
 * Created by RyanLee on 2017/12/7.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHolder> {
    private final String TAG = "RecyclerAdapter";
    private Context mContext;
    private String[] mDatas;
    private ViewGroup mParent;


    public RecyclerAdapter(Context mContext, String[] mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        Log.d(TAG, "onAttachedToRecyclerView");
        this.mParent = recyclerView;
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder" + " width = " + parent.getWidth());
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_gallery, parent, false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder" + "--> position = " + position);
        // 需要增加此代码修改每一页的宽高
        //GalleryAdapterHelper.newInstance().setItemLayoutParams(mParent, holder.itemView, position, getItemCount());
//        holder.mView.setImageResource(mDatas.get(position));

        Glide.with(mContext)
                .asBitmap()
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .load(mDatas[position])
                .into(holder.mView);
    }

    @Override
    public int getItemCount() {
        return mDatas.length;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        public final ImageView mView;

        public MyHolder(View itemView) {
            super(itemView);
            mView = (ImageView) itemView.findViewById(R.id.iv_photo);
        }
    }

    /**
     * 获取position位置的resId
     * @param position
     * @return
     */
    public String getResId(int position) {
        return mDatas == null ? "" : mDatas[position];
    }
}
