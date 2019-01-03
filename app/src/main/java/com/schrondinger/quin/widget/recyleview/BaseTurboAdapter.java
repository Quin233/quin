/*
 * Copyright 2015 - 2016 solartisan/imilk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.schrondinger.quin.widget.recyleview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schrondinger.quin.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A subclass of RecyclerView.Adapter responsible for providing views that add header/footer/empty view
 * <p/>
 * <p/>
 * author: imilk
 * https://github.com/Solartisan/TurboRecyclerViewHelper
 */
public abstract class BaseTurboAdapter<T, VH extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> implements OnLoadMoreListener {

    protected static final String TAG = "BaseTurboAdapter";

    protected static final int EMPTY_VIEW = 1 << 5;
    protected static final int LOADING_VIEW = 1 << 6;
    protected static final int FOOTER_VIEW = 1 << 7;
    protected static final int HEADER_VIEW = 1 << 8;

    private final ArrayList<OnItemClickListener> mOnItemClickListeners =
            new ArrayList<>();

    private boolean mLoading = false;
    private boolean mEmptyEnable;

    private View mHeaderView;
    private View mFooterView;
    /**
     * View to show if there are no items to show.
     */
    private View mEmptyView;

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    protected List<T> mData;


    public BaseTurboAdapter(Context context) {
        this(context, null);
    }

    /**
     * initialization
     *
     * @param context The context.
     * @param data    A new list is created out of this one to avoid mutable list
     */
    public BaseTurboAdapter(Context context, List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public void add(T item) {
        boolean isAdd = mData.add(item);
        if (isAdd)
            notifyItemInserted(mData.size() + getHeaderViewsCount());
    }

    public void add(int position, T item) {
        if (position < 0 || position > mData.size()) {
            Log.e(TAG, "position = " + position + ", IndexOutOfBounds, please check your code!");
            return;
        }
        boolean isAdd = mData.add(item);
        if (isAdd)
            notifyItemInserted(position + getHeaderViewsCount());
    }

    public void remove(T item) {
        int index = mData.indexOf(item);
        boolean isRemoved = mData.remove(item);
        if (isRemoved)
            notifyItemRemoved(index + getHeaderViewsCount());
    }

    public void remove(int position) {
        if (position < 0 || position >= mData.size()) {
            Log.e(TAG, "position = " + position + ", IndexOutOfBounds, please check your code!");
            return;
        }
        mData.remove(position);
        notifyItemRemoved(position + getHeaderViewsCount());
    }

    /**
     * additional data;
     *
     * @param data
     */
    public void addData(List<T> data) {
        if (data != null) {
            this.mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void setNewData(List<T> data) {
        mData.clear();
        addData(data);
    }


    public List<T> getData() {
        return mData;
    }


    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    public T getItem(int position) {
        if (position < 0 || position >= mData.size()) {
            Log.e(TAG, "position = " + position + ", IndexOutOfBounds, please check your code!");
            return null;
        }
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getHeaderViewsCount() {
        return mHeaderView == null ? 0 : 1;
    }

    public int getFooterViewsCount() {
        return mFooterView == null ? 0 : 1;
    }

    public int getEmptyViewCount() {
        return mEmptyView == null ? 0 : 1;
    }

    /**
     * Whether there is data exists？
     *
     * @return
     */
    protected boolean isEmpty() {
        return getHeaderViewsCount() + getFooterViewsCount() + getData().size() == 0;
    }

    @Override
    public int getItemCount() {

        int count;
        if (mLoading) { //if loading ignore footer view
            count = mData.size() + 1 + getHeaderViewsCount();
        } else {
            count = mData.size() + getHeaderViewsCount() + getFooterViewsCount();
        }
        mEmptyEnable = false;
        if (count == 0) {
            mEmptyEnable = true;
            count += getEmptyViewCount();
        }
        return count;
    }


    @Override
    public final int getItemViewType(int position) {
        if (mHeaderView != null && position == 0) {
            return HEADER_VIEW;
        } else if (mEmptyView != null && getItemCount() == 1 && mEmptyEnable) {
            return EMPTY_VIEW;
        } else if (position == mData.size() + getHeaderViewsCount()) {
            if (mLoading) {
                return LOADING_VIEW;
            } else if (mFooterView != null) {
                return FOOTER_VIEW;
            }
        }
        return getDefItemViewType(position);
    }

    protected int getDefItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public final BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder vh;
        switch (viewType) {
            case LOADING_VIEW:
                vh = onCreateLoadingViewHolder(parent);
                if (vh == null) {
                    vh = createBaseViewHolder(parent, R.layout.footer_item_default_loading);
                }
                break;
            case EMPTY_VIEW:
                vh = new BaseViewHolder(mEmptyView);
                break;
            case FOOTER_VIEW:
                vh = new BaseViewHolder(mFooterView);
                break;
            case HEADER_VIEW:
                vh = new BaseViewHolder(mHeaderView);
                break;
            default:
                vh = onCreateDefViewHolder(parent, viewType);
                dispatchItemClickListener(vh);
                break;
        }
        return vh;

    }

    /**
     * custom Loading Footer
     *
     * @param parent
     * @return
     */
    protected VH onCreateLoadingViewHolder(ViewGroup parent) {
        return null;
    }

    /**
     * create def view holder
     *
     * @param parent
     * @param viewType
     * @return
     */
    abstract protected VH onCreateDefViewHolder(ViewGroup parent, int viewType);

    private BaseViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        return new BaseViewHolder(inflateItemView(layoutResId, parent));
    }

    /**
     * @param layoutResId
     * @param parent
     * @return
     */
    protected View inflateItemView(int layoutResId, ViewGroup parent) {
        return mLayoutInflater.inflate(layoutResId, parent, false);
    }

    @Override
    public final void onBindViewHolder(BaseViewHolder holder, int position) {

        switch (holder.getItemViewType()) {

            case LOADING_VIEW:
                break;
            case HEADER_VIEW:
                break;
            case EMPTY_VIEW:
                break;
            case FOOTER_VIEW:
                break;
            default:
                convert((VH) holder, mData.get(holder.getLayoutPosition() - getHeaderViewsCount()));
                break;
        }

    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param holder A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    abstract protected void convert(VH holder, T item);


    public void addHeaderView(View header) {
        if (header == null) {
            Log.e(TAG, "header is null!!!");
            return;
        }
        this.mHeaderView = header;
        this.notifyDataSetChanged();
    }

    public void removeHeaderView() {
        if (mHeaderView != null) {
            this.mHeaderView = null;
            this.notifyDataSetChanged();
        }
    }

    public void addFooterView(View footer) {
        if (footer == null) {
            Log.e(TAG, "footer is null!!!");
            return;
        }
        this.mFooterView = footer;
        this.notifyDataSetChanged();
    }

    public void removeFooterView(View footer) {
        if (mFooterView != null) {
            this.mFooterView = null;
            this.notifyDataSetChanged();
        }
    }


    /**
     * Sets the view to show if the adapter is empty
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    /**
     * When the current adapter is empty, the BaseQuickAdapter can display a special view
     * called the empty view. The empty view is used to provide feedback to the user
     * that no data is available in this AdapterView.
     *
     * @return The view to show if the adapter is empty.
     */
    public View getEmptyView() {
        return mEmptyView;
    }


    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    BaseTurboAdapter adapter = (BaseTurboAdapter) recyclerView.getAdapter();
                    if (isFullSpanType(adapter.getItemViewType(position))) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }


    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        int type = getItemViewType(position);
        if (isFullSpanType(type)) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                lp.setFullSpan(true);
            }
        }
    }

    private boolean isFullSpanType(int type) {
        return type == HEADER_VIEW || type == FOOTER_VIEW || type == LOADING_VIEW || type == EMPTY_VIEW;
    }

    @Override
    public void onLoadingMore() {
        if (!mLoading) {
            mLoading = true;
            notifyDataSetChanged();
        }
    }

    void loadingMoreComplete(List<T> data) {
        mLoading = false;
        addData(data);
    }

    public void addOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListeners.add(listener);
    }

    public void removeOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListeners.remove(listener);
    }

    private void dispatchItemClickListener(final BaseViewHolder vh) {

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListeners != null && mOnItemClickListeners.size() > 0) {
                    for (int i = 0; i < mOnItemClickListeners.size(); i++) {
                        final OnItemClickListener listener = mOnItemClickListeners.get(i);
                        listener.onItemClick(vh, vh.getLayoutPosition() - getHeaderViewsCount());
                    }
                }
            }
        });
        vh.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListeners != null && mOnItemClickListeners.size() > 0) {
                    for (int i = 0; i < mOnItemClickListeners.size(); i++) {
                        final OnItemClickListener listener = mOnItemClickListeners.get(i);
                        listener.onItemLongClick(vh, vh.getLayoutPosition() - getHeaderViewsCount());
                    }
                    return true;
                }
                return false;
            }
        });
    }
}
