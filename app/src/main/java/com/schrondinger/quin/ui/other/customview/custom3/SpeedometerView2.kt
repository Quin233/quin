package com.schrondinger.quin.ui.other.customview.custom3

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.schrondinger.quin.R

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2018/12/24 5:10 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class SpeedometerView2 : View {

    private var mRadius = 100// 时速表的半径，默认100
    private var mScaleLength = (mRadius * 1.0f / 10).toInt() // 刻度的长度，1/10的时速表半径
    private var mPointerColor = Color.RED // 指针的颜色，默认红色
    private var mInnerCircleRadius = (mRadius * 1.0f / 3).toInt() // 内部圆半径，1/3的时速表半径
    private var mInnerCircleColor = Color.WHITE // 内部圆颜色，默认白色

    private var mCanvasWidth = mRadius*2 // 画布的宽度，默认时速表的直径
    private var mCanvasHeight = mCanvasWidth*8/10 // 画布的高度，默认时速表宽度的4/5

    private var mThickColor = Color.WHITE // 粗刻度的颜色，默认白色
    private var mThinColor = Color.WHITE // 细刻度的颜色，默认白色
    private var mThickTextColor = Color.WHITE // 粗刻度对应时速字体的颜色，默认白色
    private var mThinTextColor = Color.WHITE // 细刻度对应时速字体的颜色，默认白色
    private var mSpeedTextColor = Color.WHITE // 时速字体的颜色，默认白色
    private var mThickTextSize = 25f // 粗刻度对应时速字体的大小，默认25f
    private var mThinTextSize = 22f // 细刻度对应时速字体的颜色，默认22f
    private var mSpeedTextSize = 30f // 时速字体的大小，默认30f

    private var percent = 0f // 百分比
    private var pointProgress = -210f // 指针角度
        set(value){
            field = value
            invalidate()
        }

    private val thickScale = arrayOf(-30f,24f,24f,24f,24f,24f,24f,24f,24f,24f,24f)
    private val textVul = arrayOf("100","110","120","130","140","150","160","170","180","190","200","","","","","","","","","","0","10","20","30","40","50","60","70","80","90")
    private val thinScale = arrayOf(132f,24f,24f,24f,24f,24f,24f,24f,24f,24f)

    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr) {
        val array = context.obtainStyledAttributes(attributeSet, R.styleable.SpeedometerView2)
        mPointerColor = array.getColor(R.styleable.SpeedometerView2_mPointerColor, Color.RED)
        mThickTextSize = array.getInt(R.styleable.SpeedometerView2_mThickTextSize, 25).toFloat()
        mThickTextColor = array.getColor(R.styleable.SpeedometerView2_mThickTextColor, resources.getColor(R.color.colorWhite))
        mThinTextSize = array.getInt(R.styleable.SpeedometerView2_mThinTextSize, 25).toFloat()
        mThinTextColor = array.getColor(R.styleable.SpeedometerView2_mThinTextColor, resources.getColor(R.color.colorWhite))
        mSpeedTextColor = array.getColor(R.styleable.SpeedometerView2_mSpeedTextColor, resources.getColor(R.color.colorWhite))
        mSpeedTextSize = array.getInt(R.styleable.SpeedometerView2_mSpeedTextSize, 30).toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)

        var desiredWidth: Int
        var desiredHeight: Int

        if (widthMode == MeasureSpec.EXACTLY) {
            desiredWidth = widthSize
        } else {
            desiredWidth = mRadius * 2 + paddingLeft + paddingRight
            if (widthMode == MeasureSpec.AT_MOST) {
                desiredWidth = Math.min(widthSize, desiredWidth)
            }
        }
        desiredHeight = desiredWidth*8/10

        mCanvasWidth = desiredWidth+4
        mCanvasHeight = desiredHeight+2
        setMeasuredDimension(mCanvasWidth,mCanvasHeight)
        // 计算时速表的半径、刻度的长度、内圆的半径
        mRadius = ((desiredWidth - paddingLeft - paddingRight) * 1.0f / 2).toInt()
        mScaleLength = (mRadius * 1.0f / 10).toInt() // 刻度的长度，1/10的半径
        mInnerCircleRadius = (mRadius * 1.0f / 3).toInt() // 内部圆半径，1/3的时速表半径
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var paint= Paint(Paint.ANTI_ALIAS_FLAG)

        canvas.drawColor(Color.rgb(65,38,38))

        // 画外圆
        paint.color = Color.WHITE
        // 外圆线的宽度为2个像素
        paint.strokeWidth = 2f
        // 绘制空心圆
        paint.style = Paint.Style.STROKE
        // 圆头模式
        paint.strokeCap = Paint.Cap.ROUND
        // 将坐标原点移到时速表的圆心
        canvas.translate(mCanvasWidth * 1.0f / 2 + paddingLeft - paddingRight,mCanvasWidth * 1.0f / 2 + paddingTop - paddingBottom)
        // 绘制外圆
        canvas.drawCircle(0f,0f,mRadius.toFloat(),paint)

        // 绘制时速表背景色
        paint.reset()
        paint.isAntiAlias = true
        paint.color = Color.rgb(25,2,2)
        canvas.drawCircle(0f,0f,mRadius.toFloat()-2f,paint)

        // 保存正常的画布角度
        canvas.save()
        // 绘制粗刻度
        paint.reset()
        paint.isAntiAlias = true
        paint.color = mThickColor
        // 刻度的宽度为8个像素
        paint.strokeWidth = 8f
        for ( i in thickScale.indices){
            canvas.rotate(thickScale[i], 0f, 0f)
            canvas.drawLine(-mRadius.toFloat(),0f,(mScaleLength-mRadius).toFloat(),0f,paint)
        }

        // 绘制细刻度
        paint.reset()
        paint.isAntiAlias = true
        paint.color = mThinColor
        // 刻度的宽度为5个像素
        paint.strokeWidth = 5f
        for ( i in thinScale.indices){
            canvas.rotate(thinScale[i], 0f, 0f)
            canvas.drawLine(-mRadius.toFloat(),0f,(mScaleLength*3/5-mRadius).toFloat(),0f,paint)
        }

        // 取出上面保存的画布角度
        canvas.restore()

        // 绘制内圆
        paint.reset()
        paint.isAntiAlias = true
        paint.color = mInnerCircleColor
        paint.strokeWidth = 4f
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(0f,0f,mInnerCircleRadius.toFloat(),paint)

        // 绘制数字
        paint.reset()
        paint.isAntiAlias = true
        paint.textAlign = Paint.Align.CENTER
        for ( i in textVul.indices){
            when(i.rem(2)){
                0->{
                    // 粗刻度字体设置
                    paint.textSize = mThickTextSize
                    paint.color = mThickTextColor
                    paint.isFakeBoldText = true
                }
                1->{
                    // 细刻度字体设置
                    paint.textSize = mThinTextSize
                    paint.color = mThinTextColor
                    paint.isFakeBoldText = false
                }
            }
            canvas.save()
            canvas.translate(0f,-mRadius*5.5f/7)
            canvas.rotate(-i*12f)
            var  fontMetrics = paint.fontMetrics
            var top = fontMetrics.top //为基线到字体上边框的距离
            var bottom = fontMetrics.bottom //为基线到字体下边框的距离

            canvas.drawText(textVul[i], 0f, 0f- top/2 - bottom/2, paint)
            canvas.restore()
            canvas.rotate(12f)
        }

        // 绘制指针
        canvas.save()
        canvas.rotate(pointProgress)
        paint.reset()
        paint.isAntiAlias = true
        paint.color = mPointerColor
        var pointPath = Path()
        pointPath.fillType = Path.FillType.EVEN_ODD
        var a = Math.toRadians(15.0)
        pointPath.moveTo((Math.cos(a)*(mInnerCircleRadius-7f)).toFloat(),-(Math.sin(a)*mInnerCircleRadius).toFloat())
        pointPath.lineTo((Math.cos(a)*(mInnerCircleRadius-7f)).toFloat()*1.5f,-8f)
        pointPath.lineTo((mRadius-mScaleLength*3/5).toFloat(),0f)
        pointPath.lineTo((Math.cos(a)*(mInnerCircleRadius-7f)).toFloat()*1.5f,8f)
        pointPath.lineTo((Math.cos(a)*(mInnerCircleRadius-7f)).toFloat(),(Math.sin(a)*mInnerCircleRadius).toFloat())
        pointPath.close()
        canvas.drawPath(pointPath,paint)
        canvas.restore()


        // 绘制内部实心圆
        paint.color = Color.rgb(25,2,2)
        paint.style = Paint.Style.FILL
        canvas.drawCircle(0f,0f,mInnerCircleRadius.toFloat()-2f,paint)

        // 绘制指针带的圆
        paint.color = mPointerColor
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(0f,0f,mInnerCircleRadius-7f,paint)

        // 绘制速度
        paint.reset()
        paint.color = mSpeedTextColor
        paint.textSize = mSpeedTextSize
        paint.textAlign = Paint.Align.CENTER
        var  fontMetrics = paint.fontMetrics
        var top = fontMetrics.top //为基线到字体上边框的距离
        var bottom = fontMetrics.bottom //为基线到字体下边框的距离
        var baseLineY = (0 - top/2 - bottom/2) //基线中间点的y轴

        percent = (pointProgress+210f)/240f

        canvas.drawText(200.times(percent).toInt().toString() + "Km/h",0f,baseLineY,paint)


    }


    fun start(){
        val animator = ObjectAnimator.ofFloat(this, "pointProgress", pointProgress, 30f)
        animator.duration = 2000
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.start()
    }

    fun stop(){
        val animator = ObjectAnimator.ofFloat(this, "pointProgress", pointProgress, -210f)
        animator.duration = 2000
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.start()
    }


}