package com.example.administrator.jkbd.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.jkbd.ExamApplication;
import com.example.administrator.jkbd.R;
import com.example.administrator.jkbd.bean.ExamInfo;
import com.example.administrator.jkbd.bean.Question;
import com.example.administrator.jkbd.biz.ExamBiz;
import com.example.administrator.jkbd.biz.IExamBiz;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */

public class ExamActivity extends AppCompatActivity {
    TextView tvexif, tvextitle, tvop1, tvop2, tvop3, tvop4,tvload,tvnum;
    ImageView imview;
    LinearLayout layoutloading;
    ProgressBar dialog;
    IExamBiz biz;
    boolean loadisExamInfo = false;
    boolean loadisQuestion = false;
    boolean loadisExamInfoReceive = false;
    boolean loadisQuestionReceive = false;
    LoadExamBroadCast loadexambrodcast;
    LoadQuestionBroadCast loadquestionbrodcast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examstart);
        loadexambrodcast=new LoadExamBroadCast();
        loadquestionbrodcast=new LoadQuestionBroadCast();
        biz = new ExamBiz();
        setListener();
        initView();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loadexambrodcast!=null){
            unregisterReceiver(loadexambrodcast);
        }
        if(loadquestionbrodcast!=null){
            unregisterReceiver(loadquestionbrodcast);
        }
    }

    private void setListener() {
        registerReceiver(loadexambrodcast,new IntentFilter(ExamApplication.LOAD_EXAM_INFO));
        registerReceiver(loadquestionbrodcast,new IntentFilter(ExamApplication.LOAD_EXAM_QUESTION));
    }

    public void loadData() {
        layoutloading.setEnabled(false);
        dialog.setVisibility(View.VISIBLE);
        tvload.setText("数据下载中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                biz.beginExam();
            }
        }).start();
    }

    private void initData() {
     if (loadisExamInfoReceive&&loadisQuestionReceive){
         if (loadisExamInfo && loadisQuestion) {
             layoutloading.setVisibility(View.GONE);
             ExamInfo examInfo = ExamApplication.getInstance().getExamInfo();
             if (examInfo != null) {
                 showData(examInfo);
             }
                 showExam( biz.getExam());
         }
         else {
             layoutloading.setEnabled(true);
             dialog.setVisibility(View.GONE);
             tvload.setText("下载失败，点击重新下载！");
         }
     }
    }


    private void showExam(Question que) {
        if (que!= null) {
            tvnum.setText(biz.getExamIndex());
            tvextitle.setText(que.getQuestion());
            tvop1.setText(que.getItem1());
            tvop2.setText(que.getItem2());
            tvop3.setText(que.getItem3());
            tvop4.setText(que.getItem4());
            if(que.getUrl()!=null&&!que.getUrl().equals("")) {
                imview.setVisibility(View.VISIBLE);
                Picasso.with(ExamActivity.this).load(que.getUrl()).into(imview);
            }else {
                imview.setVisibility(View.GONE);
            }
        }
    }

    private void showData(ExamInfo examInfo) {
        tvexif.setText(examInfo.toString());
    }

    private void initView() {
        tvexif = (TextView) findViewById(R.id.eaxminfo);
        tvextitle = (TextView) findViewById(R.id.tv_exam_title);
        tvop1 = (TextView) findViewById(R.id.tv_op1);
        tvop2 = (TextView) findViewById(R.id.tv_op2);
        tvop3 = (TextView) findViewById(R.id.tv_op3);
        tvop4 = (TextView) findViewById(R.id.tv_op4);
        tvload=(TextView)findViewById(R.id.tv_load);
        tvnum= (TextView) findViewById(R.id.tv_examnum);
        imview = (ImageView) findViewById(R.id.im_exam_image);
        layoutloading=(LinearLayout)findViewById(R.id.layout_loading);
        dialog=(ProgressBar)findViewById(R.id.load_dialog);
        layoutloading.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    public void perExam(View view) {
        showExam(biz.preQuestion());
    }

    public void nextExam(View view) {
        showExam(biz.nextQuestion());
    }

    class LoadExamBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isSuccess = intent.getBooleanExtra(ExamApplication.LOAD_DATA_SUCCESS, false);
            Log.e("LoadExamBroadcast","LoadExamBroadcast,isSuccess="+isSuccess);
            if (isSuccess) {
                loadisExamInfo = true;
            }
            loadisExamInfoReceive=true;
            initData();
        }
    }

    class LoadQuestionBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isSuccess = intent.getBooleanExtra(ExamApplication.LOAD_DATA_SUCCESS, false);
            Log.e("LoadQuestionBroadcast","LoadQuestionBroadcast,isSuccess="+isSuccess);
            if (isSuccess) {
                loadisQuestion = true;
            }
            loadisQuestionReceive=true;
            initData();
        }
    }
}