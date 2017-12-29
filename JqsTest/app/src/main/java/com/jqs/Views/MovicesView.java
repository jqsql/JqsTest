package com.jqs.Views;

import com.jqs.Beans.MovicesList_Bean;

import java.util.List;

/**
 * 电影操作
 */

public interface MovicesView {
    void notifyReflashView(List<MovicesList_Bean.SubjectsBean> bean);//提醒刷新视图
}
