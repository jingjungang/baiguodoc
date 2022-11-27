package com.ukang.baiyu.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ukang.baiyu.activity.R;

public class AsyncLoadingImg {

	private static ImageLoader imageLoader;
	
	/**
	 * ���ImageLoader����*/
	public static ImageLoader getImageLoader(Context context){
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
        .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
        .discCacheFileNameGenerator(new Md5FileNameGenerator())
        .tasksProcessingOrder(QueueProcessingType.LIFO).build();
    ImageLoader.getInstance().init(config);
    imageLoader=ImageLoader.getInstance();
    return imageLoader;
	}
	
	/**
	 * ����Ĭ��ͼƬ
	 * @return
	 */
	public static DisplayImageOptions getDefaultOptions() {
		//����ͼƬ���ص�����
        com.nostra13.universalimageloader.core.DisplayImageOptions.Builder b = new DisplayImageOptions.Builder();
        b.showImageForEmptyUri(R.drawable.no_pic);
        b.showImageOnFail(R.drawable.no_pic);
        b.showImageOnLoading(R.drawable.loading);
        b.resetViewBeforeLoading(Boolean.TRUE);
        b.cacheOnDisc(Boolean.TRUE);
        b.cacheInMemory(Boolean.TRUE);
        b.imageScaleType(ImageScaleType.EXACTLY_STRETCHED);
        return b.bitmapConfig(Bitmap.Config.RGB_565).build();
    }
}
