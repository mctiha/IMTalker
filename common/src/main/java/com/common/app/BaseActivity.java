package com.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by mth on 2017/5/23.
 */

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWidows();

        if (initArgs(getIntent().getExtras())) {
            setContentView(getLayoutId());
            initWidget();
            initData();
        } else {
            finish();
        }

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
}