package com.jqs.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jqs.Beans.AlbumImageBean;
import com.jqs.Beans.RxBusModle;
import com.jqs.R;
import com.jqs.Utils.ALog;
import com.jqs.Utils.APP_Property;
import com.jqs.Utils.AlbumUtils.AlbumSetting;
import com.jqs.Utils.App_Utils;
import com.jqs.Utils.BaseActivityUtils.BaseActivity;
import com.jqs.Utils.BaseRecyclerAdapterUtils.BaseItemDecoration;
import com.jqs.Utils.BaseRecyclerAdapterUtils.BaseRecyclerAdapter;
import com.jqs.Utils.BaseRecyclerAdapterUtils.BaseTypeRecyclerAdapter;
import com.jqs.Utils.BaseRecyclerAdapterUtils.BaseViewHolder;
import com.jqs.Utils.BaseRecyclerAdapterUtils.interfaces.OnRecycleItemClickListener;
import com.jqs.Utils.BaseRecyclerAdapterUtils.util.ManagerType;
import com.jqs.Utils.RxJavaUtils.RxBus;
import com.jqs.Utils.TimeUtils;
import com.jqs.greendao.gen.CircleBean;
import com.jqs.greendao.gen.CircleBeanDao;
import com.jqs.greendao.gen.GreenDaoManager;
import com.jqs.greendao.gen.UserInfoBean;

import java.util.ArrayList;
import java.util.List;

public class PublishCircleActivity extends BaseActivity {
	private static final int AlbumCode=1000;//相册请求码
	private TextView mPublish;//发布
	private TextView mTitle;//标题
	private ImageView mReturn;//返回
	private EditText mContent;//内容
	private RecyclerView mRecyclerView;//photo选择器
	//工具部分
	private BaseTypeRecyclerAdapter mSimpleAdapter;//测试适配器
	//数据部分
	private List<AlbumImageBean> mImageBeanList=new ArrayList<>();
	private List<String> mPhotos=new ArrayList<>();

	private CircleBeanDao mBeanDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_circle);
	    initView();
	    initData();
	    initListen();

    }

	/**
	*初始化
	*/
	private void initView(){
		mPublish= (TextView) findViewById(R.id.Top_Bar_Operate);
		mTitle= (TextView) findViewById(R.id.Top_Bar_Title);
		mReturn= (ImageView) findViewById(R.id.Top_Bar_ReBack);
		mContent= (EditText) findViewById(R.id.Publish_Circle_Content);
		mRecyclerView= (RecyclerView) findViewById(R.id.Publish_Circle_Photos);
		mBeanDao= GreenDaoManager.getInstance(this).getDaoSession().getCircleBeanDao();

		GridLayoutManager manager=new GridLayoutManager(this,4);
		mRecyclerView.setLayoutManager(manager);
		mRecyclerView.addItemDecoration(new BaseItemDecoration(this,5, ManagerType.GRID,4));
		//区分布局类型
		BaseTypeRecyclerAdapter.TypeViewSupport<AlbumImageBean> typeViewSupport=
				new BaseTypeRecyclerAdapter.TypeViewSupport<AlbumImageBean>() {
			@Override
			public int getLayoutId(int itemType) {
				if(itemType==1)
					return R.layout.publish_addcircle_item;
				return R.layout.publish_circle_item;
			}

			@Override
			public int getItemViewType(int position, AlbumImageBean albumImageBean) {
				if(mImageBeanList==null)
					return 1;
				return albumImageBean == null && position==mImageBeanList.size()-1 ? 1 :0;
			}
		};
		mImageBeanList.add(null);
		mSimpleAdapter=new BaseTypeRecyclerAdapter<AlbumImageBean>(this,mImageBeanList,typeViewSupport) {
			@Override
			protected void convert(BaseViewHolder holder, AlbumImageBean albumImageBean) {
				if(albumImageBean!=null) {
					holder.setImageBitmap(R.id.Publish_Circle_PhotoItem,albumImageBean.getBitmap());
					//holder.setPicassoLocalImage(R.id.Publish_Circle_PhotoItem, albumImageBean.getImageName());
				}
			}
		};
		mRecyclerView.setAdapter(mSimpleAdapter);
	}
	/**
	*数据处理
	*/
	private void initData(){
		mTitle.setText("发布动态");
	}
	/**
	*事件监听
	*/
	private void initListen(){
		//返回
		mReturn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		//发布按钮
		mPublish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				CircleBean bean=null;
				if(mPhotos!=null && mPhotos.size()>0){
					//带图
					bean=new CircleBean(TimeUtils.getNowTimeStr(),mContent.getText().toString(),
							1,mPhotos,null,null,6,4,false,APP_Property.user);
				}else {
					//纯文字
					bean=new CircleBean(TimeUtils.getNowTimeStr(),mContent.getText().toString(),
							0,null,null,null,6,4,false,APP_Property.user);
				}
				mBeanDao.insert(bean);
				RxBus.getDefault().post(new RxBusModle("PublishCircle",bean));
				finish();
			}
		});
		//预览点击
		mSimpleAdapter.setItemClickListener(new OnRecycleItemClickListener() {
			@Override
			public void onClick(BaseViewHolder holder, Object o, int position) {
				if(holder.getItemViewType()==1){
					//添加布局
					Intent intent=new Intent(PublishCircleActivity.this, AlbumForAll_Activity.class);
					startActivityForResult(intent,AlbumCode);
				}else {

				}
			}
		});
	}

	/**
	 * 界面销毁时，把相册选中数据清空
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		AlbumSetting.clear();
	}

	/**
	 * 结果回调
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==AlbumCode){
			mImageBeanList.clear();
			mImageBeanList.addAll(AlbumSetting.CheckedSureBitmapList);
			if(mImageBeanList.size()<9)
				mImageBeanList.add(null);//添加加号布局
			mSimpleAdapter.notifyDataSetChanged();
			for (AlbumImageBean path:AlbumSetting.CheckedSureBitmapList) {
				mPhotos.add(path.getImageName());
			}
		}
	}
}
