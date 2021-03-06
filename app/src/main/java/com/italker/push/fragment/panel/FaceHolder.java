package com.italker.push.fragment.panel;


import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.italker.R;
import com.italker.common.face.Face;
import com.italker.common.widget.recycler.RecyclerAdapter;

import butterknife.BindView;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class FaceHolder extends RecyclerAdapter.ViewHolder<Face.Bean> {

    @BindView(R.id.im_face)
    ImageView mFace;

    public FaceHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(Face.Bean bean) {
        if (bean != null
                // drawable 资源 id
                && ((bean.preview instanceof Integer)
                // face zip 包资源路径
                || bean.preview instanceof String)) {
            Glide.with(itemView.getContext())
                    .asBitmap()
                    .load(bean.preview)
                    .apply(new RequestOptions()
                            .format(DecodeFormat.PREFER_ARGB_8888) //设置解码格式8888，保证清晰度
                            .placeholder(R.drawable.default_face))
                    .into(mFace);
        }
    }
}
