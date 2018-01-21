package com.example.chunyu.switchcardview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.chunyu.switchcardview.R;
import com.example.chunyu.switchcardview.modle.RowData;
import com.example.chunyu.switchcardview.view.SwitchCardView;

import java.util.ArrayList;

/**
 * Created by 人间一小雨 on 2018/1/21 上午10:50
 * Email: 746431278@qq.com
 */

public class BaseRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context mContext;

    private ArrayList<RowData> mDatas = new ArrayList<>();

    private SparseArray<Integer> cardViewIndexRecord = new SparseArray<>();


    public BaseRecyclerViewAdapter(Context context) {
        mContext = context;
    }


    public void setData(ArrayList<RowData> datas) {
        if (datas != null) {
            mDatas.clear();
            mDatas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RowData.TYPE_NORMAL) {
            return new BaseCardViewVH(LayoutInflater.from(mContext).inflate(R.layout.base_recyclerview_item, parent, false));
        } else if (viewType == RowData.TYPE_SWITCH_CARDVIEW) {
            return new SwitchCardViewVH(LayoutInflater.from(mContext).inflate(R.layout.recyclerview_switch_cardview_item, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        RowData rowData = mDatas.get(position);
        if (holder instanceof SwitchCardViewVH) {
            Integer integer = cardViewIndexRecord.get(position);
            ((SwitchCardViewVH) holder).renderView((ArrayList<String>) rowData.data, integer == null ? 0 : integer.intValue());
        } else if (holder instanceof BaseCardViewVH) {
            ((BaseCardViewVH) holder).renderView((String) rowData.data);
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {

        //记录一下，当把SwitchCardView 滑动出界面的时候，已经滑动到那个位置了，
        //当再次滑动回来的时候,在进行卡片位置的恢复。
        int position = holder.getAdapterPosition();
        if (holder instanceof SwitchCardViewVH) {
            cardViewIndexRecord.append(position, ((SwitchCardViewVH) holder).getCurrentIndex());
        } else {
            cardViewIndexRecord.append(position, null);
        }

        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        RowData rowData = mDatas.get(position);
        return rowData.type;
    }

    public final class SwitchCardViewVH extends RecyclerView.ViewHolder {

        private SwitchCardView mSwitchCardView;

        private SwitchCardViewAdapter mSwitchCardViewAdapter;

        public SwitchCardViewVH(View itemView) {
            super(itemView);
            mSwitchCardView = (SwitchCardView) itemView;
            mSwitchCardViewAdapter = new SwitchCardViewAdapter(mContext);
            mSwitchCardView.setAdapter(mSwitchCardViewAdapter);
        }

        public void renderView(ArrayList<String> datas, int changeToIndex) {
            mSwitchCardViewAdapter.setData(datas, changeToIndex);
        }

        public int getCurrentIndex() {
            return mSwitchCardView.getCurrentIndex();
        }


    }

    public static final class BaseCardViewVH extends RecyclerView.ViewHolder {
        Button button;

        public BaseCardViewVH(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.normal_item_btn);
        }

        public void renderView(String str) {
            button.setText(str);
        }
    }


}
