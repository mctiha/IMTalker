package com.italker.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.italker.common.widget.convention.PlaceHolderView;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by mth on 2017/5/23.
 */

public abstract class BaseActivity extends AppCompatActivity {


    protected PlaceHolderView mPlaceHolderView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWidows();

        if (initArgs(getIntent().getExtras())) {
            setContentView(getLayoutId());
            initBefore();
            initWidget();
            initData();
        } else {
            finish();
        }

    }


    /**
     * 初始化控件调用之前
     */
    protected void initBefore() {

    }

    protected void initWidows() {

    }

    protected boolean initArgs(Bundle bundle) {
        return true;
    }


    protected abstract int getLayoutId();


    protected void initWidget() {
        ButterKnife.bind(this);
    }

    protected void initData() {
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

        List<Fragment> mlist = getSupportFragmentManager().getFragments();

        if (mlist != null && mlist.size() > 0) {
            for (Fragment fragment : mlist) {
                if (fragment instanceof BaseFragment) {
                    if (((BaseFragment) fragment).onBackPressed()) {
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
        finish();
    }

    /**
     * 设置占位布局
     *
     * @param placeHolderView 继承了占位布局规范的View
     */
    public void setPlaceHolderView(PlaceHolderView placeHolderView) {
        this.mPlaceHolderView = placeHolderView;
    }
}
