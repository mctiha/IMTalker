package com.italker;

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
import com.common.app.BaseActivity;
import com.common.widget.PortraitView;
import com.italker.fragment.ContactFragment;
import com.italker.fragment.GroupFragment;
import com.italker.fragment.HomeFragment;
import com.italker.helper.NavHelper;
import com.italker.helper.utils.ScreenUtils;

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



    private NavHelper<Integer> mNavHelper;

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

        navigation.getMenu().performIdentifierAction(R.id.action_home, 0);

    }


    @OnClick(R.id.im_search)
    public void onSearch() {

    }

    @OnClick(R.id.btn_action)
    public void onAction() {

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
            transY = ScreenUtils.dipToPx(getResources(), 76);
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
