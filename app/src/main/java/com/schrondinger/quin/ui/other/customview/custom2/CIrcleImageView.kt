package com.schrondinger.quin.ui.other.customview.custom2

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView
import android.graphics.Paint.ANTI_ALIAS_FLAG
import com.schrondinger.quin.R


/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2019/2/19 5:22 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class CIrcleImageView @JvmOverloads  constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): ImageView(context, attrs, defStyleAttr){

    private var mCanvasWidth = 100 // 默认画布的宽度
    private var mCanvasHeight = 100 // 默认画布的高度
    private var mBitmap:Bitmap? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var measureWidth = measuredWidth
        var measureHeight = measuredHeight
        if (measureWidth<measureHeight){
            measureHeight = measureWidth
        }else{
            measureWidth = measureHeight
        }
        mCanvasWidth = measureWidth-paddingLeft-paddingRight
        mCanvasHeight = measureHeight-paddingTop-paddingBottom

        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_default_music)
        setMeasuredDimension(measureWidth,measureHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val mPaint = Paint(ANTI_ALIAS_FLAG)
        mPaint.color  = Color.WHITE
        mPaint.style = Paint.Style.FILL
        canvas.drawCircle(mBitmap!!.width * 0.5f, mBitmap!!.height *0.5f, mBitmap!!.width *0.5f, mPaint)
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(mBitmap!!, 0f, 0f, mPaint)
    }
}