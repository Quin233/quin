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

@ActivityInject(rootViewId = R.layout.fragment_mine_three)
public class MineThreeFragment extends BaseFragment {
    @BindView(R.id.rv_favorite)
    RecyclerView mRv;

    private String[] data = {"刘备","关羽","张飞","赵云","马超","黄忠","魏延","马岱","张苞","马谡","姜维","严颜","王平","关兴","张嶷","孟达","张翼","马忠","吴班","吴懿","高翔","傅佥"};

    public static MineThreeFragment newInstance() {

        Bundle args = new Bundle();

        MineThreeFragment fragment = new MineThreeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView() {
        super.initView();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRv.setLayoutManager(mLayoutManager);
        mRv.setAdapter(new MineThreeFragment.MyAdapter());
    }

    class MyAdapter extends RecyclerView.Adapter<MineThreeFragment.MyAdapter.ViewHolder>{

        @Override
        public MineThreeFragment.MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.test_frament_item, parent, false);
            return new MineThreeFragment.MyAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MineThreeFragment.MyAdapter.ViewHolder holder, int position) {
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