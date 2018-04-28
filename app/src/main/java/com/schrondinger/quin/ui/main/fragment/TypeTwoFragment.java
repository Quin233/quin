package com.schrondinger.quin.ui.main.fragment;


import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.schrondinger.quin.R;
import com.schrondinger.quin.base.activity.ActivityInject;
import com.schrondinger.quin.base.activity.BaseFragment;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@ActivityInject(rootViewId = R.layout.fragment_type_two)
public class TypeTwoFragment extends BaseFragment {
    @BindView(R.id.testImage)
    ImageView mIv_testImage;

    private Bitmap getImageFromAssetsFile(String fileName)
    {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try
        {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return image;

    }

    @Override
    public void initData() {
        super.initData();

        Flowable.just("goutouren.jpg"). // 被订阅的数据源
                subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(String s) {
                        return  getImageFromAssetsFile(s);
                    }
                }).subscribe(new Consumer<Bitmap>() {
            @Override
            public void accept(Bitmap bitmap) {
                mIv_testImage.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public void initView() {
        super.initView();
    }
}
