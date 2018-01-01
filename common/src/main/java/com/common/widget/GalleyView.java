package com.common.widget;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.common.R;
import com.common.widget.recyclerss.RecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mth on 2017/12/30.
 */

public class GalleyView extends RecyclerView {

    private static final int MIN_IMAGE_FILE_SIZE = 10 * 1024;
    private static int Loader_ID = 0x0100;
    private static int MAX_IMAGE_COUNT = 3;
    private LoaderCallback mLoaderCallback = new LoaderCallback();
    private GalleyAdapter mAdapter = new GalleyAdapter();
    private List<Image> mSelectList = new LinkedList<>();
    private SelectedChangeListener mListener;

    public GalleyView(Context context) {
        super(context);
        init();
    }

    public GalleyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(), 4));
        setAdapter(mAdapter);
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Image>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Image image) {
                if (onItenSelectClick(image)) {
                    //noinspection unchecked
                    holder.updateData(image);
                }
            }
        });
    }

    public int setup(LoaderManager loaderManager, SelectedChangeListener listener) {
        mListener = listener;
        loaderManager.initLoader(Loader_ID, null, mLoaderCallback);
        return Loader_ID;
    }

    private boolean onItenSelectClick(Image image) {
        boolean notifyRefrech;
        if (mSelectList.contains(image)) {
            mSelectList.remove(image);
            image.isSelect = false;
            notifyRefrech = true;
        } else {
            if (mSelectList.size() >= MAX_IMAGE_COUNT) {
                String str = getResources().getString(R.string.label_gallery_select_max_size, MAX_IMAGE_COUNT);
                Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                notifyRefrech = false;
            } else {
                mSelectList.add(image);
                image.isSelect = true;
                notifyRefrech = true;
            }
        }
        if (notifyRefrech) {
            notifySelectChange();
        }

        return notifyRefrech;
    }

    private void notifySelectChange() {

        SelectedChangeListener listener = mListener;
        if (listener != null) {
            listener.selectedChangeCount(mSelectList.size());
        }

    }

    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {

        private final String[] IMAGE_PROJECTION = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_ADDED
        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == Loader_ID) {
                return new CursorLoader(getContext(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION, null, null, MediaStore.Images.Media.DATE_ADDED + " DESC");
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            List<Image> images = new ArrayList<>();

            if (data != null) {
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();

                    int indexId = data.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                    int indexPath = data.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    int indexDate = data.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);

                    do {
                        int id = data.getInt(indexId);
                        String path = data.getString(indexPath);
                        long date = data.getLong(indexDate);

                        File file = new File(path);
                        if (!file.exists() || file.length() < MIN_IMAGE_FILE_SIZE) {
                            // 如果没有图片，或者图片大小太小，则跳过
                            continue;
                        }

                        Image image = new Image();
                        image.id = id;
                        image.path = path;
                        image.date = date;
                        images.add(image);
                    } while (data.moveToNext());
                }
            }

            UpdateSource(images);
        }



        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            UpdateSource(null);
        }
    }

    private void UpdateSource(List<Image> images) {

        mAdapter.replace(images);

    }


    public String[] getSelectPath() {

        String[] paths = new String[mSelectList.size()];
        int index = 0;
        for (Image im : mSelectList) {
            paths[index++] = im.path;
        }
        return paths;
    }

    public void Clear(){
        for (Image image : mSelectList) {
            image.isSelect = false;
        }
        mSelectList.clear();
        mAdapter.notifyDataSetChanged();
    }

    private static class Image {

        int id;
        String path;
        long date;
        boolean isSelect;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Image image = (Image) o;

            return path != null ? path.equals(image.path) : image.path == null;

        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }

    private class GalleyAdapter extends RecyclerAdapter<Image> {

        @Override
        protected int getItemViewType(int position, Image image) {
            return R.layout.call_galley;
        }

        @Override
        protected ViewHolder<Image> onCreateViewHolder(View root, int Type) {
            return new GalleyViewholder(root);
        }
    }

    private class GalleyViewholder extends RecyclerAdapter.ViewHolder<Image> {

        ImageView mImageView;
        View shade;
        CheckBox mCheckBox;

        public GalleyViewholder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.im_image);
            shade = itemView.findViewById(R.id.view_shade);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.cb_select);
        }

        @Override
        protected void onBind(Image image) {

            Glide.with(getContext()).load(image.path)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .placeholder(R.color.grey_200)
                    .into(mImageView);

            shade.setVisibility(image.isSelect ? VISIBLE : INVISIBLE);
            mCheckBox.setChecked(image.isSelect);
            mCheckBox.setVisibility(VISIBLE);
        }
    }

    public interface SelectedChangeListener{
        void selectedChangeCount(int count);
    }
}
