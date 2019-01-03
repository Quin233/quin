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

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by hp on 2018/1/8.
 */

public abstract  class BaseMVPFragment<P extends BasePresenter, M extends BaseModel> extends BaseFragment implements BaseView {

    public P mPresenter;
    public M mModel;
//    protected KProgressHUD mKProgressHUD;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
//        if (this instanceof BaseView) {
            mPresenter.setVM(this, mModel);
//        }
    }


    @Override
    public void onDestroy() {
        if (mPresenter != null) mPresenter.onDestroy();
//        mKProgressHUD=null;
        super.onDestroy();
    }

    @Override
    public void onInternetError() {
//        showShortToast("网络异常");
    }

    @Override
    public void onRequestError(int tag,String msg) {
//        showShortToast(msg);
        Logger.d(msg);
//        if(mKProgressHUD!=null){
//            if (mKProgressHUD.isShowing())
//                mKProgressHUD.dismiss();
//        }
        //错误信息提示出来
        if (msg.startsWith("{")){//json错误
            try {
                JSONObject jsonObject=new JSONObject(msg);
//                Alerter.create(getActivity()).setText(jsonObject.optString("errormsg","")).setDuration(5000).show();
                if (jsonObject.optString("errorcode").equals("role.invalid_user")){//会话超时
                    //清除session信息
                    SpUtil.clearCookies();
                    Constants.loginState=false;

                    RxManager rxManager=new RxManager();
                    rxManager.post(RxManager.ISLOGIN,new User());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{//常规错误
//            Alerter.create(getActivity()).setText(msg).setDuration(5000).show();
        }
    }

    @Override
    public void onRequestStart(int tag) {

    }

    @Override
    public void initLoad() {
        super.initLoad();
//        mKProgressHUD=KProgressHUD.create(getActivity())
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setLabel("数据加载中")
//                .setCancellable(true);
    }

    @Override
    public void onRequestEnd(int tag) {
//        if(mKProgressHUD!=null){
//            if (mKProgressHUD.isShowing())
//                mKProgressHUD.dismiss();
//        }
    }
}
