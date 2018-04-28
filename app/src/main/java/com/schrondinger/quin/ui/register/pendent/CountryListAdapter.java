package com.schrondinger.quin.ui.register.pendent;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.schrondinger.quin.R;
import com.schrondinger.quin.Utils.Util;
import com.schrondinger.quin.base.recyleview.BaseTurboAdapter;
import com.schrondinger.quin.base.recyleview.BaseViewHolder;
import com.schrondinger.quin.bean.CountryRegionResult;
import java.util.List;


/**
 * @author vondear
 */
public class CountryListAdapter extends BaseTurboAdapter<CountryRegionResult.CountryRegionListBean, BaseViewHolder> {

    public CountryListAdapter(Context context) {
        super(context);
    }

    public CountryListAdapter(Context context, List<CountryRegionResult.CountryRegionListBean> data) {
        super(context, data);
    }

    @Override
    protected int getDefItemViewType(int position) {
        CountryRegionResult.CountryRegionListBean country = getItem(position);
        if (Util.isNullOrEmpty(country.getCode())){
            return 1;
        }else{
            return 2;
        }
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2)
            return new CountryHolder(inflateItemView(R.layout.item_country_list, parent));
        else
            return new PinnedHolder(inflateItemView(R.layout.item_pinned_header, parent));
    }


    @Override
    protected void convert(BaseViewHolder holder, CountryRegionResult.CountryRegionListBean item) {
        if (holder instanceof CountryHolder) {
            CountryHolder countryHolder = (CountryHolder)holder;
            countryHolder.country_name_cn.setText(item.getC_Name());
            countryHolder.country_name_en.setText(item.getE_Name());
        }else {
            ((PinnedHolder)holder).country_tip.setText(item.getE_Name());
        }
    }

    public int getLetterPosition(String letter){
        for (int i = 0 ; i < getData().size(); i++){
            if(Util.isNullOrEmpty(getData().get(i).getCode()) && letter.equals(getData().get(i).getE_Name())){
                return i;
            }
        }
        return -1;
    }

    class CountryHolder extends BaseViewHolder {

        TextView country_name_cn;
        TextView country_name_en;

        public CountryHolder(View view) {
            super(view);
            country_name_cn = findViewById(R.id.countryNameCN);
            country_name_en = findViewById(R.id.countryNameEN);
        }
    }


    class PinnedHolder extends BaseViewHolder {

        TextView country_tip;

        public PinnedHolder(View view) {
            super(view);
            country_tip = findViewById(R.id.city_tip);
        }
    }
}
