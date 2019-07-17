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
import com.schrondinger.quin.bean.music.MusicContentItem

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2019/1/7 4:01 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class MusicFunctionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private var recyclerItemLis:ArrayList<MusicContentItem>

    var context : Context
    constructor(context: Context, recyclerItemList: ArrayList<MusicContentItem>){
        this.recyclerItemLis = recyclerItemList
        this.context = context
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.music_function_item, viewGroup, false)
        return MusicFunctionHolder(view)
    }

    override fun getItemCount(): Int {
        return recyclerItemLis.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, p1: Int) {
        (holder as MusicFunctionHolder).imgMusicFunction.setImageResource(recyclerItemLis[p1].resource)
        holder.tvMusicFunciton.text = recyclerItemLis[p1].text
        holder.llMusicFunciton.setOnClickListener { mOnItemClickListener?.onItemClick(p1) }
    }


    class MusicFunctionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var llMusicFunciton = itemView.findViewById(R.id.ll_music_function) as LinearLayout
        var imgMusicFunction = itemView.findViewById(R.id.img_music_function) as ImageView
        var tvMusicFunciton = itemView.findViewById(R.id.tv_music_function_name) as TextView
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    private var mOnItemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mOnItemClickListener = listener
    }

}