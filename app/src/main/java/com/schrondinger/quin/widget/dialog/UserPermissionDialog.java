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

import com.schrondinger.quin.R;


public class UserPermissionDialog extends Dialog implements View.OnClickListener {
	private Context ctx;
	private View.OnClickListener okListener;
	private Button btncenter;

	public UserPermissionDialog(Context context, View.OnClickListener onclicklistener) {
		super(context, R.style.Theme_Dialog);
		this.ctx = context;
		if (onclicklistener != null) {
			this.okListener=onclicklistener;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager m = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		Display d = m.getDefaultDisplay();
		LayoutParams p = getWindow().getAttributes();
		p.width = (int) (d.getWidth() * 0.9);
		getWindow().setAttributes(p);
		getWindow().setGravity(Gravity.CENTER);
		
		setContentView(R.layout.alertpermissiondialog);
		
		p.width = (int) (d.getWidth() * 0.8);
		getWindow().setAttributes(p);
		getWindow().setGravity(Gravity.CENTER);

		btncenter = (Button) findViewById(R.id.btncenter);
		btncenter.setOnClickListener(this);

		this.setCanceledOnTouchOutside(false);

		
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btncenter:
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
