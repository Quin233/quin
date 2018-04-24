package com.schrondinger.quin.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.schrondinger.quin.R;


/**
 * Created by HP on 2018/1/19.
 */

public class UserDefinedDialog extends Dialog implements View.OnClickListener {
	private Context ctx;
	private String title;
	private String msg;
	private String center="确定";
	private String left="取消";
	private String right="确定";
	private Button btnleft, btncenter, btnright;
	private boolean IsTwoButton = false;
	private View.OnClickListener okListener;
	private View.OnClickListener cancelListener;
	
	private TextView tvtitle,tvcontent;

	public void setButtoncenter(String strcenter) {
		this.center=strcenter;
	}

	public void setLeft(String strleft) {
		this.left = strleft;
	}
	public void setRight(String strright) {
		this.right = strright;
	}

	public UserDefinedDialog(Context context,String title, String message, View.OnClickListener onclicklistener, View.OnClickListener cancelListener) {
		super(context, R.style.Theme_Dialog);
		this.ctx = context;
		this.msg = message;
		this.title = title;
		if (onclicklistener != null) {
			this.okListener=onclicklistener;
		}
		if(cancelListener != null){
			IsTwoButton=true;
			this.cancelListener=cancelListener;
		}
	}
	
	public UserDefinedDialog(Context context,String title, String message, View.OnClickListener onclicklistener, View.OnClickListener cancelListener, String left, String right) {
		super(context, R.style.Theme_Dialog);
		this.ctx = context;
		this.msg = message;
		this.title = title;
		if (onclicklistener != null) {
			this.okListener=onclicklistener;
		}
		if(cancelListener != null){
			IsTwoButton=true;
			this.cancelListener=cancelListener;
		}
		
		this.left=left;
		this.right=right;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager m = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		Display d = m.getDefaultDisplay();
		LayoutParams p = getWindow().getAttributes();
//		p.height = (int) (d.getHeight() * 0.3);
		p.width = (int) (d.getWidth() * 0.9);
		//p.alpha = 0.8f;
		//p.dimAmount = 0.0f;
		getWindow().setAttributes(p);
		getWindow().setGravity(Gravity.CENTER);
		
		setContentView(R.layout.dialog_alert);
		
		p.width = (int) (d.getWidth() * 0.9);
		//p.alpha = 0.8f;
		//p.dimAmount = 0.0f;
		getWindow().setAttributes(p);
		getWindow().setGravity(Gravity.CENTER);
		
		
		tvtitle = (TextView) findViewById(R.id.dialogtitle);
		tvcontent = (TextView) findViewById(R.id.dialogcontent);
		
		
		
		btnleft = (Button) findViewById(R.id.btnleft);
		btnright = (Button) findViewById(R.id.btnright);

		btnleft.setOnClickListener(this);
		btnleft.setText(left);
		btnright.setOnClickListener(this);
		btnright.setText(right);
		
		btncenter = (Button) findViewById(R.id.btncenter);
		btncenter.setText(center);
		btncenter.setOnClickListener(this);
		if(IsTwoButton){
			btnleft.setVisibility(View.VISIBLE);
			btnright.setVisibility(View.VISIBLE);
			btncenter.setVisibility(View.GONE);
		}
		tvtitle.setText(title);
		tvcontent.setText(msg);
		
		this.setCanceledOnTouchOutside(false);

		
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnleft:
			
			if(cancelListener != null){
				cancelListener.onClick(v);
			}
			

			break;
		case R.id.btncenter:
			if(okListener != null){
				okListener.onClick(v);
			}
			break;
		case R.id.btnright:
			if(okListener != null){
				okListener.onClick(v);
			}
			break;
			
		}
		this.cancel();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode){
			case KeyEvent.KEYCODE_HOME:return true;
			case KeyEvent.KEYCODE_BACK:return true;
			case KeyEvent.KEYCODE_CALL:return true;
			case KeyEvent.KEYCODE_SYM: return true;
			case KeyEvent.KEYCODE_VOLUME_DOWN: return true;
			case KeyEvent.KEYCODE_VOLUME_UP: return true;
			case KeyEvent.KEYCODE_STAR: return true;
			}
			return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
	}
	
}
