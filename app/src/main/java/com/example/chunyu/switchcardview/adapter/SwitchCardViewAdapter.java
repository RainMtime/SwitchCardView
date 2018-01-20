package com.example.chunyu.switchcardview.adapter;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.chunyu.switchcardview.view.SwitchCardView;

import java.util.ArrayList;

/**
 * Created by 人间一小雨 on 2018/1/20 下午2:45
 * Email: 746431278@qq.com
 */

public class SwitchCardViewAdapter extends SwitchCardView.Adapter {

    private final static String TAG = "SwitchCardViewAdapter";

    private ArrayList<Object> mData = new ArrayList<>();

    @NonNull
    @Override
    public View createTopCardView(@NonNull ViewGroup parent) {
        return null;
    }

    @NonNull
    @Override
    public View createBottomCardView(@NonNull ViewGroup parent) {
        return null;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getCurrentIndex() {
        return 0;
    }

    @Override
    public void renderView(int position, @NonNull View view) {

    }

    @Override
    public void onReleaseTouchForBeginResetAnim() {
        Log.i(TAG,"onReleaseTouchForBeginResetAnim");
        super.onReleaseTouchForBeginResetAnim();
    }

    @Override
    public void onReleaseTouchForBeginSwitchAnim() {
        Log.i(TAG,"onReleaseTouchForBeginSwitchAnim");
        super.onReleaseTouchForBeginSwitchAnim();
    }

    @Override
    public void onReleaseTouchForEndResetAnim() {

        Log.i(TAG,"onReleaseTouchForEndResetAnim");
        super.onReleaseTouchForEndResetAnim();
    }

    @Override
    public void onReleaseTouchForEndSwitchAnim() {
        Log.i(TAG,"onReleaseTouchForEndSwitchAnim");
        super.onReleaseTouchForEndSwitchAnim();
    }
}
