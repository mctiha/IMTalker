package com.italker.activities;

import android.content.Context;
import android.content.Intent;

import com.common.app.BaseActivity;
import com.italker.R;
import com.italker.fragment.account.UpdateInfoFragment;

/**
 * @author mth
 */
public class AccountActivity extends BaseActivity {


    private UpdateInfoFragment mUpdateInfoFragment;

    public static void show(Context mContext) {
        mContext.startActivity(new Intent(mContext, AccountActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mUpdateInfoFragment = new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mUpdateInfoFragment)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mUpdateInfoFragment.onActivityResult(requestCode, resultCode, data);
    }
}
