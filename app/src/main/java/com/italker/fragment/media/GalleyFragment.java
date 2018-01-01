package com.italker.fragment.media;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.common.widget.GalleyView;
import com.italker.R;
import com.common.tools.ScreenUtils;

/**
 *
 */
public class GalleyFragment extends BottomSheetDialogFragment {


    private GalleyView mGalleyView;
    private OnSelectedListener mOnSelectedListener;


    public GalleyFragment() {
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
        mGalleyView = (GalleyView) root.findViewById(R.id.view_galleyView);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGalleyView.setup(getLoaderManager(), new GalleyView.SelectedChangeListener() {
            @Override
            public void selectedChangeCount(int count) {
                if (count > 0) {
                    dismiss();
                    if (mOnSelectedListener != null) {
                        String[] paths = mGalleyView.getSelectPath();
                        mOnSelectedListener.onSelectedImage(paths[0]);
                        mOnSelectedListener = null;
                    }
                }
            }
        });
    }


    public GalleyFragment setListener(OnSelectedListener listener){
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
