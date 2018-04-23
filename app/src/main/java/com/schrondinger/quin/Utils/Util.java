package com.schrondinger.quin.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.schrondinger.quin.R;

import java.lang.ref.WeakReference;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

//import it.sauronsoftware.base64.Base64;
public class Util {
    /**
     * 判断对象是否是null或者是""
     * @param obj 目标对象
     * @return boolean值
     */
    public static boolean isNullOrEmpty(Object obj) {
        if (obj instanceof Object[]) {
            Object[] o = (Object[]) obj;
            for (int i = 0; i < o.length; i++) {
                Object object = o[i];
                if (object instanceof Date) {
                    if (object.equals(new Date(0)))
                        return true;
                } else if ((object == null) || (("").equals(object))) {
                    return true;
                }
            }
        } else {
            if (obj instanceof Date) {
                if (obj.equals(new Date(0))) {
                    return true;
                }
            } else if ((obj == null) || (("").equals(obj.toString()))) {
                return true;
            }
        }

        return false;
    }

    /**
     * 将dip转换为px
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 设置view的背景图片
     * @param rlVenueBg 需要设置背景的view
     * @param res 背景图片资源id
     * @param context 当前activity
     */
    public static void loadImageToView(final View rlVenueBg, int res, Context context){

        Glide.with(context)
                .load(res)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(180,180) { //设置宽高
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            rlVenueBg.setBackground(drawable); //设置背景
                        }
                    }
                });
    }

    /**
     * 设置ImageView资源
     * @param imageView 目标ImageView
     * @param res 资源id
     * @param context 上下文
     */
    public static void loadImage(ImageView imageView, int res,Context context){
        final WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(imageView);
        ImageView target = imageViewWeakReference.get();
        if (target != null) {
            Glide.with(context).load(res).into(target);
        }
    }

    /**
     * 设置ImageView资源
     * @param imageView 目标ImageView
     * @param imageUrl 网络图片url
     * @param context 上下文
     */
    public static void loadImage(ImageView imageView, String imageUrl,Context context){
        final WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(imageView);
        ImageView target = imageViewWeakReference.get();
        if (target != null) {
            Glide.with(context).load(imageUrl).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(target);
        }
    }
}
