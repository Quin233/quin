package com.schrondinger.quin.ui.music.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.schrondinger.quin.R
import com.schrondinger.quin.bean.music.Music

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2019/1/8 2:05 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class MusicListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private var recyclerItemLis:ArrayList<Music>
    val ITEMTYPE = 1
    val MORETYPE = 2

    var context : Context

    constructor(context: Context, recyclerItemList: ArrayList<Music>){
        this.recyclerItemLis = recyclerItemList
        this.context = context
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.music_list_item, viewGroup, false)
        return MusicListHolder(view)
    }

    override fun getItemCount(): Int {
        return recyclerItemLis.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, p1: Int) {
        (holder as MusicListHolder).tvMusicName.text = recyclerItemLis[p1].title
        holder.tvMusicArtist.text = recyclerItemLis[p1].artist


        holder.llMusicList.setOnClickListener { mOnItemClickListener?.onItemClick(p1,ITEMTYPE) }
        holder.ivMore.setOnClickListener{mOnItemClickListener?.onItemClick(p1,MORETYPE)}
    }

    class MusicListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var llMusicList = itemView.findViewById(R.id.ll_music_list) as LinearLayout
        var ivMore = itemView.findViewById(R.id.iv_more) as ImageView
        var ivHighQuality = itemView.findViewById(R.id.iv_high_quality) as ImageView
        var tvMusicName = itemView.findViewById(R.id.tv_music_name) as TextView
        var tvMusicArtist = itemView.findViewById(R.id.tv_music_artist) as TextView
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int,clickType:Int)
    }
    private var mOnItemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mOnItemClickListener = listener
    }

}