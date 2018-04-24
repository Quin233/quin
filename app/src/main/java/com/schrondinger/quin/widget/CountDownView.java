package com.schrondinger.quin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.schrondinger.quin.R;


/**
 * Created by HP on 2018/1/3.
 */

public class CountDownView extends View {

    private static final int BACKGROUND_COLOR = 0x50555555;
    private static final float BORDER_WIDTH = 15f;
    private static final int BORDER_COLOR = 0xFF6ADBFE;
    private static final String TEXT = "跳过";
    private static final float TEXT_SIZE = 50f;
    private static final int TEXT_COLOR = 0xFFFFFFFF;

    private CountDownTimerListener listener;
    private CountDownTimer countDownTimer;
    private float progress = 360;
    private StaticLayout staticLayout;

    private int backgroundColor;
    private float borderWidth;
    private int borderColor;
    private String text;
    private int textColor;
    private float textSize;

    private Paint circlePaint;
    private TextPaint textPaint;
    private Paint borderPaint;

    public CountDownView(Context context) {
        super(context);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
        backgroundColor = ta.getColor(R.styleable.CountDownView_background_color, BACKGROUND_COLOR);
        borderWidth = ta.getDimension(R.styleable.CountDownView_border_width, BORDER_WIDTH);
        borderColor = ta.getColor(R.styleable.CountDownView_border_color, BORDER_COLOR);
        text = ta.getString(R.styleable.CountDownView_text);
        if (text == null) {
            text = TEXT;
        }
        textSize = ta.getDimension(R.styleable.CountDownView_text_size, TEXT_SIZE);
        textColor = ta.getColor(R.styleable.CountDownView_text_color, TEXT_COLOR);
        ta.recycle();
        init();
    }
    private void init(){
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setDither(true);
        circlePaint.setColor(backgroundColor);
        circlePaint.setStyle(Paint.Style.FILL);

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);

        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setDither(true);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setStyle(Paint.Style.STROKE);

        int textWidth = (int) textPaint.measureText(text);
        staticLayout = new StaticLayout(text, textPaint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            width = staticLayout.getWidth();
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            height = staticLayout.getHeight();
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int min = Math.min(width, height);
        //画底盘
        canvas.drawCircle(width / 2, height / 2, min / 2, circlePaint);
        //画边框
        RectF rectF;
        if (width > height) {
            rectF = new RectF(width / 2 - min / 2 + borderWidth / 2, 0 + borderWidth / 2, width / 2 + min / 2 - borderWidth / 2, height - borderWidth / 2);
        } else {
            rectF = new RectF(borderWidth / 2, height / 2 - min / 2 + borderWidth / 2, width - borderWidth / 2, height / 2 - borderWidth / 2 + min / 2);
        }
        canvas.drawArc(rectF, -90, progress, false, borderPaint);
        //画居中的文字
        canvas.translate(width / 2, height / 2 - staticLayout.getHeight() / 2);
        staticLayout.draw(canvas);
    }

    public void start(final long times){
        if (listener != null) {
            listener.onStartCount();
        }
        countDownTimer = new CountDownTimer(times, 36) {
            @Override
            public void onTick(long millisUntilFinished) {
                progress=(millisUntilFinished/3600f)*720;
                invalidate();
            }

            @Override
            public void onFinish() {
                progress=0;
                invalidate();
                if (listener != null) {
                    listener.onFinishCount();
                }

            }
        }.start();
    }
    public void cancel(){
        if (countDownTimer!=null){
            countDownTimer.cancel();
            if (listener!=null){
                listener.onFinishCount();
            }
        }
    }
    public void setCountDownTimerListener(CountDownTimerListener listener){
        this.listener = listener;
    }

    public interface CountDownTimerListener {

        void onStartCount();

        void onFinishCount();
    }
}
