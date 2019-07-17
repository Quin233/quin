package com.schrondinger.quin.widget.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.schrondinger.quin.R


/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2019/3/7 3:15 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
abstract class CommonAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private var mLayoutInflater: LayoutInflater? = null
    private var mDataList: List<T>? = null
    private var mLayoutId: Int = 0
    private var mFixX: Int = 0
    private var mMoveViewList = arrayListOf<View>()

    constructor(context: Context, dataList: List<T>, layoutId: Int){
        this.mLayoutInflater = LayoutInflater.from(context)
        this.mDataList = dataList
        this.mLayoutId = layoutId
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CommonViewHolder {

        var itemView = mLayoutInflater!!.inflate(mLayoutId, parent, false)
        val holder = CommonViewHolder(itemView)
        //获取可滑动的view布局
        val moveLayout = holder.getView(R.id.id_move_layout) as LinearLayout
        moveLayout.scrollTo(mFixX, 0)
        mMoveViewList.add(moveLayout)
        return holder
    }

//    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
//        bindData(holder, mDataList!![position])
//    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bindData(holder as CommonViewHolder, mDataList!![position])
    }

    override fun getItemCount(): Int {
        return if (mDataList == null) 0 else mDataList!!.size
    }

    abstract fun bindData(holder: CommonViewHolder, data: T)

    fun getMoveViewList(): ArrayList<View> {
        return mMoveViewList
    }

    fun setFixX(fixX: Int) {
        mFixX = fixX
    }


    /**
     * ViewHold部分
     */
    class CommonViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) ,View.OnLongClickListener, View.OnClickListener{
        private var viewSparseArray = SparseArray<Any>()
        private var commonClickListener: onItemCommonClickListener? = null

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        /**
         * 根据 ID 来获取 View
         * @param viewId viewID
         * @param <T>    泛型
         * @return 将结果强转为 View 或 View 的子类型
        </T> */
        fun <T : View> getView(viewId: Int): T {
            // 先从缓存中找，找打的话则直接返回
            // 如果找不到则 findViewById ，再把结果存入缓存中
            var view = viewSparseArray.get(viewId)
            if (view == null) {
                view = itemView.findViewById(viewId)
                viewSparseArray.put(viewId, view)
            }
            return view as T
        }

        fun setText(viewId: Int, text: CharSequence): CommonViewHolder {
            var tv = getView(viewId) as TextView
            tv.text = text
            return this
        }

        fun setViewVisibility(viewId: Int, visibility: Int): CommonViewHolder {
            (getView(viewId) as TextView).visibility = visibility
            return this
        }

        fun setImageResource(viewId: Int, resourceId: Int): CommonViewHolder {
            val imageView = getView(viewId) as ImageView
            imageView.setImageResource(resourceId)
            return this
        }

        interface onItemCommonClickListener {

            fun onItemClickListener(position: Int)

            fun onItemLongClickListener(position: Int)

        }

        fun setCommonClickListener(commonClickListener: onItemCommonClickListener) {
            this.commonClickListener = commonClickListener
        }

        override fun onClick(v: View) {
            commonClickListener?.onItemClickListener(adapterPosition)
        }

        override fun onLongClick(v: View): Boolean {
            commonClickListener?.onItemLongClickListener(adapterPosition)
            return false
        }

    }

}