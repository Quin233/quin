package com.schrondinger.quin.ui.game.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.schrondinger.quin.R
import com.schrondinger.quin.Utils.GlideImageLoader
import com.schrondinger.quin.Utils.Util
import com.schrondinger.quin.bean.common.BannerItem
import com.schrondinger.quin.bean.common.MenuItem
import com.schrondinger.quin.bean.game.GameButtonItem
import com.schrondinger.quin.bean.music.MusicContentItem
import com.schrondinger.quin.bean.music.MusicTitleItem
import com.schrondinger.quin.widget.CircleImageView
import com.youth.banner.Banner
import com.youth.banner.BannerConfig

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2019/3/7 2:07 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class GameMainAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>{
    var recyclerItemLis = mutableListOf<MenuItem<Any>>()
    var context : Context

    val ITEM_TYPE_BANNER = 0
    val ITEM_TYPE_MENU = 1
    val ITEM_TYPE_DIVIDER = 2
    val ITEM_TYPE_TITLE = 3
    val ITEM_TYPE_CONTENT = 4

    constructor(context: Context, recyclerItemList: MutableList<MenuItem<Any>>){
        this.recyclerItemLis = recyclerItemList
        this.context = context
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            ITEM_TYPE.ITEM_TYPE_BANNER.ordinal->{ // Banner
                val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.include_game_banner, viewGroup, false)
                return BannerViewHolder(view)
            }
            ITEM_TYPE.ITEM_TYPE_MENU.ordinal->{ // 按钮
                val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.include_game_menu_item, viewGroup, false)
                return MenuViewHolder(view)
            }
            ITEM_TYPE.ITEM_TYPE_DIVIDER.ordinal->{ // 分割线
                val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.include_all_divider_item, viewGroup, false)
                return DividerViewHolder(view)
            }
            ITEM_TYPE.ITEM_TYPE_TITLE.ordinal->{ // 标题
                val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.include_game_title, viewGroup, false)
                return TitleViewHolder(view)
            }
            ITEM_TYPE.ITEM_TYPE_CONTENT.ordinal->{ // 主内容
                val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.include_music_content_item, viewGroup, false)
                return ContentViewHolder(view)
            }
            else->{
                return super.createViewHolder(viewGroup, viewType)
            }
        }
    }

    override fun getItemCount(): Int {
        return recyclerItemLis.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val menuItem = recyclerItemLis[position]
        if (holder is BannerViewHolder){
            val bannerItems = menuItem.data as MutableList<BannerItem>
            if (Util.isNullOrEmpty(bannerItems[0].url)) {
                val images = ArrayList<Int>()
                for (item in bannerItems) {
                    images.add(item.resource)
                }
                holder.banner.setImages(images)
                        .setImageLoader(GlideImageLoader())
                        .setDelayTime(5000)
                        .setIndicatorGravity(BannerConfig.RIGHT)
                        .start()
            } else {
                //加载网络图片
                val images = ArrayList<String>()
                for (item in bannerItems) {
                    images.add(item.url)
                }
                holder.banner.setImages(images)
                        .setImageLoader(GlideImageLoader())
                        .setDelayTime(5000)
                        .setIndicatorGravity(BannerConfig.RIGHT)
                        .start()
            }
            holder.banner.setOnBannerListener { position ->
                if (mOnBannerItemClickListener != null) {
                    mOnBannerItemClickListener?.onBannerClick(position,menuItem.needLogin)
                }
            }
        }else if (holder is TitleViewHolder) {
            holder.tvMusicTitle.text = (menuItem.data as MusicTitleItem).text
            holder.llTitle.setOnClickListener { v -> mOnMenuItemClickListener?.onMenuClick(v, position,ITEM_TYPE_TITLE) }
        } else if (holder is MenuViewHolder) {
            holder.ivIcon.setImageResource((menuItem.data as GameButtonItem).resource)
            holder.tvText.text = menuItem.data.text
            holder.llMenu.setOnClickListener { v -> mOnMenuItemClickListener?.onMenuClick(v, position,ITEM_TYPE_MENU) }
        }else if (holder is ContentViewHolder){
            var mImage = holder.imgBg
            Glide.with(context).load((menuItem.data as MusicContentItem).resource).asBitmap().into(object: SimpleTarget<Bitmap>(){
                override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                    mImage.setImageResource(R.drawable.bg_default_music)
                }
                override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>?) {
                    mImage.layoutParams = RelativeLayout.LayoutParams(resource.width,resource.height)
                    mImage.setImageBitmap(resource)
                }
            })
            holder.tvContent1.text = menuItem.data.text
            holder.tvContent2.text = menuItem.data.text2
            if (Util.isNullOrEmpty(menuItem.data.number)){
                holder.imgHeadSet.visibility = View.GONE

            }else{
                holder.imgHeadSet.visibility = View.VISIBLE
                holder.tvNumber.text = menuItem.data.number
            }
            holder.rlContent.setOnClickListener { v -> mOnMenuItemClickListener?.onMenuClick(v, position,ITEM_TYPE_CONTENT) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return recyclerItemLis[position].dataType
    }

    fun getSpanSize(pos: Int): Int {
        val viewType = getItemViewType(pos)
        when (viewType) {
            ITEM_TYPE_BANNER -> return 5
            ITEM_TYPE_TITLE -> return 5
            ITEM_TYPE_MENU -> return 1
            ITEM_TYPE_CONTENT -> return 5
            ITEM_TYPE_DIVIDER -> return 5
        }
        return 0
    }

    //Banner
    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var banner = itemView.findViewById(R.id.banner) as Banner
    }

    //标题栏
    class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvMusicTitle = itemView.findViewById(R.id.tv_game_title) as TextView
        var llTitle = itemView.findViewById(R.id.ll_title) as LinearLayout
    }

    //列表条目
    class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imgBg = view.findViewById(R.id.img_bg) as ImageView
        var tvNumber = view.findViewById(R.id.tv_music_number) as TextView
        var tvContent1 = view.findViewById(R.id.tv_text1) as TextView
        var tvContent2 = view.findViewById(R.id.tv_text2) as TextView
        var rlContent = view.findViewById(R.id.rl_content) as RelativeLayout
        var imgHeadSet = view.findViewById(R.id.iv_headset) as ImageView
    }

    //按钮
    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivIcon = view.findViewById(R.id.iv_icon) as CircleImageView
        var tvText = view.findViewById(R.id.tv_text) as TextView
        var llMenu = view.findViewById(R.id.ll_menu) as LinearLayout
    }


    //分割线
    class DividerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    interface OnBannerItemClickListener {
        fun onBannerClick(position: Int,needLogin:Boolean)
    }
    private var mOnBannerItemClickListener: OnBannerItemClickListener? = null
    fun setOnBannerItemClickListener(listener: OnBannerItemClickListener) {
        this.mOnBannerItemClickListener = listener
    }

    interface OnMenuItemClickListener {
        fun onMenuClick(view: View, position: Int, type:Int)
    }
    private var mOnMenuItemClickListener: OnMenuItemClickListener? = null
    fun setOnMenuItemClickListener(listener: OnMenuItemClickListener) {
        this.mOnMenuItemClickListener = listener
    }

    enum class ITEM_TYPE {
        ITEM_TYPE_BANNER,
        ITEM_TYPE_MENU,
        ITEM_TYPE_DIVIDER,
        ITEM_TYPE_TITLE,
        ITEM_TYPE_CONTENT
    }
}