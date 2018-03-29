package com.italker.push.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.italker.R;
import com.italker.common.app.BaseActivity;
import com.italker.common.tools.ScreenUtils;
import com.italker.common.widget.PortraitView;
import com.italker.factory.persistence.Account;
import com.italker.push.fragment.main.ContactFragment;
import com.italker.push.fragment.main.GroupFragment;
import com.italker.push.fragment.main.HomeFragment;
import com.italker.push.helper.NavHelper;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author mth
 */
public class MainActivity extends BaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnTabChangedListener<Integer> {


    @BindView(R.id.im_portrait)
    PortraitView imPortrait;

    @BindView(R.id.tex_title)
    TextView texTitle;

    @BindView(R.id.lay_container)
    FrameLayout layContainer;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @BindView(R.id.activity_main)
    CoordinatorLayout activityMain;

    @BindView(R.id.appbar)
    AppBarLayout appbar;

    @BindView(R.id.im_search)
    ImageView imSearch;

    @BindView(R.id.btn_action)
    FloatingActionButton btnAction;


    public static void show(Context context){
        context.startActivity(new Intent(context, MainActivity.class));
    }


    private NavHelper<Integer> mNavHelper;

    @Override
    protected boolean initArgs(Bundle bundle) {
        if (Account.isComplete()) {
            // 判断用户信息是否完全，完全则走正常流程
            return super.initArgs(bundle);
        } else {
            UserActivity.show(this);
            return false;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mNavHelper = new NavHelper<>(this, R.id.lay_container, getSupportFragmentManager(), this);

        mNavHelper.add(R.id.action_home, new NavHelper.Tab<Integer>(HomeFragment.class, R.string.title_home))
                .add(R.id.action_group, new NavHelper.Tab<Integer>(GroupFragment.class, R.string.title_group))
                .add(R.id.action_contact, new NavHelper.Tab<Integer>(ContactFragment.class, R.string.title_contact));

        navigation.setOnNavigationItemSelectedListener(this);

        Glide.with(this)
                .load(R.drawable.bg_src_morning)
                .centerCrop()
                .into(new ViewTarget<View, GlideDrawable>(appbar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });
    }

    @Override
    protected void initData() {
        super.initData();
        // 从底部导中接管我们的Menu，然后进行手动的触发第一次点击
        navigation.getMenu().performIdentifierAction(R.id.action_home, 0);


        imPortrait.setup(Glide.with(this), Account.getUser());
    }

    @OnClick(R.id.im_portrait)
    public void onPortrait(){
        PersonalActivity.show(this, Account.getUserId());
    }

    @OnClick(R.id.im_search)
    void onSearchMenuClick() {
        // 在群的界面的时候，点击顶部的搜索就进入群搜索界面
        // 其他都为人搜索的界面
        int type = Objects.equals(mNavHelper.getCurrentTab().extra, R.string.title_group) ?
                SearchActivity.TYPE_GROUP : SearchActivity.TYPE_USER;
        SearchActivity.show(this, type);
    }

    @OnClick(R.id.btn_action)
    void onActionClick() {
        // 浮动按钮点击时，判断当前界面是群还是联系人界面
        // 如果是群，则打开群创建的界面
        if (Objects.equals(mNavHelper.getCurrentTab().extra, R.string.title_group)) {
            // 打开群创建界面
            GroupCreateActivity.show(MainActivity.this);
        } else {
            // 如果是其他，都打开添加用户的界面
            SearchActivity.show(this, SearchActivity.TYPE_USER);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        return mNavHelper.performClickMenu(item.getItemId());
    }

    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        texTitle.setText(newTab.extra);

        float transY = 0;
        float rotation = 0;

        if (Objects.equals(newTab.extra, R.string.title_home)) {
            transY = ScreenUtils.dipToPx(76);
        } else {
            if (Objects.equals(newTab.extra, R.string.title_group)) {
                btnAction.setImageResource(R.drawable.ic_group_add);
                rotation = -360;
            } else if (Objects.equals(newTab.extra, R.string.title_contact)) {
                btnAction.setImageResource(R.drawable.ic_contact_add);
                rotation = 360;
            }
        }

        btnAction.animate().translationY(transY).rotation(rotation).setDuration(480)
                .setInterpolator(new AnticipateOvershootInterpolator(1))
                .start();
    }

}
