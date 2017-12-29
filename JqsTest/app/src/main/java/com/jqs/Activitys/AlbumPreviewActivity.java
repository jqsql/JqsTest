package com.jqs.Activitys;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jqs.Beans.AlbumImageBean;
import com.jqs.R;
import com.jqs.Utils.AlbumUtils.AlbumSetting;
import com.jqs.Utils.AlbumUtils.PhotoPagerAdapter;
import com.jqs.Utils.AlbumUtils.PhotoView;
import com.jqs.Utils.AlbumUtils.ViewPagerFixed;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片预览界面
 * by jqs
 */
public class AlbumPreviewActivity extends AppCompatActivity {
    //控件
    private ImageView mReturn;//返回
    private TextView mNowPosition;//当前位置
    private TextView mMaxPosition;//总张数
    private Button mOk;//完成
    private ViewPagerFixed mViewPager;
    private CheckBox mCheckBox;//选择
    private PhotoPagerAdapter mPagerAdapter;

    ArrayList<View> ViewDataList;
    private List<AlbumImageBean> mData;//图片集合
    private int mPosition;//位置
    private AlbumImageBean bean;//当前对象
    int mMaxNumber;//当前可以选择的张数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_preview);
        mMaxNumber= AlbumSetting.MaxNumber-AlbumSetting.CheckedSureBitmapList.size();
        initView();
        initData();
        initListen();

    }

    /**
     * 初始化
     */
    private void initView() {
        mReturn = (ImageView) findViewById(R.id.Album_Preview_Return);
        mNowPosition = (TextView) findViewById(R.id.Album_Preview_NowPosition);
        mMaxPosition = (TextView) findViewById(R.id.Album_Preview_MaxPosition);
        mOk = (Button) findViewById(R.id.Album_Preview_Ok);
        mViewPager = (ViewPagerFixed) findViewById(R.id.Album_Preview_ViewPager);
        mCheckBox = (CheckBox) findViewById(R.id.Album_Preview_Checked);
    }

    /**
     * 数据处理
     */
    private void initData() {
        Intent intent = getIntent();
        mData = (List<AlbumImageBean>) intent.getSerializableExtra("data");
        mPosition = intent.getIntExtra("position", 0);

        if(AlbumSetting.CheckedBitmapList.size()!=0) {
            mOk.setEnabled(true);
            mOk.setBackgroundColor(Color.parseColor("#0e932e"));
            mOk.setText("完成(" + AlbumSetting.CheckedBitmapList.size() + "/"+mMaxNumber+")");
        }else {
            mOk.setEnabled(false);
            mOk.setBackgroundColor(Color.parseColor("#21532d"));
            mOk.setText("完成");
        }

        ViewDataList = new ArrayList<>();
        if (mData != null) {
            mNowPosition.setText((mPosition + 1) + "");
            mMaxPosition.setText("/"+mData.size());
            for (int i = 0; i < mData.size(); i++) {
                PhotoView img = new PhotoView(this);
                ViewDataList.add(img);
            }
            bean=mData.get(mPosition);
            for (AlbumImageBean al:AlbumSetting.CheckedBitmapList) {
                if(bean.getImageName().equals(al.getImageName())){//已经被选择
                    mCheckBox.setChecked(true);
                }
            }
        }

        mPagerAdapter = new PhotoPagerAdapter(this,ViewDataList,mData);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(mPosition);

    }

    /**
     * 事件监听
     */
    private void initListen() {
        /**
         * 返回
         */
        mReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /**
        * 滑动事件
        */
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mNowPosition.setText(position + 1 + "");
                mPosition=position;
                bean=mData.get(mPosition);
                mCheckBox.setChecked(false);
                for (AlbumImageBean al:AlbumSetting.CheckedBitmapList) {
                    if(bean.getImageName().equals(al.getImageName())){//已经被选择
                        mCheckBox.setChecked(true);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /**
         * 选中点击事件
         */
        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bean!=null) {
                    if (!mCheckBox.isChecked()) {
                        for (AlbumImageBean al:AlbumSetting.CheckedBitmapList) {
                            if(bean.getImageName().equals(al.getImageName()))
                            AlbumSetting.CheckedBitmapList.remove(al);
                        }
                    } else {
                        if (AlbumSetting.CheckedBitmapList.size() < mMaxNumber) {//小于剩余MaxNumber张可以选择
                            bean.setBitmap(null);
                            AlbumSetting.CheckedBitmapList.add(bean);
                        } else {
                            mCheckBox.setChecked(false);
                            Toast.makeText(AlbumPreviewActivity.this,
                                    "最多选" + AlbumSetting.MaxNumber + "张", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(AlbumSetting.CheckedBitmapList.size()!=0) {
                        mOk.setEnabled(true);
                        mOk.setBackgroundColor(Color.parseColor("#0e932e"));
                        mOk.setText("完成(" + AlbumSetting.CheckedBitmapList.size() + "/"+mMaxNumber+")");
                    }else {
                        mOk.setEnabled(false);
                        mOk.setBackgroundColor(Color.parseColor("#21532d"));
                        mOk.setText("完成");
                    }
                }
            }
        });
        /**
         * 完成
         */
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumSetting.clearActivity();
                finish();
            }
        });
    }


}
