package com.jqs.Fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jqs.Activitys.PublishCircleActivity;
import com.jqs.Beans.APPColor_BusBean;
import com.jqs.Beans.MovicesList_Bean;
import com.jqs.Beans.RxBusModle;
import com.jqs.Presenter.MovicesPresenter;
import com.jqs.PresenterImp.MovicesPresenterImp;
import com.jqs.R;
import com.jqs.Utils.ALog;
import com.jqs.Utils.BaseRecyclerAdapterUtils.BaseItemDecoration;
import com.jqs.Utils.BaseRecyclerAdapterUtils.BaseRecyclerAdapter;
import com.jqs.Utils.BaseRecyclerAdapterUtils.BaseViewHolder;
import com.jqs.Utils.BaseRecyclerAdapterUtils.interfaces.OnLoadMoreClickListener;
import com.jqs.Utils.BaseRecyclerAdapterUtils.interfaces.OnRecycleItemClickListener;
import com.jqs.Utils.BaseRecyclerAdapterUtils.util.LoadingType;
import com.jqs.Utils.BaseRecyclerAdapterUtils.util.ManagerType;
import com.jqs.Utils.RxJavaUtils.RxBus;
import com.jqs.Views.MovicesView;
import com.jqs.greendao.gen.CircleBean;
import com.jqs.greendao.gen.CircleBeanDao;
import com.jqs.greendao.gen.GreenDaoManager;
import com.jqs.greendao.gen.UserInfoBean;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 圈子界面
 */

public class CircleFragment extends Fragment{
    View mView;
    private RecyclerView mRecyclerView;
    private TextView mTitle;//标题
    private SwipeRefreshLayout mRefreshLayout;
    private FloatingActionButton fab;//按钮
    //数据部分
    private List<CircleBean> mList=new ArrayList<>();
    //工具部分
    private CircleBeanDao mCircleBeanDao;
    private BaseRecyclerAdapter mSimpleAdapter;//测试适配器
    //标志部分
    private boolean isFirst;
    private boolean isOnRefresh=true;//true：下拉刷新 false：上拉加载
    private int mLastDate=0;//最后一条数据的位置

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.app_circle_main,null);
	    initView();
	    initData();
	    initListen();
        return mView;
    }

	/**
	*初始化
	*/
	private void initView(){
        mRecyclerView=  mView.findViewById(R.id.Main_Circle_RecyclerView);
        mRefreshLayout=  mView.findViewById(R.id.Main_Circle_SwipeRefreshLayout);
        fab = mView.findViewById(R.id.Circle_fab);
        mTitle=  mView.findViewById(R.id.Top_Bar_Title);
        mCircleBeanDao= GreenDaoManager.getInstance(getActivity()).getDaoSession().getCircleBeanDao();
        //线性布局设置
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new BaseItemDecoration(getActivity(), ManagerType.LINEAR));
        mSimpleAdapter = new BaseRecyclerAdapter<CircleBean>(getActivity(),R.layout.circle_main_item,mList) {
            @Override
            protected void convert(BaseViewHolder holder, CircleBean circleBean) {

                UserInfoBean author=circleBean.getAuthor();
                if(author!=null) {
                    Uri uri = Uri.parse(author.getUserHeader());//获取图片
                    holder.setPicassoCircleImage(R.id.Circle_HeadImg, uri)
                            .setText(R.id.Circle_Content, circleBean.getContent())
                            .setText(R.id.Circle_NickName, author.getUserName())
                            .setText(R.id.Circle_PublishTime, circleBean.getTimeStr())
                            .setOnClickListener(R.id.Circle_TransmitLayout, new View.OnClickListener() {
                                //转发
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setOnClickListener(R.id.Circle_CommentLayout, new View.OnClickListener() {
                                //评论
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setOnClickListener(R.id.Circle_LikeLayout, new View.OnClickListener() {
                                //点赞
                                @Override
                                public void onClick(View view) {

                                }
                            });
                }
            }
        };
        mRecyclerView.setAdapter(mSimpleAdapter);

	}
	/**
	*数据处理
	*/
	private void initData(){
        mTitle.setText("论坛");
        getCircleList();
	}
	/**
	*事件监听
	*/
	private void initListen(){
          fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ALog.e("点击了按钮"+isFirst);
                Intent intent=new Intent(getActivity(), PublishCircleActivity.class);
                startActivity(intent);
                /*if(!isFirst) {
                    RxBus.getDefault().post(new APPColor_BusBean(R.color.Green));
                    isFirst=true;
                }else {
                    RxBus.getDefault().post(new APPColor_BusBean(R.color.Red));
                    isFirst=false;
                }*/
            }
        });
        mSimpleAdapter.setItemClickListener(new OnRecycleItemClickListener() {
            @Override
            public void onClick(BaseViewHolder holder, Object o, int position) {
                Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
            }
        });
        //下拉刷新
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isOnRefresh=true;
                mLastDate=0;
                getCircleList();
            }
        });
        //上拉加载
        mSimpleAdapter.setLoadMoreListener(new OnLoadMoreClickListener() {
            @Override
            public void onRefresh() {
                isOnRefresh=false;
                getCircleList();
                ALog.e("一直在刷新");
            }
        });
        //发布回调
        RxBus.getDefault().doSubscribeMain(this, RxBusModle.class, new Consumer<RxBusModle>() {
            @Override
            public void accept(RxBusModle rxBusModle) throws Exception {
                if("PublishCircle".equals(rxBusModle.getFlag()) && rxBusModle.getContent() instanceof CircleBean) {
                    mList.add(0, (CircleBean) rxBusModle.getContent());
                    mSimpleAdapter.notifyDataSetChanged();
                }
            }
        });
	}

    private void getCircleList(){
        if(isOnRefresh){
            mList.clear();
            mRefreshLayout.setRefreshing(false);
            QueryBuilder bd=mCircleBeanDao.queryBuilder();
            bd.limit(10);//设置查询多少条数据
            bd.offset(mLastDate);//设置跳过的条数
            bd.orderDesc(CircleBeanDao.Properties.CircleId);
            mLastDate+=10;
            mList.addAll(bd.list());
            mSimpleAdapter.notifyDataSetChanged();
        }else {
            QueryBuilder bd=mCircleBeanDao.queryBuilder();
            bd.limit(10);//设置查询多少条数据
            bd.offset(mLastDate);//设置跳过的条数
            bd.orderDesc(CircleBeanDao.Properties.CircleId);
            mLastDate+=10;
            mList.addAll(bd.list());
            mSimpleAdapter.setOnRefrash(false, mList);
        }


    }
}
