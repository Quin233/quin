package com.schrondinger.quin.Utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.youth.banner.loader.ImageLoader

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2019/1/4 5:25 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class GlideImageLoader : ImageLoader(){
    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        Glide.with(context.applicationContext)
                .load(path)
                .crossFade()
                .into(imageView)
    }
}