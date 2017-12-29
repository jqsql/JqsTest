package com.jqs.Utils.BaseRecyclerAdapterUtils;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jqs.R;
import com.jqs.Utils.ALog;
import com.jqs.Utils.BaseRecyclerAdapterUtils.interfaces.OnLoadMoreClickListener;
import com.jqs.Utils.BaseRecyclerAdapterUtils.interfaces.OnRecycleItemClickListener;
import com.jqs.Utils.BaseRecyclerAdapterUtils.util.LoadingType;
import com.jqs.Utils.BaseRecyclerAdapterUtils.util.ManagerType;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseRecyclerAdapter 万能适配器
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    public static final int TYPE_COMMON_VIEW = 100001;//普通布局标志
    public static final int TYPE_FOOT_VIEW = 100002;//最后一条item布局标志
    private List<T> mList;
    private int mLayoutID;
    private Context mContext;
    private boolean IsOpenLoadMore=true;//是否开启加载更多操作
    private boolean IsOnRefrash=false;//是否正在加载中
    private ManagerType layoutManagerType;//是否正在加载中
    private LoadingType mLoadingType=LoadingType.PTRLOADING;//加载更多的状态
    private OnRecycleItemClickListener mItemClickListener;//item点击事件监听
    private OnLoadMoreClickListener mLoadMoreListener;//加载更多事件监听
    private int[] lastPositions;//最后一个的位置
    private int lastVisibleItemPosition;//最后一个可见的item的位置

    public void setItemClickListener(OnRecycleItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setLoadMoreListener(OnLoadMoreClickListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    public BaseRecyclerAdapter(Context context, int layoutID, List<T> list) {
        mList = list==null ? new ArrayList<T>():list;
        mLayoutID=layoutID;
        mContext = context;
    }
    public BaseRecyclerAdapter(Context context, int layoutID, List<T> list,boolean isOpenLoadMore) {
        mList = list==null ? new ArrayList<T>():list;
        mLayoutID=layoutID;
        mContext = context;
        IsOpenLoadMore=isOpenLoadMore;
    }
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder=null;
        switch (viewType) {
            case TYPE_FOOT_VIEW:
                holder=BaseViewHolder.get(mContext,parent, R.layout.foot_layout_view);
                break;
            case TYPE_COMMON_VIEW:
                holder=BaseViewHolder.get(mContext,parent,mLayoutID);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {
                if(IsOpenLoadMore) {
                    if (position != getItemCount() - 1) {
                        holder.setViewPosition(position);
                        convert(holder, mList.get(position));
                        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mItemClickListener.onClick(holder, mList.get(position), position);
                            }
                        });
                    } else {
                        //底部加载布局
                        switch (mLoadingType) {
                            case LOADING:
                                holder.setText(R.id.Foot_View_HintText, "正在加载...")
                                        .setVisibility(R.id.Foot_View_ProgressBar, View.VISIBLE);
                                break;
                            case LOADFAIL:
                                holder.setText(R.id.Foot_View_HintText, "加载失败")
                                        .setVisibility(R.id.Foot_View_ProgressBar, View.GONE);
                                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (mLoadMoreListener != null) {
                                            mLoadingType = LoadingType.LOADING;
                                            notifyItemChanged(position);
                                            mLoadMoreListener.onRefresh();
                                        }
                                        IsOnRefrash = true;
                                    }
                                });
                                break;
                            case PTRLOADING:
                                holder.setText(R.id.Foot_View_HintText, "加载更多")
                                        .setVisibility(R.id.Foot_View_ProgressBar, View.GONE);
                                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (mLoadMoreListener != null) {
                                            mLoadingType = LoadingType.LOADING;
                                            notifyItemChanged(position);
                                            mLoadMoreListener.onRefresh();
                                        }
                                        IsOnRefrash = true;
                                    }
                                });
                                break;
                            case NOMORE:
                                holder.setText(R.id.Foot_View_HintText, "没有更多内容")
                                        .setVisibility(R.id.Foot_View_ProgressBar, View.GONE);
                                break;
                        }
                    }
                }else {
                    //未开启上拉加载
                    holder.setViewPosition(position);
                    convert(holder, mList.get(position));
                    holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mItemClickListener.onClick(holder, mList.get(position), position);
                        }
                    });
                }
    }
    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (isFooterView(holder.getLayoutPosition())) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isFooterView(position)) {
                        return gridManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
        startLoadMore(recyclerView, layoutManager);
    }

    @Override
    public int getItemCount() {
        return mList.size()+getFooterViewCount();
    }

    @Override
    public int getItemViewType(int position) {
        if(isFooterView(position))
            return TYPE_FOOT_VIEW;
        return TYPE_COMMON_VIEW;
    }

    protected abstract void convert(BaseViewHolder holder, T t);

    /**
     * 判断是否是最后一个item
     * @param position
     * @return
     */
    private boolean isFooterView(int position) {
        return IsOpenLoadMore && position >= getItemCount() - 1;
    }
    private void startLoadMore(RecyclerView recyclerView, final RecyclerView.LayoutManager layoutManager) {
        if (!IsOpenLoadMore) {
            return;
        }
        if (layoutManagerType == null) {
            if (layoutManager instanceof LinearLayoutManager) {
                layoutManagerType = ManagerType.LINEAR;
            } else if (layoutManager instanceof GridLayoutManager) {
                layoutManagerType = ManagerType.GRID;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                layoutManagerType = ManagerType.STAGGERED_GRID;
            } else {
                throw new RuntimeException(
                        "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            // 用于监听ListView滑动状态的变化
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                ALog.e(""+newState);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //滑动时手指离开屏幕
                    if (!IsOnRefrash && visibleItemCount>0 && lastVisibleItemPosition + 1 >= totalItemCount) {
                        if(mLoadMoreListener != null) {
                            mLoadingType=LoadingType.LOADING;
                            notifyItemChanged(lastVisibleItemPosition);
                            mLoadMoreListener.onRefresh();
                        }
                        IsOnRefrash=true;
                    }
                }
            }
            //用于监听ListView屏幕滚动
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                switch (layoutManagerType) {
                    case LINEAR:
                        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager)
                                .findLastVisibleItemPosition();
                        break;
                    case GRID:
                        lastVisibleItemPosition = ((GridLayoutManager) layoutManager)
                                .findLastVisibleItemPosition();
                        break;
                    case STAGGERED_GRID:
                        StaggeredGridLayoutManager staggeredGridLayoutManager
                                = (StaggeredGridLayoutManager) layoutManager;
                        if (lastPositions == null) {
                            lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                        }
                        staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                        lastVisibleItemPosition = findMax(lastPositions);
                        break;
                }
            }
        });
    }

    /**
     * 加载成功的几种情况
     * @param onRefrash
     * @param list
     */
    public void setOnRefrash(boolean onRefrash,List<T> list) {
        if(list.size()==mList.size()){
            mLoadingType=LoadingType.NOMORE;
        }else{
            mLoadingType=LoadingType.PTRLOADING;
        }
        IsOnRefrash = onRefrash;
        notifyDataSetChanged();
    }

    /**
     * 加载失败
     * @param onRefrash
     */
    public void setFailOnRefrash(boolean onRefrash) {
        mLoadingType=LoadingType.LOADFAIL;
        IsOnRefrash = onRefrash;
        notifyDataSetChanged();
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
    /**
     * 返回 footer view 数量
     *
     * @return
     */
    public int getFooterViewCount() {
        return IsOpenLoadMore && !mList.isEmpty() ? 1 : 0;
    }

}
