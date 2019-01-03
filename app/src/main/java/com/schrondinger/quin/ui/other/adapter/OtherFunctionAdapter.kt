package com.schrondinger.quin.ui.other.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.schrondinger.quin.R
import java.util.ArrayList

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2018/12/17 9:29 AM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class OtherFunctionAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder> {

    var recyclerItemList = ArrayList<MenuItem>()

    val ITEM_TYPE_TITLE = 0
    val ITEM_TYPE_MENU = 1
    val ITEM_TYPE_DIVIDER = 2

    constructor(recyclerItemList: ArrayList<MenuItem>){
        this.recyclerItemList = recyclerItemList
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            ITEM_TYPE.ITEM_TYPE_TITLE.ordinal->{ // 标题栏
                val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.include_all_title, viewGroup, false)
                return TitleViewHolder(view)
            }
            ITEM_TYPE.ITEM_TYPE_MENU.ordinal->{ // 主菜单
                val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.include_all_menu_item, viewGroup, false)
                return MenuViewHolder(view)
            }
            ITEM_TYPE.ITEM_TYPE_DIVIDER.ordinal->{ // 分割线
                val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.include_all_divider_item, viewGroup, false)
                return DividerViewHolder(view)
            }
            else->{
                return super.createViewHolder(viewGroup, viewType)
            }
        }
    }

    override fun getItemCount(): Int {
        return recyclerItemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val menuItem = recyclerItemList[position]
        if (holder is TitleViewHolder) {
            holder.tv_function_title.text = menuItem.text
        } else if (holder is MenuViewHolder) {
            holder.iv_icon.setImageResource(menuItem.iconId)
            holder.tv_text.text = menuItem.text
            //
            when(menuItem.text){
                "财富智投","哈哈哈"->{
                    holder.iv_new.visibility = View.VISIBLE
                }
                else->{
                    holder.iv_new.visibility = View.GONE
                }
            }
            holder.rl_menu.setOnClickListener { v -> mOnMenuItemClickListener?.onMenuClick(v, menuItem) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return recyclerItemList[position].dataType
    }

    fun getSpanSize(pos: Int): Int {
        val viewType = getItemViewType(pos)
        when (viewType) {
            ITEM_TYPE_TITLE -> return 4
            ITEM_TYPE_MENU -> return 1
            ITEM_TYPE_DIVIDER -> return 4
        }
        return 0
    }

    //标题栏
    class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_function_title: TextView

        init {
            tv_function_title=itemView.findViewById(R.id.tv_function_title) as TextView
        }
    }

    //列表条目
    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var rl_menu: RelativeLayout
        var iv_icon: ImageView
        var iv_new: ImageView
        var tv_text: TextView

        init {
            rl_menu = view.findViewById(R.id.rl_menu) as RelativeLayout
            iv_icon = view.findViewById(R.id.iv_icon) as ImageView
            iv_new = view.findViewById(R.id.iv_new) as ImageView
            tv_text = view.findViewById(R.id.tv_text) as TextView
        }
    }

    //divider
    class DividerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnMenuItemClickListener {
        fun onMenuClick(view: View, data: MenuItem)
    }

    private var mOnMenuItemClickListener: OnMenuItemClickListener? = null
    fun setOnMenuItemClickListener(listener: OnMenuItemClickListener) {
        this.mOnMenuItemClickListener = listener
    }

    enum class ITEM_TYPE {
        ITEM_TYPE_TITLE,
        ITEM_TYPE_MENU,
        ITEM_TYPE_DIVIDER
    }

}