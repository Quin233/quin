package com.schrondinger.quin.base.activity;

import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.schrondinger.quin.Utils.Constants;
import com.schrondinger.quin.Utils.SpUtil;
import com.schrondinger.quin.Utils.TUtil;
import com.schrondinger.quin.base.mvp.BaseModel;
import com.schrondinger.quin.base.mvp.BasePresenter;
import com.schrondinger.quin.base.mvp.BaseView;
import com.schrondinger.quin.bean.User;
import com.schrondinger.quin.common.RxManager;
import com.schrondinger.quin.widget.LoadingView;
import com.schrongder.nba.kotlin.TopToast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by HP on 2018/1/3.
 */

public abstract class BaseMVPActivity<P extends BasePresenter,M extends BaseModel> extends BaseActivity implements BaseView {
    public P mPresenter;
    public M mModel;

    protected LoadingView mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (this instanceof BaseView) {
            mPresenter.setVM(this, mModel);
        }
    }


    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.onDestroy();
//        mLoadingView=null;
        super.onDestroy();
    }

    @Override
    public void onRequestError(int tag,String msg) {
        Logger.d(msg);
        if(mLoadingView!=null){
            if (mLoadingView.isShowing())
                mLoadingView.dismiss();
        }
        //错误信息提示出来
        TopToast topToast = new TopToast(this);
        if (msg.startsWith("{")){//json错误
            try {
                JSONObject jsonObject=new JSONObject(msg);
                topToast.showType(TopToast.Error).setMessageText(jsonObject.optString("errormsg","")).duration(TopToast.LENGTH_LONG).show();
                if (jsonObject.optString("errorcode").equals("role.invalid_user")){//会话超时
                    //清除session信息
                    SpUtil.clearCookies();
                    Constants.INSTANCE.setLoginState(false);

                    RxManager rxManager=new RxManager();
                    rxManager.post(rxManager.ISLOGIN,new User());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{//常规错误
            topToast.showType(TopToast.Error).setMessageText(msg).duration(TopToast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestEnd(int tag) {
        if(mLoadingView!=null){
            if (mLoadingView.isShowing())
                mLoadingView.dismiss();
        }
    }

    @Override
    public void initLoad() {
        super.initLoad();
        mLoadingView=LoadingView.create(this).setLabel("加载中...");
    }

}
