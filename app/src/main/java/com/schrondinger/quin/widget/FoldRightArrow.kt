package com.schrondinger.quin.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.animation.ObjectAnimator



/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2019/1/7 3:25 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class FoldRightArrow @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr){

    val FOLDMODE = 0
    val UNFOLDMODE = 1

    private var xLength = 60
    private var yLength = 60
    private  var desiredWidth = xLength
    private  var desiredHeight = yLength
    private var mode = FOLDMODE // 0 是折叠模式 1 是打开模式

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        if (heightMode == MeasureSpec.EXACTLY) {
            desiredHeight = heightSize
        } else {
            desiredHeight = yLength + paddingTop + paddingBottom
            if (heightMode == MeasureSpec.AT_MOST) {
                desiredHeight = Math.min(heightSize, desiredHeight)
            }
        }
        desiredWidth = desiredHeight
        setMeasuredDimension(desiredWidth,desiredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var paint = Paint()
        paint.isAntiAlias = true
        paint.strokeWidth = 2f
        paint.style = Paint.Style.STROKE
        var path = Path()
        path.moveTo(desiredWidth/2f,0f)
        path.lineTo(desiredWidth.toFloat(),desiredHeight/2f)
        path.lineTo(desiredWidth/2f,desiredHeight.toFloat())
        canvas.drawPath(path,paint)
    }

    fun isUnFold():Boolean{
        return this.mode == UNFOLDMODE
    }

    fun setMode(mode:Boolean){
        if(mode){ // 打开模式
            val animator = ObjectAnimator.ofFloat(this, "rotation", 0f, 90f)
            animator.duration = 100
            animator.start()
            this.mode = UNFOLDMODE
        }else{
            val animator = ObjectAnimator.ofFloat(this, "rotation", 90f, 0f)
            animator.duration = 100
            animator.start()
            this.mode = FOLDMODE
        }
    }
}