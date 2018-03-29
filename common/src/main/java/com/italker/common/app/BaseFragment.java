package com.italker.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.italker.common.widget.convention.PlaceHolderView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 父类Fragment
 * Created by mth on 2017/5/23.
 */

public abstract class BaseFragment extends Fragment {

    private View mView;
    protected Unbinder mUnbinder;
    protected PlaceHolderView mPlaceHolderView;
    // 标示是否第一次初始化数据
    protected boolean mIsFirstInitData = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(getLayoutId(), container, false);
            initWidget(mView);
        }else{
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
        }
        return mView;

    }



    protected void initArgs(Bundle bundle){};


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mIsFirstInitData) {
            // 触发一次以后就不会触发
            mIsFirstInitData = false;
            // 触发
            onFirstInit();
        }

        // 当View创建完成后初始化数据
        initData();
    }

    protected abstract int getLayoutId();

    protected void initWidget(View view) {
        mUnbinder =  ButterKnife.bind(this, view);
    }

    protected void initData() {}

    /**
     * 当首次初始化数据的时候会调用的方法
     */
    protected void onFirstInit() {}

    /**
     *  trur 拦截
     *  false 不拦截
     * @return
     */
    public boolean onBackPressed() {
        return false;
    }



    public void setPlaceHolderView(PlaceHolderView placeHolderView) {
        mPlaceHolderView = placeHolderView;
    }
}
