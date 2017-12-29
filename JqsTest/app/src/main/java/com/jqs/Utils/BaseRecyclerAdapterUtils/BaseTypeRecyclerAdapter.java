package com.jqs.Utils.BaseRecyclerAdapterUtils;

import android.content.Context;
import android.view.ViewGroup;

import com.jqs.Utils.ALog;

import java.util.List;

/**
 * 多种布局的RecyclerAdapter适配器
 */

public abstract class BaseTypeRecyclerAdapter<T> extends BaseRecyclerAdapter<T>{
    private TypeViewSupport mViewSupport;
    private Context mContext;
    private List<T> mList;

    public interface TypeViewSupport<T>
    {
        int getLayoutId(int itemType);

        int getItemViewType(int position, T t);
    }

    public BaseTypeRecyclerAdapter(Context context, List<T> list,TypeViewSupport ViewSupport) {
        super(context, -1, list,false);
        mList=list;
        mContext=context;
        mViewSupport=ViewSupport;
    }

    @Override
    public int getItemViewType(int position) {
        ALog.e("position:"+position);
        return mViewSupport.getItemViewType(position,mList.get(position));
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = mViewSupport.getLayoutId(viewType);
        BaseViewHolder holder = BaseViewHolder.get(mContext, parent, layoutId);
        return holder;
    }
}
