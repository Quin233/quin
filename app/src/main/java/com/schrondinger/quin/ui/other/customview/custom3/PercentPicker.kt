package com.schrondinger.quin.ui.other.customview.custom3

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.orhanobut.logger.Logger

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2018/12/21 10:50 AM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class PercentPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr){

    private var lineHeight = 6
    private var lineWidth = 0
    private var circleRadius = 30
    private var circlePosition = circleRadius
    private var actionDownX = 0
    private var currentCircleX = circleRadius
    private var onMyMoveListener:OnMyMoveListener? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        lineWidth = measuredWidth
        setMeasuredDimension(lineWidth,circleRadius*2)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var paint= Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        canvas?.drawRect(circleRadius.toFloat(),(circleRadius-lineHeight/2).toFloat(),(lineWidth-circleRadius).toFloat(),(circleRadius+lineHeight/2).toFloat(),paint)

        paint.reset()
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
        canvas?.drawCircle(circlePosition.toFloat(),circleRadius.toFloat(),circleRadius.toFloat(),paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN->{
                actionDownX = event.x.toInt()
            }
            MotionEvent.ACTION_MOVE->{
                if ((currentCircleX + event.x.toInt()-actionDownX) <= circleRadius){
                    circlePosition =  circleRadius
                }else if((currentCircleX + event.x.toInt()-actionDownX) >= (lineWidth-circleRadius)){
                    circlePosition = lineWidth-circleRadius
                } else{
                    circlePosition = currentCircleX + event.x.toInt()-actionDownX
                }
                onMyMoveListener?.onMove(((circlePosition-circleRadius).toFloat())/(lineWidth-2*circleRadius).toFloat())
            }
            MotionEvent.ACTION_UP->{
                currentCircleX = circlePosition
            }
        }
        invalidate()   //重绘当前界面
        return true
    }


    interface OnMyMoveListener{ fun onMove(percent:Float)}
    fun setOnMyMoveListener(onMyMoveListener:OnMyMoveListener){
        this.onMyMoveListener = onMyMoveListener
    }
}
