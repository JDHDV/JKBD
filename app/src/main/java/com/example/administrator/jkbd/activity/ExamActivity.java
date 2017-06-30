package com.example.administrator.jkbd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.administrator.jkbd.ExamApplication;
import com.example.administrator.jkbd.R;
import com.example.administrator.jkbd.bean.ExamInfo;

/**
 * Created by Administrator on 2017/6/29.
 */

public class ExamActivity extends AppCompatActivity{
    TextView tvexif;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examstart);
        initView();
        initData();
    }

    private void initData() {
        ExamInfo examInfo= ExamApplication.getInstance().getExamInfo();
        if(examInfo!=null){
            showData(examInfo);
        }
    }

    private void showData(ExamInfo examInfo) {
        tvexif.setText(examInfo.toString());
    }

    private void initView() {
        tvexif=(TextView)findViewById(R.id.eaxminfo);
    }

}
