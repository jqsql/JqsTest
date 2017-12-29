package com.jqs.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.jqs.R;
import com.jqs.Utils.AlbumUtils.PhotoView;

/**
 * 图片加载工具类
 */

public class ImageLoader {
    private static ImageLoader mImageLoader;
    private static Context mContext;
    private static RequestOptions options;//基本配置
    private int error_image= R.drawable.error_png;//错误图
    private int empty_image= R.drawable.error_png;//空图
    private int loading_image= R.drawable.error_png;//加载时显示的图

    public ImageLoader() {
        options=new RequestOptions()
                .placeholder(loading_image)
                .error(error_image)
                .fallback(empty_image);
    }
    public static ImageLoader getInstance(Context context){
        mContext = context;
        if(mImageLoader==null){
            synchronized (ImageLoader.class){
                if(mImageLoader==null)
                    mImageLoader=new ImageLoader();
            }
        }
        return mImageLoader;
    }

    /**
     * 加载网络图片
     */
    public void setLoadImages(ImageView imageView,String path){
        Glide.with(mContext)
                .load(path)
                .apply(options)
                .into(imageView);
    }
    /**
     * 加载GIF图片
     */
    public void setLoadGif(ImageView imageView,String path){
        Glide.with(mContext)
                .asGif()
                .load(path)
                .apply(options)
                .into(imageView);
    }
    /**
     * 取消加载GIF图片
     */
    public void clearLoadGif(View imageView){
        Glide.with(mContext)
                .clear(imageView);
    }
}
