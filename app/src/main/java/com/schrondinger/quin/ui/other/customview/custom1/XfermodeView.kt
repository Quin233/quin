package com.schrondinger.quin.ui.other.customview.custom1

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2019/2/19 5:45 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class XfermodeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr){
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var paint = Paint()
        //设置背景色
        canvas.drawARGB(255, 139, 197, 186);

        var canvasWidth = canvas.width
        var r = canvasWidth / 3f
        //绘制黄色的圆形
        paint.color = Color.YELLOW
        canvas.drawCircle(r, r, r, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
        //绘制蓝色的矩形
        paint.color = Color.BLUE
        canvas.drawRect(r, r, r * 2.7f, r * 2.7f, paint)

        paint.xfermode = null
    }
}