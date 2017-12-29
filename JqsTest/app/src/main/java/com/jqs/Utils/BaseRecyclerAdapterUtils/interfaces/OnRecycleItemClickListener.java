package com.jqs.Utils.BaseRecyclerAdapterUtils.interfaces;

import com.jqs.Utils.BaseRecyclerAdapterUtils.BaseViewHolder;

/**
 * recycleView  item点击事件
 */

public interface OnRecycleItemClickListener<T> {
    void onClick(BaseViewHolder holder,T t,int position);
}
