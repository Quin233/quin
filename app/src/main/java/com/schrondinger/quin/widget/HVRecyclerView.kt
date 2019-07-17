package com.schrondinger.quin.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.schrondinger.quin.Utils.Util.dip2px
import com.schrondinger.quin.widget.adapter.CommonAdapter

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2019/3/7 2:55 PM
 * 描    述：可垂直水平滑动的RecyclerView
 * 修订历史：
 * ================================================
 */
class HVRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    //头部title布局
    private var mRightTitleLayout: LinearLayout? = null
    //手指按下时的位置
    private var mStartX = 0f
    //滑动时和按下时的差值
    private var mMoveOffsetX = 0
    //最大可滑动差值
    private var mFixX = 0
    //左边标题集合
    private var mLeftTextList = arrayOf<String>()
    //左边标题的宽度集合
    private var mLeftTextWidthList = arrayOf<Int>()
    //右边标题集合
    private var mRightTitleList = arrayOf<String>()
    //右边标题的宽度集合
    private var mRightTitleWidthList= arrayOfNulls<Int>(0)
    //展示数据时使用的RecycleView
    private var mRecyclerView: RecyclerView? = null
    //RecycleView的Adapter
    private var mAdapter: Any? = null
    //需要滑动的View集合
    private var mMoveViewList = arrayListOf<View>()
    //右边可滑动的总宽度
    private var mRightTotalWidth = 0
    //右边单个view的宽度
    private var mRightItemWidth = 60
    //左边view的宽度
    private var mLeftViewWidth = 80
    //左边view的高度
    private var mLeftViewHeight = 40
    //触发拦截手势的最小值
    private var mTriggerMoveDis = 30

    private fun initView() {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(createHeadLayout())
        linearLayout.addView(createMoveRecyclerView())
        addView(linearLayout, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    /**
     * 创建头部布局
     * @return
     */
    private fun createHeadLayout(): View {
        val headLayout = LinearLayout(context)
        headLayout.gravity = Gravity.CENTER
        val leftLayout = LinearLayout(context)
        addListHeaderTextView(mLeftTextList[0], mLeftTextWidthList[0], leftLayout)
        leftLayout.gravity = Gravity.CENTER
        headLayout.addView(leftLayout, 0, ViewGroup.LayoutParams(dip2px(context, mLeftViewWidth.toFloat()), dip2px(context, mLeftViewHeight.toFloat())))

        mRightTitleLayout = LinearLayout(context)
        for (i in 0 until mRightTitleList.size) {
            addListHeaderTextView(mRightTitleList[i], mRightTitleWidthList[i]!!, mRightTitleLayout!!)
        }
        headLayout.addView(mRightTitleLayout)
        return headLayout
    }

    /**
     * 创建数据展示布局
     * @return
     */
    private fun createMoveRecyclerView(): View {
        val linearLayout = RelativeLayout(context)
        mRecyclerView = RecyclerView(context)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView?.layoutManager = layoutManager
        if (null != mAdapter) {
            if (mAdapter is CommonAdapter<*>) {
                mRecyclerView?.adapter = mAdapter as CommonAdapter<*>
                mMoveViewList = (mAdapter as CommonAdapter<*>).getMoveViewList()
            }
        }

        linearLayout.addView(mRecyclerView, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT))
        return linearLayout
    }

    /**
     * 设置adapter
     * @param adapter
     */
    fun setAdapter(adapter: Any) {
        mAdapter = adapter
        initView()
    }

    /**
     * 设置头部title单个布局
     * @param headerName
     * @param width
     * @param leftLayout
     * @return
     */
    private fun addListHeaderTextView(headerName: String, width: Int, leftLayout: LinearLayout): TextView {
        var textView = TextView(context)
        textView.text = headerName
        textView.gravity = Gravity.CENTER
        leftLayout.addView(textView, width, dip2px(context, 50f))
        return textView
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> mStartX = ev.x
            MotionEvent.ACTION_MOVE -> {
                var offsetX = Math.abs(ev.x - mStartX).toInt()
                return offsetX > mTriggerMoveDis  //水平移动大于30触发拦截
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    /**
     * 右边可滑动的总宽度
     * @return
     */
    private fun rightTitleTotalWidth(): Int {
        if (0 == mRightTotalWidth) {
            for (i in 0 until mRightTitleWidthList.size) {
                mRightTotalWidth += mRightTitleWidthList[i]!!
            }
        }
        return mRightTotalWidth
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_MOVE -> {
                val offsetX = Math.abs(event.x - mStartX).toInt()
                if (offsetX > 30) {
                    mMoveOffsetX = (mStartX - event.x + mFixX).toInt()
                    if (0 > mMoveOffsetX) {
                        mMoveOffsetX = 0
                    } else {
                        //当滑动大于最大宽度时，不在滑动（右边到头了）
                        if (mRightTitleLayout!!.width + mMoveOffsetX > rightTitleTotalWidth()) {
                            mMoveOffsetX = rightTitleTotalWidth() - mRightTitleLayout!!.width
                        }
                    }
                    //跟随手指向右滚动
                    mRightTitleLayout?.scrollTo(mMoveOffsetX, 0)
                    if (null != mMoveViewList) {
                        for (i in 0 until mMoveViewList.size) {
                            //使每个item随着手指向右滚动
                            mMoveViewList[i].scrollTo(mMoveOffsetX, 0)
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP -> mFixX = mMoveOffsetX //设置最大水平平移的宽度
        }

        return super.onTouchEvent(event)
    }

    /**
     * 列表头部数据
     * @param headerListData
     */
    fun setHeaderListData(headerListData: Array<String>) {
        mRightTitleList = headerListData
        mRightTitleWidthList = arrayOfNulls(headerListData.size)
        for (i in headerListData.indices) {
            mRightTitleWidthList[i] = dip2px(context, mRightItemWidth.toFloat())
        }
        mLeftTextWidthList = arrayOf(dip2px(context, mLeftViewWidth.toFloat()))
        mLeftTextList = arrayOf("位置")
    }

}