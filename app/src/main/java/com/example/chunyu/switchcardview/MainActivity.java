package com.example.chunyu.switchcardview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.chunyu.switchcardview.adapter.SwitchCardViewAdapter;
import com.example.chunyu.switchcardview.view.SwitchCardView;

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
         mSwitchCardViewAdapter = new SwitchCardViewAdapter();
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
