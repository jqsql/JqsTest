package com.jqs.Utils.AlbumUtils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jqs.Beans.FolderBean;
import com.jqs.R;

import java.util.List;

/**
 * 相册弹出框
 */

public class AlbumPopupWindow extends PopupWindow{
    private int mWidth;
    private int mHeight;
    private View mConverView;
    private ListView mListView;
    private List<FolderBean> mDatas;
    private DirAdapter mAdapter;

    public interface OnDirSelectedListener{
        void onSelect(FolderBean folderBean);
    }
    public OnDirSelectedListener mListener;
    public void setDirListener(OnDirSelectedListener listener){
        mListener=listener;
    }


    public AlbumPopupWindow(Context context,List<FolderBean> datas) {
        calWidthAndHeight(context);
        mConverView= LayoutInflater.from(context).inflate(R.layout.album_popup_view,null);
        mDatas=datas;

        setContentView(mConverView);
        setWidth(mWidth);
        setHeight(mHeight);

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        initView(context);
        initListen();
    }

    /**
     * 初始化
     */
    private void initView(Context context) {
        mListView= (ListView) mConverView.findViewById(R.id.Album_Popup_ListView);
        mAdapter=new DirAdapter(context,mDatas);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 事件监听
     */
    private void initListen() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (FolderBean a:mDatas) {
                    a.setChecked(false);
                }
                FolderBean bean=mDatas.get(position);
                bean.setChecked(true);
                mAdapter.notifyDataSetChanged();
                if(mListener!=null){
                    mListener.onSelect(bean);
                }
            }
        });
    }

    /**
     * 计算popup宽高
     */
    private void calWidthAndHeight(Context context) {
        WindowManager wm= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        mWidth=outMetrics.widthPixels;
        mHeight= (int) (outMetrics.heightPixels*0.7);

    }


    private class DirAdapter extends ArrayAdapter<FolderBean> {
        private LayoutInflater mInflater;
        public DirAdapter(Context context,List<FolderBean> objects) {
            super(context, 0,objects);
            mInflater=LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder mViewHolder;
            if(convertView==null){
                mViewHolder=new ViewHolder();
                convertView=mInflater.inflate(R.layout.album_popup_item,null);
                mViewHolder.mImageView= (ImageView) convertView.findViewById(R.id.Album_Popup_Item_Img);
                mViewHolder.mName= (TextView) convertView.findViewById(R.id.Album_Popup_Item_Name);
                mViewHolder.mCount= (TextView) convertView.findViewById(R.id.Album_Popup_Item_Count);
                mViewHolder.mCheck= (ImageView) convertView.findViewById(R.id.Album_Popup_Item_CheckBox);
                convertView.setTag(mViewHolder);
            }else {
                mViewHolder= (ViewHolder) convertView.getTag();
            }
            FolderBean bean=getItem(position);
            ImageLoader.getInstance().loadImage(bean.getFiestImgPath(),mViewHolder.mImageView);
            mViewHolder.mName.setText(bean.getName()+"");
            mViewHolder.mCount.setText(bean.getCount()+"张");
            if(bean.isChecked()) {
                mViewHolder.mCheck.setVisibility(View.VISIBLE);
            }else {
                mViewHolder.mCheck.setVisibility(View.GONE);
            }
            return convertView;
        }

        private class ViewHolder{
            ImageView mImageView;
            TextView mName;
            TextView mCount;
            ImageView mCheck;
        }

    }

}
