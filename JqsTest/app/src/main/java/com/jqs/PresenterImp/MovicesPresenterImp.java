package com.jqs.PresenterImp;

import com.jqs.Beans.MovicesList_Bean;
import com.jqs.Dao.MovicesDao;
import com.jqs.Dao.OnResultClick;
import com.jqs.DaoImp.MovicesDaoImp;
import com.jqs.Presenter.MovicesPresenter;
import com.jqs.Views.MovicesView;

import java.util.List;

/**
 *
 */

public class MovicesPresenterImp implements MovicesPresenter,OnResultClick<List<MovicesList_Bean.SubjectsBean>>{
    private MovicesDao mMovicesDao;
    private MovicesView mMovicesView;

    public MovicesPresenterImp(MovicesView movicesView) {
        mMovicesView = movicesView;
        mMovicesDao=new MovicesDaoImp();
    }

    @Override
    public void getMovicesList(int start, int count) {
        mMovicesDao.getMovicesList(start,count,this);
    }


    @Override
    public void success(List<MovicesList_Bean.SubjectsBean> bean) {
        mMovicesView.notifyReflashView(bean);
    }

    @Override
    public void error(Throwable throwable) {

    }
}
