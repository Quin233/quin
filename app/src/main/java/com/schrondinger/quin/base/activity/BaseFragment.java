/*
 * Copyright 2016 Freelander
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.schrondinger.quin.base.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.schrondinger.quin.Utils.Util;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * Created by hp on 2018/1/8.
 */
public  class BaseFragment extends SupportFragment implements BaseFunction,View.OnClickListener {

    protected View mContentView;
    private ViewGroup container;

    private int title;
    protected Unbinder mUnbinder;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getActivity().getClass().isAnnotationPresent(ActivityInject.class)){
            ActivityInject annotation=getClass().getAnnotation(ActivityInject.class);
            if (annotation.rootViewId()!=-1){
                mContentView=inflater.inflate(annotation.rootViewId(),null);
            }else {
                throw new RuntimeException("rootView is null");
            }

            title=annotation.title();
        }
        this.container = container;
        return mContentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder= ButterKnife.bind(this,view);

        initData();
        initView();
        initListener();
        initLoad();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void initData(){
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initLoad() {

    }



    protected void setToolBar(Toolbar toolbar, int title) {
        if(title!=0){
            toolbar.setTitle(title);
        }else {
            toolbar.setTitle("");
        }

        ((SupportActivity)getActivity()).setSupportActionBar(toolbar);
        ((SupportActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((SupportActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    protected void setToolBar(Toolbar toolbar, RelativeLayout relativeLayout){
        ((SupportActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setBackground(new ColorDrawable(0x00000000));
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Resources resources = getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen","android");
            int height = resources.getDimensionPixelSize(resourceId)+ Util.dip2px(getActivity(), 56);
            relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height));
        }
//        relativeLayout.setBackgroundResource(R.drawable.toolbar_bg);
    }

    protected void setToolBar(Toolbar toolbar){
        ((SupportActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");
    }

    protected void openActivity(Class<? extends BaseActivity> toActivity) {
        openActivity(toActivity, null);
    }

    protected void openActivity(Class<? extends BaseActivity> toActivity, Bundle parameter) {
        Intent intent = new Intent(getActivity(), toActivity);
        if (parameter != null) {
            intent.putExtras(parameter);
        }
        startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    @Override
    public void onClick(View v) {

    }
}
