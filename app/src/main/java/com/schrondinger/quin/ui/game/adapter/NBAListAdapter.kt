package com.schrondinger.quin.ui.game.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.schrondinger.quin.R
import com.schrondinger.quin.bean.game.NBAer
import com.schrondinger.quin.widget.adapter.CommonAdapter

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2019/3/8 2:26 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class NBAListAdapter : CommonAdapter<NBAer>{

    private var commonClickListener: CommonViewHolder.onItemCommonClickListener? = null
    constructor(context: Context,dataList:List<NBAer> , layoutId:Int, listener:CommonViewHolder.onItemCommonClickListener ) : super(context,dataList,layoutId) {
        this.commonClickListener = listener
    }

    override fun bindData(holder: CommonViewHolder, data: NBAer) {
        holder.setText(R.id.tv_title_left, data.teamPosition) // 位置
                .setText(R.id.tv_title_right1, data.cName) // 名字
                .setText(R.id.tv_title_right2, data.position) // 定位
                .setText(R.id.tv_title_right3, data.age) // 年龄
                .setText(R.id.tv_title_right4, data.score) // 评分
                .setText(R.id.tv_title_right5, data.score)
                .setText(R.id.tv_title_right6, data.score)
                .setText(R.id.tv_title_right7, data.score)
                .setText(R.id.tv_title_right8, data.score)
                .setCommonClickListener(commonClickListener!!)
    }

}