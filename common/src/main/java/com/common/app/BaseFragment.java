package com.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by mth on 2017/5/23.
 */

public abstract class BaseFragment extends Fragment {

    private View mView;
    protected Unbinder mUnbinder;

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
        initData();
    }

    protected abstract int getLayoutId();



    protected void initWidget(View view) {
        mUnbinder =  ButterKnife.bind(this, view);
    }

    protected void initData() {

    }

    /**
     *  trur 拦截
     *  false 不拦截
     * @return
     */
    public boolean onBackPressed() {


        return false;
    }
}
