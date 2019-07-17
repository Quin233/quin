package com.schrondinger.quin.widget

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import com.schrondinger.quin.R
import com.schrondinger.quin.Utils.Util


/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2019/1/22 5:29 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class PhonographView : View{

    private var mPhonographSrc:Int = R.drawable.music_bg_default
    private var mPhonographBitmapFile = ""
    private var mPhonographRadius:Int = 200 // 唱片机的半径
    private var mPhonographWidth:Int = 10 // 唱片机边缘画笔宽度

    private var mCanvasWidth = (mPhonographRadius+mPhonographWidth)*2 // 画布的宽度
    private var mCanvasHeight = (mCanvasWidth*1.35).toInt() // 画布的高度，默认宽度的1.35

    private lateinit var mPhonographBgBitmap:Bitmap
    private lateinit var mPhonographSrcBitmap:Bitmap

    private var animator:ObjectAnimator? = null // 唱片的动画
    private var animator2:ObjectAnimator? = null // 杆的动画
    private var animator3:ObjectAnimator? = null // 杆的动画

    private var progress = 0f // 唱片家唱片的旋转角度
        set(value){
            field = value
            invalidate()
        }

    private var progress2 = 0f // 唱片机杆的旋转角度
        set(value) {
            field = value
            if (!Util.isNullOrEmpty(animator) && (!animator!!.isStarted  || animator!!.isPaused) ){
                invalidate()
            }
        }

    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr) {
        val array = context.obtainStyledAttributes(attributeSet, R.styleable.PhonographView)
        mPhonographSrc = array.getColor(R.styleable.PhonographView_mSrc, R.mipmap.ic_launcher)
    }

    fun setSrc(src:Int){
        this.mPhonographSrc = src
        var  musicBitmap = getBitmap(mPhonographSrc,(mPhonographRadius*2)*7/10) // 音乐图片的宽度是唱片机的0.7
        val matrixMusic = Matrix()
        // 缩放原图
        var scaleX2 = ((mPhonographRadius*2)*7/10)/musicBitmap.width.toFloat()
        matrixMusic.postScale(scaleX2, scaleX2)
        mPhonographSrcBitmap = Bitmap.createBitmap(musicBitmap, 0, 0, musicBitmap.width, musicBitmap.height, matrixMusic, true)
        invalidate()
    }

    fun setFile(src: String){
        this.mPhonographBitmapFile = src
        var musicBitmap = getBitmap(mPhonographBitmapFile,(mPhonographRadius*2)*7/10) // 音乐图片的宽度是唱片机的0.7
        val matrixMusic = Matrix()
        var scaleX2 = ((mPhonographRadius*2)*7/10)/musicBitmap.width.toFloat()
        matrixMusic.postScale(scaleX2, scaleX2)
        mPhonographSrcBitmap = Bitmap.createBitmap(musicBitmap, 0, 0, musicBitmap.width, musicBitmap.height, matrixMusic, true)
        invalidate()
    }

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
            desiredWidth = (mPhonographRadius+mPhonographWidth) * 2 + paddingLeft + paddingRight
            if (widthMode == MeasureSpec.AT_MOST) {
                desiredWidth = Math.min(widthSize, desiredWidth)
            }
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            desiredHeight = heightSize
        }else {
            desiredHeight = ((mPhonographRadius+mPhonographWidth) * 2.7).toInt() + paddingLeft + paddingRight
            if (heightMode == MeasureSpec.AT_MOST) {
                desiredHeight = Math.min(heightSize, desiredHeight)
            }
        }

        if (desiredHeight < desiredWidth*1.35f){
            desiredWidth = (desiredHeight/1.35f).toInt()
        }else{
            desiredHeight = (desiredWidth*1.35f).toInt()
        }

        mCanvasWidth = desiredWidth
        mCanvasHeight = desiredHeight

        mPhonographRadius = mCanvasWidth/2-mPhonographWidth

        //
        // 获取唱片机的bitmap，并画出来
        var bitmap = getBitmap(R.drawable.phonograph_bg,mPhonographRadius*2)
        val matrix = Matrix()
        // 缩放原图
        var scaleX = (mPhonographRadius*2)/bitmap.width.toFloat()
        matrix.postScale(scaleX, scaleX)
        mPhonographBgBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

        //
        // 获取音乐的bitmap，并画出来
        var  musicBitmap = getBitmap(mPhonographSrc,(mPhonographRadius*2)*7/10) // 音乐图片的宽度是唱片机的0.7
        val matrixMusic = Matrix()
        // 缩放原图
        var scaleX2 = ((mPhonographRadius*2)*7/10)/musicBitmap.width.toFloat()
        matrixMusic.postScale(scaleX2, scaleX2)
        mPhonographSrcBitmap = Bitmap.createBitmap(musicBitmap, 0, 0, musicBitmap.width, musicBitmap.height, matrixMusic, true)


        setMeasuredDimension(mCanvasWidth,mCanvasHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.WHITE
        paint.strokeWidth = mPhonographWidth.toFloat()
        paint.style = Paint.Style.FILL

        canvas.drawCircle((mPhonographRadius+mPhonographWidth).toFloat(),(mCanvasHeight-mPhonographRadius-mPhonographWidth).toFloat(),mPhonographRadius.toFloat(),paint)

        canvas.save()
        canvas.translate((mPhonographRadius+mPhonographWidth).toFloat(),(mCanvasHeight-mPhonographRadius-mPhonographWidth).toFloat())
        canvas.rotate(progress)
        // 画唱片背景
        canvas.drawBitmap(mPhonographBgBitmap,-mPhonographRadius.toFloat(),-mPhonographRadius.toFloat(),paint)
        // 画音乐图片
        canvas.drawCircle(0f,0f,mPhonographRadius*0.7f,paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(mPhonographSrcBitmap,-mPhonographRadius*0.7f,-mPhonographRadius*0.7f,paint)
        canvas.restore()

        // 画顶部
        paint.reset()
        paint.isAntiAlias = true
        paint.color = Color.WHITE
        paint.strokeWidth = 10f
        canvas.drawLine(mCanvasWidth*0.2f,5f,mCanvasWidth*0.8f,5f,paint)
        canvas.drawCircle(mCanvasWidth*0.5f,0f,30f,paint)

        // 画唱片机 杆
        canvas.save()
        canvas.translate(mCanvasWidth*0.5f,0f)
        paint.reset()
        paint.isAntiAlias = true
        paint.color = Color.WHITE
        paint.strokeWidth = 20f
        paint.strokeJoin = Paint.Join.ROUND

        var  circlePaint = Paint()
        circlePaint.isAntiAlias = true
        circlePaint.color = Color.WHITE
        var lineLength = (mCanvasWidth/2) * 0.9f
        canvas.rotate(progress2)
        canvas.drawLine(0f,0f,lineLength*0.7f,lineLength*0.8f,paint)
        canvas.drawCircle(lineLength*0.7f,lineLength*0.8f,10f,circlePaint)
        canvas.drawLine(lineLength*0.7f,lineLength*0.8f,lineLength,lineLength,paint)
        canvas.drawCircle(lineLength,lineLength,26f,circlePaint)

    }

    fun start(){
        if (Util.isNullOrEmpty(animator) || !animator!!.isStarted){
            animator = ObjectAnimator.ofFloat(this, "progress", 0f, 360f)
            animator?.duration = 16000
            animator?.repeatCount = -1
            animator?.interpolator = LinearInterpolator()
            animator?.repeatMode = ValueAnimator.RESTART
            animator?.start()
            animator2 = ObjectAnimator.ofFloat(this, "progress2", 0f, 15f)
            animator2?.duration = 400
            animator2?.interpolator = LinearInterpolator()
            animator2?.start()
        }


    }

    fun pause(){
        if (!Util.isNullOrEmpty(animator) && animator!!.isRunning){
            animator?.pause()
            animator3 = ObjectAnimator.ofFloat(this, "progress2", 15f, 0f)
            animator3?.duration = 400
            animator3?.interpolator = LinearInterpolator()
            animator3?.start()
        }
    }

    fun stop(){
        if (!Util.isNullOrEmpty(animator) && animator!!.isStarted){
            animator3 = ObjectAnimator.ofFloat(this, "progress2", 15f , 0f)
            animator3?.duration = 400
            animator3?.interpolator = LinearInterpolator()
            animator3?.start()
        }
        animator?.end()
    }

    fun resume(){
        if (!Util.isNullOrEmpty(animator) && animator!!.isStarted){
            animator?.resume()
            animator2?.start()
        }
    }


    /**
     * resource：图片的资源id
     * width：期望宽度
     */
    private fun getBitmap(resource:Int,width:Int):Bitmap{

        var options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        options.inScaled = false // 禁止自动缩放

        val bmp = BitmapFactory.decodeResource(resources, resource, options) // 这里的bmp = null
        var height = options.outHeight*width/options.outWidth

        if (options.outWidth>width){
            options.inSampleSize = options.outWidth / width * 2 // 缩小内存占用
        }
        options.outWidth = width
        options.outHeight = height

        options.inPreferredConfig = Bitmap.Config.RGB_565 // 内存占用减少一半，舍弃了透明度，同时三色值也有部分损失，但是图片失真度很小。

        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(resources, resource, options)
    }
    private fun getBitmap(resource:String,width:Int):Bitmap{

        var options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        options.inScaled = false // 禁止自动缩放

        val bmp = BitmapFactory.decodeFile(resource, options) // 这里的bmp = null

        var height = options.outHeight*width/options.outWidth

        if (options.outWidth>width){
            options.inSampleSize = options.outWidth / width * 2 // 缩小内存占用
        }
        options.outWidth = width
        options.outHeight = height

        options.inPreferredConfig = Bitmap.Config.RGB_565 // 内存占用减少一半，舍弃了透明度，同时三色值也有部分损失，但是图片失真度很小。

        options.inJustDecodeBounds = false
        var xx =  BitmapFactory.decodeFile(resource, options)
        val canvas = Canvas(xx)

        return bmp
    }

}