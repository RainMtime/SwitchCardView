package com.example.chunyu.switchcardview.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.chunyu.switchcardview.R;
import com.example.chunyu.switchcardview.adapter.BaseRecyclerViewAdapter;
import com.example.chunyu.switchcardview.modle.RowData;

import java.util.ArrayList;

/**
 * Created by 人间一小雨 on 2018/1/20 下午7:10
 * Email: 746431278@qq.com
 */

public class RecyclerViewTestActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;

    private BaseRecyclerViewAdapter mBaseRecyclerViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recyclerview_test_activity_layout);
        initView();
        initAdapter();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void initAdapter() {
        mBaseRecyclerViewAdapter = new BaseRecyclerViewAdapter(this);

        mBaseRecyclerViewAdapter.setData(mockData());

        mRecyclerView.setAdapter(mBaseRecyclerViewAdapter);

    }

    @NonNull
    private ArrayList<RowData> mockData() {
        ArrayList<RowData> datas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            RowData rowData = new RowData();
            rowData.type = RowData.TYPE_NORMAL;
            rowData.data = "我是第" + i + "个普通元素";
            //让第2个和第4个元素展示成卡片样式。
            if (i == 2 || i == 4) {
                rowData.type = RowData.TYPE_SWITCH_CARDVIEW;
                ArrayList<String> cardViewData = new ArrayList<>();
                for (int j = 0; j < 10; j++) {
                    cardViewData.add("我是第" + j + "个卡片");
                }
                rowData.data = cardViewData;
            }
            datas.add(rowData);
        }
        return datas;
    }
}
