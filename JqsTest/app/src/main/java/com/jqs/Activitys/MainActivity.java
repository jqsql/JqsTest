package com.jqs.Activitys;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentController;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.jqs.Beans.APPColor_BusBean;
import com.jqs.Fragments.CircleFragment;
import com.jqs.Fragments.MoviesListFragment;
import com.jqs.R;
import com.jqs.Utils.ALog;
import com.jqs.Utils.APP_Property;
import com.jqs.Utils.BaseActivityUtils.BaseActivity;
import com.jqs.Utils.RxJavaUtils.RxBus;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    //侧滑布局部分
    private ImageView mHeaderBackground;//侧滑背景图
    private CircleImageView mImageView;//侧滑头像
    //工具操作类
    FragmentManager mFragmentManager;
    //数据部分
    MoviesListFragment mMoviesListFragment;//推荐电影列表界面
    CircleFragment mCircleFragment;//论坛列表信息界面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	    initView();
	    initData();
	    initListen();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            //首页
            mFragmentManager.beginTransaction().replace(R.id.APP_Main_FrameLayout,mMoviesListFragment).commit();
        } else if (id == R.id.nav_gallery) {
            //论坛
            mCircleFragment=new CircleFragment();
            mFragmentManager.beginTransaction().replace(R.id.APP_Main_FrameLayout,mCircleFragment).commit();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

	/**
	*初始化
	*/
	private void initView(){
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this, drawer,0,0);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mMoviesListFragment=new MoviesListFragment();
        mFragmentManager=getSupportFragmentManager();

        //添加侧滑头部文件
        View headerView= navigationView.inflateHeaderView(R.layout.nav_header_main);
        mHeaderBackground=headerView.findViewById(R.id.NavigationView_imageView);
        mImageView=headerView.findViewById(R.id.NavigationView_Header);

	}
	/**
	*数据处理
	*/
	private void initData(){
        mFragmentManager.beginTransaction().replace(R.id.APP_Main_FrameLayout,mMoviesListFragment).commit();
        Picasso.with(this)
                .load(APP_Property.user.getPortrait())
                .into(mHeaderBackground);
        Picasso.with(this)
                .load(APP_Property.user.getUserHeader())
                .into(mImageView);
	}
	/**
	*事件监听
	*/
	private void initListen(){


	}


}
