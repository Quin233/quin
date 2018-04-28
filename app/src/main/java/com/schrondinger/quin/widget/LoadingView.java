package com.schrondinger.quin.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.schrondinger.quin.R;


/**
 * Created by HP on 2018/1/17.
 */

public class LoadingView {

    private LoadingDialog mLoadingDialog;
    private Context mContext;
    private String label;

    public LoadingView(Context context){
        mContext = context;
        mLoadingDialog = new LoadingDialog(context);
    }

    /**
     * 创建一个弹框
     * @param context
     * @return
     */
    public static LoadingView create(Context context) {
        return new LoadingView(context);
    }

    public LoadingView setLabel(String label){
        mLoadingDialog.setLabel(label);
        return this;
    }

    public LoadingView show() {
        if (!isShowing()) {
            mLoadingDialog.show();
        }
        return this;
    }
    public boolean isShowing() {
        return mLoadingDialog != null && mLoadingDialog.isShowing();
    }

    public void dismiss() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    private class LoadingDialog extends Dialog{
        private TextView mLabelText;
        private GifView mImageview;
        private String mLabel;

        public LoadingDialog(Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_loading);

            Window window = getWindow();
            window.setBackgroundDrawable(new ColorDrawable(0));
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.dimAmount = 0;
            layoutParams.gravity = Gravity.CENTER;

            setCanceledOnTouchOutside(false);
            initViews();
        }

        private void initViews() {
            mLabelText = findViewById(R.id.tv_loading);
            if (mLabel != null) {
                mLabelText.setText(mLabel);
                mLabelText.setVisibility(View.VISIBLE);
            } else {
                mLabelText.setVisibility(View.GONE);
            }
            mImageview = findViewById(R.id.iv_loading);
            mImageview.setMovieResource(R.drawable.loading_cat);
        }

        public void setLabel(String label) {
            mLabel = label;
            if (mLabelText != null) {
                if (label != null) {
                    mLabelText.setText(label);
                    mLabelText.setVisibility(View.VISIBLE);
                } else {
                    mLabelText.setVisibility(View.GONE);
                }
            }
        }
    }
}
