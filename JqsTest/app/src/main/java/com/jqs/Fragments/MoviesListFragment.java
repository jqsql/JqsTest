package com.jqs.Fragments;

import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jqs.Activitys.MainActivity;
import com.jqs.Beans.MovicesList_Bean;
import com.jqs.Presenter.MovicesPresenter;
import com.jqs.PresenterImp.MovicesPresenterImp;
import com.jqs.R;
import com.jqs.Utils.BaseRecyclerAdapterUtils.BaseRecyclerAdapter;
import com.jqs.Utils.BaseRecyclerAdapterUtils.BaseViewHolder;
import com.jqs.Utils.BaseRecyclerAdapterUtils.interfaces.OnLoadMoreClickListener;
import com.jqs.Utils.BaseRecyclerAdapterUtils.interfaces.OnRecycleItemClickListener;
import com.jqs.Utils.BaseRecyclerAdapterUtils.util.LoadingType;
import com.jqs.Views.MovicesView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jqs on 2017/12/9.
 */

public class MoviesListFragment extends Fragment implements MovicesView {
    View mView;
    private RecyclerView mRecyclerView;
    private TextView mTitle;//标题
    private BaseRecyclerAdapter mSimpleAdapter;//测试适配器
    private SwipeRefreshLayout mRefreshLayout;
    private List<MovicesList_Bean.SubjectsBean> mList=new ArrayList<>();
    private MovicesPresenter mPresenter;
    private boolean isOnRefresh=true;//true：下拉刷新 false：上拉加载
    private int mLastDate=0;//最后一条数据的位置

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.app_bar_main,null);
	    initView();
	    initData();
	    initListen();
        return mView;
    }

	/**
	*初始化
	*/
	private void initView(){
        mRecyclerView=  mView.findViewById(R.id.Main_RecyclerView);
        mRefreshLayout=  mView.findViewById(R.id.Main_SwipeRefreshLayout);
        mTitle=  mView.findViewById(R.id.Top_Bar_Title);
        mPresenter=new MovicesPresenterImp(this);
        //瀑布流设置
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position=parent.getChildAdapterPosition(view);
                outRect.left=18;
                outRect.right=18;
                outRect.bottom=16;
                if(position==0 || position==1){
                    outRect.top=16;
                }
            }
        });
        mSimpleAdapter = new BaseRecyclerAdapter<MovicesList_Bean.SubjectsBean>(getActivity(),R.layout.movies_list_item,mList) {
            @Override
            protected void convert(BaseViewHolder holder, MovicesList_Bean.SubjectsBean Movies_item_img) {
                //设置随机高度
                int height=900+holder.getViewPosition()%3*100;
                /*int dpHeight=px2dip(height);*/
                ViewGroup.LayoutParams lp=  holder.getConvertView().getLayoutParams();
                lp.height=height;
                holder.getConvertView().setLayoutParams(lp);
                StringBuilder castList=new StringBuilder("");
                for (MovicesList_Bean.SubjectsBean.CastsBean cast:Movies_item_img.getCasts()) {
                    castList.append(cast.getName()+"、");
                }
                Uri uri=Uri.parse(Movies_item_img.getImages().getMedium());//获取图片
                holder.setPicassoImage(R.id.Movies_item_img,uri)
                        .setText(R.id.Movies_item_name,Movies_item_img.getTitle())
                        .setText(R.id.Movies_item_Author,Movies_item_img.getYear())
                        .setText(R.id.Movies_item_Time,"主演："+castList.substring(0,castList.length()-1));
            }
        };
        mRecyclerView.setAdapter(mSimpleAdapter);
	}
	/**
	*数据处理
	*/
	private void initData(){
        mTitle.setText("推荐");
        mPresenter.getMovicesList(mLastDate,mLastDate+10);
	}
	/**
	*事件监听
	*/
	private void initListen(){
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
                mPresenter.getMovicesList(mLastDate,10);
            }
        });
        //上拉加载
        mSimpleAdapter.setLoadMoreListener(new OnLoadMoreClickListener() {
            @Override
            public void onRefresh() {
                isOnRefresh=false;
                mPresenter.getMovicesList(mLastDate,10);
            }
        });
	}
    /**
     * 刷新列表
     * @param bean
     */
    @Override
    public void notifyReflashView(List<MovicesList_Bean.SubjectsBean> bean) {
        if(isOnRefresh){
            mList.clear();
            mRefreshLayout.setRefreshing(false);
            mLastDate+=10;
            mList.addAll(bean);
            mSimpleAdapter.notifyDataSetChanged();
        }else {
            mLastDate+=10;
            mList.addAll(bean);
            mSimpleAdapter.setOnRefrash(false,mList);
        }
    }
}
