package com.schrondinger.quin.ui.mine.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.schrondinger.quin.R;
import com.schrondinger.quin.base.activity.ActivityInject;
import com.schrondinger.quin.base.activity.BaseFragment;

import butterknife.BindView;

@ActivityInject(rootViewId = R.layout.fragment_mine_two)
public class MineTwoFragment extends BaseFragment {
    @BindView(R.id.rv_movement)
    RecyclerView mRv;

    private String[] data = {"11","12","13","14","15","16","17","18","19","20","21","22","11","12","13","14","15","16","17","18","19","20","21","22"};
    public static MineTwoFragment newInstance() {

        Bundle args = new Bundle();

        MineTwoFragment fragment = new MineTwoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView() {
        super.initView();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRv.setLayoutManager(mLayoutManager);
        mRv.setAdapter(new MineTwoFragment.MyAdapter());
    }

    class MyAdapter extends RecyclerView.Adapter<MineTwoFragment.MyAdapter.ViewHolder>{

        @Override
        public MineTwoFragment.MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.test_frament_item, parent, false);
            return new MineTwoFragment.MyAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MineTwoFragment.MyAdapter.ViewHolder holder, int position) {
            if (holder == null) {
                return;
            }
            holder.textView.setText(data[position]);
        }

        @Override
        public int getItemCount() {
            return data.length;
        }
        class ViewHolder extends RecyclerView.ViewHolder {

            TextView textView;

            //在布局中找到所含有的UI组件
            public ViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.txt);
            }
        }
    }
}
