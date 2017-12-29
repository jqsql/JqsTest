package com.jqs.Activitys;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jqs.Beans.AlbumImageBean;
import com.jqs.Beans.FolderBean;
import com.jqs.R;
import com.jqs.Utils.AlbumUtils.AlbumImageAdapter;
import com.jqs.Utils.AlbumUtils.AlbumPopupWindow;
import com.jqs.Utils.AlbumUtils.AlbumSetting;
import com.jqs.Utils.BaseActivityUtils.BaseActivity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 显示相册所有图片
 */
public class AlbumForAll_Activity extends BaseActivity {
    //控件
    ImageView mReturn;//返回
    Button mOk;//完成
    GridView mGridView;
    RelativeLayout mBottomLayout;//底部布局
    TextView mDirName;//相册名称
    TextView mDirCount;//相册内图片数量

    private ProgressDialog mProgressDialog;
    private List<AlbumImageBean> mAllImgs=new ArrayList<>();//所有图片
    private File mCurrentDir;


    private AlbumPopupWindow mPopupWindow;

    private List<FolderBean> mFolderBeans = new ArrayList<>();//相册文件夹集合
    int mMaxNumber;//当前可以选择的张数
    //适配器
    AlbumImageAdapter mAlbumImageAdapter;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                mProgressDialog.dismiss();
                //绑定数据到View中
                data2View();
                //初始化popupwindow
                initDirPopupWindow();

            }
        }
    };

    /**
     * 初始化popupwindow
     */
    private void initDirPopupWindow() {
        mPopupWindow=new AlbumPopupWindow(AlbumForAll_Activity.this,mFolderBeans);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });
        mPopupWindow.setDirListener(new AlbumPopupWindow.OnDirSelectedListener() {
            @Override
            public void onSelect(FolderBean folderBean) {
                mAlbumImageAdapter=new AlbumImageAdapter(folderBean.getImgs(),folderBean.getDir(),
                        AlbumForAll_Activity.this);
                mGridView.setAdapter(mAlbumImageAdapter);
                mDirCount.setText(folderBean.getCount()+"张");
                mDirName.setText(folderBean.getName());
                //选择个数回调
                mAlbumImageAdapter.setOnCheckedClick(new AlbumImageAdapter.OnCheckedClick() {
                    @Override
                    public void setCheckedPosition(int Position) {
                        if(Position==0) {
                            mOk.setText("完成");
                        }else {
                            mOk.setText("完成(" + Position + "/"+mMaxNumber+")");
                        }
                    }
                });
                mPopupWindow.dismiss();
            }
        });



    }

    /**
     * 内容区域变亮
     */
    private void lightOn() {
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=1.0f;
        getWindow().setAttributes(lp);
    }
    /**
     * 内容区域变暗
     */
    private void lightOff() {
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=.3f;
        getWindow().setAttributes(lp);
    }

    /**
     * 绑定数据到view中
     */
    private void data2View() {
        //所有图片
        if(mCurrentDir==null){
            Toast.makeText(this,"未扫描到任何图片!",Toast.LENGTH_SHORT).show();
            return;
        }

        //插入所有图片对象
        FolderBean folderBean=new FolderBean();
        folderBean.setDir("");
        if(mAllImgs!=null && mAllImgs.size()!=0)
        folderBean.setFiestImgPath(mAllImgs.get(0).getImageName());
        folderBean.setCount(mAllImgs.size());
        folderBean.setImgs(mAllImgs);
        folderBean.setChecked(true);
        mFolderBeans.add(0,folderBean);

        mAlbumImageAdapter=new AlbumImageAdapter(mAllImgs,"",AlbumForAll_Activity.this);
        mGridView.setAdapter(mAlbumImageAdapter);


        mDirCount.setText(mAllImgs.size()+"张");
        mDirName.setText("所有图片");


        //选择个数回调
        mAlbumImageAdapter.setOnCheckedClick(new AlbumImageAdapter.OnCheckedClick() {
            @Override
            public void setCheckedPosition(int Position) {
                if(Position==0) {
                    mOk.setText("完成");
                    mOk.setEnabled(false);
                    mOk.setBackgroundColor(Color.parseColor("#21532d"));
                }else {
                    mOk.setText("完成(" + Position + "/"+mMaxNumber+")");
                    mOk.setEnabled(true);
                    mOk.setBackgroundColor(Color.parseColor("#0e932e"));
                }
            }
        });

    }

    /**-------------------------
     * onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_forall);
        AlbumSetting.AlumberActivity.add(this);
        mMaxNumber=AlbumSetting.MaxNumber-AlbumSetting.CheckedSureBitmapList.size();
        initView();
        initData();
        initListen();
    }

    /**
     * 初始化
     */
    private void initView() {
        mReturn= (ImageView) findViewById(R.id.Album_ForAll_Return);
        mOk= (Button) findViewById(R.id.Album_ForAll_Ok);
        mGridView = (GridView) findViewById(R.id.Album_ForAll_GridView);
        mBottomLayout = (RelativeLayout) findViewById(R.id.Album_ForAll_BottomLayout);
        mDirName = (TextView) findViewById(R.id.Album_ForAll_DirName);
        mDirCount = (TextView) findViewById(R.id.Album_ForAll_DirCount);
    }

    /**
     * 数据处理
     */
    private void initData() {
        //利用ContentProvider扫描手机中的所有图片
        if (!Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "当前存储卡不可用!", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");


        new Thread() {
            @Override
            public void run() {
                Uri mImgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = AlbumForAll_Activity.this.getContentResolver();
                Cursor cursor=cr.query(mImgUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " +
                                MediaStore.Images.Media.MIME_TYPE + "=? or "+
                                MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png","image/gif"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                Set<String> mDirPaths=new HashSet<>();

                while (cursor.moveToNext()){
                    final String path=cursor.getString(
                            cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile=new File(path).getParentFile();
                    if(parentFile==null)
                        continue;

                    final String dirPath=parentFile.getAbsolutePath();
                    final FolderBean folderBean;
                    if(mDirPaths.contains(dirPath)){
                        continue;
                    }else {
                        mDirPaths.add(dirPath);
                        folderBean=new FolderBean();
                        folderBean.setDir(dirPath);
                        folderBean.setFiestImgPath(path);
                    }
                    if(parentFile.list()==null)
                        continue;

                    final List<AlbumImageBean> mImageBeanList=new ArrayList<>();
                    parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            if(name.endsWith(".jpg")
                                     || name.endsWith(".jpeg")
                                     || name.endsWith(".png")) {
                                AlbumImageBean mImageBean = new AlbumImageBean();
                                mImageBean.setImageName(dirPath+"/"+name);
                                mImageBeanList.add(mImageBean);
                                return true;
                            }else if(name.endsWith(".gif")){
                                AlbumImageBean mImageBean = new AlbumImageBean();
                                mImageBean.setImageName(dirPath+"/"+name);
                                mImageBean.setType(1);
                                mImageBeanList.add(mImageBean);
                                return true;
                            }
                            return false;
                        }
                    });


                    mAllImgs.addAll(mImageBeanList);

                    int picSize=mImageBeanList.size();
                    //把顺序调反
                    Collections.reverse(mImageBeanList);
                    //将最新的设为封面
                    if(mImageBeanList!=null && mImageBeanList.size()!=0)
                    folderBean.setFiestImgPath(mImageBeanList.get(0).getImageName());

                    folderBean.setImgs(mImageBeanList);
                    folderBean.setCount(picSize);

                    mFolderBeans.add(folderBean);

                    mCurrentDir=parentFile;
                }

                //按时间倒序排列
                Collections.sort(mAllImgs, new Comparator<AlbumImageBean>() {
                    @Override
                    public int compare(AlbumImageBean o1, AlbumImageBean o2) {
                        Long time1 = new File(o1.getImageName()).lastModified();
                        Long time2 = new File(o2.getImageName()).lastModified();
                        return time2.compareTo(time1);
                    }
                });
                cursor.close();
                //通知扫描完成
                mHandler.sendEmptyMessage(1);
                /*//扫描完成，释放临时变量的内存
                mDirPaths=null;*/

            }
        }.start();


    }

    /**
     * 事件监听
     */
    private void initListen() {
        //返回
        mReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumSetting.CheckedBitmapList.clear();
                finish();
            }
        });
        //完成
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumSetting.clearActivity();
            }
        });
        //下部PopupWindow
        mBottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
                mPopupWindow.showAsDropDown(mBottomLayout,0,0);
                lightOff();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            int size=AlbumSetting.CheckedBitmapList.size();
            if(size==0) {
                mOk.setText("完成");
                mOk.setEnabled(false);
                mOk.setBackgroundColor(Color.parseColor("#21532d"));
            }else {
                mOk.setText("完成(" + size + "/"+mMaxNumber+")");
                mOk.setEnabled(true);
                mOk.setBackgroundColor(Color.parseColor("#0e932e"));
            }
            mAlbumImageAdapter.notifyDataSetChanged();
        }
    }
}
