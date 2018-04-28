package com.schrongder.nba.kotlin

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.annotation.IntDef
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.facebook.rebound.SimpleSpringListener
import com.facebook.rebound.Spring
import com.facebook.rebound.SpringConfig
import com.facebook.rebound.SpringSystem
import com.schrondinger.quin.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


/**
 * Created by hp on 2018/1/19.
 *
 */

class TopToast(private var activiry:AppCompatActivity) {
    private val decorView: ViewGroup
    private val view: View?
    private var totalHeight = 0
    private var showing = false
    private val animationDuration = 800
    private val icon: ImageView?
    private val mTvContent:TextView?


    @IntDef(LENGTH_SHORT, LENGTH_LONG, LENGTH_SHOW)
    //@Retention(RetentionPolicy.SOURCE)
    @kotlin.annotation.Retention
    annotation class Duration


    companion object {
        const val  SUCCESS = 0
        const val WARRING = SUCCESS + 1
        const val  Error = WARRING + 1
        const val LENGTH_SHORT = 1500

        const val LENGTH_LONG = 2000

        const val LENGTH_SHOW = -1
        fun creat(activiry: AppCompatActivity): TopToast {
            return TopToast(activiry)
        }
    }

    init {
         decorView =activiry.window.decorView as ViewGroup
        view = View.inflate(activiry, R.layout.kotlin_item_toast, null)
        icon = view.findViewById(R.id.icon) as ImageView
        mTvContent = view.findViewById(R.id.tv_content) as TextView
        totalHeight = getHeight(activiry,view.findViewById(R.id.status_bar))
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,totalHeight)
    }


    fun showType(type: Int): TopToast {
        when(type) {
            SUCCESS -> {icon!!.setImageResource(R.drawable.success)}
            WARRING ->{icon!!.setImageResource(R.drawable.warring)}
            Error ->{icon!!.setImageResource(R.drawable.error)}
            else ->{icon!!.setImageResource(R.drawable.success)}
        }
        return this
    }

    fun hideIcon(hide:Boolean): TopToast {
        if (hide) icon!!.visibility = View.GONE else icon!!.visibility = View.VISIBLE
        return this
    }

    fun setIcon(resourceId:Int): TopToast {
        icon!!.setImageResource(resourceId)
        return this
    }

    fun setIcon(drawable:Drawable): TopToast {
        icon!!.setImageDrawable(drawable)
        return this
    }

    fun setIcon(bitmap:Bitmap): TopToast {
        icon!!.setImageBitmap(bitmap)
        return this
    }

    fun setBackgroundColorInt(color: Int): TopToast {
        view!!.setBackgroundColor(color)
        return this
    }

    fun setBackground(drawable: Drawable): TopToast {
        view!!.background = drawable
        return this
    }

    fun setBackgroundColorResourceId(resourceId: Int): TopToast {
        view!!.setBackgroundResource(resourceId)
        return this
    }


   fun duration(@Duration duration:Int): TopToast {
        if (duration != LENGTH_SHOW) {
            Observable.just(view).delay(duration.toLong(),TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        cancel()
                    }
        }
        return this
    }



   fun show() {
            if (!showing) {
                showing = true
                decorView!!.addView(view)
                //平移动画    无弹簧效果    已废弃
                /*val floatArrayOf = floatArrayOf(-totalHeight.toFloat(), 60f,50f,40f,30f,20f,0f)
                val animator = ObjectAnimator.ofFloat(view, "translationY",*floatArrayOf )
                animator.interpolator = LinearInterpolator()
                animator.setDuration(animationDuration.toLong())
                animator.start()*/
                val springSystem = SpringSystem.create()
                val spring = springSystem.createSpring()
                spring.currentValue = (-totalHeight).toDouble()
                spring.springConfig = SpringConfig.fromOrigamiTensionAndFriction(44.0,4.0)
                spring.addListener(object : SimpleSpringListener(){
                    override fun onSpringUpdate(spring: Spring?) {
                        val value = spring!!.currentValue
                        view!!.translationY = value.toFloat()
                    }
                })
                spring.setEndValue(0.0)
            }
    }

  fun isShowing(): Boolean {
        return showing
    }


    fun setMessageTextColor(color:Int): TopToast {
        mTvContent!!.setTextColor(color)
        return this
    }

   fun setMessageTextSize(size:Float): TopToast {
        mTvContent!!.textSize = size
        return this
    }
    fun setMessageText(msg:String): TopToast {
        mTvContent!!.text = msg
        return this
    }

    private fun cancel(){
        val floatArrayOf = floatArrayOf(0f, -totalHeight.toFloat()+30,-totalHeight.toFloat()+10,-totalHeight.toFloat())
        val animator = ObjectAnimator.ofFloat(view, "translationY", *floatArrayOf)
        animator.setDuration(animationDuration.toLong())
        animator.addListener(object :AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
               if (view!!.parent != null){
                   (view.parent as ViewGroup).removeView(view)
                   showing = false
               }
            }
        })
    animator.start()
    }

   private fun getHeight(context:Context,v:View):Int {
        val values = context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        val actionBarHeight = values.getDimensionPixelSize(0, 0)
        values.recycle()
        var statusBarHeights = 0
        val resourcesId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourcesId > 0) {
            statusBarHeights = context.resources.getDimensionPixelSize(resourcesId)
            v.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,statusBarHeights)
        }
        return actionBarHeight + statusBarHeights;
    }
}