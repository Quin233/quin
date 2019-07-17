package com.schrondinger.quin.ui.other.customview.custom4

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2019/1/24 5:02 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class DiscountGroupView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var textSize = 0f // 文字的大小
    private var mCanvasWidth = 100 // 默认画布的宽度
    private var mCanvasHeight = 100 // 默认画布的高度

    private var lineOneCount = 0 // 第一行显示几个
    private var lineTwoCount = 0 // 第二行显示几个

    private var lineOne = 0f // 计算时第一行文字的长度
    private var lineTwo = 0f // 计算时第二行文字的长度

    private var lineHeight = 20 // 两行文字之间的间隔

    private var textPadding = 10 // 两段文字之间的间隔

    private var textLeftPadding = 15 // 每段文字左右的padding（即背景色比文字多出来的部分）



    private var textList = arrayListOf<String>()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        var desiredWidth: Int
        var desiredHeight: Int

        if (widthMode == MeasureSpec.EXACTLY) {
            desiredWidth = widthSize
        }else {
            desiredWidth = mCanvasWidth + paddingLeft + paddingRight
            if (widthMode == MeasureSpec.AT_MOST) {
                desiredWidth = Math.min(widthSize, desiredWidth)
            }
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            desiredHeight = heightSize
        }else {
            desiredHeight = mCanvasHeight + paddingLeft + paddingRight
            if (heightMode == MeasureSpec.AT_MOST) {
                desiredHeight = Math.min(heightSize, desiredHeight)
            }
        }

        mCanvasWidth = desiredWidth
        mCanvasHeight = desiredHeight
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (textList.size<=0)return

        lineOneCount = 0
        lineTwoCount = 0

        var paint = Paint()

        paint.isAntiAlias = true

        // 获取文字所占高度 textHeight
        var bottom = paint.fontMetrics.bottom
        textSize = ((mCanvasHeight-lineHeight) / 2 - bottom-10)
        paint.textSize = textSize
        var textHeight = textSize+bottom+10


        // 初始化第一行文字的长度
        lineOne = paint.measureText(textList[0])+textLeftPadding*2+textPadding

        for (i in textList.indices){
            if (lineOne<= mCanvasWidth){
                // 显示的个数
                lineOneCount += 1
                lineOne +=  (paint.measureText(textList[i+1])+textPadding+textLeftPadding*2)

                // 初始化第二行文字第一个的长度
                lineTwo = paint.measureText(textList[i+1])+textPadding+textLeftPadding*2
            }else{
                // 如果不是最后一个，长度就要加一个...的长度
//                if (i != textList.size-1 && lineTwo + paint.measureText("...")<=mCanvasWidth){
//                    lineTwoCount += 1
//                    lineTwo += (paint.measureText(textList[i+1])+textPadding+textLeftPadding*2)
//                }else if(i == textList.size-1 && lineTwo <=mCanvasWidth){ // 如果是最后一个
//                    lineTwoCount += 1
//                    lineTwo += (paint.measureText(textList[i+1])+textPadding+textLeftPadding*2)
//                }else{
//                    break
//                }
                if(lineTwo<=mCanvasWidth){
                    lineTwoCount += 1
                    lineTwo += (paint.measureText(textList[i+1])+textPadding+textLeftPadding*2)
                }else{
                    break
                }
            }
        }

        // 文字背景颜色的画笔
        var colorPaint = Paint()
        colorPaint.isAntiAlias = true
        colorPaint.style = Paint.Style.FILL
        colorPaint.color = Color.YELLOW

        var lineOneTextX = textPadding.toFloat()+textLeftPadding
        if(lineOneCount == 1){
            // 画第一行第一个文字和背景色
            canvas?.drawRect(textPadding.toFloat(),0f,lineOneTextX+paint.measureText(textList[0])+textLeftPadding,textHeight,colorPaint)
            canvas?.drawText(textList[0],lineOneTextX,textSize,paint)
        }

        for (i in 0 .. (lineOneCount-1)){
            if (i == 0){
                // 画第一行第一个文字
                canvas?.drawRect(textPadding.toFloat(),0f,lineOneTextX+paint.measureText(textList[0])+textLeftPadding,textHeight,colorPaint)
                canvas?.drawText(textList[i],lineOneTextX,textSize,paint)
            }else{
                lineOneTextX += lineOneTextX+paint.measureText(textList[i-1])+textPadding+textLeftPadding*2

                // 画第一行后面的文字
                canvas?.drawRect(lineOneTextX-textLeftPadding,0f,lineOneTextX+paint.measureText(textList[i])+textLeftPadding,textHeight,colorPaint)
                canvas?.drawText(textList[i],lineOneTextX,textSize,paint)
            }
        }



        var lineTwoTextX = textPadding.toFloat()+textLeftPadding

        if (lineTwoCount == 1){
            // 画第二行第一个文字
            canvas?.drawRect(textPadding.toFloat(),lineHeight+textHeight,lineTwoTextX+paint.measureText(textList[lineOneCount])+textLeftPadding,lineHeight+textSize*2,colorPaint)
            canvas?.drawText(textList[lineOneCount],lineTwoTextX,lineHeight+textSize*2,paint)
        }

        for (i in  lineOneCount .. (lineOneCount+lineTwoCount-1)){
            if (i == lineOneCount){
                // 画第二行第一个文字
                canvas?.drawRect(textPadding.toFloat(),lineHeight+textHeight,lineTwoTextX+paint.measureText(textList[i])+textLeftPadding,lineHeight+textHeight*2,colorPaint)
                canvas?.drawText(textList[i],lineTwoTextX,lineHeight+textHeight+textSize,paint)
            }else{
                lineTwoTextX += lineTwoTextX+paint.measureText(textList[i-1])+textPadding+textLeftPadding*2

                // 画第二行后面的文字
                canvas?.drawRect(lineTwoTextX-textLeftPadding,lineHeight+textHeight,lineTwoTextX+paint.measureText(textList[i])+textLeftPadding,lineHeight+textHeight*2,colorPaint)
                canvas?.drawText(textList[i],lineTwoTextX,lineHeight+textHeight+textSize,paint)
            }
        }
    }

    fun setData(dateSrc:ArrayList<String>){
        this.textList = dateSrc
        invalidate()
    }
}