package com.italker;

import android.widget.TextView;

import com.common.app.BaseActivty;

import butterknife.BindView;

public class MainActivity extends BaseActivty {


    @BindView(R.id.txt_holle)
    TextView mTextView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mTextView.setText("agrjiaregjoiar");
    }
}
