package com.italker.push.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.italker.common.widget.recycler_gallery.AnimManager;
import com.italker.common.widget.recycler_gallery.GalleryRecyclerView;
import com.italker.R;
import com.italker.push.helper.RecyclerAdapter;

import java.util.HashMap;
import java.util.Map;

public class GalleryActivity extends AppCompatActivity implements GalleryRecyclerView.OnItemClickListener {

    private GalleryRecyclerView mRecyclerView;
    private RelativeLayout mContainer;

    private Map<String, Drawable> mTSDraCacheMap = new HashMap<>();
    private static final String KEY_PRE_DRAW = "key_pre_draw";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mRecyclerView = (GalleryRecyclerView) findViewById(R.id.rv_list);
        mContainer = (RelativeLayout) findViewById(R.id.rl_container);

        String[] paths = getIntent().getStringArrayExtra("paths");

        final RecyclerAdapter adapter = new RecyclerAdapter(getApplicationContext(), paths);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.initFlingSpeed(9000)                                   // 设置滑动速度（像素/s）
                .initPageParams(0, 60)     // 设置页边距和左右图片的可见宽度，单位dp
                .setAnimFactor(0.15f)                                   // 设置切换动画的参数因子
                .setAnimType(AnimManager.ANIM_BOTTOM_TO_TOP)            // 设置切换动画类型，目前有AnimManager.ANIM_BOTTOM_TO_TOP和目前有AnimManager.ANIM_TOP_TO_BOTTOM
                .setOnItemClickListener(this);                          // 设置点击事件


        // 背景高斯模糊 & 淡入淡出
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    setBlurImage();
                }
            }
        });
        setBlurImage();
    }

    /**
     * 设置背景高斯模糊
     */
    public void setBlurImage() {
        RecyclerAdapter adapter = (RecyclerAdapter) mRecyclerView.getAdapter();

        if (adapter == null || mRecyclerView == null) {
            return;
        }


//        Glide.with(this).load(((RecyclerAdapter) mRecyclerView.getAdapter()).getResId(mRecyclerView.getScrolledPosition()))
//                .asBitmap()
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        // 将该Bitmap高斯模糊后返回到resBlurBmp
//                        Bitmap resBlurBmp = BlurBitmapUtil.blurBitmap(mRecyclerView.getContext(), resource, 15f);
//                        // 再将resBlurBmp转为Drawable
//                        Drawable resBlurDrawable = new BitmapDrawable(resBlurBmp);
//                        // 获取前一页的Drawable
//                        Drawable preBlurDrawable = mTSDraCacheMap.get(KEY_PRE_DRAW) == null ? resBlurDrawable : mTSDraCacheMap.get(KEY_PRE_DRAW);
//
//                        /* 以下为淡入淡出效果 */
//                        Drawable[] drawableArr = {preBlurDrawable, resBlurDrawable};
//                        TransitionDrawable transitionDrawable = new TransitionDrawable(drawableArr);
//                        mContainer.setBackgroundDrawable(transitionDrawable);
//                        transitionDrawable.startTransition(500);
//
//                        // 存入到cache中
//                        mTSDraCacheMap.put(KEY_PRE_DRAW, resBlurDrawable);
//                    }
//                });

//        mRecyclerView.post(new Runnable() {
//            @Override
//            public void run() {
//                // 获取当前位置的图片资源ID
//                int resourceId = ((RecyclerAdapter) mRecyclerView.getAdapter()).getResId(mRecyclerView.getScrolledPosition());
//                // 将该资源图片转为Bitmap
//                Bitmap resBmp = BitmapFactory.decodeResource(getResources(), resourceId);
//                // 将该Bitmap高斯模糊后返回到resBlurBmp
//                Bitmap resBlurBmp = BlurBitmapUtil.blurBitmap(mRecyclerView.getContext(), resBmp, 15f);
//                // 再将resBlurBmp转为Drawable
//                Drawable resBlurDrawable = new BitmapDrawable(resBlurBmp);
//                // 获取前一页的Drawable
//                Drawable preBlurDrawable = mTSDraCacheMap.get(KEY_PRE_DRAW) == null ? resBlurDrawable : mTSDraCacheMap.get(KEY_PRE_DRAW);
//
//                /* 以下为淡入淡出效果 */
//                Drawable[] drawableArr = {preBlurDrawable, resBlurDrawable};
//                TransitionDrawable transitionDrawable = new TransitionDrawable(drawableArr);
//                mContainer.setBackgroundDrawable(transitionDrawable);
//                transitionDrawable.startTransition(500);
//
//                // 存入到cache中
//                mTSDraCacheMap.put(KEY_PRE_DRAW, resBlurDrawable);
//            }
//        });
    }




    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getApplicationContext(), "position=" + position, Toast.LENGTH_SHORT).show();
    }

}
