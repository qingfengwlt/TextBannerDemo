package com.wlt.textbannerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.wlt.listener.TextViewFlipperClickListener;
import com.wlt.textviewflowlibrary.TextViewFlipper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TextViewFlipperClickListener, View.OnClickListener {

    private TextViewFlipper mTvf,mTvf2;
    private List<String> datas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvf=findViewById(R.id.tvf_test);
        mTvf2=findViewById(R.id.tvf_test_2);
        datas=new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            datas.add("今日头条滚动广告条"+i);
        }
        mTvf2.setDatasWithDrawableIconLeft(datas,getResources().getDrawable(android.R.drawable.ic_search_category_default),this);
        mTvf.setDatas(datas);
        mTvf.setOnClickListener(this);
        mTvf.setFlipInterval(1000);//视图切换间隔设置
//        mTvf2.setFlipInterval(2000);
    }

    @Override
    public void onItemClick(String data, int position) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mTvf.isFlipping()) {
            mTvf.startFlipping();
        }
        if (!mTvf2.isFlipping()) {
            mTvf2.startFlipping();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mTvf.isFlipping()){
            mTvf.stopFlipping();
        }
        if (mTvf2.isFlipping()){
            mTvf2.stopFlipping();
        }
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(this, "整个事件点击", Toast.LENGTH_SHORT).show();

    }

}
