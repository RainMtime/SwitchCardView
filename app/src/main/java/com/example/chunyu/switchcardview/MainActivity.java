package com.example.chunyu.switchcardview;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.chunyu.switchcardview.adapter.SwitchCardViewAdapter;
import com.example.chunyu.switchcardview.view.SwitchCardView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mNormalBtn;

    private Button mRecyclerViewBtn;

    private Button mListViewTestBtn;

    SwitchCardView mSwitchCardView;

    SwitchCardViewAdapter mSwitchCardViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initCardViewAdapter();
    }

    private void initView() {
        mNormalBtn = findViewById(R.id.normal_btn);
        mRecyclerViewBtn = findViewById(R.id.recyclerViewbtn);
        mListViewTestBtn = findViewById(R.id.listview_btn);
        mSwitchCardView = findViewById(R.id.switch_cardview);

        mNormalBtn.setOnClickListener(this);
        mRecyclerViewBtn.setOnClickListener(this);
        mListViewTestBtn.setOnClickListener(this);


    }

    private void initCardViewAdapter() {
        mSwitchCardViewAdapter = new SwitchCardViewAdapter(this);
        mSwitchCardViewAdapter.setData(mockDatas());
        mSwitchCardView.setAdapter(mSwitchCardViewAdapter);
    }

    @NonNull
    private ArrayList<String> mockDatas() {
        ArrayList<String> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            datas.add("我是第" + i + "个卡片");
        }
        return datas;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.normal_btn:
                break;
            case R.id.listview_btn:
                break;
            case R.id.recyclerViewbtn:
                break;
            default:
                break;
        }
    }
}
