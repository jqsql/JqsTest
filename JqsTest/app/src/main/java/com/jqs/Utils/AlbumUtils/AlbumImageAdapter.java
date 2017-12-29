package com.jqs.Utils.AlbumUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.jqs.Activitys.AlbumPreviewActivity;
import com.jqs.Beans.AlbumImageBean;
import com.jqs.R;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 图片加载器 图片适配器
 */

public class AlbumImageAdapter extends BaseAdapter {
    //private static Set<String> mSelectList = new HashSet<>();
    List<AlbumImageBean> mData;
    Context mContext;
    Activity mActivity;
    LayoutInflater mInflater;
    String mDirPath;//文件夹所在路径

    public interface OnCheckedClick {
        void setCheckedPosition(int Position);
    }

    private OnCheckedClick mCheckedClick;

    public void setOnCheckedClick(OnCheckedClick mCheckedClick) {
        this.mCheckedClick = mCheckedClick;
    }

    public AlbumImageAdapter(List<AlbumImageBean> data, String dirPath, Context context) {
        mData = data;
        mContext = context;
        mActivity = (Activity) context;
        mDirPath = dirPath;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class AlbumViewHolder {
        private ImageView mImageView;
        private CheckBox mImageButton;
        private ImageView mGif;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final AlbumViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.album_gridview_item, parent, false);
            mViewHolder = new AlbumViewHolder();
            mViewHolder.mImageButton = (CheckBox) convertView.findViewById(R.id.Album_GridView_Item_ImgBtn);
            mViewHolder.mImageView = (ImageView) convertView.findViewById(R.id.Album_GridView_Item_Img);
            mViewHolder.mGif=convertView.findViewById(R.id.Album_GridView_Item_GifFlag);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (AlbumViewHolder) convertView.getTag();
        }
        final AlbumImageBean bean = mData.get(position);

        //重置状态
        mViewHolder.mImageView.setImageBitmap(null);
        mViewHolder.mImageView.setBackgroundColor(Color.parseColor("#F1F2F6"));
        mViewHolder.mImageView.setColorFilter(null);
        mViewHolder.mImageButton.setChecked(false);
        mViewHolder.mGif.setVisibility(View.GONE);
        for (AlbumImageBean al : AlbumSetting.CheckedBitmapList) {
            if (al.getImageName().equals(bean.getImageName())) {//已经被选择
                mViewHolder.mImageView.setColorFilter(Color.parseColor("#77000000"));
                mViewHolder.mImageButton.setChecked(true);
            }
        }
        if(bean.getType()==1){
            mViewHolder.mGif.setVisibility(View.VISIBLE);
        }
        ImageLoader.getInstance().loadImage(bean.getImageName(), mViewHolder.mImageView);

        //选中
        mViewHolder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewHolder.mImageButton.setChecked(false);//重置
                if (AlbumSetting.CheckedBitmapList.contains(bean)) {//已经被选择
                    AlbumSetting.CheckedBitmapList.remove(bean);
                    mViewHolder.mImageView.setColorFilter(null);
                    mViewHolder.mImageButton.setChecked(false);
                } else {//未被选择
                    if (AlbumSetting.CheckedBitmapList.size() < (AlbumSetting.MaxNumber -
                            AlbumSetting.CheckedSureBitmapList.size())) {//小于剩余MaxNumber张可以选择
                        AlbumSetting.CheckedBitmapList.add(bean);
                        mViewHolder.mImageView.setColorFilter(Color.parseColor("#77000000"));
                        mViewHolder.mImageButton.setChecked(true);
                    } else {
                        Toast.makeText(mContext,
                                "最多选" + AlbumSetting.MaxNumber + "张", Toast.LENGTH_SHORT).show();
                    }
                }
                mCheckedClick.setCheckedPosition(AlbumSetting.CheckedBitmapList.size());
            }
        });
        //预览
        mViewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AlbumPreviewActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("data", (Serializable) mData);
                mActivity.startActivityForResult(intent, 1);
            }
        });

        return convertView;
    }
}
