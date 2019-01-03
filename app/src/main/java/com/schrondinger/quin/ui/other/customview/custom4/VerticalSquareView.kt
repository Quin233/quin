package com.schrondinger.quin.ui.other.customview.custom4

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2018/12/26 2:42 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class VerticalSquareView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr){

    private val CANVASPOSITIONX = 100 // 坐标离画布左边的距离
    private val CANVASPOSITIONY = 60 // 坐标离画布下边的距离

    private var MODE = 1 // 柱状图模式：1；折线图模式：0

    private lateinit var dataList:Array<Array<String>> // 数据源
    private var actionDownX = 0 // 按下时候x的位置
    private var actionDownY = 0 // 按下时候y的位置
    private var canvasPosition = CANVASPOSITIONX // 坐标离画布左边的距离
    private var currentPosition = CANVASPOSITIONX

    private var squareList = arrayListOf<Square>() // 所有柱状图的坐标信息

    private var selectSquare = -1 // 被选中的柱状图的角标

    private var widthPixels = resources.displayMetrics.widthPixels

    private var squareWidth = 50f // 柱状图的宽度

    private var yMaxValue = 0f // 数据里面最大值
    private var yHeight = 0 // y轴刻度的最高值
    private var maxValue = 0 // y轴刻度的最大值
    private var parts = 4 // y轴刻度的份数


    private var xLength = 500
    private var yLength = 300

    private  var desiredWidth = xLength
    private  var desiredHeight = yLength

    fun setData(dataList:Array<Array<String>>):VerticalSquareView{
        this.dataList = dataList
        for(i in dataList.indices){
            yMaxValue = if (yMaxValue<dataList[i][1].toFloat()) dataList[i][1].toFloat() else yMaxValue
        }
        return this
    }

    fun setMax_Parts(maxValue:String,parts:Int){
        this.maxValue = maxValue.toInt()
        this.parts = parts
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
        if (maxValue == 0 || maxValue/parts == 0){
            maxValue = yMaxValue.toInt()
        }
        yHeight = (desiredHeight-paddingTop-paddingBottom) * 7/10
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        var xs = dataList.size // x轴的个数
        xLength = (2*xs+1)*squareWidth.toInt()

        // 将坐标移至左下角
        canvas.translate(canvasPosition.toFloat(),desiredHeight-CANVASPOSITIONY.toFloat())
        var paint = Paint(Paint.ANTI_ALIAS_FLAG)
        canvas.drawLine(0f,0f,xLength.toFloat(),0f,paint)


        // 画柱状图（折线图）和x轴的数据
        if (squareList.size !=0) squareList.clear()
        // 折线图需要的Path
        var linePath = Path()
        for(i in dataList.indices){
            // 柱状图的高度
            var y = yHeight  * (dataList[i][1].toFloat()/yMaxValue)
            when(MODE){
                1->{
                    // 画柱状图
                    drawR(canvas,i,y)
                }
                0->{
                    //画折线图
                    drawL(canvas,i,y,linePath)
                    var paint1 = Paint(Paint.ANTI_ALIAS_FLAG)
                    paint1.color = Color.RED
                    paint1.style = Paint.Style.STROKE
                    paint1.strokeWidth = 2f
                    canvas.drawPath(linePath,paint1)
                }
            }

            // 话x轴的刻度
            paint.color = Color.BLACK
            paint.textSize = 35f
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(dataList[i][0],(2*i+1.5f)*squareWidth,35f,paint)
        }

        // 画y轴
        paint.strokeWidth = 1f
        canvas.drawLine((CANVASPOSITIONX-canvasPosition).toFloat(),0f, (CANVASPOSITIONX-canvasPosition).toFloat(),-0.9f*(desiredHeight-paddingTop-paddingBottom),paint)
        // 画y轴左边白色部分
        paint.color = Color.WHITE
        canvas.drawRect(-canvasPosition.toFloat(),-yHeight.toFloat(),(CANVASPOSITIONX-canvasPosition).toFloat()-1f,CANVASPOSITIONX.toFloat(),paint)
        // 画y轴刻度
        paint.color = Color.BLACK
        for (i in 1..parts){
            canvas.drawLine((CANVASPOSITIONX-canvasPosition-20).toFloat(),-(i.toFloat()/parts.toFloat())*yHeight,(CANVASPOSITIONX-canvasPosition).toFloat(),-(i.toFloat()/parts.toFloat())*yHeight,paint)
        }
        // 画y轴刻度数
        paint.textSize = 40f
        for (i in 0..maxValue step maxValue/parts){
            canvas.drawText(i.toString(),(-canvasPosition+40).toFloat(),-(i.toFloat()/maxValue.toFloat())*yHeight,paint)
        }
        // 画柱状图和饼状图的切换图标
        canvas.drawText(if (MODE == 1) "柱" else  "折",(-canvasPosition+40).toFloat(),-(yHeight)-80f,paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN->{
                actionDownX = event.x.toInt()
                actionDownY = event.y.toInt()

                selectSquare = pointInSquareList(actionDownX-canvasPosition,actionDownY-desiredHeight+CANVASPOSITIONY)
                if (isPointInSquare(actionDownX, actionDownY,Square(0f,0f,CANVASPOSITIONX.toFloat(),desiredHeight.toFloat()-yHeight.toFloat()))){
                    if (MODE == 1) MODE = 0 else MODE = 1
                }
            }
            MotionEvent.ACTION_MOVE->{

                canvasPosition = currentPosition + event.x.toInt()-actionDownX
                if (canvasPosition > CANVASPOSITIONX){
                    canvasPosition = CANVASPOSITIONX
                }
                if (canvasPosition < (widthPixels-xLength-30)){
                    canvasPosition = (widthPixels-xLength-30)
                }
            }
            MotionEvent.ACTION_UP->{
                currentPosition = canvasPosition
            }
        }
        invalidate()   //重绘当前界面
        return true
    }


    /**
     * 折线图模式
     */
    private fun drawL(canvas: Canvas,i: Int,y:Float,linePath: Path){
        if (i == 0){
            linePath.moveTo((2*i+1.5f)*squareWidth,-y)
        }else{
            linePath.lineTo((2*i+1.5f)*squareWidth,-y)
        }

        var paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        paint.textSize = 35f
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(dataList[i][1],(2*i+1.5f)*squareWidth,-y-4f,paint)
    }


    /**
     * 柱状图模式
     */
    private fun drawR(canvas:Canvas,i:Int,y:Float){
        var paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.RED
        paint.strokeWidth = squareWidth
        canvas.drawRect((2*i+1)*squareWidth,-y,(2*i+2)*squareWidth,0f,paint)
        squareList.add(Square((2*i+1)*squareWidth,-y,(2*i+2)*squareWidth,0f))


        paint.reset()
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        paint.textSize = 35f
        paint.textAlign = Paint.Align.CENTER

        if (i == selectSquare){
            canvas.drawText(dataList[i][1],(2*i+1.5f)*squareWidth,-y-4f,paint)
        }
    }


    private data class Square(val left:Float,val top:Float, val right:Float,val bottom:Float)

    private fun pointInSquareList(pointX:Int,pointY: Int):Int{
        for (i in squareList.indices){
            if (isPointInSquare(pointX,pointY,squareList[i])){
                return i
            }
        }
        return -1
    }
    private fun isPointInSquare(pointX:Int,pointY: Int,square: Square): Boolean{
        Log.d("test1","pointX=$pointX,pointY=$pointY,square.left=${square.left},square.right=${square.right},square.top=${square.top},square.bottom=${square.bottom}.")
        if (pointX>=square.left && pointX<=square.right && pointY>= square.top && pointY<=square.bottom){
            return true
        }
        return false
    }
}