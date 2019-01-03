package com.schrondinger.quin.ui.other.customview.custom4

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import java.util.*

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2018/12/28 11:06 AM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class PieChartView  @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr){

    private lateinit var dataList:Array<Array<String>> // 数据源
    private var colorList = arrayListOf<Int>() // 颜色列表
    private var totalData = 0 // 总数据量

    private var xLength = 500 // 默认的宽度
    private var yLength = 300 // 默认高度

    private  var desiredWidth = xLength
    private  var desiredHeight = yLength

    fun setData(dataList:Array<Array<String>>){
        this.dataList = dataList
        for (i in dataList.indices){
            totalData += dataList[i][1].toInt()
            colorList.add(randomColor())
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        if (widthMode == MeasureSpec.EXACTLY) {
            desiredWidth = widthSize
        } else {
            desiredWidth = xLength + paddingLeft + paddingRight
            if (widthMode == MeasureSpec.AT_MOST) {
                desiredWidth = Math.min(widthSize, desiredWidth)
            }
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            desiredHeight = heightSize
        } else {
            desiredHeight = yLength + paddingTop + paddingBottom
            if (heightMode == MeasureSpec.AT_MOST) {
                desiredHeight = Math.min(heightSize, desiredHeight)
            }
        }
        setMeasuredDimension(desiredWidth,desiredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)

        var sweepArc = 0f
        var startArc = 0f
        var rectF = RectF(50f+20f,50f+20f,(desiredHeight-50f-20f)*1.3f,desiredHeight-50f-20f)
        var paint = Paint(Paint.ANTI_ALIAS_FLAG)
        for (i in dataList.indices){
            paint.color = colorList[i]
            sweepArc = dataList[i][1].toFloat()/totalData.toFloat()*360f
            canvas.drawArc(rectF,startArc,sweepArc,true,paint)
            startArc += sweepArc

            paint.reset()
            paint.isAntiAlias = true
            paint.color = Color.BLACK
            paint.textSize = 35f
            canvas.drawText(dataList[i][0]+"："+dataList[i][1],(desiredHeight-50f-20f)*1.3f + 80f ,(70f+(desiredHeight-50f-20f-50f-20f)*(i.toFloat()/dataList.size.toFloat())),paint)
        }

    }

    private fun randomColor():Int {
        var random = Random()
        var r = random . nextInt (128)
        var g = random . nextInt (128)
        var b = random . nextInt (128)
        return Color.rgb(r, g, b)
    }
}