package com.schrondinger.quin.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.schrondinger.quin.R;


/**
 * Created by HP on 2018/1/24.
 */

public class UserHeadSelectDialog extends Dialog implements View.OnClickListener {
    private Context ctx;
    private LinearLayout mLlCamera;
    private LinearLayout mLlPicture;
    private  Button mBtnCancel;
    private View.OnClickListener cameraClicklistener;
    private View.OnClickListener pictureClicklistener;
    public UserHeadSelectDialog(Context context,View.OnClickListener cameraClicklistener, View.OnClickListener pictureClicklistener) {
        super(context);
        this.ctx = context;
        if (cameraClicklistener != null) {
            this.cameraClicklistener=cameraClicklistener;
        }
        if (pictureClicklistener != null) {
            this.pictureClicklistener=pictureClicklistener;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager m = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
//		p.height = (int) (d.getHeight() * 0.3);
        p.width = (int) (d.getWidth() * 0.9);
        getWindow().setAttributes(p);
        getWindow().setGravity(Gravity.CENTER);

        setContentView(R.layout.dialog_user_head_select);
        mLlCamera = (LinearLayout) findViewById(R.id.ll_camera);
        mLlPicture = (LinearLayout) findViewById(R.id.ll_picture);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);

        mLlCamera.setOnClickListener(this);
        mLlPicture.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_camera:
                if(cameraClicklistener != null){
                    cameraClicklistener.onClick(v);
                }
                break;
            case R.id.ll_picture:
                if(pictureClicklistener != null){
                    pictureClicklistener.onClick(v);
                }
                break;
            case R.id.btnCancel:
                this.cancel();
                break;

        }
        this.cancel();
    }
}
