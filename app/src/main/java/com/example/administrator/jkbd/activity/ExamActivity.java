package com.example.administrator.jkbd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.jkbd.ExamApplication;
import com.example.administrator.jkbd.R;
import com.example.administrator.jkbd.bean.ExamInfo;
import com.example.administrator.jkbd.bean.Question;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */

public class ExamActivity extends AppCompatActivity{
    TextView tvexif,tvextitle,tvop1,tvop2,tvop3,tvop4;
    ImageView imview;
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
        List<Question> examList=ExamApplication.getInstance().getExamList();
        if(examList!=null){
                  showExam(examList);
        }
    }

    private void showExam(List<Question> examList) {
        Question que=examList.get(0);
        if(que!=null){
            tvextitle.setText(que.getQuestion());
            tvop1.setText(que.getItem1());
            tvop2.setText(que.getItem2());
            tvop3.setText(que.getItem3());
            tvop4.setText(que.getItem4());
            Picasso.with(ExamActivity.this).load(que.getUrl()).into(imview);
        }
    }

    private void showData(ExamInfo examInfo) {
        tvexif.setText(examInfo.toString());
    }

    private void initView() {
        tvexif=(TextView)findViewById(R.id.eaxminfo);
        tvextitle=(TextView)findViewById(R.id.tv_exam_title);
        tvop1=(TextView)findViewById(R.id.tv_op1);
        tvop2=(TextView)findViewById(R.id.tv_op2);
        tvop3=(TextView)findViewById(R.id.tv_op3);
        tvop4=(TextView)findViewById(R.id.tv_op4);
        imview=(ImageView)findViewById(R.id.im_exam_image);
    }

}
