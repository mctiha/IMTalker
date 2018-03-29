package com.italker.push.fragment.media;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.italker.R;
import com.italker.common.tools.ScreenUtils;
import com.italker.common.widget.GalleryView;
import com.italker.push.activities.GalleryActivity;

/**
 *
 */
public class GalleryFragment extends BottomSheetDialogFragment {


    private GalleryView mGalleyView;
    private OnSelectedListener mOnSelectedListener;



    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TransStatusBottomSheetDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_galley, null);
        mGalleyView = (GalleryView) root.findViewById(R.id.view_galleyView);

        return root;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            String[] pahts = mGalleyView.getSelectedPath();
            if (pahts != null && pahts.length > 0) {
                startActivity(new Intent(getActivity(), GalleryActivity.class).putExtra("paths", pahts));
                dismiss();
            } else {
                Toast.makeText(getActivity(), "请选择～", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGalleyView.setup(getLoaderManager(), new GalleryView.SelectedChangeListener() {

            @Override
            public void onSelectedCountChanged(int count) {
                if (count > 0) {
                    dismiss();
                    if (mOnSelectedListener != null) {
                        String[] paths = mGalleyView.getSelectedPath();
                        mOnSelectedListener.onSelectedImage(paths[0]);
                        mOnSelectedListener = null;
                    }
                }
            }
        });
    }


    public GalleryFragment setListener(OnSelectedListener listener){
        mOnSelectedListener = listener;
        return this;
    }

    public interface OnSelectedListener {
        void onSelectedImage(String path);
    }



    public static class TransStatusBottomSheetDialog extends BottomSheetDialog {
        public TransStatusBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public TransStatusBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
            super(context, theme);
        }

        protected TransStatusBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            final Window window = getWindow();
            if (window == null) {
                return;
            }


            // 得到屏幕高度
            int screenHeight = ScreenUtils.getScreenHeight(getOwnerActivity());
            // 得到状态栏的高度
            int statusHeight = ScreenUtils.getStatusBarHeight(getOwnerActivity());

            // 计算dialog的高度并设置
            int dialogHeight = screenHeight - statusHeight;
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    dialogHeight <= 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);

        }
    }
}
