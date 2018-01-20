package com.example.chunyu.switchcardview.adapter;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chunyu.switchcardview.R;
import com.example.chunyu.switchcardview.view.SwitchCardView;

import java.util.ArrayList;

/**
 * Created by 人间一小雨 on 2018/1/20 下午2:45
 * Email: 746431278@qq.com
 */

public class SwitchCardViewAdapter extends SwitchCardView.Adapter {

    private final static String TAG = "SwitchCardViewAdapter";

    private ArrayList<String> mData = new ArrayList<>();

    private Context mContext;

    public SwitchCardViewAdapter(@NonNull Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public View createTopCardView(@NonNull ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(R.layout.switch_cardview_item, null);
    }

    @NonNull
    @Override
    public View createBottomCardView(@NonNull ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(R.layout.switch_cardview_item, null);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(ArrayList<String> datas) {
        if (datas != null) {
            mData.clear();
            mData.addAll(datas);
        }
        notifyChangedToIndex(0);
    }

    @Override
    public void renderView(int position, View view) {
        TextView textView = view.findViewById(R.id.cardview_item_text);
        textView.setText(mData.get(position));
    }

    @Override
    public void onReleaseTouchForBeginResetAnim() {
        Log.i(TAG, "onReleaseTouchForBeginResetAnim");
        super.onReleaseTouchForBeginResetAnim();
    }

    @Override
    public void onReleaseTouchForBeginSwitchAnim() {
        Log.i(TAG, "onReleaseTouchForBeginSwitchAnim");
        super.onReleaseTouchForBeginSwitchAnim();
    }

    @Override
    public void onReleaseTouchForEndResetAnim() {

        Log.i(TAG, "onReleaseTouchForEndResetAnim");
        super.onReleaseTouchForEndResetAnim();
    }

    @Override
    public void onReleaseTouchForEndSwitchAnim() {
        Log.i(TAG, "onReleaseTouchForEndSwitchAnim");
        super.onReleaseTouchForEndSwitchAnim();
    }
}
