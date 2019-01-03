package com.schrondinger.quin.ui.other.customview.custom3

import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.View
import com.schrondinger.quin.R
import android.content.res.TypedArray
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2018/12/21 4:57 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class SpeedometerView : View{

    var mWidth = 0
    var mHeight = 0
    var strokeWidth = 20 // 线条宽度

    var progress = 0f // 进度
        set(value){
            field = value
            invalidate()
        }



    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measuredWidth
        mHeight = measuredWidth*8/10
        setMeasuredDimension(mWidth,mHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var paint= Paint(Paint.ANTI_ALIAS_FLAG)

        val position = FloatArray(3)
        position[0] = 0.0f
        position[1] = 0.66f
        position[2] = 1.0f


        val shader = SweepGradient(mWidth/2.toFloat(), mWidth/2.toFloat(), intArrayOf(Color.GREEN, Color.RED, Color.BLUE), position)
        var matrix = Matrix()
        matrix.setRotate(150f, mWidth/2.toFloat(), mWidth/2.toFloat())
        shader.setLocalMatrix(matrix)
        paint.shader = shader
        paint.style = Paint.Style.STROKE // 画线模式
//        paint.strokeWidth = strokeWidth.toFloat() // 画笔宽度
//        paint.strokeCap = Paint.Cap.ROUND // 圆头模式

        var dashPath = Path()
        dashPath.addRect(0f,0f,6f,16f,Path.Direction.CCW)
        val pathEffect = PathDashPathEffect(dashPath, 10f, 0f, PathDashPathEffect.Style.ROTATE)
        paint.pathEffect = pathEffect
        canvas?.drawArc((strokeWidth/2).toFloat()+16f, (strokeWidth/2).toFloat()+16f, (mWidth-strokeWidth/2).toFloat()-16f, (mWidth-strokeWidth/2).toFloat()-16f, 150f, progress, false, paint)

        paint.reset()
        paint.color = Color.BLACK
        paint.textSize = 50f
        paint.textAlign = Paint.Align.CENTER

        var  fontMetrics = paint.fontMetrics
        var top = fontMetrics.top //为基线到字体上边框的距离
        var bottom = fontMetrics.bottom //为基线到字体下边框的距离

        var baseLineY = (mHeight/2 - top/2 - bottom/2) //基线中间点的y轴

        var percent = progress/240f

        canvas?.drawText(220.times(percent).toInt().toString() + "Km/h",mWidth/2.toFloat(),baseLineY,paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN->{
                val animator = ObjectAnimator.ofFloat(this, "progress", progress, 240f)
                animator.duration = 2000
                animator.interpolator = AccelerateDecelerateInterpolator()
                animator.start()
            }
            MotionEvent.ACTION_MOVE->{

            }
            MotionEvent.ACTION_UP->{
                val animator = ObjectAnimator.ofFloat(this, "progress", progress, 0f)
                animator.duration = 2000
                animator.start()
            }
        }
        return true

    }
}

